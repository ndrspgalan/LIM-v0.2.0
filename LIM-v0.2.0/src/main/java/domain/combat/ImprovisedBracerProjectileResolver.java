package domain.combat;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.ArmorProtectionProfile;
import java.util.Objects;

/**
 *  — ESCUDO IMPROVISADO. El brazal izquierdo alzado añade únicamente a HEAD
 * una capa exterior de +2,5 pp de cobertura. Conserva perfil/material/desgaste reales.
 * En ventana de PARRY, DESVIAR mantiene la intercepción reactiva histórica.
 */
public final class ImprovisedBracerProjectileResolver {
    private static final double COVERAGE=0.025;
    private static final double EPSILON=1e-9;
    private final ImprovisedBracerBlockPolicy policy=new ImprovisedBracerBlockPolicy();
    private final CombatTechniqueUnlockPolicy techniques=new CombatTechniqueUnlockPolicy();

    public ProjectileBlockResult resolve(PhysicalDamage projectileLethality, boolean intersectsBracerHitbox, EquipmentState equipment){
        return resolve(projectileLethality,intersectsBracerHitbox,false,0,0.0,equipment);
    }
    public ProjectileBlockResult resolve(PhysicalDamage projectileLethality, boolean intersectsBracerHitbox,
            boolean parryWindow,int dexterity,double recoilUnits,EquipmentState equipment){
        Objects.requireNonNull(projectileLethality); Objects.requireNonNull(equipment);
        if(!intersectsBracerHitbox||!policy.canBlock(equipment)) return new ProjectileBlockResult(false,false,projectileLethality,0);
        ArmorPiece bracer=policy.activeBracer(equipment);
        if(parryWindow&&techniques.canDeflect(dexterity)){
            return new ProjectileBlockResult(true,true,new PhysicalDamage(0,0,0),0,true,
                    techniques.deflectionStunDurationSeconds(dexterity),StaggerPolicy.knockbackDistanceMeters(Math.max(0,recoilUnits)));
        }
        ArmorProtectionProfile p=bracer.currentProtection();
        PhysicalDamage covered=transmit(projectileLethality.scaledBy(COVERAGE),p);
        PhysicalDamage uncovered=projectileLethality.scaledBy(1.0-COVERAGE);
        PhysicalDamage residual=uncovered.plus(covered);
        double absorbedBlunt=Math.max(0,projectileLethality.blunt()*COVERAGE-covered.blunt());
        double wear=absorbedBlunt>EPSILON?bracer.applyBluntWear(1.0,ArmorHitLocation.BODY):0;
        return new ProjectileBlockResult(true,total(residual)<=EPSILON,residual,wear);
    }
    public double raisedHeadCoverageRatio(){return COVERAGE;}
    private static PhysicalDamage transmit(PhysicalDamage d,ArmorProtectionProfile p){return new PhysicalDamage(
            ArmorMitigationPolicy.transmitted(d.piercing(),p.piercing()),ArmorMitigationPolicy.transmitted(d.slashing(),p.slashing()),ArmorMitigationPolicy.transmitted(d.blunt(),p.blunt()));}
    private static double total(PhysicalDamage d){return d.piercing()+d.slashing()+d.blunt();}
}

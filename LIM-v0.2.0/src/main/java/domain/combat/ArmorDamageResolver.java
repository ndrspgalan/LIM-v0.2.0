package domain.combat;

import domain.combat.ai.observation.AttackSourceType;
import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquippedArmorLayer;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;
import domain.inventory.item.armor.*;

import java.util.*;

/**
 * : resolución física estratificada. Un impacto se divide por cobertura espacial y,
 * en cada franja cubierta, atraviesa todas las capas aplicables de exterior a interior.
 * Cada capa mitiga el daño residual y resuelve su propio desgaste antes de que el daño
 * superviviente abandone la armadura y pase a resistencias/bonificadores del personaje.
 */
public final class ArmorDamageResolver {
    private static final double NOMINAL_ARMOR_WEAR_PER_IMPACT = 1.0;
    private static final double EPSILON = 1.0e-9;
    private final ArmorCoverageResolver coverageResolver = new ArmorCoverageResolver();

    public ArmorImpactResult resolve(PhysicalDamage grossDamage, ArmorCombatHitbox hitbox,
                                     ArmorEquipmentLayout layout, double physicalStability) {
        return resolve(grossDamage, hitbox, layout, physicalStability, AttackSourceType.MELEE, null, null);
    }

    public ArmorImpactResult resolve(PhysicalDamage grossDamage, ArmorCombatHitbox hitbox,
                                     ArmorEquipmentLayout layout, double physicalStability, AttackSourceType sourceType) {
        return resolve(grossDamage, hitbox, layout, physicalStability, sourceType, null, null);
    }

    public ArmorImpactResult resolveMelee(PhysicalDamage grossDamage, ArmorCombatHitbox hitbox,
                                          ArmorEquipmentLayout layout, double physicalStability,
                                          WeaponItem weapon, WeaponMode mode) {
        return resolve(grossDamage, hitbox, layout, physicalStability, AttackSourceType.MELEE,
                Objects.requireNonNull(weapon), Objects.requireNonNull(mode));
    }

    public ArmorImpactResult resolveMelee(ArmorCombatHitbox hitbox, ArmorEquipmentLayout layout,
                                          double physicalStability, WeaponItem weapon, WeaponMode mode) {
        Objects.requireNonNull(weapon); Objects.requireNonNull(mode);
        return resolveMelee(MeleeWeaponImpactPolicy.baseImpact(weapon, mode),
                hitbox, layout, physicalStability, weapon, mode);
    }

    private ArmorImpactResult resolve(PhysicalDamage grossDamage, ArmorCombatHitbox hitbox,
                                      ArmorEquipmentLayout layout, double physicalStability,
                                      AttackSourceType sourceType, WeaponItem weapon, WeaponMode mode) {
        Objects.requireNonNull(grossDamage); Objects.requireNonNull(hitbox); Objects.requireNonNull(layout); Objects.requireNonNull(sourceType);
        if (!Double.isFinite(physicalStability) || physicalStability < 0) throw new IllegalArgumentException("Estabilidad física inválida.");

        List<EquippedArmorLayer> applicable = coverageResolver.applicableArmor(hitbox, layout); // ya exterior->interior
        boolean materialSynergy = hitbox.isHead() && applicable.stream().map(EquippedArmorLayer::piece)
                .anyMatch(ArmorPiece::inhibitsHeadBluntMultiplier);
        PhysicalDamage adjustedGross = hitbox.isHead() && !materialSynergy ? grossDamage.withHeadBluntMultiplier() : grossDamage;

        List<String> damaged = new ArrayList<>();
        List<String> broken = new ArrayList<>();
        Set<ArmorPiece> wearApplied = Collections.newSetFromMap(new IdentityHashMap<>());
        Set<ArmorPiece> damageReported = Collections.newSetFromMap(new IdentityHashMap<>());
        Set<ArmorPiece> brokenReported = Collections.newSetFromMap(new IdentityHashMap<>());
        boolean weaponHeavyWearApplied = false;

        double maximum = hitbox.maximumGlobalCoverageRatio();
        double effectiveCoverage = Math.min(maximum,
                applicable.stream().mapToDouble(l -> l.piece().combatCoverageRatio(hitbox)).max().orElse(0.0));

        // La fracción anatómica no cubierta nunca pasa por armadura, incluso si una pieza tiene protección 100 en un canal.
        PhysicalDamage net = adjustedGross.scaledBy(1.0 - effectiveCoverage);

        TreeSet<Double> boundaries = new TreeSet<>();
        boundaries.add(0.0); boundaries.add(effectiveCoverage);
        for (EquippedArmorLayer layer : applicable) {
            double c = Math.min(effectiveCoverage, layer.piece().combatCoverageRatio(hitbox));
            if (c > EPSILON) boundaries.add(c);
        }
        List<Double> points = new ArrayList<>(boundaries);
        for (int i=1;i<points.size();i++) {
            double to=points.get(i), width=to-points.get(i-1);
            if(width<=EPSILON) continue;
            List<EquippedArmorLayer> segment=applicable.stream()
                    .filter(l->l.piece().combatCoverageRatio(hitbox)+EPSILON>=to).toList();
            if(segment.isEmpty()){ net=net.plus(adjustedGross.scaledBy(width)); continue; }

            PhysicalDamage residual=adjustedGross;
            for(EquippedArmorLayer layer:segment){
                ArmorPiece armor=layer.piece();
                ArmorProtectionProfile profile=armor.currentProtection(sourceType);
                PhysicalDamage before=residual;
                residual=new PhysicalDamage(
                        ArmorMitigationPolicy.transmitted(before.piercing(),profile.piercing()),
                        ArmorMitigationPolicy.transmitted(before.slashing(),profile.slashing()),
                        ArmorMitigationPolicy.transmitted(before.blunt(),profile.blunt()));

                // Una pieza se desgasta como máximo una vez por impacto, pero todas las capas alcanzadas son candidatas.
                if(wearApplied.add(armor)){
                    boolean wasDepleted=armor.isDepleted();
                    ArmorProfileWearResult wear=armor.applyProfileWear(NOMINAL_ARMOR_WEAR_PER_IMPACT,
                            before.piercing(),before.slashing(),before.blunt(),profile);
                    if(wear.any() && damageReported.add(armor)) damaged.add(armor.name());
                    if(!wasDepleted && armor.isDepleted() && brokenReported.add(armor)) broken.add(armor.name());
                }
                if(weapon!=null && !weaponHeavyWearApplied && armor.materialClass()==ArmorMaterialClass.HEAVY
                        && (profile.piercing()>EPSILON || profile.slashing()>EPSILON || profile.blunt()>EPSILON)){
                    weapon.applyHeavyArmorWear(mode,profile); weaponHeavyWearApplied=true;
                }
            }
            net=net.plus(residual.scaledBy(width));
        }

        double transferredImpact=ArmorBluntTransferPolicy.transferredImpact(adjustedGross.blunt());
        boolean wetPaperEquipped=layout.layers().stream().map(EquippedArmorLayer::piece).distinct()
                .anyMatch(a->a.isWet()&&a.containsMaterial(ArmorMaterial.PAPER));
        double effectivePhysicalStability=wetPaperEquipped?physicalStability*0.55:physicalStability;
        StaggerResult stagger=StaggerPolicy.resolve(Math.max(0.0,transferredImpact-effectivePhysicalStability));
        return new ArmorImpactResult(net,stagger,damaged,broken,EquipmentState.empty());
    }

    /** Perfil informativo equivalente al pipeline secuencial de todas las capas. */
    public ArmorProtectionProfile totalProtection(ArmorCombatHitbox hitbox, ArmorEquipmentLayout layout) {
        Objects.requireNonNull(hitbox); Objects.requireNonNull(layout);
        List<EquippedArmorLayer> applicable=coverageResolver.applicableArmor(hitbox,layout);
        double maximum=hitbox.maximumGlobalCoverageRatio();
        TreeSet<Double> boundaries=new TreeSet<>(); boundaries.add(0.0); boundaries.add(maximum);
        for(var l:applicable) boundaries.add(Math.min(maximum,l.piece().combatCoverageRatio(hitbox)));
        List<Double> pts=new ArrayList<>(boundaries);
        double transmittedP=1.0, transmittedC=1.0, transmittedB=1.0;
        // Integramos por franjas: fuera de cobertura transmite 100%; dentro, atraviesa todas las capas.
        double globalTP=0,globalTC=0,globalTB=0;
        for(int i=1;i<pts.size();i++){
            double to=pts.get(i),w=to-pts.get(i-1); if(w<=EPSILON)continue;
            List<EquippedArmorLayer> seg=applicable.stream().filter(l->l.piece().combatCoverageRatio(hitbox)+EPSILON>=to).toList();
            double tp=1,tc=1,tb=1;
            for(var l:seg){ var p=l.piece().currentProtection(AttackSourceType.MELEE); tp*=1-p.piercing()/100.0; tc*=1-p.slashing()/100.0; tb*=1-p.blunt()/100.0; }
            globalTP+=tp*w; globalTC+=tc*w; globalTB+=tb*w;
        }
        if(maximum<1){globalTP+=1-maximum;globalTC+=1-maximum;globalTB+=1-maximum;}
        return new ArmorProtectionProfile((1-globalTP)*100,(1-globalTC)*100,(1-globalTB)*100);
    }
}

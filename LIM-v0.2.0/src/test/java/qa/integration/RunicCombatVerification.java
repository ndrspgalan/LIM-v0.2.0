package qa.integration;

import domain.ability.AttackKind;
import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.combat.runic.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;
import domain.inventory.item.armor.*;
import domain.runic.RunicMarkCatalog;
import domain.runic.RunicMarkId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class RunicCombatVerification {
    private static final double EPS = 1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyResonanceSignatureAndPersistence();
        verifyCompositeRecoilOrder();
        verifyMirrorRepeatsSequenceOnce();
        verifyBindingVowNoRecursion();
        verifySilenceAndFeintLockBreak();
        verifyPhonemicDerivationUsesCoverageWithoutWear();
    }

    private static void verifyResonanceSignatureAndPersistence() {
        WeaponItem weapon = weapon("Arma A");
        WeaponItem other = weapon("Arma B");
        ResonanceCombatMemory memory = new ResonanceCombatMemory();
        Object attacker = "A", target = "T";
        AttackSignature light3 = AttackSignature.of(AttackKind.LIGHT, 3, weapon);
        org.junit.jupiter.api.Assertions.assertTrue(!memory.register(attacker,target,light3,30,true,ImpactOrigin.PRIMARY_ATTACK).triggered(), "Primer golpe no resuena.");
        ResonanceResult repeated = memory.register(attacker,target,light3,12,true,ImpactOrigin.PRIMARY_ATTACK);
        close(repeated.rawCurseDamage(),30,"Debe repetir el daño físico neto anterior.");
        org.junit.jupiter.api.Assertions.assertTrue(!memory.register(attacker,target,AttackSignature.of(AttackKind.LIGHT,2,weapon),9,true,ImpactOrigin.PRIMARY_ATTACK).triggered(), "Ordinal distinto no resuena.");
        org.junit.jupiter.api.Assertions.assertTrue(!memory.register(attacker,target,AttackSignature.of(AttackKind.LIGHT,2,other),9,true,ImpactOrigin.PRIMARY_ATTACK).triggered(), "Arma distinta no resuena.");
        memory.onTargetDeath(target);
        org.junit.jupiter.api.Assertions.assertTrue(memory.rememberedPairs()==0,"La muerte limpia la memoria del objetivo.");
    }

    private static void verifyCompositeRecoilOrder() {
        CompositeImpact impact = new CompositeImpactResolver().resolve(new PhysicalDamage(0,0,50),40,0,20,10);
        close(impact.physicalRecoilUnits(),30,"Retroceso físico");
        close(impact.mentalRecoilUnits(),36,"CORDURA 10 mitiga un 10 % de 40 Maldición neta.");
        close(impact.accumulatedStagger().knockbackDistanceMeters(), StaggerPolicy.knockbackDistanceMeters(66), "Debe sumar antes de StaggerPolicy.");
    }

    private static void verifyMirrorRepeatsSequenceOnce() {
        MirrorAttackScheduler scheduler = new MirrorAttackScheduler();
        AtomicInteger executions = new AtomicInteger();
        scheduler.schedule(10.0,new MirroredAttackCommand("A",executions::incrementAndGet),true,ImpactOrigin.PRIMARY_ATTACK);
        org.junit.jupiter.api.Assertions.assertTrue(scheduler.executeDue(10.49)==0,"No debe ejecutarse antes de 0,5 s.");
        org.junit.jupiter.api.Assertions.assertTrue(scheduler.executeDue(10.5)==1,"Debe ejecutarse a los 0,5 s.");
        org.junit.jupiter.api.Assertions.assertTrue(executions.get()==1 && scheduler.executeDue(20)==0,"La secuencia se repite una sola vez.");
        scheduler.schedule(20,new MirroredAttackCommand("A",executions::incrementAndGet),true,ImpactOrigin.MIRROR_SEQUENCE);
        org.junit.jupiter.api.Assertions.assertTrue(scheduler.pendingCount()==0,"El reflejo no debe reflejarse a sí mismo.");
    }

    private static void verifyBindingVowNoRecursion() {
        CharacterSheet sheet = sheet(35);
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK,RunicMarkCatalog.require(RunicMarkId.VOTO_VINCULANTE)));
        RunicAttackAugmentationPolicy policy = new RunicAttackAugmentationPolicy();
        close(policy.bindingVowCurseDamage(sheet,equipment,ImpactOrigin.PRIMARY_ATTACK),35,"Voto añade FE.");
        close(policy.bindingVowCurseDamage(sheet,equipment,ImpactOrigin.RESONANCE),0,"Voto no aumenta impactos secundarios.");
    }

    private static void verifySilenceAndFeintLockBreak() {
        CharacterSheet sheet = sheet(21);
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK,RunicMarkCatalog.require(RunicMarkId.SILENCIO)));
        SilencePolicy policy = new SilencePolicy();
        close(policy.emittedSoundIntensity(80,sheet,equipment),0,"Silencio anula sonido.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.retainsTargetLockAfterFeint(true,sheet,equipment),"FINTAR rompe el fijado.");
        CharacterSheet latent = sheet(20);
        close(policy.emittedSoundIntensity(80,latent,equipment),0,"La Marca Rúnica no debe exigir FE 21.");
    }

    private static void verifyPhonemicDerivationUsesCoverageWithoutWear() {
        ArmorPiece chest = new ArmorPiece("Coraza","Armadura de verificación.",1,ArmorInventoryCategory.CHEST,ArmorHitLocation.BODY,.5,
                new ArmorProtectionProfile(0,0,10),ArmorMaterial.STEEL,ArmorForm.STANDARD,List.of(),List.of());
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.CHEST,chest));
        double before = chest.currentBluntProtection();
        var result = new PhonemicDerivationResolver().resolve(40,ArmorHitLocation.BODY,equipment,25,true);
        // Descubierto 20 + rama cubierta 20 mitigada por B10%=18; resistencia 25% => 28,5.
        close(result.afterArmor(),38,"Derivación porcentual por cobertura");
        close(result.netCurseDamage(),28.5,"Resistencia posterior");
        close(chest.currentBluntProtection(),before,"La derivación no desgasta la armadura.");
    }

    private static WeaponItem weapon(String name) {
        return new WeaponItem(name,"Arma de verificación.",1,new InventoryFootprint(1,1),1,
                List.of(new WeaponMode("Modo",new LethalityProfile(1,1,1))),List.of(),List.of(),List.of(),
                OptionalDouble.empty(),0,false,
                new WeaponConfigurationPolicy(List.of(new WeaponConfiguration(GripMode.ONE_HANDED,WeaponActionMode.PRIMARY))),Set.of());
    }
    private static CharacterSheet sheet(int faith) { return CharacterSheet.of(30,30,30,30,30,30,faith,30,30); }
    private static void close(double a,double e,String m){ if(Math.abs(a-e)>EPS) throw new IllegalStateException(m+": "+a+" != "+e); }
    
}

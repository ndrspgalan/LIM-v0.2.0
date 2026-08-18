package qa.integration;

import domain.character.Gender;
import domain.combat.*;
import domain.combat.moveset.*;
import domain.combat.runic.CompositeImpactResolver;
import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

/**  — Pico/Zapapico/Piqueta, física DE_ROTOR y continuidad de impacto. */
public final class PickMattockMovesetAndRotorVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyRotorContracts(); verifyMovesets(); verifyImpactAndStagger(); verifyUnarmedMass(); verifyLeftHandDexterity(); verifyDorsalIsolation();
    }

    private static void verifyRotorContracts(){
        WeaponItem pico=MeleeWeaponCatalog.pico(), zap=MeleeWeaponCatalog.zapapico(), rotor=MeleeWeaponCatalog.espadonDeRotor();
        for(WeaponItem w:new WeaponItem[]{pico,zap,rotor}){
            org.junit.jupiter.api.Assertions.assertTrue(w.hasTrait(WeaponTrait.DE_ROTOR),w.name()+" debe ser DE_ROTOR.");
            org.junit.jupiter.api.Assertions.assertTrue(w.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK),w.name()+" debe admitir HEAVY.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(!pico.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),"Pico DE_ROTOR no admite CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(!zap.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),"Zapapico DE_ROTOR no admite CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),": Espadón de Rotor es la excepción DE_ROTOR con CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(strength(pico)==24&&strength(zap)==24,"DE_ROTOR 2H no debe recibir FUERZA x0,75.");
        org.junit.jupiter.api.Assertions.assertTrue(strength(rotor)==38,"Espadón Rotor debe requerir FUERZA 38 sin x0,75 ni -1.");
        close(new HeavyAttackImpactPolicy().resolve(pico,pico.modes().getFirst()).blunt(),94.36,"HEAVY Pico");
        close(new HeavyAttackImpactPolicy().resolve(zap,zap.modes().getFirst()).blunt(),87.36,"HEAVY Zapapico");
        close(new HeavyAttackImpactPolicy().resolve(rotor,rotor.modes().getFirst()).blunt(),145.32,"HEAVY Espadón Rotor");
    }

    private static void verifyMovesets(){
        var p=MeleeWeaponCatalog.pico().offensiveMoveset().orElseThrow();
        var z=MeleeWeaponCatalog.zapapico().offensiveMoveset().orElseThrow();
        var q=MeleeWeaponCatalog.piqueta().offensiveMoveset().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(p.lightAttackCount()==4&&z.lightAttackCount()==4&&q.lightAttackCount()==3,"Longitudes LIGHT  incorrectas.");
        org.junit.jupiter.api.Assertions.assertTrue(p.motion("H").orElseThrow().bodyAdvance()==BodyAdvance.COMMITTED,"Fuerte del Pico debe comprometer avance.");
        org.junit.jupiter.api.Assertions.assertTrue(z.motion("H").orElseThrow().bodyAdvance()==BodyAdvance.COMMITTED,"Fuerte del Zapapico debe comprometer avance.");
        org.junit.jupiter.api.Assertions.assertTrue(p.transition("L3","H").orElseThrow().executionTimeMultiplier()<1,"Pico L3→H debe ahorrar recuperación.");
        org.junit.jupiter.api.Assertions.assertTrue(z.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,"Zapapico debe premiar alternancia azada→pico.");
        org.junit.jupiter.api.Assertions.assertTrue(q.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,"Piqueta debe invertir martillo→cincel sin neutralizar.");
    }

    private static void verifyImpactAndStagger(){
        WeaponItem pico=MeleeWeaponCatalog.pico(); WeaponMode mode=pico.modes().getFirst();
        PhysicalDamage base=MeleeWeaponImpactPolicy.baseImpact(pico,mode);
        close(base.blunt(),67.4,"Pico debe sumar 2,4 kg una sola vez al contundente basal.");
        var impact=new ArmorDamageResolver().resolveMelee(ArmorCombatHitbox.CHEST, ArmorEquipmentLayout.empty(),10,pico,mode);
        StaggerResult expected=StaggerPolicy.resolve(57.4); // sin armadura: B67,4 - estabilidad 10
        close(impact.stagger().knockbackDistanceMeters(),expected.knockbackDistanceMeters(),"Retroceso Rotor debe usar el mismo contundente.");
        close(impact.stagger().staggerDurationSeconds(),expected.staggerDurationSeconds(),"Duración stagger Rotor debe usar el mismo contundente.");

        var composite=new CompositeImpactResolver().resolve(base,40,0,10,25);
        close(composite.physicalRecoilUnits(),57.4,"ESTABILIDAD FÍSICA debe descontar el contundente Rotor ya bonificado por masa.");
        close(composite.mentalRecoilUnits(),30,"CORDURA 25 debe reducir a 75 % la presión mental de 40 Maldición.");
        StaggerResult combined=StaggerPolicy.resolve(57.4+30);
        close(composite.accumulatedStagger().knockbackDistanceMeters(),combined.knockbackDistanceMeters(),"Físico y mental deben converger en la misma curva de retroceso.");
        close(composite.accumulatedStagger().staggerDurationSeconds(),combined.staggerDurationSeconds(),"Físico y mental deben converger en la misma curva de duración.");
    }

    private static void verifyUnarmedMass(){
        WeaponItem human=UnarmedWeaponFactory.create(1.80,20, Gender.HOMBRE);
        double expected=20+domain.combat.UnarmedMassPolicy.equivalentMassKg(Gender.HOMBRE);
        close(human.modes().getFirst().lethality().blunt(),expected,"DESARMADO humano = FUERZA + masa ofensiva.");
        org.junit.jupiter.api.Assertions.assertTrue(!human.hasTrait(WeaponTrait.DE_ROTOR),"DESARMADO no debe recibir una segunda bonificación de masa Rotor.");
    }

    private static void verifyLeftHandDexterity(){
        org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.dexterityRequirement(1.0,EquipmentSlot.RIGHT_HAND,true)==10,"RIGHT_HAND conserva DESTREZA base.");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.dexterityRequirement(1.0,EquipmentSlot.LEFT_HAND,true)==13,"LEFT_HAND dual aplica x1,25 antes de techo.");
    }

    private static void verifyDorsalIsolation(){
        org.junit.jupiter.api.Assertions.assertTrue(!MeleeWeaponCatalog.pico().hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE),"Pico DE_ROTOR no pertenece al sistema dorsal.");
        org.junit.jupiter.api.Assertions.assertTrue(!MeleeWeaponCatalog.zapapico().hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE),"Zapapico DE_ROTOR no pertenece al sistema dorsal.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE),"Sólo Espadón conserva compatibilidad dorsal.");
    }

    private static int strength(WeaponItem w){return w.requirements().stream().filter(r->r.attribute()==domain.character.sheet.Attribute.FUERZA).findFirst().orElseThrow().minimumValue();}
    private static void close(double a,double e,String m){if(Math.abs(a-e)>1e-9)throw new AssertionError(m+": "+a+" != "+e);}
    
}

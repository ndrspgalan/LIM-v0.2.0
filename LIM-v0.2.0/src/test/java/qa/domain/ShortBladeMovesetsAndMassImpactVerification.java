package qa.domain;

import domain.character.sheet.CharacterSheet;
import domain.combat.MeleeWeaponImpactPolicy;
import domain.combat.ShieldCombatPolicy;
import domain.combat.StrengthMassBluntPolicy;
import domain.combat.moveset.TransitionContinuity;
import domain.inventory.item.WeaponActionMode;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.firearms.BayonetChargePolicy;
import domain.inventory.item.firearms.BayonetChargeState;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.meleeWeapons.ShieldCatalog;

/**  — cuchillo/daga y extensión controlada de masa contundente. */
public final class ShortBladeMovesetsAndMassImpactVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyShortBladeMovesets();
        verifySufficientErgonomicsMass();
        verifyBayonetStrengthAndMass();
        verifyShieldStrengthAndMass();
    }

    private static void verifyShortBladeMovesets(){
        var butcher=MeleeWeaponCatalog.cuchilloDeCarnicero();
        var bm=butcher.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(bm.lightAttackCount()==4,"Cuchillo de Carnicero debe poseer cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(butcher.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==4,"Combo LIGHT del Carnicero debe sincronizarse con el moveset.");
        org.junit.jupiter.api.Assertions.assertTrue(bm.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,
                "Carnicero L1→L2 debe convertir recuperación en ataque.");
        org.junit.jupiter.api.Assertions.assertTrue(bm.transition("L4","L1").orElseThrow().continuity()==TransitionContinuity.NATURAL,
                "Carnicero debe cerrar el ciclo hacia L1.");

        var dagger=MeleeWeaponCatalog.daga();
        var primary=dagger.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        var alternate=dagger.offensiveMovesetFor(WeaponActionMode.ALTERNATIVE).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(primary.lightAttackCount()==5,"Daga Oscilatorio debe poseer cinco LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(alternate.lightAttackCount()==4,"Daga Invertido debe poseer cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(dagger.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==5,"PRIMARY de Daga debe sincronizar cinco LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(dagger.lightAttackComboFor(WeaponActionMode.ALTERNATIVE).attackCount()==4,"ALTERNATIVE de Daga debe sincronizar cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(primary.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,
                "Oscilatorio debe encadenar estocada→extracción cortante.");
        org.junit.jupiter.api.Assertions.assertTrue(alternate.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,
                "Invertido debe convertir extracción en corte ascendente.");
    }

    private static void verifySufficientErgonomicsMass(){
        var axe=MeleeWeaponCatalog.hachaDeLenador();
        var sabre=MeleeWeaponCatalog.cimitarra();
        var hammer=MeleeWeaponCatalog.martilloDeBola();
        for(var w:new domain.inventory.item.WeaponItem[]{axe,sabre,hammer}){
            org.junit.jupiter.api.Assertions.assertTrue(w.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE),w.name()+" debe declarar ERGONOMIA_SUFICIENTE.");
            double expected=w.modes().getFirst().lethality().blunt()+w.weightKg();
            close(MeleeWeaponImpactPolicy.baseImpact(w,w.modes().getFirst()).blunt(),expected,
                    w.name()+" debe sumar +1 contundente por kilogramo.");
        }
        var dagger=MeleeWeaponCatalog.daga();
        close(MeleeWeaponImpactPolicy.baseImpact(dagger,dagger.modes().getFirst()).blunt(),10,
                "Daga ordinaria sin ergonomía suficiente no debe sumar masa.");
    }

    private static void verifyBayonetStrengthAndMass(){
        var rifle=FirearmCatalog.repeatingRifleV881();
        close(rifle.weightKg(),4.05,"Masa del Fusil de Repetición.");
        close(rifle.bayonetChargeProfile(30).blunt(),34.05,
                "Bayonetazo cargado debe aplicar FUERZA + masa del fusil.");

        var state=new BayonetChargeState();
        var policy=new BayonetChargePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.begin(state,100).active(),"La carga de bayoneta debe iniciarse.");
        var impact=policy.impact(state,30,rifle.weightKg());
        org.junit.jupiter.api.Assertions.assertTrue(impact.impact()&&!impact.active(),"El impacto debe cerrar la carga.");
        close(impact.impactProfile().blunt(),34.05,
                "BayonetChargePolicy debe usar la misma autoridad FUERZA + masa.");
    }

    private static void verifyShieldStrengthAndMass(){
        var shield=ShieldCatalog.pavesinaCementadaDeAsaltoV881();
        var sheet=CharacterSheet.of(20,45,12,45,20,20,3,20,11);
        double expected=StrengthMassBluntPolicy.blunt(45,shield.weightKg());
        close(expected,53.8,"Autoridad FUERZA + masa del escudo.");
        close(ShieldCombatPolicy.lightAttackLethality(sheet,shield).blunt(),expected,
                "Arrollamiento debe aplicar +1 Ct/FUERZA y +1 Ct/kg.");
    }

    private static void close(double actual,double expected,String message){
        if(Math.abs(actual-expected)>1e-9)throw new AssertionError(message+": "+actual+" != "+expected);
    }
    
}

package qa.domain;

import domain.combat.*;
import domain.combat.moveset.MeleeMovesetProfile;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.meleeWeapons.special.ThermoMechanicalKatanaPolicy;
import domain.inventory.item.meleeWeapons.special.ThermoMechanicalKatanaState;
import domain.movement.LocomotionMode;

/**  — movesets de espadas y preparación cargada especializada de Helicoidal/Katana. */
public final class SwordMovesetsAndChargedSpecializationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyMovesets();
        verifyChargedPreparation();
        verifyKatanaImpactAndStamina();
        verifyKatanaThermalDraw();
    }

    private static void verifyMovesets() {
        var scimitar = MeleeWeaponCatalog.cimitarra();
        var helical = MeleeWeaponCatalog.espadaHelicoidal();
        var katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        profile(scimitar,5);
        profile(helical,4);
        profile(katana,4);
        org.junit.jupiter.api.Assertions.assertTrue(helical.offensiveMoveset().orElseThrow().motion("H1").orElseThrow().action()==WeaponCombatAction.HEAVY_ATTACK,
                "Helicoidal debe declarar H1 desde guardia alta.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.offensiveMoveset().orElseThrow().motion("C-LR").orElseThrow().action()==WeaponCombatAction.CHARGED_ATTACK,
                "Helicoidal debe declarar salida cargada izquierda→derecha.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.offensiveMoveset().orElseThrow().motion("C-RL").orElseThrow().action()==WeaponCombatAction.CHARGED_ATTACK,
                "Helicoidal debe declarar salida cargada derecha→izquierda.");
        var kh=katana.offensiveMoveset().orElseThrow().motion("H1").orElseThrow();
        var kc=katana.offensiveMoveset().orElseThrow().motion("C1").orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(kh.endState().equals(kc.endState()) && kh.trajectory().contains("apuntándolo") && kc.trajectory().contains("mismo desenvaine"),
                "HEAVY y CHARGED de Katana deben compartir desenvaine longitudinal y salida apuntando al rival.");
    }

    private static void verifyChargedPreparation() {
        var helical = MeleeWeaponCatalog.espadaHelicoidal();
        var katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        var policy = new ChargedAttackPreparationPolicy();
        var movement = new ChargedAttackMovementPolicy();
        var specialization = new ChargedAttackSpecializationPolicy();

        org.junit.jupiter.api.Assertions.assertTrue(specialization.style(helical)==ChargedAttackPreparationStyle.CONTINUOUS_FLOURISH,
                "Helicoidal debe usar floritura continua.");
        org.junit.jupiter.api.Assertions.assertTrue(specialization.style(katana)==ChargedAttackPreparationStyle.HELD_READY_IN_SHEATH,
                "Katana debe esperar en saya.");

        var hs = new ChargedAttackPreparationState(); hs.start(); hs.advance(30.0);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.ready(helical,hs),"La floritura Helicoidal no autoejecuta por tiempo.");
        org.junit.jupiter.api.Assertions.assertTrue(!movement.allows(helical,hs,LocomotionMode.RUNNING) && !movement.allows(helical,hs,LocomotionMode.TROTTING)
                        && movement.allows(helical,hs,LocomotionMode.WALKING),
                "Durante CHARGED Helicoidal sólo debe permitirse caminar.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.release(helical,hs,ChargedAttackReleaseSide.LEFT)==ChargedAttackReleaseVariant.LEFT_TO_RIGHT,
                "Salida izquierda de floritura debe resolver swing izquierda→derecha.");

        var ks = new ChargedAttackPreparationState(); ks.start(); ks.advance(60.0);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.ready(katana,ks),"La espera de Katana no autoejecuta por tiempo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.release(katana,ks,null)==ChargedAttackReleaseVariant.FORWARD_DRAW,
                "Katana debe liberar desenvaine longitudinal.");
    }

    private static void verifyKatanaImpactAndStamina() {
        var katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        var charged = new ChargedAttackImpactPolicy();
        var heavy = new HeavyAttackImpactPolicy();
        var stamina = new CombatStaminaCostPolicy();
        double base = katana.modes().getFirst().lethality().blunt();
        close(heavy.resolve(katana,katana.modes().getFirst()).blunt(),base*1.20,"Katana HEAVY contundente");
        close(charged.resolve(katana,katana.modes().getFirst()).blunt(),base*1.20,"Katana CHARGED contundente");
        close(charged.resolve(katana,katana.modes().getFirst(),true).blunt(),base*1.20,"Aura no cambia excepción CHARGED Katana");
        close(stamina.cost(katana,WeaponCombatAction.HEAVY_ATTACK),katana.weightKg()*1.20,"Katana HEAVY PA");
        close(stamina.cost(katana,WeaponCombatAction.CHARGED_ATTACK),katana.weightKg()*1.20,"Katana CHARGED PA");
    }

    private static void verifyKatanaThermalDraw() {
        var katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        var state = new ThermoMechanicalKatanaState(300,false,false);
        var thermal = new ThermoMechanicalKatanaPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(thermal.additionalBurnDamageForAttack(katana,state,WeaponCombatAction.HEAVY_ATTACK)==100,
                "HEAVY preparado debe exponer Quemadura 100.");
        org.junit.jupiter.api.Assertions.assertTrue(thermal.performAggressiveDrawAttack(katana,state,WeaponCombatAction.HEAVY_ATTACK)==100 && state.drawn() && state.burning(),
                "HEAVY debe desenvainar, prender y aplicar Quemadura 100.");
        org.junit.jupiter.api.Assertions.assertTrue(thermal.additionalBurnDamage(katana,state)==67,"Tras el impacto la combustión ordinaria vuelve a Quemadura 67.");
    }

    private static void profile(WeaponItem weapon,int expectedLights) {
        MeleeMovesetProfile p=weapon.offensiveMoveset().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(p.lightAttackCount()==expectedLights,weapon.name()+" debe tener "+expectedLights+" LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(weapon.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==expectedLights,
                weapon.name()+" debe sincronizar LightAttackComboProfile.");
    }
    private static void close(double a,double e,String m){if(Math.abs(a-e)>1e-9)throw new AssertionError(m+": "+a+" != "+e);}
    
}

package qa.domain;

import domain.combat.ProjectileMirrorParryPolicy;
import domain.combat.moveset.TransitionContinuity;
import domain.inventory.item.WeaponActionMode;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.firearms.FirearmCatalog;

/** Verificación : Hoz/Guadaña/Boathook y nicho balístico de la Espada Helicoidal. */
public final class HookedWeaponsAndLeadProjectileDeflectionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifySickle();
        verifyScythe();
        verifyBoathook();
        verifyLead46Deflection();
    }

    private static void verifySickle() {
        var w = MeleeWeaponCatalog.hoz();
        var m = w.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(m.lightAttackCount() == 3, "La Hoz debe tener LIGHT x3.");
        org.junit.jupiter.api.Assertions.assertTrue(w.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK), "La Hoz conserva HEAVY de enganche.");
        org.junit.jupiter.api.Assertions.assertTrue(!w.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK), "La Hoz no debe tener CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(m.motion("H1").orElseThrow().action() == WeaponCombatAction.HEAVY_ATTACK, "H1 debe ser el enganche de la Hoz.");
        org.junit.jupiter.api.Assertions.assertTrue(m.transition("L1","L2").orElseThrow().continuity() == TransitionContinuity.EXCELLENT,
                "La recuperación L1->L2 de la Hoz debe ser excelente.");
    }

    private static void verifyScythe() {
        var w = MeleeWeaponCatalog.guadana();
        var m = w.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(m.lightAttackCount() == 4, "La Guadaña debe tener LIGHT x4.");
        org.junit.jupiter.api.Assertions.assertTrue(m.motion("C1").orElseThrow().action() == WeaponCombatAction.CHARGED_ATTACK,
                "La Guadaña debe usar C1 como moulinet cargado + siega amplia.");
        org.junit.jupiter.api.Assertions.assertTrue(m.motion("D1").orElseThrow().trajectory().contains("Patada frontal"),
                "DESTABILIZE de Guadaña debe ser patada frontal.");
        org.junit.jupiter.api.Assertions.assertTrue(m.transition("L2","C1").orElseThrow().continuity() == TransitionContinuity.EXCELLENT,
                "L2 debe alimentar naturalmente el moulinet cargado de Guadaña.");
    }

    private static void verifyBoathook() {
        var w = MeleeWeaponCatalog.boathook();
        var m = w.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(m.lightAttackCount() == 3, "Boathook debe tener LIGHT x3.");
        org.junit.jupiter.api.Assertions.assertTrue(!w.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK), "Boathook no debe tener CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(m.motion("D1").orElseThrow().trajectory().contains("Patada frontal"),
                "DESTABILIZE de Boathook debe ser patada frontal.");
        org.junit.jupiter.api.Assertions.assertTrue(m.motion("L3").orElseThrow().contactSurface().contains("Culata"),
                "L3 del Boathook debe reutilizar la culata.");
        org.junit.jupiter.api.Assertions.assertTrue(m.transition("L1","H1").orElseThrow().continuity() == TransitionContinuity.EXCELLENT,
                "El empuje longitudinal debe poder deslizarse directamente al enganche.");
    }

    private static void verifyLead46Deflection() {
        var helical = MeleeWeaponCatalog.espadaHelicoidal();
        var policy = new ProjectileMirrorParryPolicy();
        var pneumatic = policy.resolve(helical, WeaponCombatAction.LIGHT_ATTACK,
                FirearmCatalog.repeatingPneumaticRifleV881(), true);
        org.junit.jupiter.api.Assertions.assertTrue(pneumatic.successful() && pneumatic.originalTrajectoryCancelled(),
                "La Helicoidal debe desviar el .46 de plomo neumático si intersecta la hitbox ofensiva.");
        close(pneumatic.lateralDeflectionDegrees(), ProjectileMirrorParryPolicy.LEAD_46_DEFLECTION_DEGREES,
                "Ángulo de desvío .46 de plomo");

        var bifilar = policy.resolve(helical, WeaponCombatAction.LIGHT_ATTACK,
                FirearmCatalog.bifilarElectromagneticRifleV881(), true);
        org.junit.jupiter.api.Assertions.assertTrue(!bifilar.successful(), "El .46 de tungsteno bifilar no debe heredar el nicho de plomo neumático.");

        var pistol = policy.resolve(helical, WeaponCombatAction.LIGHT_ATTACK,
                FirearmCatalog.autoloadingPistolV881(), true);
        org.junit.jupiter.api.Assertions.assertTrue(!pistol.successful(), "La munición .45 encamisada no debe ser desviable por esta política de nicho.");

        var noCollision = policy.resolve(helical, WeaponCombatAction.CHARGED_ATTACK,
                FirearmCatalog.repeatingPneumaticRifleV881(), false);
        org.junit.jupiter.api.Assertions.assertTrue(!noCollision.successful(), "Sin intersección física de hitbox no existe desvío automático.");
    }

    private static void close(double actual, double expected, String label) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(label + ": " + actual + " != " + expected);
    }
    
}

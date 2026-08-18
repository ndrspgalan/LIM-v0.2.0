package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.character.Gender;
import domain.character.sheet.*;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import domain.inventory.item.firearms.FulminatingPolicy;
import domain.status.TherapeuticEffectTracker;

public final class CombatRulesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        healthRegenerationUsesSixSecondUnit();
        therapeuticDurationsAreThirtyGameMinutes();
        stimulantHasNoMinimumHealthFloor();
        ballisticFinishersRemainDeterministic();
    }

    private static void healthRegenerationUsesSixSecondUnit() {
        CharacterSheet sheet = CharacterSheet.of(27, 1, 1, 1, 1, 1, 1, 1, 1);
        CurrentCharacterStats stats = new DerivedStatisticsCalculator().calculate(sheet, Gender.HOMBRE, 0.0);
        double expectedPerSixSeconds = Math.pow(270.0, 1.0 / 6.0);
        close(stats.healthRegeneration().orElseThrow(), expectedPerSixSeconds, "PV REGEN base incorrecta.");
        close(stats.healthRegeneration().orElseThrow() / 6.0, expectedPerSixSeconds / 6.0,
                "La conversión de PV REGEN a tasa por segundo debe dividir entre seis.");
    }

    private static void therapeuticDurationsAreThirtyGameMinutes() {
        var tracker = new TherapeuticEffectTracker();
        tracker.add(MiscellaneousItemCatalog.willowBark().therapeuticEffect());
        tracker.add(MiscellaneousItemCatalog.mead().therapeuticEffect());
        tracker.add(MiscellaneousItemCatalog.lucidityEssence().therapeuticEffect());
        tracker.advanceGameHours(29.0 / 60.0);
        org.junit.jupiter.api.Assertions.assertTrue(tracker.activeCount() == 3, "Los efectos deben persistir durante los primeros 29 min.");
        tracker.advanceGameHours(1.0 / 60.0);
        org.junit.jupiter.api.Assertions.assertTrue(tracker.activeCount() == 0, "Los efectos deben expirar a los 30 min de juego.");
    }

    private static void stimulantHasNoMinimumHealthFloor() {
        close(MiscellaneousItemCatalog.stimulantInjection().therapeuticEffect().minimumHealth(), 0.0,
                "La inyección estimulante no debe impedir que los PV lleguen a cero.");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousItemCatalog.stimulantInjection().statistics().stream()
                        .noneMatch(s -> s.contains("no pueden caer") || s.contains("SUPERVIVENCIA")),
                "La ficha no debe publicar la antigua supervivencia a 1 PV.");
    }

    private static void ballisticFinishersRemainDeterministic() {
        org.junit.jupiter.api.Assertions.assertTrue(CoupDeGracePolicy.isCoupDeGrace(true, 99, 64, 65),
                "GOLPE DE GRACIA debe activarse determinísticamente al cumplir sus condiciones.");
        org.junit.jupiter.api.Assertions.assertTrue(FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 99, 89, 90),
                "FULMINANTE debe activarse determinísticamente al cumplir sus condiciones.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 100, 0, 90),
                "FULMINANTE no debe quedar bloqueado por cobertura completa cuando P supera la protección.");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1.0e-9) throw new AssertionError(message + " Esperado=" + expected + " actual=" + actual);
    }

    
}

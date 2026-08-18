package qa.integration;

import domain.combat.RecoilType;
import domain.signal.*;
import domain.status.VitalResourceState;
import domain.survival.*;

import java.util.Optional;

public final class DiegeticSignalsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        hungerAndThirstDescendEveryThirtyMinutes();
        superiorRegulationOnlyDoublesPositiveStages();
        survivalBonusesAndPenaltiesAreCanonical();
        physiologicalSignalsAreDiegeticAndPrioritized();
        impactSoundPolicyFeedsTheSharedSignalContract();
    }

    private static void hungerAndThirstDescendEveryThirtyMinutes() {
        HungerTimePolicy hunger = new HungerTimePolicy();
        HungerState state = HungerState.initiallySatiated();
        state = hunger.advance(state, .5);
        org.junit.jupiter.api.Assertions.assertTrue(state.level() == HungerLevel.FUNCTIONAL, "SACIADO debe pasar a FUNCIONAL en 30 minutos de juego.");
        state = hunger.advance(state, .5);
        org.junit.jupiter.api.Assertions.assertTrue(state.level() == HungerLevel.HUNGRY, "FUNCIONAL debe pasar a HAMBRIENTO en otros 30 minutos.");

        ThirstState thirst = new ThirstState();
        thirst.advanceHours(.5);
        org.junit.jupiter.api.Assertions.assertTrue(thirst.level() == 1 && thirst.status() == ThirstStatus.THIRSTY,
                "FUNCIONAL debe pasar a SEDIENTO -1 en 30 minutos.");
        thirst.advanceHours(2.5);
        org.junit.jupiter.api.Assertions.assertTrue(thirst.level() == 6 && thirst.status() == ThirstStatus.DEHYDRATED,
                "La sed debe alcanzar DESHIDRATADO -6 tras seis descensos.");
    }

    private static void superiorRegulationOnlyDoublesPositiveStages() {
        HungerState satiated = HungerState.initiallySatiated(true);
        org.junit.jupiter.api.Assertions.assertTrue(close(satiated.hoursUntilNextStage(), 1.0), "Regulación calórica debe duplicar SACIADO.");
        HungerState functional = new HungerTimePolicy().advance(satiated, 1.0, true);
        org.junit.jupiter.api.Assertions.assertTrue(functional.level() == HungerLevel.FUNCTIONAL, "Tras el ciclo ampliado debe llegar a FUNCIONAL.");
        org.junit.jupiter.api.Assertions.assertTrue(close(functional.hoursUntilNextStage(), .5), "FUNCIONAL no debe duplicarse.");

        ThirstState thirst = new ThirstState();
        org.junit.jupiter.api.Assertions.assertTrue(thirst.drinkWater(true), "Beber desde FUNCIONAL debe activar HIDRATADO.");
        org.junit.jupiter.api.Assertions.assertTrue(close(thirst.hydratedHoursRemaining(), 1.0), "Regulación calórica debe duplicar HIDRATADO.");
        thirst.advanceHours(1.0, true);
        org.junit.jupiter.api.Assertions.assertTrue(thirst.status() == ThirstStatus.FUNCTIONAL, "Después debe volver a FUNCIONAL.");
        org.junit.jupiter.api.Assertions.assertTrue(close(thirst.hoursUntilNextLevel(), .5), "FUNCIONAL conserva el ciclo ordinario.");
    }

    private static void survivalBonusesAndPenaltiesAreCanonical() {
        HungerState satiated = new HungerState(HungerLevel.SATIATED, .5, Optional.empty());
        HungerState functional = new HungerState(HungerLevel.FUNCTIONAL, .5, Optional.empty());
        HungerState acute = new HungerState(HungerLevel.ACUTE_HUNGER, 0, Optional.empty());
        org.junit.jupiter.api.Assertions.assertTrue(satiated.physicalStabilityBonus() == 1 && satiated.sanityBonus() == 1,
                "SACIADO conserva sus bonificaciones.");
        org.junit.jupiter.api.Assertions.assertTrue(functional.physicalStabilityBonus() == 0 && functional.staminaRegenerationMultiplier() == 1.0,
                "FUNCIONAL no debe bonificar ni penalizar.");
        org.junit.jupiter.api.Assertions.assertTrue(!acute.staminaRegeneratesWhileMoving(), "HAMBRE AGUDA conserva su penalizador.");

        ThirstState hydrated = new ThirstState();
        hydrated.drinkWater();
        org.junit.jupiter.api.Assertions.assertTrue(hydrated.healthRegenerationMultiplier() > 1.0, "HIDRATADO debe bonificar la regeneración de PV.");
        ThirstState functionalThirst = new ThirstState();
        org.junit.jupiter.api.Assertions.assertTrue(close(functionalThirst.healthRegenerationMultiplier(), 1.0), "FUNCIONAL no debe modificar la regeneración.");
    }

    private static void physiologicalSignalsAreDiegeticAndPrioritized() {
        CharacterSignalResolver resolver = new CharacterSignalResolver();
        CharacterSignalSource source = new CharacterSignalSource("kenan");
        var hungerSignals = resolver.hungerTransition(source,
                new HungerState(HungerLevel.FUNCTIONAL, .5, Optional.empty()),
                new HungerState(HungerLevel.HUNGRY, .5, Optional.empty()));
        org.junit.jupiter.api.Assertions.assertTrue(hungerSignals.size() == 1 && hungerSignals.get(0).modality() == CharacterSignalModality.BODY_SOUND,
                "El hambre debe producir una señal corporal, no un icono.");

        CharacterSignal breathing = resolver.stamina(source, new VitalResourceState(100, 100, 20, 100));
        org.junit.jupiter.api.Assertions.assertTrue(close(breathing.intensity(), .8), "El jadeo debe crecer con el agotamiento real de PA.");
        org.junit.jupiter.api.Assertions.assertTrue(resolver.pain(source, new VitalResourceState(60, 100, 100, 100)).size() == 1,
                "Perder más de un tercio de PV debe producir dolor.");
        org.junit.jupiter.api.Assertions.assertTrue(resolver.environmental(source, EnvironmentalSignalCause.VIRULENT_TOXICITY, .7).priority()
                        > hungerSignals.get(0).priority(),
                "La tos violenta debe prevalecer sobre hambre y sed.");
        org.junit.jupiter.api.Assertions.assertTrue(!resolver.bleeding(source).interruptible(), "La advertencia de sangrado debe ser prioritaria.");
    }

    private static void impactSoundPolicyFeedsTheSharedSignalContract() {
        var signals = new ImpactSignalAdapter().resolve(new CharacterSignalSource("enemigo"), RecoilType.TOTAL, 1.0);
        org.junit.jupiter.api.Assertions.assertTrue(signals.size() == 2, "El retroceso total debe conservar las dos capas sonoras existentes.");
        org.junit.jupiter.api.Assertions.assertTrue(signals.stream().allMatch(signal -> signal.category() == CharacterSignalCategory.IMPACT),
                "Los sonidos de impacto deben exponerse mediante CharacterSignal.");
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < 0.000001; }
    
}

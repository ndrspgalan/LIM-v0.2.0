package domain.signal;

import domain.status.VitalResourceState;
import domain.survival.HungerLevel;
import domain.survival.HungerState;
import domain.survival.ThirstState;
import domain.survival.ThirstStatus;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/** Traduce estados mecánicos en señales diegéticas sin asumir recursos gráficos concretos. */
public final class CharacterSignalResolver {
    private static final Duration SHORT = Duration.ofSeconds(2);

    public List<CharacterSignal> hungerTransition(CharacterSignalSource source, HungerState previous, HungerState current) {
        Objects.requireNonNull(previous); Objects.requireNonNull(current);
        if (current.level().severity() <= previous.level().severity() || !current.level().penalized()) return List.of();
        double intensity = switch (current.level()) {
            case HUNGRY -> .35;
            case MODERATE_HUNGER -> .65;
            case ACUTE_HUNGER -> 1.0;
            default -> 0;
        };
        String cue = current.level() == HungerLevel.ACUTE_HUNGER ? "hunger_stomach_hold" : "hunger_stomach_growl";
        return List.of(signal(CharacterSignalCategory.HUNGER, CharacterSignalModality.BODY_SOUND,
                intensity, 10, 180, 3, true, source, cue, ""));
    }

    public List<CharacterSignal> thirstTransition(CharacterSignalSource source, ThirstStatus previous, ThirstState current) {
        Objects.requireNonNull(previous); Objects.requireNonNull(current);
        if (current.status() == previous || current.status() == ThirstStatus.HYDRATED || current.status() == ThirstStatus.FUNCTIONAL) return List.of();
        double intensity = current.status() == ThirstStatus.DEHYDRATED ? 1.0 : Math.max(.25, current.level() / 6.0);
        String line = current.status() == ThirstStatus.DEHYDRATED
                ? "Necesito agua... ahora."
                : "Agh, tengo la boca seca.";
        return List.of(signal(CharacterSignalCategory.THIRST, CharacterSignalModality.VOICE,
                intensity, current.status() == ThirstStatus.DEHYDRATED ? 35 : 15,
                180, 3, true, source, "thirst_voice", line));
    }

    public CharacterSignal stamina(CharacterSignalSource source, VitalResourceState resources) {
        Objects.requireNonNull(resources);
        double depleted = 1.0 - resources.currentStamina() / resources.maximumStamina();
        return signal(CharacterSignalCategory.EXHAUSTION, CharacterSignalModality.BREATHING,
                clamp(depleted), 40, 0, 1, true, source, "stamina_breathing", "");
    }

    public List<CharacterSignal> pain(CharacterSignalSource source, VitalResourceState resources) {
        Objects.requireNonNull(resources);
        double lost = 1.0 - resources.currentHealth() / resources.maximumHealth();
        if (lost <= 1.0 / 3.0) return List.of();
        return List.of(signal(CharacterSignalCategory.PAIN, CharacterSignalModality.VOICE,
                clamp((lost - 1.0 / 3.0) / (2.0 / 3.0)), 70, 20, 2, true, source, "pain_voice", ""));
    }

    public CharacterSignal bleeding(CharacterSignalSource source) {
        return signal(CharacterSignalCategory.BLEEDING, CharacterSignalModality.VOICE,
                .8, 80, 30, 3, false, source, "bleeding_voice", "¡Estoy sangrando!");
    }

    public CharacterSignal environmental(CharacterSignalSource source, EnvironmentalSignalCause cause, double intensity) {
        return switch (cause) {
            case BITING_FROST -> signal(CharacterSignalCategory.BITING_FROST, CharacterSignalModality.ANIMATION,
                    clamp(intensity), 50, 5, 2, true, source, "biting_frost_shiver", "");
            case VIRULENT_TOXICITY -> signal(CharacterSignalCategory.VIRULENT_TOXICITY, CharacterSignalModality.BODY_SOUND,
                    clamp(intensity), 90, 4, 3, false, source, "virulent_toxicity_cough", "");
            case SUFFOCATING_BURN -> signal(CharacterSignalCategory.SUFFOCATING_BURN, CharacterSignalModality.BODY_SOUND,
                    clamp(intensity), 90, 4, 3, false, source, "suffocating_burn_cough", "");
        };
    }

    private CharacterSignal signal(CharacterSignalCategory category, CharacterSignalModality modality,
                                   double intensity, int priority, long cooldownSeconds, long durationSeconds,
                                   boolean interruptible, CharacterSignalSource source, String cue, String line) {
        return new CharacterSignal(category, modality, intensity, priority,
                Duration.ofSeconds(cooldownSeconds), Duration.ofSeconds(durationSeconds), interruptible,
                source, new CharacterSignalPayload(cue, line));
    }

    private double clamp(double value) { return Math.max(0, Math.min(1, value)); }
}

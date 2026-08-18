package domain.signal;

import java.time.Duration;
import java.util.Objects;

public record CharacterSignal(
        CharacterSignalCategory category,
        CharacterSignalModality modality,
        double intensity,
        int priority,
        Duration cooldown,
        Duration duration,
        boolean interruptible,
        CharacterSignalSource source,
        CharacterSignalPayload payload
) {
    public CharacterSignal {
        Objects.requireNonNull(category); Objects.requireNonNull(modality);
        Objects.requireNonNull(cooldown); Objects.requireNonNull(duration);
        Objects.requireNonNull(source); Objects.requireNonNull(payload);
        if (intensity < 0 || intensity > 1) throw new IllegalArgumentException("La intensidad debe estar entre 0 y 1.");
        if (priority < 0) throw new IllegalArgumentException("La prioridad no puede ser negativa.");
        if (cooldown.isNegative() || duration.isNegative()) throw new IllegalArgumentException("Los tiempos no pueden ser negativos.");
    }
}

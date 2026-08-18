package domain.combat.ai.declarative;

import java.util.Objects;

/** Relación causal tipada expuesta por un objeto utilizable. */
public record ConsumableEffectFact(String key, String value) {
    public ConsumableEffectFact {
        Objects.requireNonNull(key); Objects.requireNonNull(value);
        if(key.isBlank() || value.isBlank()) throw new IllegalArgumentException("El efecto declarativo no puede estar vacío.");
    }
}

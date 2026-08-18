package domain.combat.ai.declarative;

import java.util.Objects;

/** Efecto temporal que LIM ya mantiene como estado persistente del encuentro/personaje. */
public record ActiveEffectFact(String name, double remaining, String timeScale) {
    public ActiveEffectFact {
        Objects.requireNonNull(name); Objects.requireNonNull(timeScale);
        if(name.isBlank() || timeScale.isBlank() || remaining < 0) throw new IllegalArgumentException("Efecto activo no válido.");
    }
}

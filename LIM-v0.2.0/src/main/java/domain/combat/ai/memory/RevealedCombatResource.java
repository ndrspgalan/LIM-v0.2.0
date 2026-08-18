package domain.combat.ai.memory;

import java.util.Objects;

/** Hecho observado durante el encuentro; no implica que el recurso siga disponible. */
public record RevealedCombatResource(String actorId, String resourceId, double observedAtSeconds) {
    public RevealedCombatResource {
        Objects.requireNonNull(actorId); Objects.requireNonNull(resourceId);
        if (!Double.isFinite(observedAtSeconds) || observedAtSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
    }
}

package domain.combat.ai.declarative;

import domain.combat.moveset.TransitionContinuity;
import java.util.Objects;

/** Relación cinética factual entre el último movimiento y un candidato. */
public record TransitionFact(TransitionContinuity continuity, double executionTimeMultiplier, String rationale) {
    public TransitionFact {
        Objects.requireNonNull(continuity);
        if (!Double.isFinite(executionTimeMultiplier) || executionTimeMultiplier <= 0) throw new IllegalArgumentException("Multiplicador temporal inválido.");
        if (rationale == null || rationale.isBlank()) throw new IllegalArgumentException("Racional cinético obligatorio.");
    }
    public static TransitionFact neutral(String rationale) { return new TransitionFact(TransitionContinuity.NEUTRAL, 1.0, rationale); }
}

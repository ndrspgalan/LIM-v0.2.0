package domain.combat.ai.declarative;

import java.util.Objects;

/** Evidencia sensorial sin interpretación táctica. */
public record SensoryFact(KnowledgeOrigin origin, KnowledgeTemporalState temporalState, double ageSeconds, double intensity, String detail) {
    public SensoryFact {
        Objects.requireNonNull(origin); Objects.requireNonNull(temporalState); detail=Objects.requireNonNull(detail);
        if ((!Double.isFinite(ageSeconds) && !Double.isInfinite(ageSeconds)) || ageSeconds<0) throw new IllegalArgumentException("Edad inválida.");
        if (!Double.isFinite(intensity)||intensity<0||intensity>1) throw new IllegalArgumentException("Intensidad inválida.");
    }
}

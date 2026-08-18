package domain.ability;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Resultado mecánico, serializable en términos de dominio y no limitado a un texto. */
public record MasteryExecutionResult(
        MasteryExecutionStatus status,
        String manifestationId,
        String message,
        Map<String, Double> resourceDeltas,
        List<MasteryEffect> effects
) {
    public MasteryExecutionResult {
        Objects.requireNonNull(status);
        manifestationId = manifestationId == null ? "" : manifestationId;
        message = message == null ? "" : message;
        resourceDeltas = Map.copyOf(resourceDeltas == null ? Map.of() : resourceDeltas);
        effects = List.copyOf(effects == null ? List.of() : effects);
    }
    public boolean successful() { return status != MasteryExecutionStatus.REJECTED; }
    public static MasteryExecutionResult rejected(String id, String message) {
        return new MasteryExecutionResult(MasteryExecutionStatus.REJECTED, id, message, Map.of(), List.of());
    }
    public static MasteryExecutionResult of(MasteryExecutionStatus status, String id, String message,
                                            Map<String, Double> deltas, List<MasteryEffect> effects) {
        return new MasteryExecutionResult(status, id, message, deltas, effects);
    }
    public MasteryActionResult toActionResult() { return new MasteryActionResult(successful(), message); }
}

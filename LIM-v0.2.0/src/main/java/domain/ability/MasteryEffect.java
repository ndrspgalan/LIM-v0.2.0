package domain.ability;

import java.util.Map;

/** Efecto producido por una maestría y consumible por combate, movimiento o mundo. */
public record MasteryEffect(
        String id,
        String sourceManifestationId,
        String targetId,
        double remainingRealSeconds,
        boolean sustained,
        Map<String, Double> magnitudes
) {
    public MasteryEffect {
        if (id == null || id.isBlank() || sourceManifestationId == null || sourceManifestationId.isBlank())
            throw new IllegalArgumentException("Un efecto debe tener identidad y origen.");
        targetId = targetId == null ? "" : targetId;
        if (!Double.isFinite(remainingRealSeconds) || remainingRealSeconds < 0)
            throw new IllegalArgumentException("Duración inválida.");
        magnitudes = Map.copyOf(magnitudes == null ? Map.of() : magnitudes);
    }
    public MasteryEffect tick(double seconds) {
        if (sustained) return this;
        return new MasteryEffect(id, sourceManifestationId, targetId,
                Math.max(0, remainingRealSeconds - seconds), false, magnitudes);
    }
    public boolean expired() { return !sustained && remainingRealSeconds <= 0; }
}

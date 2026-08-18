package domain.combat.runic;

public record ResonanceResult(boolean triggered, double rawCurseDamage) {
    public ResonanceResult {
        if (!Double.isFinite(rawCurseDamage) || rawCurseDamage < 0) {
            throw new IllegalArgumentException("El daño de Resonancia debe ser finito y no negativo.");
        }
    }
    public static ResonanceResult inactive() { return new ResonanceResult(false, 0.0); }
}

package domain.combat;

/** Resultado mecánico común de DESVIAR/PARRY y Mirror Parry. */
public record ParryResolution(
        boolean successful,
        boolean attackInterrupted,
        double stunDurationSeconds,
        double recoilDistanceMeters,
        String reason
) {
    public ParryResolution {
        if (!Double.isFinite(stunDurationSeconds) || stunDurationSeconds < 0) throw new IllegalArgumentException("La duración debe ser finita y no negativa.");
        if (!Double.isFinite(recoilDistanceMeters) || recoilDistanceMeters < 0) throw new IllegalArgumentException("El retroceso debe ser finito y no negativo.");
        reason = reason == null ? "" : reason;
    }
    public static ParryResolution rejected(String reason) { return new ParryResolution(false,false,0,0,reason); }
    public static final double PARRY_STUN_SECONDS = 2.0;
    /** Compatibilidad: DESTREZA 20 y sin unidades de retroceso declaradas. */
    public static ParryResolution success() { return success(PARRY_STUN_SECONDS,0.0); }
    public static ParryResolution success(double stunSeconds, double recoilDistanceMeters) {
        return new ParryResolution(true,true,stunSeconds,recoilDistanceMeters,
                "El ataque rival queda interrumpido por DESVIAR/Mirror Parry.");
    }
}

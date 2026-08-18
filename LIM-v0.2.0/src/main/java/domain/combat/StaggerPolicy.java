package domain.combat;

/**
 * Política universal de retroceso/aturdimiento.
 * En combate melee la magnitud física canónica es FUERZA + masa efectiva del ataque.
 * Otros sistemas (explosiones, presión mental, etc.) pueden seguir entregando directamente
 * una magnitud equivalente ya resuelta mediante {@link #resolve(double)}.
 */
public final class StaggerPolicy {
    public static final double MIN_DURATION_SECONDS = 0.5;
    public static final double MAX_DURATION_SECONDS = 2.0;
    public static final double MIN_KNOCKBACK_DISTANCE_METERS = 0.5;
    public static final double MAX_KNOCKBACK_DISTANCE_METERS = 2.0;
    public static final double MAX_FORCE_EQUIVALENT = 50.0;
    private StaggerPolicy() {}

    /** Magnitud canónica de stagger para un impacto melee: FUERZA + masa efectiva. */
    public static double meleeForceEquivalent(double strength, double effectiveAttackMassKg) {
        validateComponent(strength, "La FUERZA");
        validateComponent(effectiveAttackMassKg, "La masa efectiva");
        return strength + effectiveAttackMassKg;
    }

    /** Resuelve directamente un impacto melee a partir de FUERZA y masa efectiva. */
    public static StaggerResult resolveMelee(double strength, double effectiveAttackMassKg) {
        return resolve(meleeForceEquivalent(strength, effectiveAttackMassKg));
    }

    public static StaggerResult resolve(double forceEquivalent) {
        validate(forceEquivalent);
        if (forceEquivalent == 0) return new StaggerResult(0.0,0.0);
        double n=normalized(forceEquivalent);
        double distance=MIN_KNOCKBACK_DISTANCE_METERS+(MAX_KNOCKBACK_DISTANCE_METERS-MIN_KNOCKBACK_DISTANCE_METERS)*n;
        double duration=MIN_DURATION_SECONDS+(MAX_DURATION_SECONDS-MIN_DURATION_SECONDS)*n;
        return new StaggerResult(distance,duration);
    }
    public static double knockbackDistanceMeters(double forceEquivalent){return resolve(forceEquivalent).knockbackDistanceMeters();}
    private static double normalized(double value){return (Math.min(MAX_FORCE_EQUIVALENT,Math.max(1.0,value))-1.0)/(MAX_FORCE_EQUIVALENT-1.0);}
    private static void validate(double value){if(!Double.isFinite(value)||value<0)throw new IllegalArgumentException("La magnitud equivalente de retroceso debe ser finita y no negativa.");}
    private static void validateComponent(double value,String label){if(!Double.isFinite(value)||value<0)throw new IllegalArgumentException(label+" debe ser finita y no negativa.");}
}

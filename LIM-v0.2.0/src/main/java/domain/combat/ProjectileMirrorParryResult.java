package domain.combat;

/** Resultado del Mirror Parry helicoidal contra un proyectil de arma a distancia. */
public record ProjectileMirrorParryResult(
        boolean successful,
        boolean originalTrajectoryCancelled,
        boolean redirectedTowardShooter,
        double lateralDeflectionDegrees
) {
    public static ProjectileMirrorParryResult rejected() {
        return new ProjectileMirrorParryResult(false, false, false, 0.0);
    }
}

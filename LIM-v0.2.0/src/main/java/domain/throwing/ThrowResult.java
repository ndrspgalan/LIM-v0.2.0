package domain.throwing;

import domain.combat.PhysicalDamage;

import java.util.Objects;

public record ThrowResult(
        ThrownPayload payload,
        double releaseHeightMeters,
        double releaseVelocityMetersPerSecond,
        double horizontalDistanceMeters,
        double impactVelocityMetersPerSecond,
        double impactEnergyJoules,
        PhysicalDamage damage
) {
    public ThrowResult {
        payload = Objects.requireNonNull(payload, "La unidad lanzada no puede ser nula.");
        damage = Objects.requireNonNull(damage, "El daño no puede ser nulo.");
        if (releaseHeightMeters < 0 || releaseVelocityMetersPerSecond < 0 || horizontalDistanceMeters < 0
                || impactVelocityMetersPerSecond < 0 || impactEnergyJoules < 0) {
            throw new IllegalArgumentException("Los resultados físicos del lanzamiento no pueden ser negativos.");
        }
    }
}

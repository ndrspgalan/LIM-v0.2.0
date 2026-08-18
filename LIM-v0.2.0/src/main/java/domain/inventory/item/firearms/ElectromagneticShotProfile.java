package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;

import java.util.Objects;

/** Perfil dinámico del próximo disparo bifilar en el estado exacto de carga actual. */
public record ElectromagneticShotProfile(
        double crankTurns,
        double kineticEnergyJoules,
        LethalityProfile lethality,
        double effectiveRangeMeters,
        double thermalLockSeconds,
        double recoilVelocityMps
) {
    public ElectromagneticShotProfile {
        if (!Double.isFinite(crankTurns) || crankTurns < 0) throw new IllegalArgumentException("Vueltas inválidas.");
        if (!Double.isFinite(kineticEnergyJoules) || kineticEnergyJoules < 0) throw new IllegalArgumentException("Energía inválida.");
        Objects.requireNonNull(lethality, "La letalidad no puede ser nula.");
        if (!Double.isFinite(effectiveRangeMeters) || effectiveRangeMeters < 0) throw new IllegalArgumentException("Alcance inválido.");
        if (!Double.isFinite(thermalLockSeconds) || thermalLockSeconds < 0) throw new IllegalArgumentException("Bloqueo térmico inválido.");
        if (!Double.isFinite(recoilVelocityMps) || recoilVelocityMps < 0) throw new IllegalArgumentException("Retroceso inválido.");
    }
}

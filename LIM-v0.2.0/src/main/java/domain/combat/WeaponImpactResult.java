package domain.combat;

import domain.audio.ImpactSoundCue;

import java.util.Objects;

public record WeaponImpactResult(
        boolean durabilityReduced,
        ImpactSoundCue soundCue
) {
    public WeaponImpactResult {
        Objects.requireNonNull(soundCue, "La señal sonora no puede ser nula.");
    }
}

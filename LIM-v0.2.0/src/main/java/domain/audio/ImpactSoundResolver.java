package domain.audio;

import domain.combat.RecoilType;

import java.util.List;
import java.util.Objects;

public final class ImpactSoundResolver {
    private static final double CLACK_FIXED_INTENSITY = 0.45;

    public ImpactSoundCue resolve(RecoilType recoilType, double relativeRecoilIntensity) {
        Objects.requireNonNull(recoilType, "El tipo de retroceso no puede ser nulo.");
        return switch (recoilType) {
            case NONE -> new ImpactSoundCue(List.of(
                    new ImpactSoundCue.SoundLayer(
                            ImpactSound.CLACK,
                            SoundIntensity.fixed(CLACK_FIXED_INTENSITY)
                    )
            ));
            case RELATIVE -> new ImpactSoundCue(List.of(
                    new ImpactSoundCue.SoundLayer(
                            ImpactSound.CLANG,
                            new SoundIntensity(clamp(relativeRecoilIntensity))
                    )
            ));
            case TOTAL -> new ImpactSoundCue(List.of(
                    new ImpactSoundCue.SoundLayer(
                            ImpactSound.MUFFLED_GRUNT,
                            SoundIntensity.maximum()
                    ),
                    new ImpactSoundCue.SoundLayer(
                            ImpactSound.CLANG,
                            SoundIntensity.maximum()
                    )
            ));
        };
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }
}

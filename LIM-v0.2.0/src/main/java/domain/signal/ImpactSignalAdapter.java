package domain.signal;

import domain.audio.ImpactSoundCue;
import domain.audio.ImpactSoundResolver;
import domain.combat.RecoilType;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/** Reutiliza la política sonora de retroceso como productor del contrato común de señales. */
public final class ImpactSignalAdapter {
    private final ImpactSoundResolver soundResolver;
    public ImpactSignalAdapter() { this(new ImpactSoundResolver()); }
    public ImpactSignalAdapter(ImpactSoundResolver soundResolver) { this.soundResolver = Objects.requireNonNull(soundResolver); }

    public List<CharacterSignal> resolve(CharacterSignalSource source, RecoilType recoilType, double relativeIntensity) {
        ImpactSoundCue cue = soundResolver.resolve(recoilType, relativeIntensity);
        return cue.layers().stream().map(layer -> new CharacterSignal(
                CharacterSignalCategory.IMPACT,
                CharacterSignalModality.IMPACT_SOUND,
                layer.intensity().normalizedValue(),
                recoilType == RecoilType.TOTAL ? 100 : 60,
                Duration.ZERO,
                Duration.ofSeconds(1),
                false,
                source,
                CharacterSignalPayload.cue("impact_" + layer.sound().name().toLowerCase())
        )).toList();
    }
}

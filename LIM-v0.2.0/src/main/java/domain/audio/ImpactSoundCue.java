package domain.audio;

import java.util.List;

public record ImpactSoundCue(List<SoundLayer> layers) {
    public ImpactSoundCue {
        layers = List.copyOf(layers);
        if (layers.isEmpty()) {
            throw new IllegalArgumentException("Una señal sonora debe contener al menos una capa.");
        }
    }

    public record SoundLayer(ImpactSound sound, SoundIntensity intensity) {
        public SoundLayer {
            if (sound == null || intensity == null) {
                throw new IllegalArgumentException("El sonido y su intensidad son obligatorios.");
            }
        }
    }
}

package domain.bestiarium.physical_plane.aspirant;

import java.util.Objects;

/**
 * Dos distancias independientes. La solución extrema canónica no converge hasta convertirse
 * en el animal: queda en una síntesis animaloide donde la anatomía humana y la zoológica ya
 * no pueden separarse limpiamente.
 */
public record AspirantMorphology(
        ConvergentAnimalReference convergentReference,
        AnthropometricDeviation fromHuman,
        AnthropometricDeviation fromAnimal) {
    public AspirantMorphology {
        Objects.requireNonNull(convergentReference);
        Objects.requireNonNull(fromHuman);
        Objects.requireNonNull(fromAnimal);
    }

    public AspirantMorphologyRegion region() {
        int h = fromHuman.steps(), a = fromAnimal.steps();
        if (h <= 3 && a >= 6) return AspirantMorphologyRegion.HUMAN_DOMINANT;
        if (h >= 7 && a >= 7) return AspirantMorphologyRegion.INCOHERENT_CONFIGURATION;
        if (h >= 7 && a <= 5) return AspirantMorphologyRegion.ANIMALOID_SYNTHESIS;
        return AspirantMorphologyRegion.HYBRID_INTEGRATED;
    }

    /** Un diseño canónico nunca es exactamente animal ni una aberración incoherente. */
    public boolean canonicalSynthesis() {
        return fromAnimal.steps() >= 2 && region() != AspirantMorphologyRegion.INCOHERENT_CONFIGURATION;
    }
}

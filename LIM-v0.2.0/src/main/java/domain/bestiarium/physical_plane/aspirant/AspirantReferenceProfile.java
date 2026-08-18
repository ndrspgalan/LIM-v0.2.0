package domain.bestiarium.physical_plane.aspirant;

import java.util.Objects;

public record AspirantReferenceProfile(
        AspirantReferenceId id,
        ConvergentAnimalReference animalReference,
        AspirantEvolutionaryAffinity evolutionaryAffinity,
        AspirantEcologicalProfile ecology,
        String anatomicalSynthesis) {
    public AspirantReferenceProfile {
        Objects.requireNonNull(id);
        Objects.requireNonNull(animalReference);
        Objects.requireNonNull(evolutionaryAffinity);
        Objects.requireNonNull(ecology);
        if (anatomicalSynthesis == null || anatomicalSynthesis.isBlank())
            throw new IllegalArgumentException("La arquitectura ASPIRANT necesita una síntesis anatómica.");
    }

    public int minimumCambiaformasHumanDeviation() {
        return evolutionaryAffinity.minimumCambiaformasHumanDeviation();
    }
}

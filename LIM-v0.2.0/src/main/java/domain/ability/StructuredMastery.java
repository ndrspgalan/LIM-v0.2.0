package domain.ability;

import domain.character.CharacterClass;
import java.util.List;
import java.util.Objects;

public record StructuredMastery(
        MasteryId id,
        String name,
        String narrativeDescription,
        MasteryStructure structure,
        CharacterClass resonanceClass,
        List<MasteryStage> stages
) implements Mastery {
    public StructuredMastery {
        Objects.requireNonNull(id, "El identificador no puede ser nulo.");
        name = requireText(name, "El nombre");
        narrativeDescription = requireText(narrativeDescription, "La descripción narrativa");
        Objects.requireNonNull(structure, "La estructura no puede ser nula.");
        if (structure == MasteryStructure.PAIRS || structure == MasteryStructure.BRANCHED) throw new IllegalArgumentException("Use PairMastery o TransmutationMastery para esta estructura.");
        Objects.requireNonNull(resonanceClass, "La clase de resonancia no puede ser nula.");
        stages = List.copyOf(Objects.requireNonNull(stages, "Las manifestaciones no pueden ser nulas."));
        if (stages.isEmpty()) throw new IllegalArgumentException("Una familia debe contener al menos una manifestación.");
    }
    private static String requireText(String value, String label) {
        Objects.requireNonNull(value, label + " no puede ser nulo.");
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException(label + " no puede estar vacío.");
        return normalized;
    }
}

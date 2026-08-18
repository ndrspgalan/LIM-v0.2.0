package domain.ability;

import domain.character.CharacterClass;
import java.util.Objects;

public record PairMastery(
        MasteryId id,
        String name,
        String narrativeDescription,
        CharacterClass resonanceClass,
        MasteryVariant original,
        MasteryVariant refined
) implements Mastery {
    public PairMastery {
        Objects.requireNonNull(id, "El identificador no puede ser nulo.");
        name = requireText(name, "El nombre");
        narrativeDescription = requireText(narrativeDescription, "La descripción narrativa");
        Objects.requireNonNull(resonanceClass, "La clase de resonancia no puede ser nula.");
        Objects.requireNonNull(original, "La manifestación original no puede ser nula.");
        Objects.requireNonNull(refined, "La manifestación refinada no puede ser nula.");
        if (original.refined() || !refined.refined()) throw new IllegalArgumentException("El par debe contener una manifestación original y otra refinada.");
    }
    @Override public MasteryStructure structure() { return MasteryStructure.PAIRS; }
    private static String requireText(String value, String label) {
        Objects.requireNonNull(value, label + " no puede ser nulo.");
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException(label + " no puede estar vacío.");
        return normalized;
    }
}

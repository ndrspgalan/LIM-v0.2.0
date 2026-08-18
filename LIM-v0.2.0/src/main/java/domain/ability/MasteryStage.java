package domain.ability;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Nodo o modo de una familia que no usa el contrato histórico de pares. */
public record MasteryStage(
        String name,
        Set<MasteryType> natures,
        Attribute progressionAttribute,
        int threshold,
        String narrativeDescription,
        String mechanicalDescription
) {
    public MasteryStage {
        name = requireText(name, "El nombre");
        natures = Set.copyOf(Objects.requireNonNull(natures, "Las naturalezas no pueden ser nulas."));
        if (natures.isEmpty()) throw new IllegalArgumentException("Una manifestación debe tener al menos una naturaleza.");
        narrativeDescription = normalizeOptional(narrativeDescription);
        mechanicalDescription = requireText(mechanicalDescription, "La descripción mecánica");
        if (progressionAttribute == null && threshold != 0) throw new IllegalArgumentException("Sin atributo, el umbral debe ser cero.");
        if (progressionAttribute != null && (threshold < 1 || threshold > 120)) throw new IllegalArgumentException("El umbral debe estar entre 1 y 120.");
    }
    public static MasteryStage of(String name, MasteryType nature, Attribute attribute, int threshold, String mechanicalDescription) {
        return new MasteryStage(name, EnumSet.of(nature), attribute, threshold, "", mechanicalDescription);
    }
    public static MasteryStage of(String name, MasteryType nature, Attribute attribute, int threshold, String narrativeDescription, String mechanicalDescription) {
        return new MasteryStage(name, EnumSet.of(nature), attribute, threshold, narrativeDescription, mechanicalDescription);
    }
    public boolean isAccessibleTo(CharacterSheet sheet) { return progressionAttribute == null || sheet.valueOf(progressionAttribute) >= threshold; }
    public Optional<Attribute> progressionAttributeOptional() { return Optional.ofNullable(progressionAttribute); }
    public Optional<String> narrativeDescriptionOptional() { return narrativeDescription.isBlank() ? Optional.empty() : Optional.of(narrativeDescription); }
    private static String requireText(String value, String label) { Objects.requireNonNull(value, label + " no puede ser nulo."); String n=value.trim(); if(n.isEmpty()) throw new IllegalArgumentException(label + " no puede estar vacío."); return n; }
    private static String normalizeOptional(String value) { return value == null ? "" : value.trim(); }
}

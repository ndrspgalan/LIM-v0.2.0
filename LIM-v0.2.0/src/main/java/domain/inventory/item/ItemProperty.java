package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.knowledge.PropertyKnowledgePolicy;

import java.util.Objects;
import java.util.Optional;

/**
 * Propiedad narrativa tipada de un objeto. La propiedad explica por qué existen
 * sus estadísticas y separa visibilidad, requisito de activación y efecto.
 */
public record ItemProperty(
        ItemPropertyId id,
        String name,
        String narrativeDescription,
        boolean hidden,
        Attribute activationAttribute,
        int activationMinimum,
        boolean activationRequirementHidden,
        String effectiveStatistic
) {
    public ItemProperty {
        Objects.requireNonNull(id, "La identidad de la propiedad no puede ser nula.");
        name = requireText(name, "El nombre de la propiedad no puede estar vacío.");
        Objects.requireNonNull(narrativeDescription, "La descripción de la propiedad no puede ser nula.");
        narrativeDescription = narrativeDescription.trim();
        effectiveStatistic = requireText(
                effectiveStatistic,
                "La estadística efectiva no puede estar vacía."
        );
        if (activationAttribute == null && activationMinimum != 0) {
            throw new IllegalArgumentException("Una propiedad sin atributo de activación debe usar umbral cero.");
        }
        if (activationAttribute != null && (activationMinimum < 1 || activationMinimum > 120)) {
            throw new IllegalArgumentException("El umbral de activación debe estar entre 1 y 120.");
        }
    }

    public static ItemProperty alwaysActive(
            ItemPropertyId id,
            String name,
            String narrativeDescription,
            String effectiveStatistic
    ) {
        return new ItemProperty(id, name, narrativeDescription, false, null, 0, false, effectiveStatistic);
    }

    public static ItemProperty hidden(
            ItemPropertyId id,
            String name,
            String narrativeDescription,
            Attribute activationAttribute,
            int activationMinimum,
            String effectiveStatistic
    ) {
        return new ItemProperty(
                id,
                name,
                narrativeDescription,
                true,
                activationAttribute,
                activationMinimum,
                false,
                effectiveStatistic
        );
    }

    /** Propiedad y requisito mecánico ocultos: la inspección nunca imprime atributo ni umbral. */
    public static ItemProperty hiddenWithHiddenRequirement(
            ItemPropertyId id,
            String name,
            String narrativeDescription,
            Attribute activationAttribute,
            int activationMinimum,
            String effectiveStatistic
    ) {
        return new ItemProperty(
                id, name, narrativeDescription, true, activationAttribute, activationMinimum, true, effectiveStatistic
        );
    }

    public boolean isVisibleTo(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return PropertyKnowledgePolicy.visible(sheet,hidden,activationAttribute,activationMinimum);
    }

    public boolean isActiveFor(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        if (!isVisibleTo(sheet)) return false;
        return activationAttribute == null || PropertyKnowledgePolicy.requirementMet(sheet,activationAttribute,activationMinimum);
    }

    public Optional<Attribute> activationAttributeOptional() {
        return Optional.ofNullable(activationAttribute);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }
}

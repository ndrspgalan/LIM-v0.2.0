package domain.inventory.item;

import java.util.Objects;

public record HiddenItemPropertyDefinition(
        int requiredClairvoyance,
        String propertyName,
        String narrativeDescription,
        String effectiveStatistic
) {
    public HiddenItemPropertyDefinition {
        if (requiredClairvoyance < 1 || requiredClairvoyance > 120) {
            throw new IllegalArgumentException("La Clarividencia requerida debe estar entre 1 y 120.");
        }
        propertyName = requireText(propertyName, "El nombre de la propiedad no puede estar vacío.");
        narrativeDescription = requireText(narrativeDescription, "La descripción no puede estar vacía.");
        effectiveStatistic = requireText(effectiveStatistic, "El efecto no puede estar vacío.");
    }

    public ItemProperty asHiddenProperty() {
        return ItemProperty.hidden(
                ItemPropertyId.GENERIC,
                propertyName,
                narrativeDescription,
                domain.character.sheet.Attribute.CLARIVIDENCIA,
                requiredClairvoyance,
                effectiveStatistic
        );
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

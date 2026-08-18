package domain.inventory.catalog;

import java.util.Locale;
import java.util.Objects;

public record CanonicalObjectTypeId(String value) {
    public CanonicalObjectTypeId {
        Objects.requireNonNull(value, "El id canónico no puede ser nulo.");
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.isEmpty() || !value.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException("Id canónico inválido: " + value);
        }
    }

    public static CanonicalObjectTypeId of(String value) { return new CanonicalObjectTypeId(value); }
}

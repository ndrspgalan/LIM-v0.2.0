package domain.inventory.item.meleeWeapons;

import java.util.Objects;

/**
 * Ficha canónica básica de un arma/herramienta cuerpo a cuerpo cuyo dimensionado
 * técnico se cerrará en una iteración posterior.
 */
public record MeleeWeaponBasicDefinition(
        String name,
        String functionalCategory,
        String narrativeDescription
) {
    public MeleeWeaponBasicDefinition {
        name = Objects.requireNonNull(name, "El nombre no puede ser nulo.").trim();
        functionalCategory = Objects.requireNonNull(functionalCategory, "La categoría funcional no puede ser nula.").trim();
        narrativeDescription = Objects.requireNonNull(narrativeDescription, "La descripción narrativa no puede ser nula.").trim();
        if (name.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (functionalCategory.isEmpty()) throw new IllegalArgumentException("La categoría funcional no puede estar vacía.");
        if (narrativeDescription.isEmpty()) throw new IllegalArgumentException("La descripción narrativa no puede estar vacía.");
    }
}

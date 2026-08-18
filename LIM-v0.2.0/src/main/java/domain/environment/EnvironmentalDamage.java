package domain.environment;

import domain.combat.DamageCategory;
import domain.combat.DamageType;

import java.util.Objects;

public record EnvironmentalDamage(DamageType type, double amount) {
    public EnvironmentalDamage {
        Objects.requireNonNull(type, "El tipo de daño ambiental no puede ser nulo.");
        if (type.category() != DamageCategory.NON_CONVENTIONAL_PHYSICAL) {
            throw new IllegalArgumentException("El daño ambiental directo debe ser físico no convencional.");
        }
        if (amount < 0) throw new IllegalArgumentException("El daño ambiental no puede ser negativo.");
    }
}

package domain.character.sheet;

import java.util.Objects;
import java.util.OptionalDouble;

public record CurrentCharacterStats(
        OptionalDouble totalHealth,
        OptionalDouble healthRegeneration,
        OptionalDouble physicalStability,
        OptionalDouble sanity,
        OptionalDouble totalStamina,
        OptionalDouble staminaRegeneration,
        OptionalDouble currentLoadKg,
        OptionalDouble maximumLoadKg,
        DamageResistanceProfile resistances
) {
    public CurrentCharacterStats {
        totalHealth = requireNonNegative(totalHealth, "PV TOTAL");
        healthRegeneration = requireNonNegative(healthRegeneration, "PV REGEN");
        physicalStability = requireNonNegative(physicalStability, "ESTABILIDAD FÍSICA");
        sanity = requireNonNegative(sanity, "CORDURA");
        totalStamina = requireNonNegative(totalStamina, "PA TOTAL");
        staminaRegeneration = requireNonNegative(staminaRegeneration, "PA REGEN");
        currentLoadKg = requireNonNegative(currentLoadKg, "carga actual");
        maximumLoadKg = requireNonNegative(maximumLoadKg, "carga máxima");
        Objects.requireNonNull(resistances, "Las resistencias no pueden ser nulas.");
    }

    private static OptionalDouble requireNonNegative(OptionalDouble value, String name) {
        if (value == null) throw new NullPointerException(name + " no puede ser nulo.");
        if (value.isPresent() && value.getAsDouble() < 0) throw new IllegalArgumentException(name + " no puede ser negativo.");
        return value;
    }
}

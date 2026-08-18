package domain.character.sheet;

import java.util.Objects;

/**
 * Agrupa modificadores independientes para que cada fuente pueda alterar
 * únicamente las resistencias que le correspondan.
 */
public record ResistanceModifierSet(
        DamageResistanceProfile characterClass,
        DamageResistanceProfile equipment,
        DamageResistanceProfile status,
        DamageResistanceProfile species
) {
    public ResistanceModifierSet {
        Objects.requireNonNull(characterClass, "El perfil de clase no puede ser nulo.");
        Objects.requireNonNull(equipment, "El perfil de equipo no puede ser nulo.");
        Objects.requireNonNull(status, "El perfil de estado no puede ser nulo.");
        Objects.requireNonNull(species, "El perfil de especie no puede ser nulo.");
    }

    public static ResistanceModifierSet none() {
        DamageResistanceProfile zero = DamageResistanceProfile.zero();
        return new ResistanceModifierSet(zero, zero, zero, zero);
    }

    public DamageResistanceProfile applyTo(DamageResistanceProfile base) {
        return DamageResistanceProfile.combine(base, characterClass, equipment, status, species);
    }
}

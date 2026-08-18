package domain.combat.ai.threat;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.natural.NaturalCombatProfile;

import java.util.Optional;

import java.util.Objects;

public record CombatantPresence(CharacterSheet sheet, double heightMeters, Optional<NaturalCombatProfile> naturalCombatProfile) {
    public CombatantPresence {
        Objects.requireNonNull(sheet, "La hoja de personaje no puede ser nula.");
        if (heightMeters <= 0) {
            throw new IllegalArgumentException("La altura debe ser positiva.");
        }
        naturalCombatProfile = Objects.requireNonNull(naturalCombatProfile, "El perfil corporal opcional no puede ser nulo.");
    }

    public CombatantPresence(CharacterSheet sheet, double heightMeters) {
        this(sheet, heightMeters, Optional.empty());
    }

    public CombatantPresence(CharacterSheet sheet, double heightMeters, NaturalCombatProfile naturalCombatProfile) {
        this(sheet, heightMeters, Optional.of(Objects.requireNonNull(naturalCombatProfile)));
    }

    public int strength() {
        return sheet.valueOf(Attribute.FUERZA);
    }

    public int charisma() {
        return sheet.valueOf(Attribute.CARISMA);
    }

    public double globalPresence() {
        return strength() * heightMeters;
    }
}

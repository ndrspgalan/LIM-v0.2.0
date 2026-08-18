package domain.bestiarium;

import domain.ability.CharacterMasteryCollection;
import domain.character.CharacterClass;

public record BestiaryEntity(String name, CharacterClass characterClass, CharacterMasteryCollection masteries) {
    public BestiaryEntity {
        if (name == null || name.isBlank() || masteries == null) throw new IllegalArgumentException("Entrada de bestiario incompleta.");
    }
}

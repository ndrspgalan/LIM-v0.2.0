package domain.character.offense;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.LethalityProfile;

import java.util.Objects;

public final class UnarmedLethalityCalculator {
    public LethalityProfile calculate(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return new LethalityProfile(0, 0, sheet.valueOf(Attribute.FUERZA));
    }
}

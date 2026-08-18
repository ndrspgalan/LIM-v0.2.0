package domain.character.progression;

import domain.character.sheet.CharacterSheet;

import java.util.Objects;

public record CharacterProgressionState(int level, CharacterSheet sheet, MucusWallet mucusWallet) {
    public static final int MINIMUM_LEVEL = 1;
    public static final int MAXIMUM_LEVEL = 999;

    public CharacterProgressionState {
        if (level < MINIMUM_LEVEL || level > MAXIMUM_LEVEL) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 999.");
        }
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(mucusWallet, "La reserva de mucus no puede ser nula.");
    }
}

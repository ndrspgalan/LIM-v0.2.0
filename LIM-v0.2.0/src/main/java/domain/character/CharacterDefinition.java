package domain.character;

import java.util.Objects;

public record CharacterDefinition(CharacterIdentity identity) {
    public CharacterDefinition {
        Objects.requireNonNull(identity, "La identidad del personaje no puede ser nula.");
    }
}

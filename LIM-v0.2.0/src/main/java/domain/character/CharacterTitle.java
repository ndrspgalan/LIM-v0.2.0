package domain.character;

import java.util.Objects;

public record CharacterTitle(String name) {
    public CharacterTitle {
        Objects.requireNonNull(name, "El título no puede ser nulo.");
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
    }
}

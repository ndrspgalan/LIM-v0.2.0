package domain.signal;

import java.util.Objects;

public record CharacterSignalSource(String actorReference) {
    public CharacterSignalSource {
        Objects.requireNonNull(actorReference, "La referencia del actor no puede ser nula.");
        if (actorReference.isBlank()) throw new IllegalArgumentException("La referencia del actor no puede estar vacía.");
    }
}

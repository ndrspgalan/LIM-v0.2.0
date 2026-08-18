package domain.signal;

import java.util.Objects;

/** Clave semántica y texto opcional; el frontend decide voz, audio y animación concretos. */
public record CharacterSignalPayload(String cueKey, String narrativeLine) {
    public CharacterSignalPayload {
        Objects.requireNonNull(cueKey, "La clave de señal no puede ser nula.");
        Objects.requireNonNull(narrativeLine, "La línea narrativa no puede ser nula.");
        if (cueKey.isBlank()) throw new IllegalArgumentException("La clave de señal no puede estar vacía.");
    }
    public static CharacterSignalPayload cue(String cueKey) { return new CharacterSignalPayload(cueKey, ""); }
}

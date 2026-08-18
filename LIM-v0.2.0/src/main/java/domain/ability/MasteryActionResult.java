package domain.ability;

public record MasteryActionResult(boolean changed, String message) {
    public MasteryActionResult {
        if (message == null || message.isBlank()) throw new IllegalArgumentException("El mensaje no puede estar vacío.");
    }
}

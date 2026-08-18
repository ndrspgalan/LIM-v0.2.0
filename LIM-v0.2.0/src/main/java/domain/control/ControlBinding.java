package domain.control;

import java.util.Objects;

public record ControlBinding(String input, InputGesture gesture, ControlAction action, String condition) {
    public ControlBinding {
        input = Objects.requireNonNull(input, "La entrada no puede ser nula.").trim();
        Objects.requireNonNull(gesture, "El gesto no puede ser nulo.");
        Objects.requireNonNull(action, "La acción no puede ser nula.");
        condition = condition == null ? "" : condition.trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }
    }
}

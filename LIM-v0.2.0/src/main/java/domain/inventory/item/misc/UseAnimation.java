package domain.inventory.item.misc;

import java.util.List;
import java.util.Objects;

public record UseAnimation(double durationRealSeconds, List<String> steps) {
    public UseAnimation {
        if (durationRealSeconds <= 0) {
            throw new IllegalArgumentException("La duración de uso debe ser positiva.");
        }
        Objects.requireNonNull(steps, "Los pasos de la animación no pueden ser nulos.");
        if (steps.isEmpty() || steps.stream().anyMatch(step -> step == null || step.isBlank())) {
            throw new IllegalArgumentException("La animación debe contener pasos narrativos válidos.");
        }
        steps = List.copyOf(steps);
    }
}

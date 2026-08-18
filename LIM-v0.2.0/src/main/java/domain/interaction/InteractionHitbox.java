package domain.interaction;

import java.util.List;
import java.util.Objects;

public record InteractionHitbox(String id, SpatialPoint interactionPoint, List<InteractionAction> actions) {
    public InteractionHitbox {
        Objects.requireNonNull(id); Objects.requireNonNull(interactionPoint); Objects.requireNonNull(actions);
        if (id.isBlank()) throw new IllegalArgumentException("La hitbox debe tener identificador.");
        if (actions.isEmpty()) throw new IllegalArgumentException("La hitbox debe ofrecer alguna acción.");
        actions = List.copyOf(actions);
    }
}

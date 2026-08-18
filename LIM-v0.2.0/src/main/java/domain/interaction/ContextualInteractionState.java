package domain.interaction;

import java.util.List;
import java.util.Optional;

/** Solo conserva y cicla acciones mientras la hitbox continúa siendo alcanzable. */
public final class ContextualInteractionState {
    private InteractionHitbox activeHitbox;
    private int selectedIndex;

    public boolean refresh(InteractionHitbox candidate, boolean reachable) {
        if (!reachable || candidate == null) { clear(); return false; }
        if (activeHitbox == null || !activeHitbox.id().equals(candidate.id())) selectedIndex = 0;
        activeHitbox = candidate;
        return true;
    }

    public Optional<InteractionAction> selectedAction() {
        return activeHitbox == null ? Optional.empty() : Optional.of(activeHitbox.actions().get(selectedIndex));
    }

    public Optional<InteractionAction> cycle() {
        if (activeHitbox == null) return Optional.empty();
        List<InteractionAction> actions = activeHitbox.actions();
        selectedIndex = (selectedIndex + 1) % actions.size();
        return selectedAction();
    }

    public boolean interactionAvailable() { return activeHitbox != null; }
    public void clear() { activeHitbox = null; selectedIndex = 0; }
}

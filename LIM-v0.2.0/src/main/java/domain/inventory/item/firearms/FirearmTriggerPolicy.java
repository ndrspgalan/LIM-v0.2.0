package domain.inventory.item.firearms;

import java.util.Objects;

/**
 * Contrato de cadencia independiente del frame-rate.
 * Cada evento PRESS/HOLD solicita como máximo un disparo; la cadencia temporal real pertenece al runtime/frontend.
 */
public final class FirearmTriggerPolicy {
    private FirearmTriggerPolicy() {}

    public static boolean canFireOnPress(FireMode mode, FirearmTriggerState state) {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(state);
        state.press();
        return state.shotsOnCurrentPress() < mode.maxShotsPerTriggerPress();
    }

    public static boolean canFireOnHold(FireMode mode, FirearmTriggerState state) {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(state);
        if (!state.pressed()) return false;
        return switch (mode) {
            case ONE_A -> false;
            case THREE_A -> state.shotsOnCurrentPress() < 3;
            case AUTO_A -> true;
        };
    }
}

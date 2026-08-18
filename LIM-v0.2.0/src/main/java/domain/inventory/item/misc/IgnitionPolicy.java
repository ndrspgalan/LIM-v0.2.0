package domain.inventory.item.misc;

import java.util.Objects;

public final class IgnitionPolicy {
    public static final double GENERATE_SPARK_DURATION_SECONDS = 4.0;
    public static final double IGNITE_LOCK_DURATION_SECONDS = 3.0;

    public IgnitionResult executeDetailed(UtilityObjectItem amadou, UtilityObjectItem flint, UtilityAction action) {
        Objects.requireNonNull(amadou); Objects.requireNonNull(flint); Objects.requireNonNull(action);
        if (action != UtilityAction.GENERATE_SPARK && action != UtilityAction.IGNITE_LOCK) {
            return IgnitionResult.rejected(action);
        }
        if (!amadou.actions().contains(action) || !flint.actions().contains(action)) {
            return IgnitionResult.rejected(action);
        }
        if (amadou.isDepleted() || flint.isDepleted()) return IgnitionResult.rejected(action);
        amadou.consumeOne();
        flint.consumeOne();
        double duration = action == UtilityAction.GENERATE_SPARK
                ? GENERATE_SPARK_DURATION_SECONDS
                : IGNITE_LOCK_DURATION_SECONDS;
        return new IgnitionResult(true, action, duration, true, true);
    }

    /** Compatibilidad con los consumidores previos: la fuente de verdad es executeDetailed. */
    public boolean execute(UtilityObjectItem amadou, UtilityObjectItem flint, UtilityAction action) {
        return executeDetailed(amadou, flint, action).successful();
    }
}

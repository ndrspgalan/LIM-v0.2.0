package domain.status;

public final class HealingPolicy {
    public HealthState applyYarrow(HealthState state) {
        // Si existe barrera de musgo, la inhibición queda ligada a la vida de la barrera.
        if (state.protection().active()) {
            return new HealthState(state.currentHealth(),state.totalHealth(),state.protection(),true,state.lastHitDamage(),true);
        }
        return new HealthState(state.currentHealth(), state.totalHealth(), state.protection(), false,state.lastHitDamage(),false);
    }

    public HealthState applyBogMoss(HealthState state) { return new MossBarrierPolicy().apply(state); }
}

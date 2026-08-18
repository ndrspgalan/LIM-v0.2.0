package domain.status;

/** Apósito de musgo de turbera: barrera igual al último golpe neto recibido y exclusión mientras siga activa. */
public final class MossBarrierPolicy {
    public HealthState apply(HealthState state) {
        if (state == null) throw new NullPointerException("Estado nulo.");
        if (state.protection().active() || state.lastHitDamage() <= 0) return state;
        double capacity = state.lastHitDamage();
        return new HealthState(state.currentHealth(), state.totalHealth(), new HealthProtection(capacity,capacity), state.healthRegenerationReduced(), state.lastHitDamage(), state.yarrowInhibitionDeferred());
    }
}

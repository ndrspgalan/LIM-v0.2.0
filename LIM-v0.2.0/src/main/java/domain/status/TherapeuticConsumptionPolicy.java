package domain.status;

import domain.inventory.item.misc.TherapeuticItem;
import java.util.Objects;

/** Único punto de aplicación y gasto para los efectos curativos/estimulantes. */
public final class TherapeuticConsumptionPolicy {
    private final HealingPolicy healingPolicy = new HealingPolicy();

    public TherapeuticUseResult consume(HealthState health, ActiveTherapeuticEffects active,
                                        TherapeuticItem item) {
        Objects.requireNonNull(health, "El estado de salud no puede ser nulo.");
        Objects.requireNonNull(active, "Los efectos activos no pueden ser nulos.");
        Objects.requireNonNull(item, "El consumible no puede ser nulo.");
        TherapeuticEffectProfile effect = item.therapeuticEffect();
        if (!canApply(health, effect) || item.isDepleted()) {
            return new TherapeuticUseResult(health, active, false, 0);
        }

        HealthState next = applyHealing(health, effect.healingKind());
        double restored = Math.min(next.missingHealth(), next.totalHealth() * effect.instantHealthFraction());
        if (restored > 0) {
            next = new HealthState(next.currentHealth() + restored, next.totalHealth(), next.protection(),
                    next.healthRegenerationReduced());
        }
        if (!item.consumeOne()) return new TherapeuticUseResult(health, active, false, 0);
        return new TherapeuticUseResult(next, active.apply(effect), true, restored);
    }

    private boolean canApply(HealthState state, TherapeuticEffectProfile effect) {
        return switch (effect.healingKind()) {
            case YARROW -> state.healthRegenerationReduced();
            case BOG_MOSS -> !state.protection().active() && state.lastHitDamage() > 0;
            case NONE -> effect.instantHealthFraction() > 0 && state.missingHealth() > 0 || effect.hasTimedEffects();
        };
    }

    private HealthState applyHealing(HealthState state, TherapeuticEffectProfile.HealingKind kind) {
        return switch (kind) {
            case YARROW -> healingPolicy.applyYarrow(state);
            case BOG_MOSS -> healingPolicy.applyBogMoss(state);
            case NONE -> state;
        };
    }
}

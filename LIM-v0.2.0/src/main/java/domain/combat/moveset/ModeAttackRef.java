package domain.combat.moveset;

import domain.inventory.item.WeaponActionMode;
import java.util.Objects;

public record ModeAttackRef(WeaponActionMode mode, String motionId) {
    public ModeAttackRef { Objects.requireNonNull(mode); if (motionId==null||motionId.isBlank()) throw new IllegalArgumentException("motionId obligatorio"); }
}

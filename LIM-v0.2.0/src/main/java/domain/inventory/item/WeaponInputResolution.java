package domain.inventory.item;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public record WeaponInputResolution(
        boolean allowed,
        Optional<WeaponCombatAction> action,
        OptionalInt lightAttackOrdinal,
        boolean lightComboFinisherBonusApplies,
        String reason
) {
    public WeaponInputResolution {
        action = Objects.requireNonNull(action);
        lightAttackOrdinal = Objects.requireNonNull(lightAttackOrdinal);
        Objects.requireNonNull(reason);
    }

    public WeaponInputResolution(boolean allowed, Optional<WeaponCombatAction> action, String reason) {
        this(allowed, action, OptionalInt.empty(), false, reason);
    }

    public static WeaponInputResolution allowed(WeaponCombatAction action, String reason) {
        return new WeaponInputResolution(true, Optional.of(action), OptionalInt.empty(), false, reason);
    }

    public static WeaponInputResolution lightAttack(int ordinal, boolean finisherBonusApplies, String reason) {
        return new WeaponInputResolution(true, Optional.of(WeaponCombatAction.LIGHT_ATTACK),
                OptionalInt.of(ordinal), finisherBonusApplies, reason);
    }

    public static WeaponInputResolution lightAttack(int ordinal, String reason) {
        return lightAttack(ordinal, false, reason);
    }

    public static WeaponInputResolution blocked(String reason) {
        return new WeaponInputResolution(false, Optional.empty(), OptionalInt.empty(), false, reason);
    }
}

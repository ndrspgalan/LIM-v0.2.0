package domain.throwing;

import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;

/** Reincorpora una unidad recuperable a un stack compatible sin superar su capacidad. */
public final class ThrownRecoveryPolicy {
    public boolean recover(ThrowResult result, UtilityObjectItem destination) {
        if (result == null || destination == null
                || !new domain.recovery.RecoverablePolicy().canRecover(result.payload().profile().recoverable(), true)) return false;
        if (result.payload().currencyType().isPresent()) return false;
        if (!result.payload().name().equals(destination.name())) return false;
        return destination.addUnits(1);
    }


    public boolean recover(ThrowResult result, ThrowingWeaponItem destination) {
        if (result == null || destination == null
                || !new domain.recovery.RecoverablePolicy().canRecover(result.payload().profile().recoverable(), true)) return false;
        if (result.payload().currencyType().isPresent()) return false;
        if (!result.payload().name().equals(destination.name())) return false;
        return destination.addUnits(1);
    }

    public boolean recover(ThrowResult result, CurrencyStack destination) {
        if (result == null || destination == null
                || !new domain.recovery.RecoverablePolicy().canRecover(result.payload().profile().recoverable(), true)) return false;
        return result.payload().currencyType()
                .filter(type -> type == destination.currencyType())
                .map(type -> destination.addUnits(1))
                .orElse(false);
    }
}

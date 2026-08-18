package domain.inventory.item.misc;

import domain.inventory.item.WeaponItem;
import java.util.Objects;

public final class WhetstonePolicy {
    public static final double SHARPEN_DURATION_SECONDS = 8.0;

    public WhetstoneResult sharpenDetailed(UtilityObjectItem whetstone, WeaponItem weapon) {
        Objects.requireNonNull(whetstone); Objects.requireNonNull(weapon);
        if (!whetstone.actions().contains(UtilityAction.SHARPEN)
                || whetstone.isDepleted() || !weapon.canBeSharpened()) {
            return WhetstoneResult.rejected();
        }
        if (!weapon.restoreAllBluntLethality()) return WhetstoneResult.rejected();
        whetstone.consumeOne();
        return new WhetstoneResult(true, SHARPEN_DURATION_SECONDS, true);
    }

    public WhetstoneResult sharpenDetailed(UtilityObjectItem whetstone, WeaponItem weapon,
                                            domain.combat.coating.MercuryCoatingService mercury) {
        WhetstoneResult result = sharpenDetailed(whetstone, weapon);
        if (result.successful() && mercury != null) mercury.clear(weapon);
        return result;
    }

    /** Compatibilidad con los consumidores previos: la fuente de verdad es sharpenDetailed. */
    public boolean sharpen(UtilityObjectItem whetstone, WeaponItem weapon) {
        return sharpenDetailed(whetstone, weapon).successful();
    }
}

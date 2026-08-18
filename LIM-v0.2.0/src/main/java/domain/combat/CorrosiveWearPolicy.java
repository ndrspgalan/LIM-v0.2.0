package domain.combat;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.ArmorPiece;
import java.util.Objects;

/** CORROSIVO es una vía química separada del desgaste convencional. */
public final class CorrosiveWearPolicy {
    public double apply(ArmorPiece piece, double nominalLoss) {
        Objects.requireNonNull(piece);
        if (piece.hasProperty(ItemPropertyId.ANTI_CORROSIVE)) return 0.0;
        return piece.applyCorrosiveBluntLoss(nominalLoss);
    }
}

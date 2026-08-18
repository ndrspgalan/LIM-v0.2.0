package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.PersonalTransportUseProperties;

import java.util.List;

public final class CurrencyStack extends StackableMiscellaneousItem {
    public static final int MAXIMUM_UNITS = 1_000;
    public static final double UNIT_WEIGHT_KG = 0.001;

    private final CurrencyType currencyType;

    public CurrencyStack(CurrencyType currencyType, int quantity) {
        super(
                currencyType.label(),
                "Conjunto físico de " + currencyType.label().toLowerCase() + ". Cada unidad pesa un gramo y cada espacio admite hasta mil unidades antes de requerir un nuevo stack independiente.",
                MiscellaneousCategory.MONEY,
                quantity,
                MAXIMUM_UNITS,
                UNIT_WEIGHT_KG,
                new InventoryFootprint(1, 1),
                null,
                List.of("PESO UNITARIO | 1 g"),
                PersonalTransportUseProperties.all()
        );
        this.currencyType = currencyType;
    }

    public CurrencyType currencyType() {
        return currencyType;
    }
}

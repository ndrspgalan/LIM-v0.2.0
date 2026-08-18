package domain.inventory.item.ammunition;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;

import java.util.List;
import java.util.Objects;

/**
 * Estuche  de 2x1 slots. Es un único objeto que admite hasta cinco cartuchos de Cal Viva V881.
 * No constituye un compartimento logístico independiente.
 */
public final class LimeCartridgeCase extends InventoryEntry implements AmmunitionSource {
    public static final int MAX_CARTRIDGES = 5;
    public static final int SPRAY_TICKS_PER_CARTRIDGE = 56;
    public static final double EMPTY_CASE_WEIGHT_KG = 0.300;
    public static final double FULL_CARTRIDGE_WEIGHT_KG = 3.200;

    private final AmmunitionDescriptor descriptor;
    private int remainingCartridges;

    public LimeCartridgeCase(int initialCartridges) {
        super(
                "Estuche de Cartuchos de Cal Viva V881",
                "Estuche compacto de servicio que contiene hasta cinco cartuchos reemplazables de Cal Viva V881.",
                totalWeight(initialCartridges),
                new InventoryFootprint(2, 1),
                List.of(
                        "CONTENIDO | Solo Cartucho de Cal Viva V881",
                        "CAPACIDAD | 5 cartuchos",
                        "TAMAÑO | 2x1 slots",
                        "PESO VACÍO | 0,300 kg",
                        "PESO POR CARTUCHO COMPLETO | 3,200 kg"
                )
        );
        if (initialCartridges < 0 || initialCartridges > MAX_CARTRIDGES) {
            throw new IllegalArgumentException("El estuche admite entre 0 y 5 cartuchos.");
        }
        this.remainingCartridges = initialCartridges;
        this.descriptor = new AmmunitionDescriptor(
                AmmunitionFamily.CARTRIDGE,
                "Cal Viva V881",
                "Agente de Cal Viva V881",
                "Cartucho de 3 L para rociado presurizado",
                false
        );
    }

    @Override public double weightKg() { return totalWeight(remainingCartridges); }
    @Override public AmmunitionDescriptor ammunitionDescriptor() { return descriptor; }
    @Override public int remainingUnits() { return remainingCartridges; }
    @Override public int maxUnits() { return MAX_CARTRIDGES; }
    @Override public int shotsLoadedPerConsumedUnit() { return SPRAY_TICKS_PER_CARTRIDGE; }
    @Override public boolean consumeOneUnit() {
        if (remainingCartridges <= 0) return false;
        remainingCartridges--;
        return true;
    }

    public boolean addCartridge() {
        if (remainingCartridges >= MAX_CARTRIDGES) return false;
        remainingCartridges++;
        return true;
    }

    private static double totalWeight(int cartridges) {
        if (cartridges < 0 || cartridges > MAX_CARTRIDGES) throw new IllegalArgumentException("Cantidad inválida.");
        return EMPTY_CASE_WEIGHT_KG + cartridges * FULL_CARTRIDGE_WEIGHT_KG;
    }
}

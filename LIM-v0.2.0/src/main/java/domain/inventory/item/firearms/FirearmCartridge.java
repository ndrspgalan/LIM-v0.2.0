package domain.inventory.item.firearms;

import domain.inventory.item.ammunition.AmmunitionCartridge;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.ammunition.AmmunitionFamily;

/**
 * Adaptador de compatibilidad. El contrato real de cartucho vive en ammunition
 * y puede ser utilizado por Firearm o por cualquier RangedWeapon futuro.
 */
public final class FirearmCartridge extends AmmunitionCartridge {
    public FirearmCartridge(String name, String caliber, int capacity) {
        this(name, caliber, "No especificado", "Estándar", capacity, 0.0);
    }

    public FirearmCartridge(String name, String caliber, String material, String variant, int capacity) {
        this(name, caliber, material, variant, capacity, 0.0);
    }

    public FirearmCartridge(
            String name,
            String caliber,
            String material,
            String variant,
            int capacity,
            double weightKg
    ) {
        super(name, new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, caliber, material, variant, false),
                capacity, weightKg);
    }


    public FirearmCartridge(String name, String caliber, String material, String variant, int capacity, double weightKg, domain.inventory.InventoryFootprint footprint) {
        super(name, new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, caliber, material, variant, false), capacity, weightKg, footprint);
    }

    /** Conservado solo para compatibilidad con llamadas históricas; los cartuchos canónicos no se apilan. */
    public FirearmCartridge(String name, String caliber, int capacity, int maxStackSize) {
        this(name, caliber, "No especificado", "Estándar", capacity, 0.0);
        if (maxStackSize != 1) {
            throw new IllegalArgumentException("Desde  un cartucho completo es una unidad y su stack máximo es 1.");
        }
    }
}

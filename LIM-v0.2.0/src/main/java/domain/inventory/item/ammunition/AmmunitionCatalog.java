package domain.inventory.item.ammunition;

/** Catálogo global  de munición y cargas consumibles con pesos normalizados. */
public final class AmmunitionCatalog {
    private AmmunitionCatalog() {}

    public static AmmunitionCartridge pneumaticLead46Cartridge() {
        return new AmmunitionCartridge("Cartucho .46 de plomo",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, ".46", "Plomo", "Tubular lateral", false),
                20, 0.120, 0.013, new domain.inventory.InventoryFootprint(1,1));
    }

    public static AmmunitionCartridge autoloadingPistol45Magazine() {
        return new AmmunitionCartridge("Cargador .45 de Pistola V881",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, ".45", "Plomo con camisa de cobre", "Ojiva redondeada facetada", false),
                8, 0.090, 0.011875, new domain.inventory.InventoryFootprint(1,1));
    }
public static AmmunitionCartridge submachineGun9mmMagazine() {
        return new AmmunitionCartridge("Cargador de 9 mm V881",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, "9 mm", "Plomo con camisa de cobre", "Ojiva de servicio V881", false),
                25, 0.150, 0.010, new domain.inventory.InventoryFootprint(1,1));
    }
public static AmmunitionCartridge repeatingRifle792x57Clip() {
        return new AmmunitionCartridge("Cartucho completo 7,92×57 mm V881",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, "7,92×57 mm", "Plomo con camisa de cobre", "Cartucho de fusil V881", false),
                5, 0.025, 0.024, new domain.inventory.InventoryFootprint(1,1));
    }

    public static AmmunitionCartridge bifilar46Magazine() {
        return new AmmunitionCartridge("Cargador bifilar .46 V881",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, ".46", "Núcleo de tungsteno, armadura conductora y sabot separable", "Cartucho bifilar unitario", false),
                5, 0.080, 0.044, new domain.inventory.InventoryFootprint(1, 2));
    }
public static AmmunitionCartridge antiMateriel20mmCartridge() {
        return new AmmunitionCartridge("Cartucho de 4 proyectiles de 20 mm V881",
                new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, "20 mm", "Proyectil antimaterial V881", "20 mm × 180 mm · 0,130 kg por proyectil", false),
                4, 0.130, 0.130, new domain.inventory.InventoryFootprint(2, 2));
    }

    public static UnitaryAmmunitionItem clusterRocket85mm() {
        return new UnitaryAmmunitionItem("Cohete de Racimo V881 de 85 mm",
                "Cohete individual de 85 mm y 65 cm destinado al Cañón de Racimo V881.",
                4.0, new domain.inventory.InventoryFootprint(6, 2),
                new AmmunitionDescriptor(AmmunitionFamily.ROCKET, "85 mm", "Carga de racimo V881", "Cohete individual 85 mm × 650 mm", false));
    }

    public static LimeCartridgeCase limeCartridgeCase() { return new LimeCartridgeCase(LimeCartridgeCase.MAX_CARTRIDGES); }

    public static ProjectileAmmunitionItem pebble() {
        return new ProjectileAmmunitionItem("Guijarro", "Guijarro ordinario recuperable; con Honda produce 35 de daño contundente.", 0.050,
                new AmmunitionDescriptor(AmmunitionFamily.PEBBLE, "GUIJARRO", "Piedra", "GUIJARRO", true));
    }

    public static ProjectileAmmunitionItem arrow(ArrowVariant variant, double weightKg) {
        if (variant == ArrowVariant.TINDER_UNLIT || variant == ArrowVariant.TINDER_LIT) return new TinderArrowItem(weightKg);
        return new ProjectileAmmunitionItem("Flecha " + variant.label(), weightKg, variant.descriptor());
    }

    public static ProjectileAmmunitionItem piercingArrow() { return arrow(ArrowVariant.PIERCING, 0.060); }
    public static ProjectileAmmunitionItem barbedArrow() { return arrow(ArrowVariant.BARBED, 0.065); }
    public static ProjectileAmmunitionItem bladedArrow() { return arrow(ArrowVariant.BLADED, 0.070); }
    public static TinderArrowItem tinderArrow() { return new TinderArrowItem(0.075); }
}

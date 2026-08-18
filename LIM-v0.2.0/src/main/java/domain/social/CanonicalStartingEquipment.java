package domain.social;

import domain.inventory.catalog.PhysicalObjectCatalog;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.PersonalTransportType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** patrimonio material inicial tipado de un perfil social canónico. */
public record CanonicalStartingEquipment(
        List<ArmorPiece> wornGarments,
        List<String> inventoryObjectNames,
        Optional<AccessoryItem> equippedAccessory,
        List<String> weaponNames,
        List<String> ammunitionNames,
        Optional<PersonalTransportType> personalTransport,
        List<InventoryCompartmentType> inventoryExpanders,
        List<CurrencyStack> currencyStacks,
        List<ArmorMaterial> materialUnits
) {
    public CanonicalStartingEquipment {
        wornGarments = List.copyOf(Objects.requireNonNull(wornGarments));
        inventoryObjectNames = List.copyOf(Objects.requireNonNull(inventoryObjectNames));
        equippedAccessory = Objects.requireNonNull(equippedAccessory);
        weaponNames = List.copyOf(Objects.requireNonNull(weaponNames));
        ammunitionNames = List.copyOf(Objects.requireNonNull(ammunitionNames));
        personalTransport = Objects.requireNonNull(personalTransport);
        inventoryExpanders = List.copyOf(Objects.requireNonNull(inventoryExpanders));
        currencyStacks = List.copyOf(Objects.requireNonNull(currencyStacks));
        materialUnits = List.copyOf(Objects.requireNonNull(materialUnits));

        if (wornGarments.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Las prendas iniciales no admiten nulos.");
        if (inventoryObjectNames.stream().anyMatch(n -> !PhysicalObjectCatalog.containsName(n)
                && AccessoryCatalog.all().stream().noneMatch(a -> a.name().equals(n))))
            throw new IllegalArgumentException("El inventario inicial sólo puede referenciar objetos físicos o abalorios canónicos ya existentes.");
        if (inventoryExpanders.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Los expansores iniciales no admiten nulos.");
        if (new java.util.HashSet<>(inventoryExpanders).size() != inventoryExpanders.size())
            throw new IllegalArgumentException("Cada tipo de expansor personal ocupa una única ranura de equipamiento y no puede equiparse más de una vez.");
        if (currencyStacks.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Las monedas iniciales no admiten nulos.");
        if (materialUnits.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Los materiales iniciales no admiten nulos.");

        validateTransportExpanderCoupling(personalTransport, inventoryExpanders);
    }

    /** Compatibilidad para catálogos - no enriquecidos expresamente. */
    public CanonicalStartingEquipment(List<ArmorPiece> wornGarments,
                                      List<String> inventoryObjectNames,
                                      Optional<AccessoryItem> equippedAccessory,
                                      List<String> weaponNames,
                                      List<String> ammunitionNames,
                                      Optional<PersonalTransportType> personalTransport) {
        this(wornGarments, inventoryObjectNames, equippedAccessory, weaponNames, ammunitionNames, personalTransport,
                List.of(), List.of(), List.of());
    }

    public static CanonicalStartingEquipment unarmed(List<ArmorPiece> garments, List<String> inventory,
                                                      AccessoryItem accessory) {
        return new CanonicalStartingEquipment(garments, inventory, Optional.ofNullable(accessory),
                List.of(), List.of(), Optional.empty());
    }

    private static void validateTransportExpanderCoupling(Optional<PersonalTransportType> transport,
                                                           List<InventoryCompartmentType> expanders) {
        if (transport.isEmpty()) return;
        InventoryCompartmentType required = switch (transport.get()) {
            case HORSE_LEISURE -> InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE;
            case HORSE_RACING -> InventoryCompartmentType.SADDLEBAGS_HORSE_RACING;
            case HORSE_DRAFT -> InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT;
            case BICYCLE_MILITARY_V881 -> InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY;
            case MOTORCYCLE_CARDAN_V881 -> InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN;
            case BICYCLE_FOLDING_V881 -> null;
        };
        if (required != null && !expanders.contains(required))
            throw new IllegalArgumentException("El transporte inicial "+transport.get().label()+" debe incluir su expansor compatible: "+required.label());
    }
}

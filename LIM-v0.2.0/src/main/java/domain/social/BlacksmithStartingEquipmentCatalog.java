package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.logistics.InventoryCompartmentType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** equipamiento inicial canónico de Herrero V881. */
public final class BlacksmithStartingEquipmentCatalog {
    private BlacksmithStartingEquipmentCatalog(){}

    public static CanonicalStartingEquipment equipment(Subprofession subprofession, CharacterClass characterClass){
        Objects.requireNonNull(subprofession); Objects.requireNonNull(characterClass);
        if(subprofession.profession()!=Profession.BLACKSMITH)
            throw new IllegalArgumentException("El catálogo sólo materializa Herrero.");
        if(BlacksmithCanonicalProfiles.isDeprecated(subprofession,characterClass))
            throw new IllegalArgumentException("Maestro está deprecated para Herrero y no recibe loadout inicial activo.");
        boolean male=switch(characterClass){
            case LUCHADOR,INTELECTUAL,INDOMITO -> true;
            case ESPECIALISTA,APODERADO,HERALDO -> false;
            case MAESTRO -> throw new IllegalArgumentException("Maestro está deprecated para Herrero.");
        };
        CanonicalStartingEquipment base=CanonicalStartingEquipmentPackingPolicy.requireValid(switch(subprofession){
            case DOMESTIC_V881_INSTALLER -> domesticInstaller(male);
            case V881_ELECTROMECHANIC -> electromechanic(male);
            case FREQUENCY_INSTRUMENT_MAKER -> frequencyInstrumentMaker(male);
            case MATRIX_ARCHITECT -> matrixArchitect(male);
            default -> throw new IllegalArgumentException("Subprofesión Herrero sin loadout : "+subprofession);
        });
        var a=domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog.forProfile(subprofession.name(),characterClass.name());
        return CanonicalStartingEquipmentPackingPolicy.requireValid(new CanonicalStartingEquipment(base.wornGarments(),base.inventoryObjectNames(),java.util.Optional.of(a),base.weaponNames(),base.ammunitionNames(),base.personalTransport(),base.inventoryExpanders(),base.currencyStacks(),base.materialUnits()));
    }

    public static CanonicalLoadoutPlacementPlan placement(Subprofession subprofession, CharacterClass characterClass){
        return CivilianStartingEquipmentSupport.placement(equipment(subprofession,characterClass));
    }

    private static List<ArmorPiece> heavyWorkshop(boolean male){
        return male
                ? List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),
                    ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerKneeDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),
                    ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),
                    ArmorCatalog.workshopBracers(),ArmorCatalog.workshopGoggles(),ArmorCatalog.laborerKerchiefV881())
                : List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleWorkWaistcoat(),
                    ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerReinforcedPetticoatV881(),
                    ArmorCatalog.middleWorkSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),
                    ArmorCatalog.workshopBracers(),ArmorCatalog.workshopGoggles(),ArmorCatalog.laborerKerchiefV881());
    }

    private static List<ArmorPiece> precisionWorkshop(boolean male){
        return male
                ? List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerModularShirtV881(),ArmorCatalog.middleWorkWaistcoat(),
                    ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),
                    ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),
                    ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.workshopGoggles())
                : List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleWorkWaistcoat(),
                    ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),
                    ArmorCatalog.middleStraightSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),
                    ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.workshopGoggles());
    }

    private static CanonicalStartingEquipment domesticInstaller(boolean male){
        return new CanonicalStartingEquipment(heavyWorkshop(male),
                List.of("Pan","Fruta","Odre","KNIJPKAT","Emplasto de milenrama","Apósito de musgo de turbera","Caja de Herramientas"),
                Optional.empty(),List.of("Martillo de bola"),List.of(),Optional.empty(),
                List.of(InventoryCompartmentType.BACKPACK),
                List.of(new CurrencyStack(CurrencyType.VALERITA,65)),
                List.of(ArmorMaterial.VULCANIZED_RUBBER,ArmorMaterial.DIELECTRIC_CLOTH));
    }

    private static CanonicalStartingEquipment electromechanic(boolean male){
        return new CanonicalStartingEquipment(heavyWorkshop(male),
                List.of("Pan","Cecina","Odre","MAGNETLAMPE","Emplasto de milenrama","Apósito de musgo de turbera",
                        "Caja de Herramientas","Botella de Líquido Refrigerante"),
                Optional.empty(),List.of("Martillo de bola"),List.of(),Optional.empty(),
                List.of(InventoryCompartmentType.BACKPACK),
                List.of(new CurrencyStack(CurrencyType.VALERITA,75)),
                List.of(ArmorMaterial.STEEL,ArmorMaterial.VULCANIZED_RUBBER,ArmorMaterial.DIELECTRIC_CLOTH));
    }

    private static CanonicalStartingEquipment frequencyInstrumentMaker(boolean male){
        return new CanonicalStartingEquipment(precisionWorkshop(male),
                List.of("Pan","Bizcocho","Fruta","Odre","MAGNETLAMPE","Caja de Herramientas","Botella de Líquido Refrigerante"),
                Optional.empty(),List.of("Martillo de bola"),List.of(),Optional.empty(),
                List.of(InventoryCompartmentType.BACKPACK),
                List.of(new CurrencyStack(CurrencyType.VALERITA,100)),
                List.of(ArmorMaterial.LAMINATED_GLASS,ArmorMaterial.VULCANIZED_RUBBER,
                        ArmorMaterial.DIELECTRIC_CLOTH,ArmorMaterial.BRONZE));
    }

    private static CanonicalStartingEquipment matrixArchitect(boolean male){
        return new CanonicalStartingEquipment(precisionWorkshop(male),
                List.of("Pan","Fruta","Odre","MAGNETLAMPE","Caja de Herramientas","Botella de Líquido Refrigerante"),
                Optional.empty(),List.of("Martillo de bola"),List.of(),Optional.of(domain.inventory.logistics.PersonalTransportType.HORSE_DRAFT),
                List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.LEG_POUCH,
                        InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT),
                List.of(new CurrencyStack(CurrencyType.VALERITA,130)),
                List.of(ArmorMaterial.STEEL,ArmorMaterial.LAMINATED_GLASS,ArmorMaterial.VULCANIZED_RUBBER,
                        ArmorMaterial.DIELECTRIC_CLOTH,ArmorMaterial.MINERAL_MULTILAYER_FABRIC,
                        ArmorMaterial.ELECTROMECHANICAL_COMPOSITE));
    }
}

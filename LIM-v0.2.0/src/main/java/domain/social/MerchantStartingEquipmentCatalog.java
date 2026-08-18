package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.PersonalTransportType;
import java.util.*;

/**  — patrimonio material inicial de Comerciante, construido por perfil activo. */
public final class MerchantStartingEquipmentCatalog {
    private MerchantStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        if(s.profession()!=Profession.MERCHANT) throw new IllegalArgumentException("Sólo Comerciante.");
        if(MerchantCanonicalProfiles.isDeprecated(s,c)) throw new IllegalArgumentException("Perfil  deprecated: "+s+" / "+c);
        CanonicalStartingEquipment e=switch(s){
            case SHOPKEEPER -> simple(c,50,List.of("Pan","Fruta","Odre"));
            case TAVERN_KEEPER -> tavern(c);
            case BOOKSELLER -> simple(c,70,List.of("Pan","Fruta","Odre","MAGNETLAMPE"));
            case RURAL_AGGREGATOR -> rural(c);
            case V881_INDUSTRIAL_BROKER -> industrial(c,120,List.of("MAGNETLAMPE","Odre","Caja del Artesano"));
            case V881_INDUSTRIAL_CONTRACT_AGENT -> industrial(c,105,List.of("Pan","Fruta","Odre","MAGNETLAMPE"));
            case V881_INDUSTRIAL_CONSULTANT -> industrial(c,145,List.of("Pan","Fruta","Odre","MAGNETLAMPE","Monocular de Reconocimiento V881"));
            case V881_INDUSTRIALIST -> industrial(c,175,List.of("Cecina","Odre","MAGNETLAMPE","Caja de Herramientas"));
            case SHIPOWNER -> industrial(c,190,List.of("Cecina","Odre","MAGNETLAMPE","Astrolabio"));
            case FINANCIER -> simple(c,220,List.of("Pan","Fruta","Odre","MAGNETLAMPE","Astrolabio"));
            case INFRASTRUCTURE_CONCESSIONAIRE -> industrial(c,235,List.of("Cecina","Odre","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881"));
            case GRAND_MERCHANT -> industrial(c,280,List.of("Cecina","Frutos secos","Odre","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881"));
            case RESTRICTED_MATERIALS_BROKER -> industrial(c,320,List.of("Cecina","Odre","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881","Botella de Líquido Refrigerante"));
            default -> throw new IllegalArgumentException("Subprofesión no materializada en Comerciante: "+s);
        };
        var a=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        return CanonicalStartingEquipmentPackingPolicy.requireValid(new CanonicalStartingEquipment(e.wornGarments(),e.inventoryObjectNames(),Optional.of(a),e.weaponNames(),e.ammunitionNames(),e.personalTransport(),e.inventoryExpanders(),e.currencyStacks(),e.materialUnits()));
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}
    private static CanonicalStartingEquipment simple(CharacterClass c,int cash,List<String> items){return eq(garments(c),items,cash,List.of(),Optional.empty());}
    private static CanonicalStartingEquipment tavern(CharacterClass c){return eq(garments(c),List.of("Pan","Fruta","Uva deshidratada","Odre","MAGNETLAMPE"),110,List.of(),Optional.empty());}
    private static CanonicalStartingEquipment rural(CharacterClass c){return new CanonicalStartingEquipment(garments(c),List.of("Pan","Cecina","Odre","MAGNETLAMPE"),Optional.empty(),List.of(),List.of(),Optional.of(PersonalTransportType.HORSE_DRAFT),List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT),List.of(new CurrencyStack(CurrencyType.VALERITA,95)),List.of(ArmorMaterial.CLOTH));}
    private static CanonicalStartingEquipment industrial(CharacterClass c,int cash,List<String> items){return eq(garments(c),items,cash,List.of(ArmorMaterial.CLOTH),Optional.empty());}
    private static CanonicalStartingEquipment eq(List<ArmorPiece> g,List<String> i,int cash,List<ArmorMaterial> mat,Optional<PersonalTransportType> t){return new CanonicalStartingEquipment(g,i,Optional.empty(),List.of(),List.of(),t,t.isPresent()?List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT):List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER),List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),mat);}
    private static List<ArmorPiece> garments(CharacterClass c){
        boolean male=c==CharacterClass.LUCHADOR||c==CharacterClass.INTELECTUAL||c==CharacterClass.INDOMITO;
        return male?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerModularShirtV881(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.outerSackCoatV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerSackCoatV881());
    }
}

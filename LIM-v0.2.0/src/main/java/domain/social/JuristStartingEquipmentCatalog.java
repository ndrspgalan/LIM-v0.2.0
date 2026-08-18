package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/**  — patrimonio material inicial de Jurista, construido por perfil activo. */
public final class JuristStartingEquipmentCatalog {
    private JuristStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        if(s.profession()!=Profession.JURIST) throw new IllegalArgumentException("Sólo Jurista.");
        if(JuristCanonicalProfiles.isDeprecated(s,c)) throw new IllegalArgumentException("Perfil  deprecated: "+s+" / "+c);
        CanonicalStartingEquipment e=switch(s){
            case PUBLIC_SCRIBE -> eq(c,55,List.of("Pan","Fruta","Odre","MAGNETLAMPE"));
            case MAGISTRATE -> eq(c,115,List.of("Pan","Fruta","Odre","MAGNETLAMPE","Astrolabio"));
            case CONTINUITY_JURIST -> eq(c,180,List.of("Cecina","Odre","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881"));
            case DOCTRINE_CUSTODIAN -> eq(c,220,List.of("Cecina","Odre","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881","Caja del Artesano"));
            default -> throw new IllegalArgumentException("Subprofesión no materializada en Jurista: "+s);
        };
        var a=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        return CanonicalStartingEquipmentPackingPolicy.requireValid(new CanonicalStartingEquipment(e.wornGarments(),e.inventoryObjectNames(),Optional.of(a),e.weaponNames(),e.ammunitionNames(),e.personalTransport(),e.inventoryExpanders(),e.currencyStacks(),e.materialUnits()));
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}
    private static CanonicalStartingEquipment eq(CharacterClass c,int cash,List<String> items){return new CanonicalStartingEquipment(garments(c),items,Optional.empty(),List.of(),List.of(),Optional.empty(),List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER),List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),List.of(ArmorMaterial.CLOTH));}
    private static List<ArmorPiece> garments(CharacterClass c){
        boolean male=c==CharacterClass.LUCHADOR||c==CharacterClass.INTELECTUAL||c==CharacterClass.INDOMITO;
        return male?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerModularShirtV881(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.outerSackCoatV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerSackCoatV881());
    }
}

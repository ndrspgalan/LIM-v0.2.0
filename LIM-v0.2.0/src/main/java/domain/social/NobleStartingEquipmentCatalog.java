package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.PersonalTransportType;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import java.util.*;

/**  — patrimonio inicial de Noble: lategame, específico por trayectoria y sin conjuntos históricos prohibidos. */
public final class NobleStartingEquipmentCatalog {
    private NobleStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        if(s.profession()!=Profession.NOBLE) throw new IllegalArgumentException("Sólo Noble.");
        if(NobleCanonicalProfiles.isDeprecated(s,c)) throw new IllegalArgumentException("Perfil Noble deprecated: "+s+" / "+c);
        CanonicalStartingEquipment e=switch(s){
            case DYNASTIC_NOBLE -> dynastic(c);
            case CONCESSIONARY_NOBLE -> concession(c);
            case ENLIGHTENED_PATRON -> enlightened(c);
            case PATRIMONIAL_WARLORD -> warlord(c);
            case PERMANENCE_PRETENDER -> permanence(c);
            case STRATEGIC_COMMUNICATIONS_OFFICER -> specialized(c,domain.inventory.item.accessory.ArtifactAccessoryCatalog.heliograph(),List.of());
            case FORENSIC_INVESTIGATOR -> specialized(c,domain.inventory.item.accessory.ArtifactAccessoryCatalog.nocturlabe(),List.of("Cámara fotográfica V881","Contenedor toxicológico Stas-Otto V881","Aparato de Marsh V881"));
            case INTELLIGENCE_AGENT -> specialized(c,domain.inventory.item.accessory.ArtifactAccessoryCatalog.tuningFork(),List.of("Sismoscopio V881"));
            case FIELD_ELECTROATMOSPHERIC_SPECIALIST -> specialized(c,domain.inventory.item.accessory.ArtifactAccessoryCatalog.tokkosho(),List.of());
            default -> throw new IllegalArgumentException("Subprofesión no Noble: "+s);
        };
        return CanonicalStartingEquipmentPackingPolicy.requireValid(e);
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}
    private static CanonicalStartingEquipment dynastic(CharacterClass c){
        return base(c, List.of(ArmorCatalog.outerKnightCloak(),ArmorCatalog.topHatV881()), List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante"), List.of(), 340, Optional.empty());
    }
    private static CanonicalStartingEquipment concession(CharacterClass c){
        return base(c, List.of(ArmorCatalog.outerKnightCloak(),ArmorCatalog.ridingHatV881()), List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante","Monocular de Reconocimiento V881"), List.of(), 420, Optional.of(PersonalTransportType.HORSE_LEISURE));
    }
    private static CanonicalStartingEquipment enlightened(CharacterClass c){
        return base(c, List.of(ArmorCatalog.knightV881Chest(),ArmorCatalog.knightV881Bracers(),ArmorCatalog.knightV881Leggings(),ArmorCatalog.outerKnightCloak(),ArmorCatalog.enlightenedPanopticon()), List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante"), List.of(), 500, Optional.empty());
    }
    private static CanonicalStartingEquipment warlord(CharacterClass c){
        return base(c, List.of(ArmorCatalog.knightV881Chest(),ArmorCatalog.knightV881Bracers(),ArmorCatalog.knightV881Leggings(),ArmorCatalog.outerKnightCloak(),ArmorCatalog.beardedHelmetV881()), List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante","Caja de Herramientas"), List.of("Espada Helicoidal","Daga"), 380, Optional.of(PersonalTransportType.HORSE_RACING));
    }
    private static CanonicalStartingEquipment permanence(CharacterClass c){
        return base(c, List.of(ArmorCatalog.outerKnightCloak(),ArmorCatalog.topHatV881(),ArmorCatalog.normalVisionGlassesV881()), List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante","Caja de Herramientas"), List.of(), 560, Optional.of(PersonalTransportType.MOTORCYCLE_CARDAN_V881));
    }
    private static CanonicalStartingEquipment specialized(CharacterClass c,domain.inventory.item.AccessoryItem artifact,List<String> professionItems){
        var e=base(c,List.of(ArmorCatalog.outerKnightCloak()),new ArrayList<>(List.of("Esencia de lucidez","Frasco de I-RND","Inyección estimulante","Inyección estimulante","Inyección estimulante")),List.of(),460,Optional.empty());
        var items=new ArrayList<>(e.inventoryObjectNames()); items.addAll(professionItems);
        return new CanonicalStartingEquipment(e.wornGarments(),items,Optional.of(artifact),e.weaponNames(),e.ammunitionNames(),e.personalTransport(),e.inventoryExpanders(),e.currencyStacks(),e.materialUnits());
    }
    private static CanonicalStartingEquipment base(CharacterClass c,List<domain.inventory.item.armor.ArmorPiece> extra,List<String> items,List<String> weapons,int cash,Optional<PersonalTransportType> transport){
        boolean male=c==CharacterClass.LUCHADOR||c==CharacterClass.INTELECTUAL||c==CharacterClass.INDOMITO;
        var garments=new ArrayList<>(male?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerModularShirtV881(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.leatherOxfordBrogueShoesV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881()));
        garments.addAll(extra);
        List<InventoryCompartmentType> ex=new ArrayList<>(List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.LEG_POUCH));
        transport.ifPresent(t->ex.add(switch(t){case HORSE_LEISURE->InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE;case HORSE_RACING->InventoryCompartmentType.SADDLEBAGS_HORSE_RACING;case HORSE_DRAFT->InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT;case BICYCLE_MILITARY_V881->InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY;case MOTORCYCLE_CARDAN_V881->InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN;case BICYCLE_FOLDING_V881->InventoryCompartmentType.BACKPACK;}));
        var currency=List.of(new CurrencyStack(CurrencyType.VALERITA,cash));
        return new CanonicalStartingEquipment(garments,items,Optional.of(domain.inventory.item.accessory.ArtifactAccessoryCatalog.astrolabe()),weapons,List.of(),transport,List.copyOf(ex),currency,List.of(ArmorMaterial.CLOTH));
    }
}

package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/** Mendigo con hojas explícitas, abalorio biográfico y colocación física canónica. */
public final class BeggarStartingEquipmentCatalog {
    private BeggarStartingEquipmentCatalog(){}

    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.BEGGAR)throw new IllegalArgumentException("Sólo Mendigo.");
        if(BeggarCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil Mendigo deprecated: "+s+" / "+c);
        boolean male=male(c);
        CanonicalStartingEquipment base=switch(s){
            case PRISONER -> prisoner(male);
            case UNEMPLOYED -> unemployed(male);
            case WORK_DISABLED -> workDisabled(male);
            case INDIGENT -> indigent(male);
            case DISPLACED_RESIDENT -> displaced(male);
            default -> throw new IllegalArgumentException("Mendigo sin loadout activo: "+s);
        };
        var accessory=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        var e=new CanonicalStartingEquipment(base.wornGarments(),base.inventoryObjectNames(),Optional.of(accessory),
                base.weaponNames(),base.ammunitionNames(),base.personalTransport(),base.inventoryExpanders(),base.currencyStacks(),base.materialUnits());
        CanonicalStartingEquipmentPackingPolicy.requireValid(e);
        return e;
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}

    private static boolean male(CharacterClass c){return switch(c){
        case LUCHADOR,INTELECTUAL,INDOMITO -> true;
        case ESPECIALISTA,APODERADO,HERALDO -> false;
        case MAESTRO -> throw new IllegalArgumentException("Maestro deprecated.");
    };}
    private static List<CurrencyStack> cash(int v){return v<=0?List.of():List.of(new CurrencyStack(CurrencyType.VALERITA,v));}
    private static CanonicalStartingEquipment make(List<ArmorPiece> g,List<String> inv,List<InventoryCompartmentType> ex,int cash){
        return new CanonicalStartingEquipment(g,inv,Optional.empty(),List.of(),List.of(),Optional.empty(),ex,cash(cash),List.of());
    }

    private static CanonicalStartingEquipment prisoner(boolean male){
        var g=male?List.of(ArmorCatalog.innerShirt(),ArmorCatalog.innerKneeDrawersV881(),ArmorCatalog.middleStraightTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.outerEspadrillesV881())
                :List.of(ArmorCatalog.innerBlouse(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleStraightSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerEspadrillesV881());
        return make(g,List.of(),List.of(),0);
    }
    private static CanonicalStartingEquipment unemployed(boolean male){
        var g=male?List.of(ArmorCatalog.innerShirt(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleStraightTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.capV881())
                :List.of(ArmorCatalog.innerBlouse(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerLeatherAnkleBootsV881());
        return make(g,List.of("Pan","Fruta","Odre","Amadou","Pedernal"),List.of(InventoryCompartmentType.BANDOLIER),35);
    }
    private static CanonicalStartingEquipment workDisabled(boolean male){
        var g=male?List.of(ArmorCatalog.innerShirt(),ArmorCatalog.middleCardiganV881(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleLooseTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.knittedCapV881())
                :List.of(ArmorCatalog.innerBlouse(),ArmorCatalog.middleCardiganV881(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleFullSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.knittedCapV881());
        return make(g,List.of("Pan","Frutos secos","Odre","Petaca de hidromiel","Corteza de sauce"),List.of(InventoryCompartmentType.BANDOLIER),25);
    }
    private static CanonicalStartingEquipment indigent(boolean male){
        var g=male?List.of(ArmorCatalog.innerShirt(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleLooseTrousersV881(),ArmorCatalog.innerFeetWrapsV881(),ArmorCatalog.outerEspadrillesV881(),ArmorCatalog.bandanaV881())
                :List.of(ArmorCatalog.innerBlouse(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleFullSkirtV881(),ArmorCatalog.innerFeetWrapsV881(),ArmorCatalog.outerEspadrillesV881(),ArmorCatalog.bandanaV881());
        return make(g,List.of("Pan","Frutos secos","Odre","Amadou","Pedernal"),List.of(InventoryCompartmentType.BANDOLIER),8);
    }
    private static CanonicalStartingEquipment displaced(boolean male){
        var g=male?List.of(ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.broadBrimHatV881())
                :List.of(ArmorCatalog.innerRegionalBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerReinforcedPetticoatV881(),ArmorCatalog.middleWorkSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.broadBrimHatV881());
        return make(g,List.of("Pan","Cecina","Frutos secos","Uva deshidratada","Odre","Amadou","Pedernal","Emplasto de milenrama","Piedra de afilar"),
                List.of(InventoryCompartmentType.BACKPACK),60);
    }
}

package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/** Jornalero segregado por rol real, con patrimonio y colocación explícitos. */
public final class DayLaborerStartingEquipmentCatalog {
    private DayLaborerStartingEquipmentCatalog(){}

    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.DAY_LABORER)throw new IllegalArgumentException("Sólo Jornalero.");
        if(DayLaborerCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil Jornalero deprecated: "+s+" / "+c);
        boolean male=male(c);
        CanonicalStartingEquipment base=switch(s){
            case RECONSTRUCTION_LABORER -> reconstruction(male,c);
            case ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR -> electro(male,c);
            case STABLE_HAND -> stable(male,c);
            case SANITATION_OPERATOR -> sanitation(male,c);
            case FARMER -> farmer(male,c);
            case LIVESTOCK_KEEPER -> livestock(male,c);
            case HORTICULTURIST -> horticulturist(male,c);
            case FOREST_LUMBERJACK -> lumberjack(male,c);
            case EXTRACTION_MINER -> miner(male,c);
            case STEVEDORE -> stevedore(male,c);
            case AGRICULTURAL_SELECTOR_CONDITIONER -> selector(male,c);
            case HAULAGE_LABORER -> haulage(male,c);
            case COMPANION_ANIMAL_BREEDER -> companionAnimalBreeder(male,c);
            default -> throw new IllegalArgumentException("Jornalero sin loadout : "+s);
        };
        int cash=Math.max(1,(int)Math.round(s.monthlyReferenceValeritas()*0.05));
        var accessory=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        var e=new CanonicalStartingEquipment(base.wornGarments(),base.inventoryObjectNames(),Optional.of(accessory),
                base.weaponNames(),base.ammunitionNames(),base.personalTransport(),base.inventoryExpanders(),
                List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),base.materialUnits());
        CanonicalStartingEquipmentPackingPolicy.requireValid(e);
        return e;
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}

    private static boolean male(CharacterClass c){return switch(c){
        case LUCHADOR,INTELECTUAL,INDOMITO -> true;
        case ESPECIALISTA,APODERADO,HERALDO -> false;
        case MAESTRO -> throw new IllegalArgumentException("Maestro deprecated.");
    };}

    private static List<ArmorPiece> work(boolean male,boolean smock,boolean goggles){
        ArrayList<ArmorPiece>a=new ArrayList<>();
        if(male){a.add(ArmorCatalog.innerWorkShirt());a.add(ArmorCatalog.middleWorkWaistcoat());a.add(ArmorCatalog.innerKneeDrawersV881());a.add(ArmorCatalog.middleWorkTrousersV881());a.add(ArmorCatalog.innerFeetHeavyWorkSocksV881());}
        else{a.add(ArmorCatalog.innerRegionalBlouse());a.add(ArmorCatalog.middleWorkWaistcoat());a.add(ArmorCatalog.innerWomensDrawersV881());a.add(ArmorCatalog.innerReinforcedPetticoatV881());a.add(ArmorCatalog.middleWorkSkirtV881());a.add(ArmorCatalog.innerFeetHeavyKnitStockingsV881());}
        if(smock)a.add(ArmorCatalog.outerWorkSmockV881());
        a.add(ArmorCatalog.leatherHeavyWorkBootsV881());a.add(ArmorCatalog.hardenedLeatherFingerlessGloves());
        if(goggles)a.add(ArmorCatalog.workshopGoggles()); else a.add(ArmorCatalog.laborerKerchiefV881());
        return List.copyOf(a);
    }
    private static List<ArmorPiece> field(boolean male){
        if(male)return List.of(ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerKneeDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.laborerHatV881(),ArmorCatalog.laborerKerchiefV881());
        return List.of(ArmorCatalog.innerRegionalBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerReinforcedPetticoatV881(),ArmorCatalog.middleWorkSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.laborerHatV881(),ArmorCatalog.laborerKerchiefV881());
    }
    private static CanonicalStartingEquipment make(List<ArmorPiece>g,List<String>inv,List<String>w,Optional<PersonalTransportType>t,List<InventoryCompartmentType>ex,List<ArmorMaterial>mat){
        return new CanonicalStartingEquipment(g,inv,Optional.empty(),w,List.of(),t,ex,List.of(),mat);
    }
    private static List<InventoryCompartmentType> ex(boolean backpack){return backpack?List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.BANDOLIER):List.of(InventoryCompartmentType.BANDOLIER);}

    private static CanonicalStartingEquipment reconstruction(boolean m,CharacterClass c){
        return make(work(m,false,false),List.of("Pan","Cecina","Odre","Amadou","Pedernal","Apósito de musgo de turbera"),
                List.of(c==CharacterClass.LUCHADOR?"Martillo de bola":"Zapapico"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment electro(boolean m,CharacterClass c){
        return make(work(m,true,true),List.of("Pan","Frutos secos","Odre","KNIJPKAT","Caja de Herramientas"),
                c==CharacterClass.INTELECTUAL?List.of("Martillo de bola"):List.of("Piqueta"),Optional.empty(),ex(true),List.of());
    }
    private static CanonicalStartingEquipment stable(boolean m,CharacterClass c){
        List<ArmorPiece>g=m?List.of(ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleRidingWaistcoat(),ArmorCatalog.innerKneeDrawersV881(),ArmorCatalog.middleRidingTrousersV881(),ArmorCatalog.innerFeetHighStockingsV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.ridingHatV881())
                :List.of(ArmorCatalog.innerRegionalBlouse(),ArmorCatalog.middleRidingWaistcoat(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerDividedPetticoatV881(),ArmorCatalog.middleDividedSkirtV881(),ArmorCatalog.innerFeetHighStockingsV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.ridingHatV881());
        return make(g,List.of("Pan","Fruta","Odre"),c==CharacterClass.INDOMITO?List.of("Bō"):List.of(),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment sanitation(boolean m,CharacterClass c){
        return make(work(m,true,false),List.of("Pan","Cecina","Odre","KNIJPKAT","Emplasto de milenrama","Apósito de musgo de turbera","Caja de Herramientas"),
                List.of(),Optional.empty(),ex(true),List.of());
    }
    private static CanonicalStartingEquipment farmer(boolean m,CharacterClass c){
        return make(field(m),List.of("Pan","Cecina","Fruta","Odre","Amadou","Pedernal","Piedra de afilar"),
                c==CharacterClass.INTELECTUAL||c==CharacterClass.APODERADO?List.of():List.of("Hoz"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment livestock(boolean m,CharacterClass c){
        return make(field(m),List.of("Pan","Cecina","Frutos secos","Odre","Emplasto de milenrama"),
                c==CharacterClass.INDOMITO?List.of("Horca"):List.of("Bō"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment horticulturist(boolean m,CharacterClass c){
        return make(field(m),List.of("Pan","Fruta","Uva deshidratada","Odre","Piedra de afilar"),List.of("Hoz"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment lumberjack(boolean m,CharacterClass c){
        boolean draft=c==CharacterClass.INDOMITO;
        var transport=draft?Optional.of(PersonalTransportType.HORSE_DRAFT):Optional.<PersonalTransportType>empty();
        var expand=draft?List.of(InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT):ex(false);
        return make(work(m,false,false),List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Piedra de afilar","Emplasto de milenrama","Apósito de musgo de turbera"),
                List.of("Hacha de Leñador"),transport,expand,draft?List.of(ArmorMaterial.WOOD):List.of());
    }
    private static CanonicalStartingEquipment miner(boolean m,CharacterClass c){
        return make(work(m,false,true),List.of("Pan","Cecina","Frutos secos","Odre","MAGNETLAMPE","Emplasto de milenrama","Apósito de musgo de turbera"),
                List.of("Pico"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment stevedore(boolean m,CharacterClass c){
        return make(work(m,false,false),List.of("Pan","Cecina","Odre","Amadou","Pedernal"),List.of("Bō"),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment selector(boolean m,CharacterClass c){
        return make(field(m),List.of("Pan","Fruta","Uva deshidratada","Odre"),List.of(),Optional.empty(),ex(false),List.of());
    }

    private static CanonicalStartingEquipment companionAnimalBreeder(boolean m,CharacterClass c){
        if(c!=CharacterClass.INTELECTUAL||!m)throw new IllegalArgumentException("Criador de animales de compañía: sólo Intelectual masculino.");
        return make(field(true),List.of("Pan","Fruta","Frutos secos","Odre","Emplasto de milenrama","Apósito de musgo de turbera"),List.of(),Optional.empty(),ex(false),List.of());
    }
    private static CanonicalStartingEquipment haulage(boolean m,CharacterClass c){
        return make(work(m,false,false),List.of("Pan","Cecina","Odre","Apósito de musgo de turbera"),
                List.of(),Optional.of(PersonalTransportType.HORSE_DRAFT),
                List.of(InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT),List.of(ArmorMaterial.WOOD));
    }
}

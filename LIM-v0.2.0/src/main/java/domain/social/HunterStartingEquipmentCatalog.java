package domain.social;
import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;
/** patrimonio de Cazador, carcaj real y placement físico. */
public final class HunterStartingEquipmentCatalog {
 private HunterStartingEquipmentCatalog(){}
 public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
  if(s.profession()!=Profession.HUNTER)throw new IllegalArgumentException("Sólo Cazador.");if(HunterCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil Cazador deprecated: "+s+" / "+c);boolean m=male(c);
  CanonicalStartingEquipment b=switch(s){
   case ROAD_GUIDE -> switch(c){
    case INTELECTUAL -> eq(field(m),List.of("Pan","Cecina","Frutos secos","Uva deshidratada","Odre","Amadou","Pedernal","Emplasto de milenrama","MAGNETLAMPE","Astrolabio","Monocular de Reconocimiento V881","Tarro de Resina"),List.of("Rifle Neumático de Repetición V881"),List.of("Cartucho .46 de plomo"),70,List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE),Optional.of(PersonalTransportType.HORSE_LEISURE));
    case INDOMITO -> eq(field(m),List.of("Pan","Cecina","Frutos secos","Uva deshidratada","Odre","Amadou","Pedernal","Apósito de musgo de turbera","MAGNETLAMPE","Tarro de Resina"),List.of("Rifle Neumático de Repetición V881"),List.of("Cartucho .46 de plomo"),55,List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE),Optional.of(PersonalTransportType.HORSE_LEISURE));
    case ESPECIALISTA -> eq(field(m),List.of("Pan","Cecina","Fruta","Odre","Amadou","Pedernal","Emplasto de milenrama","MAGNETLAMPE","Monocular de Reconocimiento V881"),List.of("Rifle Neumático de Repetición V881"),List.of("Cartucho .46 de plomo"),64,List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE),Optional.of(PersonalTransportType.HORSE_LEISURE)); default->throw new IllegalArgumentException();};
   case WILDLIFE_TRACKER -> switch(c){
    case INDOMITO -> eq(field(m),List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Apósito de musgo de turbera","KNIJPKAT","Monocular de Reconocimiento V881"),List.of("Cuchillo de Carnicero"),List.of(),42,List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case ESPECIALISTA -> eq(field(m),List.of("Pan","Fruta","Frutos secos","Odre","Amadou","Pedernal","Emplasto de milenrama","KNIJPKAT","Monocular de Reconocimiento V881"),List.of("Cuchillo de Carnicero"),List.of(),48,List.of(InventoryCompartmentType.BACKPACK),Optional.empty()); default->throw new IllegalArgumentException();};
   case PROFESSIONAL_HUNTER -> switch(c){
    case LUCHADOR -> hunter(m,52,List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Apósito de musgo de turbera","Tarro de Resina","Piedra de afilar","Monocular de Reconocimiento V881"));
    case INDOMITO -> hunter(m,46,List.of("Pan","Cecina","Frutos secos","Uva deshidratada","Odre","Amadou","Pedernal","Emplasto de milenrama","Apósito de musgo de turbera","Tarro de Resina","Piedra de afilar"));
    case ESPECIALISTA -> hunter(m,58,List.of("Pan","Cecina","Fruta","Odre","Amadou","Pedernal","Emplasto de milenrama","Tarro de Resina","Piedra de afilar","Monocular de Reconocimiento V881")); default->throw new IllegalArgumentException();};
   case TRAPPER -> switch(c){
    case INTELECTUAL -> eq(field(m),List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Apósito de musgo de turbera","KNIJPKAT","Tarro de Resina","Piedra de afilar","Caja del Artesano"),List.of("Rifle Neumático de Repetición V881","Cuchillo de Carnicero"),List.of("Cartucho .46 de plomo"),48,List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case INDOMITO -> eq(field(m),List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Emplasto de milenrama","KNIJPKAT","Tarro de Resina","Piedra de afilar"),List.of("Rifle Neumático de Repetición V881","Cuchillo de Carnicero"),List.of("Cartucho .46 de plomo"),38,List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case ESPECIALISTA -> eq(field(m),List.of("Pan","Fruta","Frutos secos","Odre","Amadou","Pedernal","Emplasto de milenrama","KNIJPKAT","Tarro de Resina","Piedra de afilar","Caja del Artesano"),List.of("Rifle Neumático de Repetición V881","Cuchillo de Carnicero"),List.of("Cartucho .46 de plomo"),44,List.of(InventoryCompartmentType.BACKPACK),Optional.empty()); default->throw new IllegalArgumentException();};
   default->throw new IllegalArgumentException("Cazador fuera de : "+s);};
  var a=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());var e=new CanonicalStartingEquipment(b.wornGarments(),b.inventoryObjectNames(),Optional.of(a),b.weaponNames(),b.ammunitionNames(),b.personalTransport(),b.inventoryExpanders(),b.currencyStacks(),b.materialUnits());CanonicalStartingEquipmentPackingPolicy.requireValid(e);return e;
 }
 public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}
 private static CanonicalStartingEquipment hunter(boolean m,int cash,List<String>inv){return eq(field(m),inv,List.of("Arco Compuesto","Cuchillo de Carnicero"),List.of("Flecha perforante","Flecha perforante","Flecha perforante","Flecha perforante","Flecha de Púas","Flecha de Púas","Flecha de Púas","Flecha de Púas","Flecha de Hoja","Flecha de Hoja","Flecha de Hoja","Flecha de Hoja"),cash,List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.ARROW_QUIVER,InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE),Optional.of(PersonalTransportType.HORSE_LEISURE));}
 private static CanonicalStartingEquipment eq(List<ArmorPiece>g,List<String>i,List<String>w,List<String>a,int cash,List<InventoryCompartmentType>x,Optional<PersonalTransportType>t){return new CanonicalStartingEquipment(g,i,Optional.empty(),w,a,t,x,List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),List.of());}
 private static boolean male(CharacterClass c){return c==CharacterClass.LUCHADOR||c==CharacterClass.INTELECTUAL||c==CharacterClass.INDOMITO;}
 private static List<ArmorPiece> field(boolean m){return m?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.hunterHatV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerReinforcedPetticoatV881(),ArmorCatalog.middleWorkSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.hunterHatV881());}
}

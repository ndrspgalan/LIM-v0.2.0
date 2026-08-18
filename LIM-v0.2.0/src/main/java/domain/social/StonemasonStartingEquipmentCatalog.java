package domain.social;
import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;
/** patrimonio artesanal individual + placement físico real. */
public final class StonemasonStartingEquipmentCatalog {
 private StonemasonStartingEquipmentCatalog(){}
 public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
  Objects.requireNonNull(s);Objects.requireNonNull(c);
  if(s.profession()!=Profession.STONEMASON)throw new IllegalArgumentException("Sólo STONEMASON.");
  if(StonemasonCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil  deprecated: "+s+" / "+c);
  CanonicalStartingEquipment base=switch(s){
   case STONE_SETTER -> switch(c){
    case LUCHADOR -> eq(work(true),List.of("Pan","Cecina","Frutos secos","Odre","Amadou","Pedernal","Caja de Herramientas","Apósito de musgo de turbera"),List.of("Zapapico"),45,List.of(ArmorMaterial.WOOD),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case INDOMITO -> eq(work(true),List.of("Pan","Cecina","Frutos secos","Odre","Caja de Herramientas","Emplasto de milenrama","Apósito de musgo de turbera"),List.of("Zapapico"),38,List.of(ArmorMaterial.WOOD,ArmorMaterial.WOOD),List.of(InventoryCompartmentType.BACKPACK,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT),Optional.of(PersonalTransportType.HORSE_DRAFT));
    default -> throw new IllegalArgumentException();};
   case STONEWORK_MASTER -> switch(c){
    case INTELECTUAL -> eq(precise(true),List.of("Pan","Fruta","Odre","Caja de Herramientas"),List.of(),82,List.of(ArmorMaterial.WOOD),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    default -> throw new IllegalArgumentException();};
   case PRECISION_STONECUTTER -> switch(c){
    case ESPECIALISTA -> eq(work(false),List.of("Pan","Fruta","Frutos secos","Odre","Caja de Herramientas","Apósito de musgo de turbera"),List.of("Piqueta"),62,List.of(ArmorMaterial.WOOD),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    default -> throw new IllegalArgumentException();};
   default -> throw new IllegalArgumentException("Subprofesión  no materializada: "+s);
  };
  var a=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
  var e=new CanonicalStartingEquipment(base.wornGarments(),base.inventoryObjectNames(),Optional.of(a),base.weaponNames(),base.ammunitionNames(),base.personalTransport(),base.inventoryExpanders(),base.currencyStacks(),base.materialUnits());
  CanonicalStartingEquipmentPackingPolicy.requireValid(e);return e;
 }
 public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return CivilianStartingEquipmentSupport.placement(equipment(s,c));}
 private static CanonicalStartingEquipment eq(List<ArmorPiece>g,List<String>i,List<String>w,int cash,List<ArmorMaterial>mat,List<InventoryCompartmentType>x,Optional<PersonalTransportType>t){return new CanonicalStartingEquipment(g,i,Optional.empty(),w,List.of(),t,x,List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),mat);}
 private static List<ArmorPiece> work(boolean m){return m?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerKneeDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.laborerKerchiefV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.outerWorkSmockV881(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerReinforcedPetticoatV881(),ArmorCatalog.middleWorkSkirtV881(),ArmorCatalog.innerFeetHeavyKnitStockingsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.laborerKerchiefV881());}
 private static List<ArmorPiece> precise(boolean m){return m?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerWorkShirt(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleWorkTrousersV881(),ArmorCatalog.innerFeetHeavyWorkSocksV881(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.beretV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleWorkWaistcoat(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.hardenedLeatherFingerlessGloves());}
 private static List<ArmorPiece> salon(boolean m){return m?List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerModularShirtV881(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.outerSackCoatV881(),ArmorCatalog.boaterV881()):List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleRegionalBodice(),ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.outerSackCoatV881(),ArmorCatalog.walkingHatV881());}
}

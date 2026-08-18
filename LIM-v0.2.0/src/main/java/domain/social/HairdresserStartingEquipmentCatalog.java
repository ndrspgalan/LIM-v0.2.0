package domain.social;
import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;
/** patrimonio artesanal individual + placement físico real. */
public final class HairdresserStartingEquipmentCatalog {
 private HairdresserStartingEquipmentCatalog(){}
 public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
  Objects.requireNonNull(s);Objects.requireNonNull(c);
  if(s.profession()!=Profession.HAIRDRESSER)throw new IllegalArgumentException("Sólo HAIRDRESSER.");
  if(HairdresserCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil  deprecated: "+s+" / "+c);
  CanonicalStartingEquipment base=switch(s){
   case BARBER -> switch(c){
    case INTELECTUAL -> eq(salon(true),List.of("Pan","Fruta","Odre","Amadou","Pedernal","Caja del Artesano"),List.of(),48,List.of(),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case ESPECIALISTA -> eq(precise(false),List.of("Pan","Fruta","Odre","Caja del Artesano","PEINE CEREMONIAL"),List.of(),42,List.of(),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    default -> throw new IllegalArgumentException();};
   case SALON_HAIRDRESSER -> switch(c){
    case ESPECIALISTA -> eq(salon(false),List.of("Bizcocho","Fruta","Odre","Caja del Artesano","PEINE CEREMONIAL"),List.of(),68,List.of(ArmorMaterial.CLOTH),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
    case HERALDO -> eq(salon(false),List.of("Bizcocho","Uva deshidratada","Odre","Caja del Artesano","ESPEJO DE BOLSILLO"),List.of(),92,List.of(ArmorMaterial.CLOTH),List.of(InventoryCompartmentType.BACKPACK),Optional.empty());
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

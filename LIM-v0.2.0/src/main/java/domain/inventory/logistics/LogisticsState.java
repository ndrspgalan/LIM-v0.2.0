package domain.inventory.logistics;
import domain.inventory.InventoryEntry;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.worldmemory.spatial.WorldCoordinate;
import java.util.*;

public final class LogisticsState {
 private final Map<InventoryCompartmentType,InventoryCompartment> compartments; private final PersonalTransportState personalTransport;
 public LogisticsState(Map<InventoryCompartmentType,InventoryCompartment> compartments,PersonalTransportState personalTransport){
  Objects.requireNonNull(compartments);this.personalTransport=Objects.requireNonNull(personalTransport);
  EnumMap<InventoryCompartmentType,InventoryCompartment> copy=new EnumMap<>(InventoryCompartmentType.class);
  for(InventoryCompartmentType t:InventoryCompartmentType.values()){
   InventoryCompartment c=compartments.get(t);
   if(c==null){c=InventoryCompartment.empty(t,false);}
   if(c.type()!=t)throw new IllegalArgumentException("Compartimento incorrecto");copy.put(t,c);
  }
  for (InventoryCompartmentType t : InventoryCompartmentType.values()) {
   if (!PersonalTransportSaddlebagPolicy.isSaddlebagType(t)) continue;
   PersonalTransportType owner = PersonalTransportSaddlebagPolicy.transportFor(t).orElseThrow();
   if (copy.get(t).available() && !personalTransport.unit(owner).owned())
    throw new IllegalArgumentException(t.label()+" requiere poseer "+owner.label()+".");
  }
  if(copy.get(InventoryCompartmentType.BACKPACK).available() && copy.get(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available())
   throw new IllegalArgumentException("La Mochila Dorsal de Expedición V881 y el Sistema de Transporte Dorsal del Rotor ocupan la misma ranura dorsal.");
  this.compartments=Map.copyOf(copy);
 }
 /** desnudo = ningún compartimento corporal/logístico disponible. */
 public static LogisticsState emptyWithoutPersonalTransport(){EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);for(InventoryCompartmentType t:InventoryCompartmentType.values())m.put(t,InventoryCompartment.empty(t,false));return new LogisticsState(m,PersonalTransportState.none());}
 public java.util.Map<InventoryCompartmentType,InventoryCompartment> compartments(){return java.util.Map.copyOf(compartments);}
 public InventoryCompartment compartment(InventoryCompartmentType t){return compartments.get(t);} public PersonalTransportState personalTransport(){return personalTransport;}
 public Optional<PersonalTransportType> selectedPersonalTransportType(){return personalTransport.selectedType();}
 public LogisticsState withCompartment(InventoryCompartment compartment){EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);m.putAll(compartments);m.put(compartment.type(),compartment);return new LogisticsState(m,personalTransport);}

 /** recompone los dos inventarios corporales desde las prendas realmente equipadas.
  * Si al retirar/cambiar una prenda su contenido deja de caber, la operación falla: los objetos deben reubicarse antes. */
 public LogisticsState synchronizeGarmentStorage(ArmorEquipmentLayout layout){
  Objects.requireNonNull(layout,"El layout de armadura no puede ser nulo.");
  InventoryCompartment currentLegs=compartment(InventoryCompartmentType.LEGGINGS_STORAGE);
  InventoryCompartment currentChest=compartment(InventoryCompartmentType.CHEST_STORAGE);
  InventoryCompartment nextLegs=InventoryCompartment.modular(InventoryCompartmentType.LEGGINGS_STORAGE,
    GarmentStoragePolicy.modulesFor(layout,InventoryCompartmentType.LEGGINGS_STORAGE),currentLegs.entries());
  InventoryCompartment nextChest=InventoryCompartment.modular(InventoryCompartmentType.CHEST_STORAGE,
    GarmentStoragePolicy.modulesFor(layout,InventoryCompartmentType.CHEST_STORAGE),currentChest.entries());
  return withCompartment(nextLegs).withCompartment(nextChest);
 }
 public LogisticsState synchronizeGarmentStorage(domain.inventory.equipment.EquipmentState equipment){
  Objects.requireNonNull(equipment);
  InventoryCompartment currentLegs=compartment(InventoryCompartmentType.LEGGINGS_STORAGE);
  InventoryCompartment currentChest=compartment(InventoryCompartmentType.CHEST_STORAGE);
  InventoryCompartment nextLegs=InventoryCompartment.modular(InventoryCompartmentType.LEGGINGS_STORAGE,GarmentStoragePolicy.modulesFor(equipment,InventoryCompartmentType.LEGGINGS_STORAGE),currentLegs.entries());
  InventoryCompartment nextChest=InventoryCompartment.modular(InventoryCompartmentType.CHEST_STORAGE,GarmentStoragePolicy.modulesFor(equipment,InventoryCompartmentType.CHEST_STORAGE),currentChest.entries());
  return withCompartment(nextLegs).withCompartment(nextChest);
 }


 public LogisticsState attachHelmetToBackpack(ArmorPiece helmet){InventoryCompartment backpack=compartment(InventoryCompartmentType.BACKPACK);return withCompartment(backpack.withExternalHelmet(helmet));}
 public LogisticsState detachHelmetFromBackpack(){return withCompartment(compartment(InventoryCompartmentType.BACKPACK).withoutExternalHelmet());}
 public LogisticsState selectPersonalTransport(PersonalTransportType t){return new LogisticsState(compartments,personalTransport.select(t));}
 public PersonalTransportMaterialization callSelectedPersonalTransport(WorldCoordinate player,double distance){
  PersonalTransportType t=personalTransport.selectedType().orElse(null);if(t==null)return new PersonalTransportMaterialization(this,new PersonalTransportCallResult(PersonalTransportCallStatus.UNAVAILABLE,null,null,""));
  PersonalTransportCallResult r=new PersonalTransportCallPolicy().call(personalTransport.unit(t),player,distance);PersonalTransportState next=personalTransport;
  if(r.materialized())next=next.update(personalTransport.unit(t).withState(PersonalTransportOperationState.PARKED,r.coordinate()));
  return new PersonalTransportMaterialization(new LogisticsState(compartments,next),r);
 }
 public LogisticsState foldBicycle(WorldCoordinate player){PersonalTransportUnitState u=personalTransport.unit(PersonalTransportType.BICYCLE_FOLDING_V881);if(!u.owned())throw new IllegalStateException("Bicicleta no adquirida");return new LogisticsState(compartments,personalTransport.update(u.withState(PersonalTransportOperationState.FOLDED_ON_BACK,null)));}
 public boolean saddlebagsAccessibleFromInventory(){return false;}
 public boolean saddlebagsAccessibleAt(PersonalTransportType type){
  Optional<InventoryCompartmentType> saddle=PersonalTransportSaddlebagPolicy.compartmentType(type);
  return saddle.isPresent() && compartment(saddle.get()).available() && personalTransport.unit(type).physicallyPresent();
 }
 public Optional<InventoryCompartment> saddlebagsFor(PersonalTransportType type){
  return PersonalTransportSaddlebagPolicy.compartmentType(type).map(this::compartment);
 }
 public LogisticsState losePersonalTransportNarratively(PersonalTransportType type){
  PersonalTransportState u=personalTransport.lose(type); EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);m.putAll(compartments);
  PersonalTransportSaddlebagPolicy.compartmentType(type).ifPresent(t -> {
   InventoryCompartment c=m.get(t);
   if(c!=null && (!c.entries().isEmpty())) throw new IllegalStateException("No se puede perder "+type.label()+" con objetos dentro de sus alforjas.");
   m.put(t,InventoryCompartment.empty(t,false));
  });
  return new LogisticsState(m,u);
 }
 public double totalWeightKg(){double base=compartments.values().stream().mapToDouble(InventoryCompartment::totalWeightKg).sum();PersonalTransportUnitState f=personalTransport.unit(PersonalTransportType.BICYCLE_FOLDING_V881);return base+(f.operationState()==PersonalTransportOperationState.FOLDED_ON_BACK?15.0:0.0);}
}

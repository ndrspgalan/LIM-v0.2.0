package domain.inventory.item.ammunition;
import domain.inventory.InventoryEntry;import domain.inventory.InventoryFootprint;import java.util.*;
public class ProjectileAmmunitionItem extends InventoryEntry implements AmmunitionSource {
 private final AmmunitionDescriptor descriptor;private boolean available=true;
 public ProjectileAmmunitionItem(String name,String description,double weightKg,AmmunitionDescriptor descriptor){
  super(name,description,weightKg,new InventoryFootprint(1,1),List.of("Tipo | "+descriptor.family(),"Variante | "+descriptor.variant(),"UNIDAD FÍSICA | Individual fuera de contenedor especializado","Recuperable | "+(descriptor.recoverable()?"Sí":"No")));
  this.descriptor=Objects.requireNonNull(descriptor);if(descriptor.family()!=AmmunitionFamily.ARROW&&descriptor.family()!=AmmunitionFamily.PEBBLE)throw new IllegalArgumentException("Solo flechas o guijarros.");
 }
 public ProjectileAmmunitionItem(String name,double weightKg,AmmunitionDescriptor descriptor){this(name,"Proyectil individual recuperable.",weightKg,descriptor);}
 public AmmunitionDescriptor ammunitionDescriptor(){return descriptor;}public int remainingUnits(){return available?1:0;}public int maxUnits(){return 1;}public int shotsLoadedPerConsumedUnit(){return 1;}
 public boolean consumeOneUnit(){if(!available)return false;available=false;return true;}public boolean recover(){if(available||!new domain.recovery.RecoverablePolicy().canRecover(descriptor.recoverable(),true))return false;available=true;return true;}
}

package domain.inventory.container;
import domain.inventory.*;import domain.inventory.logistics.*;import java.util.*;
/** Instancia física de un expansor; vacío puede almacenarse, cargado sólo puede equiparse/inspeccionarse/soltarse. */
public final class InventoryExpanderItem extends InventoryEntry {
 private final InventoryCompartmentType compartmentType;
 public InventoryExpanderItem(InventoryCompartmentType type){super(type.label(),type.narrativeDescription(),type.structuralWeightKg(),type.storedFootprint(),List.of("CONTENEDOR | "+type.label()));this.compartmentType=Objects.requireNonNull(type);}
 public InventoryCompartmentType compartmentType(){return compartmentType;}
 public List<InventoryEntry> containedItems(){return ContainerContentsRegistry.contentsOf(this);}
 public boolean loaded(){return ContainerContentsRegistry.hasContents(this);}
}

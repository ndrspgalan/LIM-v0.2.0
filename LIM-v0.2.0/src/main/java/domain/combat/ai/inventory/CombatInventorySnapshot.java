package domain.combat.ai.inventory;
import domain.inventory.InventoryEntry;
import java.util.*;
/** Objetos físicamente activos, inmediatamente accesibles y transportados por el actor. */
public record CombatInventorySnapshot(List<InventoryEntry> equipped,List<InventoryEntry> quickAccess,List<InventoryEntry> carried){
 public CombatInventorySnapshot{equipped=List.copyOf(equipped);quickAccess=List.copyOf(quickAccess);carried=List.copyOf(carried);}
 public CombatInventorySnapshot(List<InventoryEntry> equipped,List<InventoryEntry> quickAccess){this(equipped,quickAccess,List.of());}
 public List<InventoryEntry> immediatelyAccessible(){return java.util.stream.Stream.concat(equipped.stream(),quickAccess.stream()).distinct().toList();}
 public List<InventoryEntry> allKnownOwnResources(){return java.util.stream.Stream.concat(immediatelyAccessible().stream(),carried.stream()).distinct().toList();}
 public static CombatInventorySnapshot empty(){return new CombatInventorySnapshot(List.of(),List.of(),List.of());}
}

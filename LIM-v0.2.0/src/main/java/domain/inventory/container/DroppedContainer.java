package domain.inventory.container;
import domain.inventory.InventoryEntry;import domain.worldmemory.WorldObjectInstanceId;import domain.worldmemory.spatial.WorldCoordinate;import java.util.*;
public record DroppedContainer(WorldObjectInstanceId id,InventoryEntry carrier,List<InventoryEntry> contents,WorldCoordinate coordinate){public DroppedContainer{Objects.requireNonNull(id);Objects.requireNonNull(carrier);contents=List.copyOf(Objects.requireNonNull(contents));Objects.requireNonNull(coordinate);}}

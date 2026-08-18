package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
public record CompartmentSnapshot(boolean available,List<String> itemKeys,String externalHelmetKey,List<InventoryStorageModuleSnapshot> storageModules) implements Serializable {
 public CompartmentSnapshot{itemKeys=List.copyOf(itemKeys);externalHelmetKey=externalHelmetKey==null?"":externalHelmetKey;storageModules=List.copyOf(storageModules);}
}

package domain.save.snapshot;
import java.io.Serializable;
public record InventoryStorageModuleSnapshot(String label,int verticalSlots,int horizontalSlots) implements Serializable {}

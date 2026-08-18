package domain.save.snapshot;
import java.io.Serializable;
/** Estado mutable de una instancia física persistida. */
public record InventoryItemSnapshot(String key,String name,String orientation,int quantity,int ammunitionRemaining,int pressureRemaining,double wearFraction,double armorPiercing,double armorSlashing,double armorBlunt,boolean sheathed,String gripMode,String actionMode) implements Serializable {
 public InventoryItemSnapshot{key=key==null?"":key;name=name==null?"":name;orientation=orientation==null?"DEFAULT":orientation;gripMode=gripMode==null?"":gripMode;actionMode=actionMode==null?"":actionMode;}
}

package domain.inventory.container;
import domain.inventory.InventoryEntry;import java.util.*;
/** E ejecuta la alternativa seleccionada; Q rota INSPECCIONAR OBJETO ↔ EQUIPAR cuando ambas existen. */
public final class ContainerInteractionPolicy {
 public enum Action { INSPECT_OBJECT, EQUIP }
 private ContainerInteractionPolicy(){}
 public static List<Action> actions(InventoryEntry item,boolean equipCompatible){return equipCompatible?List.of(Action.INSPECT_OBJECT,Action.EQUIP):List.of(Action.INSPECT_OBJECT);}
 public static Action rotateWithQ(List<Action> actions,Action current){int i=actions.indexOf(current);if(i<0)throw new IllegalArgumentException("Acción no disponible.");return actions.get((i+1)%actions.size());}
}

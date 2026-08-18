package domain.inventory.logistics;
import java.util.*;
public final class PersonalTransportInteractionPolicy {
 public List<PersonalTransportInteractionAction> available(PersonalTransportUnitState unit,boolean ownsSaddlebags){
  List<PersonalTransportInteractionAction> a=new ArrayList<>();
  if(unit.operationState()==PersonalTransportOperationState.FOLDED_ON_BACK){a.add(PersonalTransportInteractionAction.DEPLOY);return List.copyOf(a);}
  a.add(PersonalTransportInteractionAction.MOUNT);a.add(PersonalTransportInteractionAction.WALK);
  if(unit.type().foldable())a.add(PersonalTransportInteractionAction.FOLD);
  if(ownsSaddlebags&&unit.type().supportsSaddlebags())a.add(PersonalTransportInteractionAction.OPEN_SADDLEBAGS);
  if(unit.type().family()==PersonalTransportFamily.MOTORCYCLE){a.add(PersonalTransportInteractionAction.REFUEL);a.add(PersonalTransportInteractionAction.TOGGLE_FUEL_RESERVE);}
  a.add(PersonalTransportInteractionAction.EXAMINE);a.add(PersonalTransportInteractionAction.ASSIGN_TO_NPC);return List.copyOf(a);
 }
}

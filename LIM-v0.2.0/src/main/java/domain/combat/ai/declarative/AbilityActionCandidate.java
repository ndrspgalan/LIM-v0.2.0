package domain.combat.ai.declarative;
import domain.ability.MasteryId; import domain.ability.MasteryType; import java.util.*;
/** Capacidad declarativa : disponibilidad, estado y consecuencias; nunca recomendación. */
public record AbilityActionCandidate(MasteryId familyId,String familyName,String manifestationName,MasteryType masteryType,
 AbilityActionType actionType,boolean active,String mechanicalRelation,Map<String,Double> activeMagnitudes){
 public AbilityActionCandidate{Objects.requireNonNull(familyId);familyName=req(familyName);manifestationName=req(manifestationName);Objects.requireNonNull(masteryType);Objects.requireNonNull(actionType);mechanicalRelation=req(mechanicalRelation);activeMagnitudes=Map.copyOf(activeMagnitudes==null?Map.of():activeMagnitudes);}
 private static String req(String s){Objects.requireNonNull(s);s=s.trim();if(s.isEmpty())throw new IllegalArgumentException("Texto vacío");return s;}
}

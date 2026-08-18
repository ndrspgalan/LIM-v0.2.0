package domain.combat.ai.declarative;
import java.util.*;
/** Vista factual de un efecto de maestría persistido por LIM. */
public record AbilityEffectFact(String id,String sourceManifestationId,String targetId,double remainingRealSeconds,boolean sustained,Map<String,Double> magnitudes){
 public AbilityEffectFact{Objects.requireNonNull(id);Objects.requireNonNull(sourceManifestationId);targetId=targetId==null?"":targetId;magnitudes=Map.copyOf(magnitudes==null?Map.of():magnitudes);}
}

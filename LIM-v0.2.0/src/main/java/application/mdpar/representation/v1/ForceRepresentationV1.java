package application.mdpar.representation.v1;
import java.util.List;import java.util.Objects;
public record ForceRepresentationV1(String forceId,List<String>squadIds,boolean ownForce,EpistemicStateV1 knowledgeState){
 public ForceRepresentationV1{if(forceId==null||forceId.isBlank())throw new IllegalArgumentException("forceId");squadIds=List.copyOf(Objects.requireNonNull(squadIds));Objects.requireNonNull(knowledgeState);}
}

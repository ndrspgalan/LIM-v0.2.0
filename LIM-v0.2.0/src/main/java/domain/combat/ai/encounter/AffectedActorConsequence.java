package domain.combat.ai.encounter;
import domain.social.RelationshipType;
import java.util.Objects;
public record AffectedActorConsequence(String actorId,RelationshipType relationship,double expectedDamage,double expectedControlSeconds){
 public AffectedActorConsequence{Objects.requireNonNull(actorId);Objects.requireNonNull(relationship);if(expectedDamage<0||expectedControlSeconds<0||!Double.isFinite(expectedDamage)||!Double.isFinite(expectedControlSeconds))throw new IllegalArgumentException("Consecuencia inválida.");}
}

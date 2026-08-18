package domain.combat.ai.encounter;
import domain.social.RelationshipType;
import java.util.Objects;
public record CombatSpatialActor(String actorId,RelationshipType relationship,double distanceMeters,boolean physicallyAffectable){
 public CombatSpatialActor{Objects.requireNonNull(actorId);Objects.requireNonNull(relationship);if(distanceMeters<0||!Double.isFinite(distanceMeters))throw new IllegalArgumentException("Distancia inválida.");}
}

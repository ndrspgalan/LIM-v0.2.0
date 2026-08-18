package domain.ability;
import domain.social.RelationshipType; import java.util.Objects;
/** ANULACIÓN FUNDACIONAL no modifica el Tipo de Relación. */
public final class FoundationalNullificationRelationshipPolicy {
 private FoundationalNullificationRelationshipPolicy(){}
 public static boolean becomesHostile(RelationshipType currentRelationship){Objects.requireNonNull(currentRelationship);return false;}
 public static RelationshipType relationshipAfterEnteringField(RelationshipType currentRelationship){return Objects.requireNonNull(currentRelationship);}
}

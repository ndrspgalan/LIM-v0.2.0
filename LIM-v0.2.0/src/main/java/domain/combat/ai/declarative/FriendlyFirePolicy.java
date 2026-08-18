package domain.combat.ai.declarative;

import domain.social.RelationshipType;
import java.util.Objects;

/** las acciones ofensivas comprometidas nunca pueden dirigirse deliberadamente a amistosos o románticos. */
public final class FriendlyFirePolicy {
    private FriendlyFirePolicy() {}
    public static boolean committedOffenseForbidden(RelationshipType relationship) {
        Objects.requireNonNull(relationship);
        return relationship == RelationshipType.FRIENDLY || relationship == RelationshipType.ROMANTIC;
    }
    public static DirectedTargetEligibility offensiveTargetEligibility(RelationshipType relationship) {
        return committedOffenseForbidden(relationship) ? DirectedTargetEligibility.KNOWN_INVALID : DirectedTargetEligibility.KNOWN_VALID;
    }
}

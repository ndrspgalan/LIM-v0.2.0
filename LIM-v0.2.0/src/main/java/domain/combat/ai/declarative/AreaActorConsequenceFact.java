package domain.combat.ai.declarative;

import domain.social.RelationshipType;
import java.util.Objects;

/** Consecuencia física individual de un área conocida. No se reduce a utilidad ni safe/unsafe. */
public record AreaActorConsequenceFact(
        String actorId,
        RelationshipType relationship,
        double expectedDamage,
        double expectedControlSeconds
) {
    public AreaActorConsequenceFact {
        actorId=Objects.requireNonNull(actorId); Objects.requireNonNull(relationship);
        if(!Double.isFinite(expectedDamage)||expectedDamage<0||!Double.isFinite(expectedControlSeconds)||expectedControlSeconds<0)
            throw new IllegalArgumentException("Consecuencia de área inválida.");
    }
}

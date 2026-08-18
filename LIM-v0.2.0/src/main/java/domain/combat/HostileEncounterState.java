package domain.combat;

import domain.social.RelationshipType;

import java.util.Objects;

/** Delimita un encuentro operativo originado por una relación de tipo HOSTIL. */
public final class HostileEncounterState {
    private boolean active;

    public boolean isActive() { return active; }

    public void beginFor(RelationshipType relationshipType) {
        if (Objects.requireNonNull(relationshipType) != RelationshipType.HOSTILE) {
            throw new IllegalArgumentException("Solo una relación HOSTIL puede iniciar un encuentro hostil.");
        }
        active = true;
    }

    /** Entrada técnica para controladores que ya han validado RelationshipType.HOSTILE. */
    public void begin() { active = true; }

    public void conclude() { active = false; }
}

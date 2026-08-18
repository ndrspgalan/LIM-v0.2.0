package domain.ability;

import domain.character.Gender;
import domain.social.RelationshipType;

import java.util.Objects;

/** Estado social mínimo del objetivo fijado por INCITAR. */
public record IncitementTarget(
        String id,
        Gender gender,
        int strength,
        RelationshipType relationship,
        boolean lockedTarget
) {
    public IncitementTarget {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El objetivo necesita identificador.");
        Objects.requireNonNull(gender);
        if (strength < 0) throw new IllegalArgumentException("La FUERZA no puede ser negativa.");
        Objects.requireNonNull(relationship);
    }

    public IncitementTarget withRelationship(RelationshipType next) {
        return new IncitementTarget(id, gender, strength, Objects.requireNonNull(next), lockedTarget);
    }
}

package domain.ability;

import domain.social.RelationshipType;

/** Resultado puro de una manifestación de INCITAR. */
public record IncitementResult(
        boolean applied,
        RelationshipType relationship,
        boolean joinsAsAlly,
        String reason
) {
    public static IncitementResult rejected(RelationshipType relationship, String reason) {
        return new IncitementResult(false, relationship, false, reason);
    }
}

package domain.communication;

import domain.social.RelationshipType;
import java.util.Objects;

public record PairingCandidate(
        String userId,
        String displayName,
        double distanceMeters,
        RelationshipType relationship,
        boolean lineOfSight,
        boolean terrestrialIntercomEquipped
) {
    public PairingCandidate {
        if(userId==null || userId.isBlank()) throw new IllegalArgumentException("userId vacío.");
        if(displayName==null || displayName.isBlank()) throw new IllegalArgumentException("displayName vacío.");
        if(!Double.isFinite(distanceMeters) || distanceMeters<0) throw new IllegalArgumentException("Distancia inválida.");
        Objects.requireNonNull(relationship);
    }
}

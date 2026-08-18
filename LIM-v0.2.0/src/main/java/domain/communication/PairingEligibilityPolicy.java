package domain.communication;

import domain.environment.time.WeatherProfile;
import domain.social.RelationshipType;
import java.util.EnumSet;
import java.util.Objects;

public final class PairingEligibilityPolicy {
    private static final EnumSet<RelationshipType> ELIGIBLE =
            EnumSet.of(RelationshipType.RELIABLE, RelationshipType.FRIENDLY, RelationshipType.ROMANTIC);

    public boolean eligible(CommunicationDeviceType device, WeatherProfile weather, PairingCandidate candidate){
        Objects.requireNonNull(device); Objects.requireNonNull(weather); Objects.requireNonNull(candidate);
        if(!ELIGIBLE.contains(candidate.relationship())) return false;
        if(candidate.distanceMeters() > CommunicationRangePolicy.rangeMeters(device,weather)+1e-9) return false;
        return switch(device){
            case AERONAUT_INTERCOM -> candidate.terrestrialIntercomEquipped();
            case PANOPTICON -> candidate.lineOfSight(); // no exige otro Panóptico.
        };
    }
}

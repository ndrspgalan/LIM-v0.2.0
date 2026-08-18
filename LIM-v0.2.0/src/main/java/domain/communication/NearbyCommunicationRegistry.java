package domain.communication;

import java.util.List;
import java.util.Objects;

public final class NearbyCommunicationRegistry {
    private List<PairingCandidate> candidates = List.of();

    public List<PairingCandidate> snapshot(){ return candidates; }

    /** La capa de mundo actualiza esta lista con distancia y línea de visión en tiempo real. */
    public void replace(List<PairingCandidate> candidates){
        Objects.requireNonNull(candidates);
        if(candidates.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Candidato nulo.");
        this.candidates=List.copyOf(candidates);
    }
}

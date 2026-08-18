package domain.communication;

import domain.environment.time.WeatherProfile;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class CommunicationPairingService {
    private final PairingEligibilityPolicy eligibility=new PairingEligibilityPolicy();

    public List<PairingCandidate> available(CommunicationDeviceType device, WeatherProfile weather, List<PairingCandidate> candidates){
        Objects.requireNonNull(candidates);
        return candidates.stream()
                .filter(c->eligibility.eligible(device,weather,c))
                .sorted(Comparator.comparingDouble(PairingCandidate::distanceMeters))
                .toList();
    }

    public void pair(CommunicationPairingState state, CommunicationDeviceType device, WeatherProfile weather, PairingCandidate candidate){
        Objects.requireNonNull(state);
        if(!eligibility.eligible(device,weather,candidate))
            throw new IllegalStateException("El usuario ya no cumple relación, alcance o medio de enlace.");
        state.set(device,new CommunicationLinkMemory(candidate.userId(),candidate.userId()));
    }

    /**
     * Tiempo real: rompe si el objetivo deja de ser elegible y conserva memoria.
     * Si reaparece el último usuario y vuelve a cumplir el contrato, se re-enlaza automáticamente.
     */
    public void refresh(CommunicationPairingState state, CommunicationDeviceType device, WeatherProfile weather,
                        List<PairingCandidate> candidates, boolean deviceEquipped){
        Objects.requireNonNull(state); Objects.requireNonNull(candidates);
        CommunicationLinkMemory memory=state.memory(device);
        if(!deviceEquipped){
            if(memory.linked()) state.set(device,new CommunicationLinkMemory(null,memory.lastUserId()));
            return;
        }
        PairingCandidate current=find(memory.currentUserId(),candidates);
        if(memory.linked() && (current==null || !eligibility.eligible(device,weather,current))){
            state.set(device,new CommunicationLinkMemory(null,memory.lastUserId()));
            memory=state.memory(device);
        }
        if(!memory.linked() && memory.lastUserId()!=null){
            PairingCandidate last=find(memory.lastUserId(),candidates);
            if(last!=null && eligibility.eligible(device,weather,last)){
                state.set(device,new CommunicationLinkMemory(last.userId(),last.userId()));
            }
        }
    }

    private static PairingCandidate find(String id,List<PairingCandidate> candidates){
        if(id==null) return null;
        return candidates.stream().filter(c->c.userId().equals(id)).findFirst().orElse(null);
    }
}

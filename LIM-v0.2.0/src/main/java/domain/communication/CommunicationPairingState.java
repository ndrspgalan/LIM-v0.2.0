package domain.communication;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CommunicationPairingState {
    private final EnumMap<CommunicationDeviceType,CommunicationLinkMemory> links =
            new EnumMap<>(CommunicationDeviceType.class);

    public CommunicationPairingState(){
        for(var d:CommunicationDeviceType.values()) links.put(d,CommunicationLinkMemory.empty());
    }

    public CommunicationLinkMemory memory(CommunicationDeviceType device){
        return links.get(Objects.requireNonNull(device));
    }

    void set(CommunicationDeviceType device, CommunicationLinkMemory memory){
        links.put(Objects.requireNonNull(device),Objects.requireNonNull(memory));
    }

    public Map<CommunicationDeviceType,CommunicationLinkMemory> snapshot(){ return Map.copyOf(links); }
}

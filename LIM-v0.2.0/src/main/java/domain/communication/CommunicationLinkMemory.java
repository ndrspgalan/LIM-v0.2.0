package domain.communication;

public record CommunicationLinkMemory(String currentUserId, String lastUserId) {
    public static CommunicationLinkMemory empty(){ return new CommunicationLinkMemory(null,null); }
    public boolean linked(){ return currentUserId!=null; }
}

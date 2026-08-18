package domain.worldmemory.evidence;

public enum KnowledgeReliability {
    RUMOR,
    UNVERIFIED,
    PLAUSIBLE,
    OBSERVED,
    VERIFIED;

    public boolean isMoreReliableThan(KnowledgeReliability other) {
        return ordinal() > other.ordinal();
    }
}

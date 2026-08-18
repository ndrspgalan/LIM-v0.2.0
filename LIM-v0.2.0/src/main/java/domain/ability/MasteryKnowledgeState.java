package domain.ability;

/** Estado persistente de conocimiento de una familia de maestrías. */
public enum MasteryKnowledgeState {
    UNKNOWN,
    REVEALED,
    UNLOCKED;

    public boolean isVisible() { return this != UNKNOWN; }
    public boolean isUsable() { return this == UNLOCKED; }
}

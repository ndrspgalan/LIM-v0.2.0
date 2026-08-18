package domain.worldmemory.access;

public record WorldMemoryAccessResult(boolean allowed, boolean open, String animation, String message) {}

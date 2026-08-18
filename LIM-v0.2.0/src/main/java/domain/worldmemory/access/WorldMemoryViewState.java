package domain.worldmemory.access;

public final class WorldMemoryViewState {
    private boolean open;

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }

    public void toggle() {
        open = !open;
    }
}

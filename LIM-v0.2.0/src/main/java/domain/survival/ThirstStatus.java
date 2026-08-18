package domain.survival;

public enum ThirstStatus {
    HYDRATED(1),
    FUNCTIONAL(0),
    THIRSTY(-1),
    DEHYDRATED(-6);

    private final int gameplayValue;
    ThirstStatus(int gameplayValue) { this.gameplayValue = gameplayValue; }
    public int gameplayValue() { return gameplayValue; }
}

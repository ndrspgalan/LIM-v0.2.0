package domain.character.progression;

public record LevelUpResult(boolean success, String message) {
    public static LevelUpResult success(String message) {
        return new LevelUpResult(true, message);
    }

    public static LevelUpResult failure(String message) {
        return new LevelUpResult(false, message);
    }
}

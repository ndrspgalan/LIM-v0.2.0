package domain.orientation;

public record AstrolabeUseResult(boolean successful, boolean orientAnimationTriggered,
                                 OrientationSolution solution, String message) {
    public static AstrolabeUseResult rejected(String message) {
        return new AstrolabeUseResult(false, false, OrientationSolution.unavailable(message), message);
    }
}

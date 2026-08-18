package domain.combat;

public record HookResult(boolean triggered, double pullDistanceMeters, StaggerResult stagger, String reason) {
    public static HookResult rejected(String reason) { return new HookResult(false, 0.0, new StaggerResult(0.0, 0.0), reason); }
}

package domain.combat;

public record DismountResult(boolean dismounted, StaggerResult stagger, String reason) {
    public static DismountResult rejected(String reason) { return new DismountResult(false, new StaggerResult(0.0, 0.0), reason); }
}

package domain.observation;

public enum MonocularMagnification {
    X3(3), X4(4), X5(5);

    private final int multiplier;
    MonocularMagnification(int multiplier) { this.multiplier = multiplier; }
    public int multiplier() { return multiplier; }
    public double effectiveRangeMeters() { return 250.0 * multiplier; }

    public MonocularMagnification increase() {
        return switch (this) { case X3 -> X4; case X4, X5 -> X5; };
    }
    public MonocularMagnification decrease() {
        return switch (this) { case X5 -> X4; case X4, X3 -> X3; };
    }
}

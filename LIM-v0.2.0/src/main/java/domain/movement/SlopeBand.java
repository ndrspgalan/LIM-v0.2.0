package domain.movement;

public enum SlopeBand {
    RUN_ALLOWED(0.0, 9.0),
    TROT_MAXIMUM(10.0, 14.0),
    WALK_MAXIMUM(15.0, 74.999999),
    CLIMB_REQUIRED(75.0, 120.0),
    IMPASSABLE(120.0, Double.POSITIVE_INFINITY);

    private final double minimumDegrees;
    private final double maximumDegrees;

    SlopeBand(double minimumDegrees, double maximumDegrees) {
        this.minimumDegrees = minimumDegrees;
        this.maximumDegrees = maximumDegrees;
    }

    public double minimumDegrees() { return minimumDegrees; }
    public double maximumDegrees() { return maximumDegrees; }
}

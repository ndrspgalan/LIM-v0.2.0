package domain.worldmemory.spatial;

public enum SpatialPrecision {
    UNKNOWN,
    REGION,
    APPROXIMATE,
    OBSERVED,
    VERIFIED;

    public boolean isMorePreciseThan(SpatialPrecision other) {
        return ordinal() > other.ordinal();
    }
}

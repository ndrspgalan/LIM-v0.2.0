package domain.save.snapshot; import java.io.Serializable;
public record TerrainObservationSnapshot(double x,double y,double z,String surface,double radiusMeters,String observedAt,WorldMemorySourceSnapshot source) implements Serializable {}

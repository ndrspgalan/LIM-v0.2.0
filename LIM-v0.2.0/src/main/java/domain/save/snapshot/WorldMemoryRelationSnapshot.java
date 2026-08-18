package domain.save.snapshot; import java.io.Serializable;
public record WorldMemoryRelationSnapshot(String source,String type,String target,String note) implements Serializable {}

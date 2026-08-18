package domain.save.snapshot; import java.io.Serializable;
public record WorldMemorySourceSnapshot(String type,String reference,String acquiredAt,String reliability) implements Serializable {}

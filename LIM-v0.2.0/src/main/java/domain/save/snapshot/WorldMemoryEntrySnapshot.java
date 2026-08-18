package domain.save.snapshot; import java.io.Serializable; import java.util.*;
public record WorldMemoryEntrySnapshot(String id,String category,String title,String description,List<WorldMemorySourceSnapshot> sources,Double x,Double y,Double z,double uncertaintyRadius,String precision) implements Serializable {public WorldMemoryEntrySnapshot{sources=List.copyOf(sources);}}

package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
/** Narrativa persistente con metadatos de hitos suficientes para reconstruir MEMORAR sin inventar texto. */
public record NarrativeSnapshot(String origin,String currentArc,Set<String> milestones,Map<String,String> milestoneTitles,Map<String,String> milestoneDescriptions,Map<String,String> familyState,boolean gameCompleted,boolean allRunicMarksUnlocked,String selectedRunicMark) implements Serializable {
 public NarrativeSnapshot{origin=origin==null?"":origin;currentArc=currentArc==null?"":currentArc;milestones=Set.copyOf(milestones);milestoneTitles=Map.copyOf(milestoneTitles);milestoneDescriptions=Map.copyOf(milestoneDescriptions);familyState=Map.copyOf(familyState);selectedRunicMark=selectedRunicMark==null?"":selectedRunicMark;}
 public NarrativeSnapshot(String origin,String currentArc,Set<String> milestones,Map<String,String> familyState){this(origin,currentArc,milestones,Map.of(),Map.of(),familyState,false,false,"");}
}

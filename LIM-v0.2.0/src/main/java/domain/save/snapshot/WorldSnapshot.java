package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
public record WorldSnapshot(String dayPhase,long elapsedInPhaseSeconds,String weather,String activeSeason,String phenomenon,long phenomenonRemainingSeconds,long completedDays,List<WorldMemoryEntrySnapshot> entries,List<WorldMemoryRelationSnapshot> relations,List<TerrainObservationSnapshot> terrain,Map<String,String> droppedObjects,Map<String,String> droppedInstances,String observationMark) implements Serializable {
 public WorldSnapshot{dayPhase=dayPhase==null?"DAY":dayPhase;weather=weather==null?"SPRING_CLEAR":weather;activeSeason=activeSeason==null?"SPRING":activeSeason;phenomenon=phenomenon==null?"NONE":phenomenon;entries=List.copyOf(entries);relations=List.copyOf(relations);terrain=List.copyOf(terrain);droppedObjects=Map.copyOf(droppedObjects);droppedInstances=Map.copyOf(droppedInstances);observationMark=observationMark==null?"":observationMark;}
 public WorldSnapshot(double worldHour,Map<String,String> memoryMarkers,Map<String,String> relationships){this("DAY",Math.max(0,(long)(worldHour*3600)),"SPRING_CLEAR","SPRING","NONE",0,0,List.of(),List.of(),List.of(),memoryMarkers,Map.of(),"");}
}

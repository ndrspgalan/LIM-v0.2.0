package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
public record CombatSnapshot(boolean hostileEncounter,Set<String> activeSustained,Set<String> registeredPassives,Map<String,Double> temporaryEffects,boolean sleepProgressionActive) implements Serializable {
 public CombatSnapshot{activeSustained=Set.copyOf(activeSustained);registeredPassives=Set.copyOf(registeredPassives);temporaryEffects=Map.copyOf(temporaryEffects);}
 public CombatSnapshot(boolean hostileEncounter,Set<String> activeSustained,Set<String> registeredPassives,Map<String,Double> temporaryEffects){this(hostileEncounter,activeSustained,registeredPassives,temporaryEffects,false);}
}

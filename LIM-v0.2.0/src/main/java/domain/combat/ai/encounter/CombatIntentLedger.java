package domain.combat.ai.encounter;
import java.util.*;
public final class CombatIntentLedger{
 private final List<CombatIntentBroadcast> events=new ArrayList<>();
 public void publish(CombatIntentBroadcast e){events.add(Objects.requireNonNull(e));}
 public List<CombatIntentBroadcast> perceptible(boolean canSee,boolean canHear,double now,double maxAge){return events.stream().filter(e->e.perceptible(canSee,canHear)&&now-e.timeSeconds()>=0&&now-e.timeSeconds()<=maxAge).toList();}
 public void clear(){events.clear();}
}

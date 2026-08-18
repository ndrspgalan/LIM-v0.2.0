package domain.milestone;
import java.util.*;
/** Estado final de los siete miembros canónicos de la familia. */
public final class CanonicalFamilyState {
 private static final Set<String> REQUIRED=Set.of("Kenan","Kiara","Jacob","Iván","Alicia","Rhoy","Sofía");
 private final Set<String> alive=new HashSet<>(), united=new HashSet<>();
 public void markAliveAndUnited(String name){alive.add(name);united.add(name);} public void markDead(String name){alive.remove(name);} public void markSeparated(String name){united.remove(name);}
 public boolean completeAfterGame(){return alive.containsAll(REQUIRED)&&united.containsAll(REQUIRED);} public Set<String>required(){return REQUIRED;}
}

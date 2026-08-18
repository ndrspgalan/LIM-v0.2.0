package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
public record CharacterSnapshot(String personaId,String name,String gender,String characterClass,String title,int level,double currentHealth,double currentStamina,Map<String,Integer> attributes,Map<String,Double> mucusMl,boolean unarmedGuardDrawn,boolean unarmedRightLead) implements Serializable {
 public CharacterSnapshot{Objects.requireNonNull(personaId);Objects.requireNonNull(name);Objects.requireNonNull(gender);Objects.requireNonNull(characterClass);Objects.requireNonNull(title);attributes=Map.copyOf(attributes);mucusMl=Map.copyOf(mucusMl);}
 public CharacterSnapshot(String personaId,String name,String gender,String characterClass,String title,int level,double currentHealth,double currentStamina,Map<String,Integer> attributes){this(personaId,name,gender,characterClass,title,level,currentHealth,currentStamina,attributes,Map.of(),true,true);}
}

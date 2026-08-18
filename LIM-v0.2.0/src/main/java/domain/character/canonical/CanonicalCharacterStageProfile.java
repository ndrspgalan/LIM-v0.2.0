package domain.character.canonical;
import domain.ability.MasteryId; import domain.character.*; import domain.social.*; import java.util.*;
public record CanonicalCharacterStageProfile(String name,Gender gender,CharacterClass characterClass,CanonicalLifeStage stage,
 CanonicalBodyProfile body,CanonicalAppearanceProfile appearance,List<Subprofession> subprofessions,OptionalInt fixedLevel,List<MasteryId> forcedMasteries,
 RelationshipType relationshipAmongCanonicalPeers,boolean accessoryIntentionallyBlank,String equipmentStandard){
 public CanonicalCharacterStageProfile{Objects.requireNonNull(name);Objects.requireNonNull(gender);Objects.requireNonNull(characterClass);Objects.requireNonNull(stage);Objects.requireNonNull(body);Objects.requireNonNull(appearance);subprofessions=List.copyOf(subprofessions);Objects.requireNonNull(fixedLevel);forcedMasteries=List.copyOf(forcedMasteries);Objects.requireNonNull(relationshipAmongCanonicalPeers);Objects.requireNonNull(equipmentStandard);}
 public boolean childRules(){return stage==CanonicalLifeStage.CHILD;}
}

package domain.combat.ai.declarative;
import domain.ability.*; import java.util.Objects;
/** Fuentes autoritativas necesarias para materializar capacidades . */
public record AbilityDecisionState(CharacterMasteryCollection masteries, MasteryEffectRegistry effects){
 public AbilityDecisionState{Objects.requireNonNull(masteries);Objects.requireNonNull(effects);}
}

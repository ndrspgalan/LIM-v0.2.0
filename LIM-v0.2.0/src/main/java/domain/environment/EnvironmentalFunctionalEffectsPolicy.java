package domain.environment;
import domain.character.sheet.StaminaRecovery;
import domain.combat.stamina.StaminaLoadRecoveryPolicy;
import domain.combat.stamina.StaminaRegenerationDelayPolicy;
/** Efectos no lesivos de adversidades ambientales consolidadas. */
public final class EnvironmentalFunctionalEffectsPolicy {
 public record Effects(StaminaRecovery staminaRecovery,double staminaRegenDelaySeconds,boolean targetLockAllowed,PhysiologicalTremorPolicy.Tremor tremor){}
 public Effects resolveBitingFrost(double totalPa,double load,double maxLoad,boolean helicalRelease){return new Effects(new StaminaLoadRecoveryPolicy().resolveFrost(totalPa,load,maxLoad,helicalRelease),StaminaRegenerationDelayPolicy.FROST_DELAY_SECONDS,false,new PhysiologicalTremorPolicy().resolve(PhysiologicalTremorPolicy.Source.BITING_FROST));}
}

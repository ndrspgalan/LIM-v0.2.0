package domain.combat.stamina;
import domain.ability.MasteryWorldContext;
import domain.character.sheet.StaminaRecovery;
import java.util.Objects;
/** Une carga, Liberación/Optimización Helicoidal y Frío sin confundir velocidad con inhibición. */
public final class StaminaMasteryRuntimePolicy {
 public record Result(StaminaRecovery recovery,boolean regenerationAllowed){}
 public Result resolve(double totalPa,double load,double maxLoad,MasteryWorldContext world,boolean bitingFrost,boolean sourceInhibits){
  Objects.requireNonNull(world); StaminaLoadRecoveryPolicy loadPolicy=new StaminaLoadRecoveryPolicy();
  StaminaRecovery recovery=bitingFrost?loadPolicy.resolveFrost(totalPa,load,maxLoad,world.helicalReleaseActive()):loadPolicy.resolve(totalPa,load,maxLoad,world.helicalReleaseActive());
  boolean allowed=new StaminaRegenerationInhibitionPolicy().canRegenerate(sourceInhibits,world.helicalOptimizationActive());
  return new Result(recovery,allowed);
 }
}

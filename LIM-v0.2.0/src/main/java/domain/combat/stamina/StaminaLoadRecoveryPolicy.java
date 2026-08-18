package domain.combat.stamina;
import domain.character.sheet.StaminaRecovery;
/** Tramos discretos . La sobrecarga >=100% inmoviliza, pero nunca detiene PA REGEN. */
public final class StaminaLoadRecoveryPolicy {
 public StaminaRecovery resolve(double totalStamina,double load,double maxLoad,boolean helicalRelease){
  if(totalStamina<0||load<0||maxLoad<=0)throw new IllegalArgumentException("Valores de PA/carga inválidos.");
  double r=load/maxLoad; boolean immobilized=r>=1.0; double seconds;
  if(r<=1.0/3.0) seconds=helicalRelease?0.5:1.0;
  else if(r<=2.0/3.0) seconds=helicalRelease?1.0:1.5;
  else if(r<1.0) seconds=helicalRelease?1.5:3.0;
  else seconds=helicalRelease?3.0:5.0;
  return new StaminaRecovery(totalStamina/seconds,seconds,immobilized);
 }
 public StaminaRecovery resolveWithConsumables(double totalStamina,double load,double maxLoad,boolean helicalRelease,boolean meadActive,boolean irndAftereffect){
  if(irndAftereffect) return new StaminaRecovery(totalStamina/5.0,5.0,false);
  if(meadActive) return new StaminaRecovery(totalStamina,1.0,load/maxLoad>=1.0);
  return resolve(totalStamina,load,maxLoad,helicalRelease);
 }

 public StaminaRecovery resolveFrost(double totalStamina,double load,double maxLoad,boolean helicalRelease){
  if(totalStamina<0||load<0||maxLoad<=0)throw new IllegalArgumentException("Valores de PA/carga inválidos.");
  // FRÍO ESCARCHANTE adopta el peor régimen temporal de recuperación sin heredar inmovilización por carga.
  double seconds=helicalRelease?3.0:5.0;
  return new StaminaRecovery(totalStamina/seconds,seconds,false);
 }
}

package domain.combat.ai.remote;
import domain.inventory.InventoryEntry;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.firearms.*;
import domain.inventory.item.rangedWeapons.RangedWeaponItem;
import domain.inventory.item.throwingWeapons.*;
import java.util.Objects;
/** Adaptadores de sólo lectura desde los tres dominios de ofensiva remota. */
public final class RemoteCombatOptionFactory {
 private RemoteCombatOptionFactory(){}
 public static RemoteCombatOption firearm(FirearmItem firearm){
  Objects.requireNonNull(firearm); RemoteReadiness readiness; double prep=0,charge=0,recovery=0;
  if(firearm.ammunitionRemaining()<=0)readiness=RemoteReadiness.NEEDS_RELOAD;
  else if(firearm instanceof PneumaticFirearmItem p&&!p.hasPressure()){readiness=RemoteReadiness.NEEDS_CHARGE;charge=p.pressureStepDurationSeconds();}
  else if(firearm instanceof ElectromagneticFirearmItem b){readiness=b.triggerThermallyLocked()?RemoteReadiness.RECOVERING:b.hasElectricalCharge()?RemoteReadiness.READY:RemoteReadiness.NEEDS_CHARGE; recovery=b.triggerThermallyLocked()?b.thermalLockRemainingSeconds():0;prep=recovery;}
  else if(firearm instanceof ArcInductionFirearmItem a){readiness=!a.operationalBatteryInstalled()?RemoteReadiness.UNAVAILABLE:a.triggerThermallyLocked()?RemoteReadiness.RECOVERING:a.hasElectricalCharge()?RemoteReadiness.READY:RemoteReadiness.NEEDS_CHARGE;recovery=a.triggerThermallyLocked()?a.thermalLockRemainingSeconds():0;prep=recovery;}
  else readiness=RemoteReadiness.READY;
  FirearmTimingProfile t=firearm.timingProfile();
  return new RemoteCombatOption(firearm,RemoteOffenseFamily.FIREARM,firearmLethality(firearm),minimumFirearmDistance(firearm),firearmRange(firearm),readiness,prep,t.reloadDurationSeconds(),t.shotIntervalSeconds(),recovery,charge,firearm.ammunitionRemaining(),firearm.supportsAiming(),false,false,java.util.Optional.of(firearm.ammunitionRequirement()));
 }
 public static RemoteCombatOption ranged(RangedWeaponItem weapon,AmmunitionDescriptor ammunition,boolean ammunitionAvailable,double nowSeconds){
  Objects.requireNonNull(weapon);Objects.requireNonNull(ammunition);if(!weapon.accepts(ammunition))throw new IllegalArgumentException("Munición incompatible.");
  RemoteReadiness r=!ammunitionAvailable?RemoteReadiness.NEEDS_AMMUNITION:weapon.readyAt(nowSeconds)?RemoteReadiness.READY:RemoteReadiness.RECOVERING;
  double remaining=r==RemoteReadiness.RECOVERING?weapon.recoverySeconds():0;
  return new RemoteCombatOption(weapon,RemoteOffenseFamily.RANGED_WEAPON,weapon.lethalityFor(ammunition),2.0,weapon.currentEffectiveRangeMeters(),r,0,0,weapon.recoverySeconds(),remaining,0,ammunitionAvailable?1:0,weapon.supportsAiming(),false,ammunition.recoverable(),java.util.Optional.of(ammunition));
 }
 public static RemoteCombatOption thrown(ThrowingWeaponItem weapon,double maximumThrowDistanceMeters,LethalityProfile lethality){
  Objects.requireNonNull(weapon);RemoteReadiness r=weapon.quantity()>0?RemoteReadiness.READY:RemoteReadiness.UNAVAILABLE;
  return new RemoteCombatOption(weapon,RemoteOffenseFamily.THROWN,Objects.requireNonNull(lethality),1.5,maximumThrowDistanceMeters,r,0,0,weapon.throwIntervalSeconds(),0,0,weapon.quantity(),false,false,weapon.recoverable(),java.util.Optional.empty());
 }
 public static RemoteCombatOption improvisedThrown(InventoryEntry item,int availableUnits,double maximumThrowDistanceMeters,LethalityProfile lethality,double ignoredPreparationSeconds){
  Objects.requireNonNull(item);double cadence=ThrowingCadencePolicy.improvisedIntervalSeconds(item);
  return new RemoteCombatOption(item,RemoteOffenseFamily.THROWN,Objects.requireNonNull(lethality),0.0,maximumThrowDistanceMeters,availableUnits>0?RemoteReadiness.READY:RemoteReadiness.UNAVAILABLE,0,0,cadence,0,0,Math.max(0,availableUnits),false,true,false,java.util.Optional.empty());
 }
 private static double minimumFirearmDistance(FirearmItem f){return f instanceof ArcInductionFirearmItem||f instanceof LimeSprayerItem?0:2;}
 private static LethalityProfile firearmLethality(FirearmItem f){return f instanceof ElectromagneticFirearmItem b?b.selectedShotProfile().lethality():f.lethalityProfile();}
 private static double firearmRange(FirearmItem f){return f instanceof ElectromagneticFirearmItem b?b.selectedShotProfile().effectiveRangeMeters():f.effectiveRangeMeters();}
}

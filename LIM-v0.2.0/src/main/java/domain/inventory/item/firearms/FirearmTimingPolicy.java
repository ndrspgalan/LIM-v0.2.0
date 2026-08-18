package domain.inventory.item.firearms;
/**  — tiempos canónicos de manipulación. Separados de carga energética y thermal lock. */
public final class FirearmTimingPolicy {
 private FirearmTimingPolicy(){}
 public static FirearmTimingProfile profile(FirearmItem f){
  if(f instanceof PneumaticFirearmItem)return new FirearmTimingProfile(6.0,0.0,0.55);
  if(f instanceof ElectromagneticFirearmItem)return new FirearmTimingProfile(3.5,0.0,0.0);
  if(f instanceof AutoloadingPistolFirearmItem)return new FirearmTimingProfile(2.0,0.0,0.0);
  if(f instanceof SubmachineGunFirearmItem smg)return new FirearmTimingProfile(2.8,60.0/smg.cyclicRateRpm(),0.0);
  if(f instanceof RepeatingRifleFirearmItem)return new FirearmTimingProfile(3.0,0.0,0.0);
  if(f instanceof AntiMaterielCannonFirearmItem)return new FirearmTimingProfile(6.0,1.0,0.0);
  if(f instanceof ClusterCannonFirearmItem)return new FirearmTimingProfile(4.5,0.0,0.0);
  if(f instanceof ArcInductionFirearmItem)return new FirearmTimingProfile(0.0,0.0,0.0);
  if(f instanceof LimeSprayerItem)return new FirearmTimingProfile(4.0,0.5,0.0);
  return new FirearmTimingProfile(0,0,0);
 }
}

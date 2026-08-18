package domain.inventory.item.rangedWeapons;
import domain.inventory.item.LethalityProfile;
public record RangedWeaponShotResult(boolean fired,String message,LethalityProfile lethality,double burn,double recoverySeconds){
 public static RangedWeaponShotResult rejected(String m){return new RangedWeaponShotResult(false,m,new LethalityProfile(0,0,0),0,0);}
 public static RangedWeaponShotResult fired(LethalityProfile l,double b,double r){return new RangedWeaponShotResult(true,"Disparo efectuado.",l,b,r);}
}

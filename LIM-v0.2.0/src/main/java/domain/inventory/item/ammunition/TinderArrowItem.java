package domain.inventory.item.ammunition;
import domain.inventory.item.misc.*;
public final class TinderArrowItem extends ProjectileAmmunitionItem {
 private boolean lit;
 public TinderArrowItem(double weightKg){super("Flecha de Yesca","Flecha de hoja ligera provista de amadou; solo inflige Quemadura 100 cuando ha sido encendida.",weightKg,ArrowVariant.TINDER_UNLIT.descriptor());}
 public boolean lit(){return lit;}
 public AmmunitionDescriptor activeDescriptor(){return lit?ArrowVariant.TINDER_LIT.descriptor():ArrowVariant.TINDER_UNLIT.descriptor();}
 public boolean ignite(UtilityObjectItem amadou,UtilityObjectItem flint){if(lit)return false;boolean ok=new IgnitionPolicy().execute(amadou,flint,UtilityAction.GENERATE_SPARK);if(ok)lit=true;return ok;}
 public void extinguish(){lit=false;}
 @Override public AmmunitionDescriptor ammunitionDescriptor(){return activeDescriptor();}
}

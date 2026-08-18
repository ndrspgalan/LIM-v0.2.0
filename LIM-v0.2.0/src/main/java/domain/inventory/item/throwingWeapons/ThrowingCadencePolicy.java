package domain.inventory.item.throwingWeapons;
import domain.inventory.InventoryEntry;
/** intervalo mínimo entre lanzamientos desde acceso rápido. */
public final class ThrowingCadencePolicy {
 private ThrowingCadencePolicy(){}
 public static double intervalSeconds(ThrowingWeaponItem w){return switch(w.effect()){
  case THROWING_KNIFE -> 0.5;
  case AMMONIA_CAPSULE, PHOSPHORUS_SULFUR_EGG -> 1.0;
  case INCENDIARY_TERRACOTTA -> 2.0;
 };}
 public static double improvisedIntervalSeconds(InventoryEntry item){String n=item.name().toLowerCase(java.util.Locale.ROOT); if(n.contains("moneda"))return 0.5; if(n.contains("guijarro"))return 1.0; throw new IllegalArgumentException("Cadencia arrojadiza improvisada no formalizada para "+item.name());}
}

package application.save;
import domain.inventory.InventoryEntry;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.misc.*;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import java.lang.reflect.Method; import java.lang.reflect.Modifier; import java.util.*;
/** Resuelve una instancia canónica fresca por nombre para hidratar un save. */
public final class CanonicalInventoryEntryResolver {
 private CanonicalInventoryEntryResolver(){}
 public static InventoryEntry require(String name){return domain.character.canonical.CanonicalChildLoadoutCatalog.freshEntryByName(name).orElseGet(()->allFresh().stream().filter(e->e.name().equals(name)).findFirst().orElseThrow(()->new IllegalArgumentException("No existe factory canónica para restaurar: "+name)));}
 public static List<InventoryEntry> allFresh(){ArrayList<InventoryEntry> all=new ArrayList<>();all.addAll(MeleeWeaponCatalog.allCanonical());all.addAll(FirearmCatalog.all());all.addAll(RangedWeaponCatalog.all());all.addAll(ThrowingWeaponCatalog.all());all.addAll(AccessoryCatalog.all());all.addAll(FirearmAccessoryCatalog.all());
  all.addAll(List.of(AmmunitionCatalog.pneumaticLead46Cartridge(),AmmunitionCatalog.autoloadingPistol45Magazine(),AmmunitionCatalog.submachineGun9mmMagazine(),AmmunitionCatalog.repeatingRifle792x57Clip(),AmmunitionCatalog.bifilar46Magazine(),AmmunitionCatalog.antiMateriel20mmCartridge(),AmmunitionCatalog.clusterRocket85mm(),AmmunitionCatalog.limeCartridgeCase(),AmmunitionCatalog.pebble(),AmmunitionCatalog.piercingArrow(),AmmunitionCatalog.barbedArrow(),AmmunitionCatalog.bladedArrow(),AmmunitionCatalog.tinderArrow()));
  all.addAll(armor()); all.addAll(domain.character.canonical.CanonicalChildLoadoutCatalog.allCanonicalClothing()); all.addAll(miscellaneous()); all.addAll(List.of(MechanicalLampCatalog.magnetlampe(),MechanicalLampCatalog.knijpkat(),MucusCrystalCatalog.yellow(),MucusCrystalCatalog.greenish(),MucusCrystalCatalog.brown(),MucusCrystalCatalog.bloodied(),MucusCrystalCatalog.blackish())); return List.copyOf(all);}
 private static List<InventoryEntry> armor(){ArrayList<InventoryEntry> a=new ArrayList<>();a.addAll(ArmorCatalog.allInnerChestGarments());a.addAll(ArmorCatalog.allInnerLeggingsGarments());a.addAll(ArmorCatalog.allMiddleLeggingsGarments());a.addAll(ArmorCatalog.allMiddleChest());a.addAll(ArmorCatalog.allOuterChestGarments());a.addAll(ArmorCatalog.allInnerFeetGarments());a.addAll(ArmorCatalog.allOuterFeetGarments());a.addAll(ArmorCatalog.allBracers());a.addAll(ArmorCatalog.allLeggings());a.addAll(ArmorCatalog.allFeetArmor());a.addAll(ArmorCatalog.allHeadArmor());a.addAll(ArmorCatalog.allOuterLeggings());return a.stream().distinct().map(InventoryEntry.class::cast).toList();}
 private static List<InventoryEntry> miscellaneous(){ArrayList<InventoryEntry> out=new ArrayList<>();for(Method m:domain.inventory.item.misc.MiscellaneousItemCatalog.class.getDeclaredMethods()){if(!Modifier.isStatic(m.getModifiers())||m.getParameterCount()!=0||!InventoryEntry.class.isAssignableFrom(m.getReturnType()))continue;try{out.add((InventoryEntry)m.invoke(null));}catch(Exception ignored){}}for(domain.inventory.item.misc.CurrencyType t:domain.inventory.item.misc.CurrencyType.values())out.add(new CurrencyStack(t,1));return out;}
}

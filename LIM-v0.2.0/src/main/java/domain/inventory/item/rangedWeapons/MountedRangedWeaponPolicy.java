package domain.inventory.item.rangedWeapons;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.PersonalTransportItemUsePolicy;
import domain.inventory.logistics.PersonalTransportType;
public final class MountedRangedWeaponPolicy {
 private final PersonalTransportItemUsePolicy delegate = new PersonalTransportItemUsePolicy();
 public boolean canUseAsDriver(RangedWeaponItem weapon,PersonalTransportType transport){return delegate.canUseAsDriver(weapon,transport);}
 public boolean canUseAsPassenger(RangedWeaponItem weapon){return delegate.canUseAsPassenger(weapon);}
 public EquipmentSlot forcedSlotWhileDriving(RangedWeaponItem weapon,PersonalTransportType transport){return delegate.forcedSlotWhileDriving(weapon,transport);}
}

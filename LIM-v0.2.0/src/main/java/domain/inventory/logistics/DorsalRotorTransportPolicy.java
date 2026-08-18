package domain.inventory.logistics;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/** Contrato exclusivo del Sistema de Transporte Dorsal del Rotor V881. */
public final class DorsalRotorTransportPolicy {
    public boolean canDock(WeaponItem weapon) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        return weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)
                && weapon.isSheathed()
                && weapon.footprint().verticalSlots() <= 2
                && weapon.footprint().horizontalSlots() <= 9;
    }

    public double combinedWeightKg(WeaponItem rotor) {
        if (!canDock(rotor)) throw new IllegalStateException("El Rotor debe estar completamente retraído.");
        return InventoryCompartmentType.DORSAL_ROTOR_SYSTEM.structuralWeightKg() + rotor.weightKg();
    }
}

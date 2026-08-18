package domain.inventory;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.LogisticsState;

import java.util.Objects;

/** Estado de inventario GOLD: equipo activo plano para armas/accesorios + layout estratificado para armadura. */
public record InventoryState(
        EquipmentState equipment,
        QuickAccessBar quickAccessBar,
        LogisticsState logistics,
        ArmorEquipmentLayout armorLayout
) {
    public InventoryState {
        Objects.requireNonNull(equipment, "El equipamiento actual no puede ser nulo.");
        Objects.requireNonNull(quickAccessBar, "Los accesos rápidos actuales no pueden ser nulos.");
        Objects.requireNonNull(logistics, "La logística actual no puede ser nula.");
        Objects.requireNonNull(armorLayout, "El layout estratificado no puede ser nulo.");
        QuickAccessPolicy.validate(quickAccessBar, equipment, logistics);
        var back=equipment.itemAt(domain.inventory.equipment.EquipmentSlot.BACK_HAND);
        if(back.isPresent()) {
            if(!(back.get() instanceof domain.inventory.item.WeaponItem rotor) || !rotor.hasTrait(domain.inventory.item.WeaponTrait.DORSAL_ROTOR_COMPATIBLE))
                throw new IllegalArgumentException("BACK_HAND sólo admite el Espadón de Rotor.");
            if(rotor.isSheathed() && !logistics.compartment(domain.inventory.logistics.InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available())
                throw new IllegalArgumentException("Un Espadón de Rotor retraído en BACK_HAND requiere el Sistema de Transporte Dorsal del Rotor V881.");
        }
        if(!logistics.compartment(domain.inventory.logistics.InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).entries().isEmpty())
            throw new IllegalArgumentException("el sistema dorsal habilita BACK_HAND; ya no almacena el Rotor como contenido de grid.");
    }

    /** Compatibilidad con servicios anteriores: sin datos de capas explícitos. */
    public InventoryState(EquipmentState equipment, QuickAccessBar quickAccessBar, LogisticsState logistics) {
        this(equipment, quickAccessBar, logistics, ArmorEquipmentLayout.empty());
    }

    public static InventoryState emptyWithoutPersonalTransport() {
        return new InventoryState(
                EquipmentState.empty(),
                QuickAccessBar.empty(),
                LogisticsState.emptyWithoutPersonalTransport(),
                ArmorEquipmentLayout.empty()
        );
    }

    public InventoryState withLogistics(LogisticsState next) {
        return new InventoryState(equipment, quickAccessBar, next, armorLayout);
    }

    public InventoryState withArmorLayout(ArmorEquipmentLayout next) {
        return new InventoryState(equipment, quickAccessBar, logistics, next);
    }

    public double totalCarriedWeightKg() {
        // El layout estratificado es la autoridad de ropa/armadura. EquipmentState sigue siendo puente para
        // consumidores obsoleto, por lo que se suma por identidad para no duplicar piezas y no perder capas INNER.
        java.util.Set<domain.inventory.InventoryEntry> unique = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        for (domain.inventory.equipment.EquipmentSlot slot : domain.inventory.equipment.EquipmentSlot.values())
            equipment.itemAt(slot).ifPresent(unique::add);
        armorLayout.layers().forEach(layer -> unique.add(layer.piece()));
        double active = unique.stream().mapToDouble(item -> {
            if (item instanceof domain.inventory.item.WeaponItem weapon) return weapon.effectiveWeightKg();
            if (item instanceof domain.inventory.item.firearms.FirearmItem firearm) return firearm.effectiveHandlingWeightKg();
            return item.weightKg();
        }).sum();
        return active + logistics.totalWeightKg();
    }
}

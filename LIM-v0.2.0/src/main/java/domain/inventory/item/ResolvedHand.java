package domain.inventory.item;

import domain.inventory.equipment.EquipmentSlot;

import java.util.Objects;
import java.util.Optional;

/** Proyección inmutable de una mano y su configuración efectiva. */
public record ResolvedHand(
        EquipmentSlot hand,
        Optional<WeaponItem> weapon,
        Optional<WeaponConfiguration> effectiveConfiguration,
        HandDisposition disposition
) {
    public ResolvedHand {
        Objects.requireNonNull(hand, "La mano no puede ser nula.");
        if (hand != EquipmentSlot.RIGHT_HAND && hand != EquipmentSlot.LEFT_HAND) {
            throw new IllegalArgumentException("Solo RIGHT_HAND y LEFT_HAND son manos físicas.");
        }
        weapon = Objects.requireNonNull(weapon, "El arma opcional no puede ser nula.");
        effectiveConfiguration = Objects.requireNonNull(
                effectiveConfiguration, "La configuración opcional no puede ser nula.");
        disposition = Objects.requireNonNull(disposition, "La disposición no puede ser nula.");
        if (weapon.isEmpty() && disposition != HandDisposition.EMPTY) {
            throw new IllegalArgumentException("Una mano sin arma debe estar vacía.");
        }
        if (weapon.isPresent() && disposition == HandDisposition.ACTIVE && effectiveConfiguration.isEmpty()) {
            throw new IllegalArgumentException("Un arma activa necesita configuración efectiva.");
        }
    }

    public static ResolvedHand empty(EquipmentSlot hand) {
        return new ResolvedHand(hand, Optional.empty(), Optional.empty(), HandDisposition.EMPTY);
    }

    public static ResolvedHand active(EquipmentSlot hand, WeaponItem weapon, WeaponConfiguration configuration) {
        return new ResolvedHand(hand, Optional.of(weapon), Optional.of(configuration), HandDisposition.ACTIVE);
    }

    public static ResolvedHand stowed(EquipmentSlot hand, WeaponItem weapon) {
        return new ResolvedHand(hand, Optional.of(weapon), Optional.empty(), HandDisposition.STOWED);
    }
}

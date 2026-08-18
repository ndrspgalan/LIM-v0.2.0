package domain.combat;

import domain.inventory.item.*;
import java.util.Objects;

/**  — tiempos canónicos de preparación/ejecución previa de CHARGED; sustituye el placeholder universal de 1,5 s. */
public final class ChargedAttackTimingPolicy {
    private ChargedAttackTimingPolicy() {}

    public static double preparationSeconds(WeaponItem weapon) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        return preparationSeconds(weapon, weapon.currentConfiguration().actionMode());
    }

    public static double preparationSeconds(WeaponItem weapon, WeaponActionMode mode) {
        Objects.requireNonNull(weapon); Objects.requireNonNull(mode);
        if (weapon.hasTrait(WeaponTrait.HELICOIDAL_CONTROL) || weapon.hasTrait(WeaponTrait.THERMO_MECHANICAL)) {
            return Double.POSITIVE_INFINITY; // release-driven: no existe umbral automático.
        }
        if (weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            return mode == WeaponActionMode.PRIMARY ? 0.95 : 1.25;
        }
        if (weapon.hasTrait(WeaponTrait.STAFF_FLOURISH_HANDLING)) {
            return mode == WeaponActionMode.PRIMARY ? 0.70 : 0.80;
        }
        if ("Guadaña".equals(weapon.name())) return 1.20;
        return 1.00; // reserva para futuros CHARGED convencionales: ya no es el antiguo 1,5 s.
    }
}

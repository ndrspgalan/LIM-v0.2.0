package domain.inventory.item;

import java.util.Objects;
import java.util.Set;

/**
 * Decide qué agarre puede usar el personaje a partir de la fuerza disponible.
 * Evita que un arma compatible con 1H/2H se equipe a una mano cuando solo se cumple el requisito reducido de 2H.
 */
public final class WeaponGripEligibilityPolicy {
    private WeaponGripEligibilityPolicy() {}

    public static WeaponGripEligibility resolve(
            double weightKg,
            boolean supportsOneHanded,
            boolean supportsTwoHanded,
            int availableStrength,
            Set<WeaponTrait> traits
    ) {
        if (!supportsOneHanded && !supportsTwoHanded) {
            throw new IllegalArgumentException("El arma debe admitir al menos un agarre.");
        }
        if (availableStrength < 0) {
            throw new IllegalArgumentException("La FUERZA disponible no puede ser negativa.");
        }
        Objects.requireNonNull(traits, "Las propiedades del arma no pueden ser nulas.");

        if (supportsOneHanded && traits.contains(WeaponTrait.ERGONOMIA_SUFICIENTE)) {
            return WeaponGripEligibility.ONE_OR_TWO_HANDED;
        }

        int oneHanded = WeaponRequirementPolicy.strengthRequirement(weightKg, GripMode.ONE_HANDED, traits);
        int twoHanded = WeaponRequirementPolicy.strengthRequirement(weightKg, GripMode.TWO_HANDED, traits);

        if (supportsOneHanded && availableStrength >= oneHanded) {
            return supportsTwoHanded ? WeaponGripEligibility.ONE_OR_TWO_HANDED : WeaponGripEligibility.ONE_OR_TWO_HANDED;
        }
        if (supportsTwoHanded && availableStrength >= twoHanded) {
            return supportsOneHanded ? WeaponGripEligibility.FORCED_TWO_HANDED : WeaponGripEligibility.TWO_HANDED_ONLY;
        }
        return WeaponGripEligibility.CANNOT_WIELD;
    }
}

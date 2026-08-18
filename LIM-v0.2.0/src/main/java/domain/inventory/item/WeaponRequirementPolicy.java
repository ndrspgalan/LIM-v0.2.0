package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.inventory.equipment.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Reglas universales de requisitos físicos para armas.
 *
 * DESTREZA = techo(longitud/alcance de referencia en metros x 10).
 * En dual wielding, el arma efectiva de LEFT_HAND exige DESTREZA x1,25.
 * FUERZA   = techo(peso en kg x 10).
 * Un agarre efectivo a dos manos ordinario aplica FUERZA x0,75 antes del redondeo final.
 * DE_ROTOR NO recibe esa ventaja mecánica bimanual y tampoco aplica ya un -1 plano de FUERZA.
 */
public final class WeaponRequirementPolicy {
    public static final double TWO_HANDED_STRENGTH_FACTOR = 0.75;
    public static final double LEFT_HAND_DUAL_WIELD_DEXTERITY_FACTOR = 1.25;

    private WeaponRequirementPolicy() {}

    public static List<AttributeRequirement> calculate(
            double reachMeters,
            double weightKg,
            GripMode effectiveGrip,
            Set<WeaponTrait> traits
    ) {
        validate(reachMeters, weightKg, effectiveGrip, traits);
        int dexterity = dexterityRequirementForGrip(reachMeters, effectiveGrip, traits, null, false);
        int strength = strengthRequirement(weightKg, effectiveGrip, traits);

        List<AttributeRequirement> requirements = new ArrayList<>(2);
        requirements.add(new AttributeRequirement(Attribute.FUERZA, strength));
        requirements.add(new AttributeRequirement(Attribute.DESTREZA, dexterity));
        return List.copyOf(requirements);
    }

    /** Requisitos efectivos cuando la mano concreta sí forma parte de la resolución. */
    public static List<AttributeRequirement> calculateForHand(
            double reachMeters,
            double weightKg,
            GripMode effectiveGrip,
            Set<WeaponTrait> traits,
            EquipmentSlot hand,
            boolean dualWielding
    ) {
        validate(reachMeters, weightKg, effectiveGrip, traits);
        if (hand != EquipmentSlot.RIGHT_HAND && hand != EquipmentSlot.LEFT_HAND) {
            throw new IllegalArgumentException("La resolución contextual requiere RIGHT_HAND o LEFT_HAND.");
        }
        return List.of(
                new AttributeRequirement(Attribute.FUERZA, strengthRequirement(weightKg, effectiveGrip, traits)),
                new AttributeRequirement(Attribute.DESTREZA, dexterityRequirementForGrip(reachMeters, effectiveGrip, traits, hand, dualWielding))
        );
    }

    /** Compatibilidad : el booleano expresa ahora el agarre efectivo, no la exclusividad del arma. */
    public static List<AttributeRequirement> calculate(
            double reachMeters,
            double weightKg,
            boolean twoHandedGrip,
            Set<WeaponTrait> traits
    ) {
        return calculate(reachMeters, weightKg,
                twoHandedGrip ? GripMode.TWO_HANDED : GripMode.ONE_HANDED,
                traits);
    }

    public static int strengthRequirement(double weightKg, GripMode effectiveGrip, Set<WeaponTrait> traits) {
        if (weightKg < 0 || !Double.isFinite(weightKg)) {
            throw new IllegalArgumentException("El peso debe ser finito y no negativo.");
        }
        Objects.requireNonNull(effectiveGrip, "El agarre efectivo no puede ser nulo.");
        Objects.requireNonNull(traits, "Las propiedades del arma no pueden ser nulas.");
        double rawStrength = weightKg * 10.0;
        if ((effectiveGrip == GripMode.TWO_HANDED && !traits.contains(WeaponTrait.DE_ROTOR))
                || (effectiveGrip == GripMode.ONE_HANDED && traits.contains(WeaponTrait.STAFF_FLOURISH_HANDLING))) {
            rawStrength *= TWO_HANDED_STRENGTH_FACTOR;
        }
        if (effectiveGrip == GripMode.ONE_HANDED && traits.contains(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            rawStrength *= 1.25;
        }
        return ceilStable(rawStrength);
    }


    /** el Espadón de Rotor exige DESTREZA x1,50 cuando se gobierna a una mano. */
    public static int dexterityRequirementForGrip(double reachMeters, GripMode grip, Set<WeaponTrait> traits, EquipmentSlot hand, boolean dualWielding) {
        Objects.requireNonNull(grip); Objects.requireNonNull(traits);
        double raw = reachMeters * 10.0;
        if (grip == GripMode.ONE_HANDED && traits.contains(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) raw *= 1.50;
        if (dualWielding && hand == EquipmentSlot.LEFT_HAND) raw *= LEFT_HAND_DUAL_WIELD_DEXTERITY_FACTOR;
        return ceilStable(raw);
    }

    public static int dexterityRequirement(double reachMeters, EquipmentSlot hand, boolean dualWielding) {
        if (reachMeters < 0 || !Double.isFinite(reachMeters)) {
            throw new IllegalArgumentException("El alcance debe ser finito y no negativo.");
        }
        double raw = reachMeters * 10.0;
        if (dualWielding && hand == EquipmentSlot.LEFT_HAND) {
            raw *= LEFT_HAND_DUAL_WIELD_DEXTERITY_FACTOR;
        }
        return ceilStable(raw);
    }

    private static void validate(double reachMeters, double weightKg, GripMode effectiveGrip, Set<WeaponTrait> traits) {
        if (reachMeters < 0 || !Double.isFinite(reachMeters)) throw new IllegalArgumentException("El alcance debe ser finito y no negativo.");
        if (weightKg < 0 || !Double.isFinite(weightKg)) throw new IllegalArgumentException("El peso debe ser finito y no negativo.");
        Objects.requireNonNull(effectiveGrip, "El agarre efectivo no puede ser nulo.");
        Objects.requireNonNull(traits, "Las propiedades del arma no pueden ser nulas.");
    }

    private static int ceilStable(double value) {
        return (int) Math.ceil(value - 1.0e-9);
    }
}

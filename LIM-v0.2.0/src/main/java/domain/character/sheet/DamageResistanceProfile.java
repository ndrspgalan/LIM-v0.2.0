package domain.character.sheet;

import java.util.OptionalDouble;

public record DamageResistanceProfile(
        OptionalDouble piercing,
        OptionalDouble slashing,
        OptionalDouble blunt,
        OptionalDouble poison,
        OptionalDouble burn,
        OptionalDouble frost,
        OptionalDouble curse,
        OptionalDouble electricity,
        OptionalDouble frenzy
) {
    public DamageResistanceProfile {
        piercing = requirePercentage(piercing, "perforante");
        slashing = requirePercentage(slashing, "cortante");
        blunt = requirePercentage(blunt, "contundente");
        poison = requirePercentage(poison, "veneno");
        burn = requirePercentage(burn, "quemadura");
        frost = requirePercentage(frost, "congelación");
        curse = requirePercentage(curse, "maldición");
        electricity = requirePercentage(electricity, "electricidad");
        frenzy = requirePercentage(frenzy, "frenesí");
    }

    public static DamageResistanceProfile uniform(double value) {
        OptionalDouble resistance = OptionalDouble.of(value);
        return new DamageResistanceProfile(
                resistance, resistance, resistance, resistance, resistance,
                resistance, resistance, resistance, resistance
        );
    }


    public static DamageResistanceProfile zero() {
        return uniform(0.0);
    }

    public static DamageResistanceProfile combine(DamageResistanceProfile... profiles) {
        if (profiles == null || profiles.length == 0) {
            return zero();
        }
        double[] values = new double[9];
        for (DamageResistanceProfile profile : profiles) {
            if (profile == null) {
                throw new NullPointerException("Los perfiles de resistencia no pueden contener valores nulos.");
            }
            values[0] += profile.piercing().orElse(0.0);
            values[1] += profile.slashing().orElse(0.0);
            values[2] += profile.blunt().orElse(0.0);
            values[3] += profile.poison().orElse(0.0);
            values[4] += profile.burn().orElse(0.0);
            values[5] += profile.frost().orElse(0.0);
            values[6] += profile.curse().orElse(0.0);
            values[7] += profile.electricity().orElse(0.0);
            values[8] += profile.frenzy().orElse(0.0);
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.min(100.0, Math.max(0.0, values[i]));
        }
        return new DamageResistanceProfile(
                OptionalDouble.of(values[0]), OptionalDouble.of(values[1]), OptionalDouble.of(values[2]),
                OptionalDouble.of(values[3]), OptionalDouble.of(values[4]), OptionalDouble.of(values[5]),
                OptionalDouble.of(values[6]), OptionalDouble.of(values[7]), OptionalDouble.of(values[8])
        );
    }


    public DamageResistanceProfile replacePoison(double value) {
        return new DamageResistanceProfile(piercing, slashing, blunt, OptionalDouble.of(value), burn,
                frost, curse, electricity, frenzy);
    }

    public DamageResistanceProfile replaceFrenzy(double value) {
        return new DamageResistanceProfile(piercing, slashing, blunt, poison, burn,
                frost, curse, electricity, OptionalDouble.of(value));
    }

    public double percentageFor(domain.combat.DamageType type) {
        java.util.Objects.requireNonNull(type, "El tipo de daño no puede ser nulo.");
        OptionalDouble value = switch (type) {
            case PIERCING -> piercing;
            case SLASHING -> slashing;
            case BLUNT -> blunt;
            case POISON -> poison;
            case BURN -> burn;
            case FROST -> frost;
            case CURSE -> curse;
            case ELECTRICITY -> electricity;
            case FRENZY -> frenzy;
        };
        return value.orElse(0.0);
    }

    private static OptionalDouble requirePercentage(OptionalDouble value, String name) {
        if (value == null) {
            throw new NullPointerException("La resistencia a " + name + " no puede ser nula.");
        }
        if (value.isPresent() && (value.getAsDouble() < 0 || value.getAsDouble() > 100)) {
            throw new IllegalArgumentException(
                    "La resistencia a " + name + " debe estar entre 0 y 100 %."
            );
        }
        return value;
    }
}

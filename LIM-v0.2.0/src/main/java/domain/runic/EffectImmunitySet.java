package domain.runic;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class EffectImmunitySet {
    private final Set<EffectImmunity> values;

    private EffectImmunitySet(Set<EffectImmunity> values) {
        this.values = Set.copyOf(values);
    }

    public static EffectImmunitySet none() {
        return new EffectImmunitySet(EnumSet.noneOf(EffectImmunity.class));
    }

    public static EffectImmunitySet of(EffectImmunity... immunities) {
        Objects.requireNonNull(immunities, "Las inmunidades no pueden ser nulas.");
        EnumSet<EffectImmunity> values = EnumSet.noneOf(EffectImmunity.class);
        for (EffectImmunity immunity : immunities) {
            values.add(Objects.requireNonNull(immunity, "Una inmunidad no puede ser nula."));
        }
        return new EffectImmunitySet(values);
    }

    public static EffectImmunitySet combine(Collection<EffectImmunitySet> sets) {
        Objects.requireNonNull(sets, "Las colecciones de inmunidades no pueden ser nulas.");
        EnumSet<EffectImmunity> values = EnumSet.noneOf(EffectImmunity.class);
        for (EffectImmunitySet set : sets) {
            values.addAll(Objects.requireNonNull(set, "Un conjunto de inmunidades no puede ser nulo.").values);
        }
        return new EffectImmunitySet(values);
    }

    public boolean contains(EffectImmunity immunity) {
        return values.contains(Objects.requireNonNull(immunity, "La inmunidad no puede ser nula."));
    }

    public Set<EffectImmunity> values() {
        return values;
    }
}

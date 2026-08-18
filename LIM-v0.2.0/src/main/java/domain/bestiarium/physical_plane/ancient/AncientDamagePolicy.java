package domain.bestiarium.physical_plane.ancient;

import domain.combat.DamageCategory;
import domain.combat.DamageType;
import java.util.Objects;

/**  — ANCIENT sigue siendo materia: no posee inmunidad ontológica ni daño espiritual por especie. */
public final class AncientDamagePolicy {
    /** ANCIENT recibe los canales físicos ordinarios; CURSE/FRENZY no forman parte de su materialidad corporal. */
    public boolean canReceive(DamageType incoming) {
        return Objects.requireNonNull(incoming).category() != DamageCategory.SPIRITUAL;
    }

    /** Los ataques naturales de Anteo son contundentes salvo que otra superficie/arma física declare otro canal. */
    public DamageType naturalMeleeType(AncientForm form) {
        Objects.requireNonNull(form);
        return DamageType.BLUNT;
    }
}

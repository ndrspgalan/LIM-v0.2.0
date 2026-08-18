package domain.bestiarium.physical_plane.aspirant;

import domain.combat.DamageType;
import java.util.Objects;

/** ASPIRANT es completamente orgánico: forma y anatomía no convierten los ataques en canales místicos. */
public final class AspirantDamagePolicy {
    public boolean canReceive(DamageType incoming) {
        Objects.requireNonNull(incoming);
        return true;
    }

    /** El canal lo decide la acción/material/superficie ofensiva concreta en ambas formas. */
    public DamageType outgoingType(AspirantForm form, DamageType ordinaryType) {
        Objects.requireNonNull(form);
        return Objects.requireNonNull(ordinaryType);
    }
}

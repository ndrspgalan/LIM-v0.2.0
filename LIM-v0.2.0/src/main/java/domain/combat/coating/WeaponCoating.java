package domain.combat.coating;

import java.util.Objects;

/** Estado de recubrimiento del arma. El daño se deriva del perfil que impacta. */
public record WeaponCoating(WeaponCoatingType type) {
    public WeaponCoating {
        Objects.requireNonNull(type, "El tipo de recubrimiento no puede ser nulo.");
    }

    public static WeaponCoating curse() {
        return new WeaponCoating(WeaponCoatingType.CURSE);
    }
}

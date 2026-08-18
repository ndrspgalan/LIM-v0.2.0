package domain.combat.ai.declarative;

import domain.inventory.item.ammunition.AmmunitionDescriptor;
import java.util.Objects;

/** Identidad material de una unidad de munición/proyectil conocida por LIM. */
public record AmmunitionFact(String family, String caliber, String material, String variant, boolean recoverable) {
    public AmmunitionFact {
        if (family==null||family.isBlank()) throw new IllegalArgumentException("Familia de munición obligatoria.");
        caliber=Objects.requireNonNull(caliber); material=Objects.requireNonNull(material); variant=Objects.requireNonNull(variant);
    }
    public static AmmunitionFact from(AmmunitionDescriptor d) {
        Objects.requireNonNull(d);
        return new AmmunitionFact(d.family().name(), d.caliber(), d.material(), d.variant(), d.recoverable());
    }
}

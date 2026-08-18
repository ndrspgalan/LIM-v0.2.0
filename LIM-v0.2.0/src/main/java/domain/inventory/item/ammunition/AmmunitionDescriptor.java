package domain.inventory.item.ammunition;
import java.util.Objects;
public record AmmunitionDescriptor(AmmunitionFamily family, String caliber, String material, String variant, boolean recoverable) {
    public AmmunitionDescriptor {
        Objects.requireNonNull(family);
        caliber = text(caliber); material = text(material); variant = text(variant);
    }
    public boolean compatibleWith(AmmunitionDescriptor other) {
        return other != null && family == other.family
                && caliber.equalsIgnoreCase(other.caliber)
                && material.equalsIgnoreCase(other.material)
                && variant.equalsIgnoreCase(other.variant);
    }
    private static String text(String v) {
        Objects.requireNonNull(v);
        String n=v.trim(); if(n.isEmpty()) throw new IllegalArgumentException("Texto vacío."); return n;
    }
}

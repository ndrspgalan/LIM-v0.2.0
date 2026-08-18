package domain.inventory.item.armor;

import java.util.Objects;

/** Composición constructiva de una pieza por material y número de capas. */
public record ArmorMaterialLayer(ArmorMaterial material, int layers) {
    public ArmorMaterialLayer {
        Objects.requireNonNull(material, "El material no puede ser nulo.");
        if (layers <= 0) throw new IllegalArgumentException("Las capas deben ser positivas.");
    }

    public ArmorProtectionProfile additiveProtection() {
        ArmorProtectionProfile p = material.canonicalProtection();
        return new ArmorProtectionProfile(p.piercing() * layers, p.slashing() * layers, p.blunt() * layers);
    }

    public String label() {
        return material.label().toUpperCase(java.util.Locale.ROOT) + " x" + layers;
    }
}

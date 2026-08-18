package domain.inventory.item.firearms;

import java.util.Objects;

/** Definición material del proyectil, independiente del cartucho que lo contiene. */
public record FirearmProjectileDefinition(
        String caliber,
        double massKg,
        String material,
        String geometry
) {
    public FirearmProjectileDefinition {
        caliber = requireText(caliber, "El calibre no puede estar vacío.");
        if (!Double.isFinite(massKg) || massKg <= 0) {
            throw new IllegalArgumentException("La masa del proyectil debe ser positiva y finita.");
        }
        material = requireText(material, "El material no puede estar vacío.");
        geometry = requireText(geometry, "La geometría no puede estar vacía.");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException(message);
        return normalized;
    }
}

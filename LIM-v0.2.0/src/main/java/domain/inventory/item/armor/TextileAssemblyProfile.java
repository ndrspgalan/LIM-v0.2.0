package domain.inventory.item.armor;

import java.util.Objects;

/**
 * : trazabilidad de construcciones textiles cuyo perfil no debe confundirse
 * con una mera suma ciega de "TELA xN".
 */
public record TextileAssemblyProfile(
        String constructionLabel,
        ArmorProtectionProfile protection,
        int effectiveClothLayers
) {
    public TextileAssemblyProfile {
        if (constructionLabel == null || constructionLabel.isBlank()) {
            throw new IllegalArgumentException("La construcción textil no puede estar vacía.");
        }
        Objects.requireNonNull(protection);
        if (effectiveClothLayers <= 0) {
            throw new IllegalArgumentException("Las capas efectivas deben ser positivas.");
        }
    }

    public static TextileAssemblyProfile ordinary(int layers, ArmorProtectionProfile protection) {
        return new TextileAssemblyProfile("TELA x" + layers, protection, layers);
    }

    /** tejido ordinario canónico; la protección deriva del número de capas. */
    public static TextileAssemblyProfile ordinary(int layers) {
        if (layers <= 0) throw new IllegalArgumentException("Las capas efectivas deben ser positivas.");
        return new TextileAssemblyProfile(
                "TELA x" + layers,
                new ArmorProtectionProfile(2 * layers, 5 * layers, 2 * layers),
                layers
        );
    }

    /** Paño simple de mayor densidad: no equivale a añadir una segunda capa completa. */
    public static TextileAssemblyProfile denseCloth() {
        return new TextileAssemblyProfile(
                "PAÑO DENSO x1",
                new ArmorProtectionProfile(3, 7, 3),
                1
        );
    }

    public static TextileAssemblyProfile padded(int effectiveLayers) {
        return new TextileAssemblyProfile(
                "TELA ACOLCHADA · " + effectiveLayers + " capas efectivas",
                new ArmorProtectionProfile(2 * effectiveLayers, 5 * effectiveLayers, 2 * effectiveLayers),
                effectiveLayers
        );
    }

    public static TextileAssemblyProfile pleatedOverlap() {
        return new TextileAssemblyProfile(
                "TELA PLISADA + SOLAPE PARCIAL",
                new ArmorProtectionProfile(3, 8, 3),
                1
        );
    }

    public static TextileAssemblyProfile partialRegionalBodice() {
        return new TextileAssemblyProfile(
                "TELA + REFUERZOS TEXTILES PARCIALES",
                new ArmorProtectionProfile(3, 8, 4),
                2
        );
    }
}

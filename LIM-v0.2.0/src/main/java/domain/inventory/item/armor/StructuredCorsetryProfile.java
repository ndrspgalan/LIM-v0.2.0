package domain.inventory.item.armor;

import java.util.Objects;

/**
 * : la corsetería estructurada no se reduce a "TELA x2".
 * El material defensivo dominante sigue siendo CLOTH, pero el ensamblaje incorpora
 * rigidizadores longitudinales discontinuos que elevan distribución de carga sin
 * crear por sí solos una vía eléctrica continua.
 */
public record StructuredCorsetryProfile(
        ArmorProtectionProfile protection,
        String constructionLabel,
        boolean continuousConductivePath
) {
    public StructuredCorsetryProfile {
        Objects.requireNonNull(protection);
        if (constructionLabel == null || constructionLabel.isBlank()) {
            throw new IllegalArgumentException("La construcción no puede estar vacía.");
        }
    }

    public static StructuredCorsetryProfile canonicalV881() {
        return new StructuredCorsetryProfile(
                new ArmorProtectionProfile(4, 10, 6),
                "TELA TENSADA x2 + RIGIDIZADORES LONGITUDINALES DISCONTINUOS",
                false
        );
    }
}

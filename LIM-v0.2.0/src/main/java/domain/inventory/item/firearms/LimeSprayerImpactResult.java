package domain.inventory.item.firearms;

import domain.combat.NonConventionalImpactResult;

import java.util.List;

/** Resultado de un tick de 0,5 s del Rociador de Cal Viva V881. */
public record LimeSprayerImpactResult(
        NonConventionalImpactResult burn,
        NonConventionalImpactResult poison,
        List<String> corrodedArmorPieces,
        double bluntProtectionLostPerAffectedPiece
) {
    public LimeSprayerImpactResult {
        corrodedArmorPieces = List.copyOf(corrodedArmorPieces);
        if (bluntProtectionLostPerAffectedPiece < 0) {
            throw new IllegalArgumentException("La pérdida corrosiva no puede ser negativa.");
        }
    }
}

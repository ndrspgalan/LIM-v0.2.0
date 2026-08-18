package domain.ability;

import domain.status.VitalResourceState;
import java.util.Objects;

/** Resolución pasiva de DRENAR al morir cualquier personaje válido; no discrimina afiliación. */
public final class DrainPolicy {
    private DrainPolicy() {}

    public static DrainResult onCharacterDeath(
            VitalResourceState userResources,
            int characterVitality,
            double characterTotalHealth,
            boolean characterHealthRegenerationInhibited,
            boolean drainAccessible
    ) {
        return onCharacterDeath(userResources, characterVitality, characterTotalHealth, characterHealthRegenerationInhibited, drainAccessible, true);
    }

    public static DrainResult onCharacterDeath(
            VitalResourceState userResources, int characterVitality, double characterTotalHealth,
            boolean characterHealthRegenerationInhibited, boolean drainAccessible, boolean killedByUserMelee
    ) {
        Objects.requireNonNull(userResources, "Los recursos del usuario no pueden ser nulos.");
        if (characterVitality < 1 || characterVitality > 120) {
            throw new IllegalArgumentException("La VITALIDAD del personaje debe estar entre 1 y 120.");
        }
        if (!Double.isFinite(characterTotalHealth) || characterTotalHealth <= 0) {
            throw new IllegalArgumentException("Los PV TOTALES del personaje deben ser positivos.");
        }
        if (!drainAccessible || !killedByUserMelee) return new DrainResult(0.0, userResources.currentHealth(), false);

        double available = characterHealthRegenerationInhibited ? characterVitality : characterTotalHealth;
        double restored = Math.min(available, userResources.maximumHealth() - userResources.currentHealth());
        if (restored > 0) userResources.setCurrentHealth(userResources.currentHealth() + restored);
        return new DrainResult(restored, userResources.currentHealth(), characterHealthRegenerationInhibited);
    }

    public static boolean accessibleAtFaith(int faith) { return faith >= 32; }

    public record DrainResult(double restoredHealth, double resultingHealth, boolean reducedToRawVitality) {
        public DrainResult {
            if (restoredHealth < 0 || resultingHealth < 0) throw new IllegalArgumentException("Los PV no pueden ser negativos.");
        }
    }
}

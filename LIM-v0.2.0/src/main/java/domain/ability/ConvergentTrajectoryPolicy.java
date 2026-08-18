package domain.ability;

import domain.combat.LightComboFinisherPolicy;

/**
 * TRAYECTORIA CONVERGENTE.
 * - Finisher LIGHT ordinario: x1,11 ofensivo.
 * - Con maestría desbloqueada: finisher x1,40 y, en DESARMADO, apertura de Flow.
 * - Flow desarmado: LIGHT ofensivos x1,40 hasta completar el siguiente combo o iniciar PA REGEN.
 *
 * El coste de PA NO se resuelve aquí: incluso con Trayectoria/Flow, el finisher conserva
 * x1,11 de coste y los demás golpes usan el multiplicador energético de su tipo real.
 */
public final class ConvergentTrajectoryPolicy {
    public static final double DEFAULT_COMBO_FINISHER_MULTIPLIER = LightComboFinisherPolicy.STANDARD_OFFENSIVE_MULTIPLIER;
    public static final double COMBO_FINISHER_MULTIPLIER = LightComboFinisherPolicy.CONVERGENT_OFFENSIVE_MULTIPLIER;
    public static final double UNARMED_FLOW_MULTIPLIER = 1.40;

    private boolean open;

    public double onLightAttack(
            boolean unarmed,
            int ordinal,
            int length,
            boolean regen,
            boolean convergentTrajectoryUnlocked
    ) {
        if (ordinal < 1 || length < 1 || ordinal > length) {
            throw new IllegalArgumentException("Ordinal de combo inválido.");
        }
        if (regen) reset();

        boolean eligibleCombo = length >= 3;
        boolean finisher = eligibleCombo && ordinal == length;

        if (open && unarmed) {
            if (finisher) open = false;
            return UNARMED_FLOW_MULTIPLIER;
        }

        if (!finisher) return 1.0;

        if (convergentTrajectoryUnlocked) {
            if (unarmed) open = true;
            return COMBO_FINISHER_MULTIPLIER;
        }
        return DEFAULT_COMBO_FINISHER_MULTIPLIER;
    }

    public void onOverdriveActivated() {}
    public void onStaminaRegenerationStarted() { reset(); }
    public void reset() { open = false; }
    public boolean unarmedChainOpen() { return open; }
}

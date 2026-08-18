package domain.ability;

/**
 * Resolución universal de OVERDRIVE. Cada invocación cubre exclusivamente
 * una acción inmediata o un único pulso de una acción sostenida.
 */
public final class OverdrivePolicy {
    private static final double EPSILON = 1.0e-9;

    private OverdrivePolicy() {}

    public static ImmediateActionPayment payImmediateAction(
            double currentStamina,
            double currentHealth,
            double actionStaminaCost,
            boolean overdriveAvailable,
            boolean oneHealthFloor
    ) {
        validate(currentStamina, currentHealth, actionStaminaCost);
        double staminaSpent = Math.min(currentStamina, actionStaminaCost);
        double missing = Math.max(0.0, actionStaminaCost - staminaSpent);
        if (missing <= EPSILON) {
            return new ImmediateActionPayment(currentStamina - staminaSpent, currentHealth,
                    staminaSpent, 0.0, true, false);
        }
        if (!overdriveAvailable) {
            return new ImmediateActionPayment(currentStamina, currentHealth,
                    0.0, 0.0, false, false);
        }
        double floor = oneHealthFloor ? 1.0 : 0.0;
        double spendableHealth = Math.max(0.0, currentHealth - floor);
        if (spendableHealth + EPSILON < missing) {
            return new ImmediateActionPayment(currentStamina, currentHealth,
                    0.0, 0.0, false, true);
        }
        return new ImmediateActionPayment(0.0, currentHealth - missing,
                staminaSpent, missing, true, true);
    }

    /** Un segundo de CUSTODIA, HOMEOSTASIS o INVISIBILIDAD es un nuevo pulso inmediato. */
    public static ImmediateActionPayment paySustainedPulse(
            double currentStamina,
            double currentHealth,
            double pulseStaminaCost,
            boolean overdriveAvailable,
            boolean oneHealthFloor
    ) {
        return payImmediateAction(currentStamina, currentHealth, pulseStaminaCost,
                overdriveAvailable, oneHealthFloor);
    }

    /**
     * SANAR consume primero PA y después PV mediante OVERDRIVE. Nunca reduce
     * al sanador por debajo de 1 PV y se interrumpe al alcanzar el guardarraíl.
     */
    public static HealingTransfer healOther(
            double requestedHealing,
            double targetMissingHealth,
            double healerStamina,
            double healerHealth,
            boolean overdriveAvailable
    ) {
        if (!Double.isFinite(requestedHealing) || !Double.isFinite(targetMissingHealth)
                || requestedHealing < 0 || targetMissingHealth < 0) {
            throw new IllegalArgumentException("La curación solicitada no es válida.");
        }
        validate(healerStamina, healerHealth, 0.0);
        double desired = Math.min(requestedHealing, targetMissingHealth);
        double fromStamina = Math.min(healerStamina, desired);
        double remaining = desired - fromStamina;
        double fromHealth = overdriveAvailable
                ? Math.min(remaining, Math.max(0.0, healerHealth - 1.0))
                : 0.0;
        double healed = fromStamina + fromHealth;
        boolean interruptedAtGuardrail = remaining > fromHealth + EPSILON
                || (fromHealth > 0 && healerHealth - fromHealth <= 1.0 + EPSILON);
        return new HealingTransfer(
                healed,
                healerStamina - fromStamina,
                healerHealth - fromHealth,
                fromStamina,
                fromHealth,
                interruptedAtGuardrail
        );
    }

    private static void validate(double stamina, double health, double cost) {
        if (!Double.isFinite(stamina) || !Double.isFinite(health) || !Double.isFinite(cost)
                || stamina < 0 || health < 0 || cost < 0) {
            throw new IllegalArgumentException("Recursos o coste inválidos.");
        }
    }

    public record ImmediateActionPayment(
            double staminaAfter,
            double healthAfter,
            double staminaSpent,
            double healthSpent,
            boolean completed,
            boolean usedOverdrive
    ) {}

    public record HealingTransfer(
            double healed,
            double healerStaminaAfter,
            double healerHealthAfter,
            double staminaSpent,
            double healthSpent,
            boolean interruptedAtGuardrail
    ) {}
}

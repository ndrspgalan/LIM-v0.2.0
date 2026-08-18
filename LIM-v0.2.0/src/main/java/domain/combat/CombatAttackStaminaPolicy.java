package domain.combat;

/**
 * Regla universal de disponibilidad de PA para ataques inmediatos.
 *
 * Un ataque cuyo coste supera los PA máximos puede ejecutarse si el actor
 * comienza exactamente con la barra completa. Ese caso representa atacar
 * "fresco": se consumen todos los PA disponibles y el exceso nominal no
 * genera deuda ni exige OVERDRIVE. Con la barra incompleta, el bypass no aplica.
 */
public final class CombatAttackStaminaPolicy {
    private static final double EPSILON = 1.0e-9;

    public boolean canExecute(double currentStamina, double maximumStamina, double attackCost) {
        validate(currentStamina, maximumStamina, attackCost);
        if (currentStamina + EPSILON >= attackCost) return true;
        return attackCost > maximumStamina + EPSILON && isFull(currentStamina, maximumStamina);
    }

    /**
     * PA restantes tras un ataque permitido por esta política.
     * Los ataques supra-máximos ejecutados con la barra completa la vacían.
     */
    public double staminaAfter(double currentStamina, double maximumStamina, double attackCost) {
        if (!canExecute(currentStamina, maximumStamina, attackCost)) {
            throw new IllegalStateException("PA insuficientes para ejecutar el ataque.");
        }
        return Math.max(0.0, currentStamina - attackCost);
    }

    public boolean isFreshOverride(double currentStamina, double maximumStamina, double attackCost) {
        validate(currentStamina, maximumStamina, attackCost);
        return attackCost > maximumStamina + EPSILON && isFull(currentStamina, maximumStamina);
    }

    private boolean isFull(double currentStamina, double maximumStamina) {
        return Math.abs(currentStamina - maximumStamina) <= EPSILON;
    }

    private void validate(double currentStamina, double maximumStamina, double attackCost) {
        if (!Double.isFinite(currentStamina) || !Double.isFinite(maximumStamina) || !Double.isFinite(attackCost)) {
            throw new IllegalArgumentException("PA y coste deben ser finitos.");
        }
        if (maximumStamina <= 0.0 || currentStamina < 0.0 || currentStamina - maximumStamina > EPSILON || attackCost < 0.0) {
            throw new IllegalArgumentException("Estado de PA o coste de ataque inválido.");
        }
    }
}

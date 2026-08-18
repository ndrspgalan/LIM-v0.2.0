package domain.status;

public final class VitalResourceState {
    private final double maximumHealth;
    private final double maximumStamina;
    private double currentHealth;
    private double currentStamina;

    public VitalResourceState(double maximumHealth, double maximumStamina) {
        this(maximumHealth, maximumHealth, maximumStamina, maximumStamina);
    }

    public VitalResourceState(double currentHealth, double maximumHealth,
                              double currentStamina, double maximumStamina) {
        if (maximumHealth <= 0 || maximumStamina <= 0) {
            throw new IllegalArgumentException("Los máximos de PV y PA deben ser positivos.");
        }
        if (currentHealth < 0 || currentHealth > maximumHealth
                || currentStamina < 0 || currentStamina > maximumStamina) {
            throw new IllegalArgumentException("Los recursos actuales no son válidos.");
        }
        this.currentHealth = currentHealth;
        this.maximumHealth = maximumHealth;
        this.currentStamina = currentStamina;
        this.maximumStamina = maximumStamina;
    }

    public void restoreAll() {
        restoreHealth();
        restoreStamina();
    }

    public void restoreHealth() { currentHealth = maximumHealth; }
    public void restoreStamina() { currentStamina = maximumStamina; }

    public void setCurrentHealth(double value) {
        if (value < 0 || value > maximumHealth) throw new IllegalArgumentException("PV no válidos.");
        currentHealth = value;
    }

    public void setCurrentStamina(double value) {
        if (value < 0 || value > maximumStamina) throw new IllegalArgumentException("PA no válidos.");
        currentStamina = value;
    }

    public double currentHealth() { return currentHealth; }
    public double maximumHealth() { return maximumHealth; }
    public double currentStamina() { return currentStamina; }
    public double maximumStamina() { return maximumStamina; }
}

package domain.status;

public record HealthProtection(double originalCapacity, double currentCapacity) {
    public HealthProtection {
        if (originalCapacity < 0 || currentCapacity < 0 || currentCapacity > originalCapacity) {
            throw new IllegalArgumentException("La protección de PV no es válida.");
        }
    }

    public static HealthProtection none() {
        return new HealthProtection(0, 0);
    }

    public boolean active() {
        return currentCapacity > 0;
    }

    public HealthProtection absorb(double damage) {
        if (damage < 0) throw new IllegalArgumentException("El daño no puede ser negativo.");
        return new HealthProtection(originalCapacity, Math.max(0, currentCapacity - damage));
    }

    public HealthProtection restore(double missingHealthCapacity) {
        return new HealthProtection(originalCapacity, Math.min(originalCapacity, Math.max(0, missingHealthCapacity)));
    }
}

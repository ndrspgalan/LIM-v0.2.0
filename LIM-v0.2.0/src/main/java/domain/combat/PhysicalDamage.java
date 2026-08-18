package domain.combat;

public record PhysicalDamage(double piercing, double slashing, double blunt) {
    public PhysicalDamage {
        if (piercing < 0 || slashing < 0 || blunt < 0) {
            throw new IllegalArgumentException("El daño físico no puede ser negativo.");
        }
    }

    public PhysicalDamage withHeadBluntMultiplier() {
        return new PhysicalDamage(piercing, slashing, blunt * 1.5);
    }

    public PhysicalDamage scaledBy(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("El factor de daño no puede ser negativo.");
        }
        return new PhysicalDamage(piercing * factor, slashing * factor, blunt * factor);
    }

    public PhysicalDamage plus(PhysicalDamage other) {
        if (other == null) {
            throw new IllegalArgumentException("El daño que se suma no puede ser nulo.");
        }
        return new PhysicalDamage(
                piercing + other.piercing,
                slashing + other.slashing,
                blunt + other.blunt
        );
    }
}

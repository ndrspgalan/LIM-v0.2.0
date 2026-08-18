package domain.status;

public record HealthState(
        double currentHealth,
        double totalHealth,
        HealthProtection protection,
        boolean healthRegenerationReduced,
        double lastHitDamage,
        boolean yarrowInhibitionDeferred
) {
    public HealthState(double currentHealth,double totalHealth,HealthProtection protection,boolean healthRegenerationReduced){
        this(currentHealth,totalHealth,protection,healthRegenerationReduced,0.0,false);
    }
    public HealthState {
        if (totalHealth <= 0 || currentHealth < 0 || currentHealth > totalHealth || lastHitDamage < 0) {
            throw new IllegalArgumentException("Los PV no son válidos.");
        }
        if (protection == null) throw new NullPointerException("La protección no puede ser nula.");
        // La barrera de musgo es una reserva separada y puede coexistir con PV completos.
    }
    public double missingHealth() { return totalHealth - currentHealth; }
    public HealthState registerHit(double netDamage) {
        if (!Double.isFinite(netDamage) || netDamage < 0) throw new IllegalArgumentException("Daño inválido.");
        double absorbed=Math.min(netDamage,protection.currentCapacity());
        HealthProtection nextProtection=protection.absorb(absorbed);
        double toHealth=netDamage-absorbed;
        double nextHealth=Math.max(0,currentHealth-toHealth);
        boolean reduced=healthRegenerationReduced;
        boolean deferred=yarrowInhibitionDeferred;
        if (!nextProtection.active() && deferred) { reduced=false; deferred=false; }
        return new HealthState(nextHealth,totalHealth,nextProtection,reduced,netDamage,deferred);
    }
}

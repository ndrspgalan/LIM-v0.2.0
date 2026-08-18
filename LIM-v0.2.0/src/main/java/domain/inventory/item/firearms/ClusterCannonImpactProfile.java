package domain.inventory.item.firearms;

/** Paquete terminal del Cañón de Racimo V881 en una distancia concreta del epicentro. */
public record ClusterCannonImpactProfile(double radiusMeters, double slashing, double burn, boolean suffocatingBurn) {
    public ClusterCannonImpactProfile atDistance(double distanceMeters) {
        return new ClusterCannonImpactProfile(radiusMeters,
                domain.combat.RadialLethalityPolicy.channel(slashing,distanceMeters,radiusMeters),
                domain.combat.RadialLethalityPolicy.channel(burn,distanceMeters,radiusMeters),
                suffocatingBurn);
    }
    public boolean appliesStaggering(){ return false; }
}

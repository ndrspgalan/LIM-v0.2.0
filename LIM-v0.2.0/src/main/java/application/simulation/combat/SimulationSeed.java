package application.simulation.combat;

/** Seed raíz reproducible de un escenario GOLD. */
public record SimulationSeed(long value) {
    public long derive(String namespace, long index) {
        long z = value ^ ((long) namespace.hashCode() << 32) ^ index * 0x9E3779B97F4A7C15L;
        z ^= z >>> 30; z *= 0xBF58476D1CE4E5B9L;
        z ^= z >>> 27; z *= 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}

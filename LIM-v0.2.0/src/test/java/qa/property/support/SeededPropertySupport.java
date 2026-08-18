package qa.property.support;

import java.util.SplittableRandom;

/**
 *  — soporte mínimo para QA generativo reproducible sin acoplar LIM a una
 * librería concreta de property testing. Toda propiedad informa seed + caso.
 */
public final class SeededPropertySupport {
    public static final long DEFAULT_PROPERTY_SEED = 0x4C494D50353032L; // "LIMP502"
    public static final long DEFAULT_FUZZ_SEED = 0x4D44504152515541L;   // estable entre ejecuciones
    public static final int DEFAULT_PROPERTY_CASES = 10_000;
    public static final int DEFAULT_FUZZ_CASES = 25_000;

    private SeededPropertySupport() {}

    public static long propertySeed() {
        return Long.getLong("lim.qa.propertySeed", DEFAULT_PROPERTY_SEED);
    }

    public static long fuzzSeed() {
        return Long.getLong("lim.qa.fuzzSeed", DEFAULT_FUZZ_SEED);
    }

    public static int propertyCases() {
        return positiveIntProperty("lim.qa.propertyCases", DEFAULT_PROPERTY_CASES);
    }

    public static int fuzzCases() {
        return positiveIntProperty("lim.qa.fuzzCases", DEFAULT_FUZZ_CASES);
    }

    public static SplittableRandom random(long seed) {
        return new SplittableRandom(seed);
    }

    public static AssertionError failure(String property, long seed, int caseIndex, Object state, Throwable cause) {
        AssertionError error = new AssertionError(
                property + " falló; seed=" + seed + ", case=" + caseIndex + ", state=" + state,
                cause
        );
        return error;
    }

    public static void check(String property, long seed, int caseIndex, Object state, CheckedRunnable body) {
        try {
            body.run();
        } catch (AssertionError e) {
            throw failure(property, seed, caseIndex, state, e);
        } catch (RuntimeException e) {
            throw failure(property, seed, caseIndex, state, e);
        }
    }

    private static int positiveIntProperty(String key, int fallback) {
        int value = Integer.getInteger(key, fallback);
        if (value <= 0) throw new IllegalArgumentException(key + " debe ser > 0.");
        return value;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run();
    }
}

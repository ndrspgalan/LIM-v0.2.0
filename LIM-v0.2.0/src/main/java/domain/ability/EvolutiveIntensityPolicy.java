package domain.ability;

/** Escala lineal extraordinaria 76-120: +1 punto por cada nivel del atributo en ese tramo. */
public final class EvolutiveIntensityPolicy {
    public static final int MINIMUM_ATTRIBUTE = 76;
    public static final int MAXIMUM_ATTRIBUTE = 120;
    public static final int MINIMUM_INTENSITY = 1;
    public static final int MAXIMUM_INTENSITY = 45;

    private EvolutiveIntensityPolicy() {}

    public static int intensity(int attributeValue) {
        if (attributeValue < MINIMUM_ATTRIBUTE) return 0;
        int clamped = Math.min(MAXIMUM_ATTRIBUTE, attributeValue);
        return clamped - MINIMUM_ATTRIBUTE + 1;
    }
}

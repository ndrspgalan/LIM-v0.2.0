package domain.ability;

/** Vías físicas válidas para ANULACIÓN INCIDENTAL. */
public final class NullificationDeliveryPolicy {
    public enum Delivery { MELEE_WEAPON, RANGED_WEAPON, FIREARM, THROWN_WEAPON }
    private NullificationDeliveryPolicy() {}
    public static boolean incidentalCanApply(Delivery delivery) { return delivery != null; }
}

package qa.regression;

import domain.inventory.item.misc.MucusTearItem;

/** Verifica la corrección canónica de peso de la Lágrima. */
public final class TranspositionWeightVerification {
    private static final double EPS = 1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        close(new MucusTearItem(1).weightKg(), 0.001,
                "Una unidad debe representar 1 g");
        close(new MucusTearItem(50).weightKg(), 0.050,
                "Medio stack debe pesar 0,050 kg");
        close(new MucusTearItem(MucusTearItem.MAXIMUM_STACK).weightKg(), 0.100,
                "El stack completo de 100 Lágrimas debe pesar 0,100 kg");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPS) {
            throw new IllegalStateException(message + ": " + actual + " != " + expected);
        }
    }
}

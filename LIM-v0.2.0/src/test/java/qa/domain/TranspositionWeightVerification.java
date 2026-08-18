package qa.domain;

import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.inventory.item.misc.MucusTearItem;

import java.util.List;

/** Verifica los pesos canónicos fijados en la iteración . */
public final class TranspositionWeightVerification {
    private static final double EPS = 1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyTearWeight();
        verifyCrystalWeights();
    }

    private static void verifyTearWeight() {
        MucusTearItem oneTear = new MucusTearItem(1);
        MucusTearItem threeTears = new MucusTearItem(3);
        close(oneTear.weightKg(), 0.001, "Una unidad de Lágrima debe pesar 0,001 kg");
        close(threeTears.weightKg(), 0.003, "La masa fusionada de la Lágrima debe escalar por mL");
    }

    private static void verifyCrystalWeights() {
        var crystals = List.of(
                MucusCrystalCatalog.yellow(),
                MucusCrystalCatalog.greenish(),
                MucusCrystalCatalog.brown(),
                MucusCrystalCatalog.bloodied(),
                MucusCrystalCatalog.blackish()
        );
        double[] expected={0.050,0.020,0.005,0.0025,0.001};
        for (int i=0;i<crystals.size();i++) {
            close(crystals.get(i).weightKg(), expected[i],
                    crystals.get(i).name()+" debe conservar la masa del volumen transpuesto.");
        }
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPS) {
            throw new IllegalStateException(message + ": " + actual + " != " + expected);
        }
    }
}

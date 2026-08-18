package domain.runic.transposition;

import domain.character.progression.MucusType;
import java.util.Objects;

/**
 * : única fuente de verdad para el precursor de Transposición.
 * El mucus blanco se consume íntegramente y conserva 1 mL = 1 uso de Lágrima.
 * Los mucus cromáticos concentran una fracción funcional creciente de su volumen.
 */
public final class TranspositionYieldPolicy {
    public static final double WHITE_ML_PER_TEAR_USE = 1.0;
    private TranspositionYieldPolicy(){}

    public static boolean consumesAllAvailable(MucusType type) {
        return Objects.requireNonNull(type) == MucusType.BLANCO;
    }

    public static double precursorMlPerCrystal(MucusType type) {
        return switch (Objects.requireNonNull(type)) {
            case AMARILLENTO -> 50.0;
            case VERDOSO -> 20.0;
            case MARRON -> 5.0;
            case ENSANGRENTADO -> 2.5;
            case NEGRUZCO -> 1.0;
            case BLANCO -> throw new IllegalArgumentException("El mucus blanco produce Lágrima, no cristal.");
        };
    }

    public static String doctrineSummary() {
        return "El volumen no es la materia: el mucus ordinario es mayoritariamente una red hidratada. "
                + "Transposición retira la fase que sólo mantenía dispersa la estructura funcional y conserva aquello "
                + "capaz de sobrevivir al cambio de estado. BLANCO: todo el volumen, 1 mL por uso de Lágrima; "
                + "AMARILLENTO: 50 mL/cristal; VERDOSO: 20; MARRÓN: 5; ENSANGRENTADO: 2,5; NEGRUZCO: 1.";
    }
}

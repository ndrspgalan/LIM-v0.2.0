package domain.ability;

import domain.social.RelationshipType;
import java.util.Objects;

/**
 * ANULACIÓN : pasiva. Sólo inhibe el abalorio equipado por un adversario HOSTIL
 * con menos AGUANTE y conserva esa inhibición hasta terminar el encuentro hostil.
 */
public final class NullificationPolicy {
    private NullificationPolicy() {}

    public static boolean eligible(RelationshipType relationship, int userEndurance, int targetEndurance) {
        Objects.requireNonNull(relationship, "La relación no puede ser nula.");
        return relationship == RelationshipType.HOSTILE && userEndurance > targetEndurance;
    }

    public static SuppressionState apply(RelationshipType relationship, int userEndurance, int targetEndurance,
                                         boolean hostileEncounter, String equippedAccessoryName, boolean insideField) {
        if (!hostileEncounter || !eligible(relationship, userEndurance, targetEndurance)
                || equippedAccessoryName == null || equippedAccessoryName.isBlank()) {
            return SuppressionState.none();
        }
        return new SuppressionState(true, Double.POSITIVE_INFINITY, insideField, equippedAccessoryName.trim());
    }

    /** Compatibilidad con llamadas históricas: la supresión antigua queda limitada a abalorios. */
    public static SuppressionState incidentalContact(int endurance) {
        return new SuppressionState(true, Double.POSITIVE_INFINITY, false, "*");
    }

    /** El campo no genera cola temporal: la inhibición persiste por el encuentro una vez aplicada. */
    public static SuppressionState foundationalTick(int endurance, boolean insideField,
                                                     SuppressionState previous, double elapsedSeconds) {
        Objects.requireNonNull(previous, "El estado anterior no puede ser nulo.");
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        return previous;
    }

    /** ANULACIÓN ya no inhibe maestrías. */
    public static boolean masteryUsable(SuppressionState state) { Objects.requireNonNull(state); return true; }

    /** ANULACIÓN ya no inhibe Marcas Rúnicas. */
    public static boolean runicMarkUsable(SuppressionState state) { Objects.requireNonNull(state); return true; }

    public static boolean accessoryPropertyUsable(SuppressionState state) {
        Objects.requireNonNull(state);
        return !state.suppressed();
    }

    public static boolean accessoryPropertyUsable(SuppressionState state, String equippedAccessoryName) {
        Objects.requireNonNull(state);
        if (!state.suppressed()) return true;
        if (state.accessoryName().equals("*")) return false;
        return equippedAccessoryName == null || !state.accessoryName().equals(equippedAccessoryName);
    }

    public static boolean hiddenPropertyUsable(SuppressionState state) { return accessoryPropertyUsable(state); }

    /** Ya no existe una duración de 1-3 s: finaliza con el encuentro hostil. */
    public static double suppressionSeconds(int endurance) { return Double.POSITIVE_INFINITY; }

    public record SuppressionState(boolean suppressed, double remainingSeconds, boolean insideField, String accessoryName) {
        public SuppressionState {
            accessoryName = accessoryName == null ? "" : accessoryName;
            if (Double.isNaN(remainingSeconds) || remainingSeconds < 0) throw new IllegalArgumentException("Duración inválida.");
            if (!suppressed && (!accessoryName.isEmpty() || remainingSeconds != 0.0)) {
                throw new IllegalArgumentException("Una supresión inactiva no puede conservar estado.");
            }
        }
        public SuppressionState(boolean suppressed, double remainingSeconds, boolean insideField) {
            this(suppressed, remainingSeconds, insideField, suppressed ? "*" : "");
        }
        public static SuppressionState none() { return new SuppressionState(false, 0.0, false, ""); }
        public SuppressionState endHostileEncounter() { return none(); }
    }
}

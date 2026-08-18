package domain.bestiarium.physical_plane.aspirant;

/**
 * Fundamento causal del referente convergente. No calcula una especie mediante una tabla:
 * registra las presiones que el diseño debe justificar (predisposición, locomoción, actividad,
 * dieta, ambiente, conducta, regeneración e historia epigenética).
 */
public record AspirantSomaticHistory(String narrative) {
    public AspirantSomaticHistory {
        if(narrative==null||narrative.isBlank()) throw new IllegalArgumentException("La historia somática es obligatoria.");
    }
}

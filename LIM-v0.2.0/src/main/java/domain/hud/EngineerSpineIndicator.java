package domain.hud;

/**
 * Estado diegético que el frontend representa sobre la columna vertebral del Conjunto del Ingeniero.
 * El nivel es estructural en ;  lo enlazará con la reserva reparable de refrigerante.
 */
public record EngineerSpineIndicator(boolean visible, double levelRatio, String chromaticVariant) {
    public static final String CYAN_COOLANT = "CYAN_COOLANT";

    public EngineerSpineIndicator {
        if (levelRatio < 0 || levelRatio > 1) {
            throw new IllegalArgumentException("El nivel vertebral debe estar entre 0 y 1.");
        }
        if (chromaticVariant == null || chromaticVariant.isBlank()) {
            throw new IllegalArgumentException("La variante cromática no puede estar vacía.");
        }
    }

    public static EngineerSpineIndicator hidden() {
        return new EngineerSpineIndicator(false, 0, CYAN_COOLANT);
    }
}

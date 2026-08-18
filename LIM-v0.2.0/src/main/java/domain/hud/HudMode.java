package domain.hud;

/**
 * Conserva el nombre histórico HudMode, pero desde  solo representa el flujo temporal.
 * No existe un HUD convencional que mostrar u ocultar.
 */
public enum HudMode {
    REALTIME(false),
    PAUSED(true);

    private final boolean gameplayPaused;

    HudMode(boolean gameplayPaused) {
        this.gameplayPaused = gameplayPaused;
    }

    /** Compatibilidad semántica: nunca hay un HUD convencional visible. */
    public boolean hudVisible() { return false; }
    public boolean gameplayPaused() { return gameplayPaused; }
}

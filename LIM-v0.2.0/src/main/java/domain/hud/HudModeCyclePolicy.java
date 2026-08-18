package domain.hud;

import java.util.Objects;

/** Alternancia binaria entre juego activo y pausa silenciosa. */
public final class HudModeCyclePolicy {
    public HudMode next(HudMode current) {
        Objects.requireNonNull(current, "El modo temporal no puede ser nulo.");
        return current == HudMode.REALTIME ? HudMode.PAUSED : HudMode.REALTIME;
    }
}

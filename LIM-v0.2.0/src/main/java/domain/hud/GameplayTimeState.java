package domain.hud;

import java.util.EnumSet;
import java.util.Set;

/** Estado temporal con causas de pausa independientes y componibles. */
public final class GameplayTimeState {
    private final EnumSet<PauseOrigin> pauseOrigins = EnumSet.noneOf(PauseOrigin.class);

    public void pause(PauseOrigin origin) { pauseOrigins.add(origin); }
    public void resume(PauseOrigin origin) { pauseOrigins.remove(origin); }
    public boolean isPaused() { return !pauseOrigins.isEmpty(); }
    public boolean isPausedBy(PauseOrigin origin) { return pauseOrigins.contains(origin); }
    public Set<PauseOrigin> pauseOrigins() { return Set.copyOf(pauseOrigins); }
}

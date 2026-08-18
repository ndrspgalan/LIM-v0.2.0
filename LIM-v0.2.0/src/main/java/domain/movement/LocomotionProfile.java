package domain.movement;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** Resultado de evaluar la transitabilidad de una superficie. */
public final class LocomotionProfile {
    private final SlopeBand slopeBand;
    private final Set<LocomotionMode> allowedModes;

    public LocomotionProfile(SlopeBand slopeBand, Set<LocomotionMode> allowedModes) {
        if (slopeBand == null) throw new IllegalArgumentException("La banda de pendiente no puede ser nula.");
        if (allowedModes == null) throw new IllegalArgumentException("Los modos permitidos no pueden ser nulos.");
        this.slopeBand = slopeBand;
        this.allowedModes = allowedModes.isEmpty()
                ? Collections.emptySet()
                : Collections.unmodifiableSet(EnumSet.copyOf(allowedModes));
    }

    public SlopeBand slopeBand() { return slopeBand; }
    public Set<LocomotionMode> allowedModes() { return allowedModes; }
    public boolean allows(LocomotionMode mode) {
        if (mode == null) throw new IllegalArgumentException("El modo no puede ser nulo.");
        return allowedModes.contains(mode);
    }
    public boolean traversable() { return !allowedModes.isEmpty(); }
    public boolean requiresClimbing() { return slopeBand == SlopeBand.CLIMB_REQUIRED && allows(LocomotionMode.CLIMBING); }
}

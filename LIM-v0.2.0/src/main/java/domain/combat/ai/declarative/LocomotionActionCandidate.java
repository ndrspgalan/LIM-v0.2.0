package domain.combat.ai.declarative;

import java.util.Objects;
import java.util.OptionalDouble;

/** Alternativa locomotora factual con coste y velocidad corporal explícitos. */
public record LocomotionActionCandidate(
        Kind kind, double staminaCostPerSecond, double speedMetersPerSecond,
        OptionalDouble oneShotStaminaCost, OptionalDouble verticalHeightMeters, OptionalDouble horizontalDistanceMeters, String relation
) {
    public enum Kind { RUN, TROT, WALK, CROUCH_WALK, CRAWL, CLIMB, SWIM, FAST_SWIM, DIVE, JUMP_VERTICAL, JUMP_HORIZONTAL }
    public LocomotionActionCandidate {
        Objects.requireNonNull(kind); oneShotStaminaCost=Objects.requireNonNull(oneShotStaminaCost); verticalHeightMeters=Objects.requireNonNull(verticalHeightMeters); horizontalDistanceMeters=Objects.requireNonNull(horizontalDistanceMeters);
        if(!Double.isFinite(staminaCostPerSecond)||staminaCostPerSecond<0||!Double.isFinite(speedMetersPerSecond)||speedMetersPerSecond<0) throw new IllegalArgumentException("Coste/velocidad locomotora inválidos.");
        if(relation==null||relation.isBlank()) throw new IllegalArgumentException("Relación locomotora obligatoria.");
    }
}

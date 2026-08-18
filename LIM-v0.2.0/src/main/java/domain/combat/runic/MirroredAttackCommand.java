package domain.combat.runic;

import java.util.Objects;

/** Repite la orden ofensiva, no el resultado del impacto anterior. */
public record MirroredAttackCommand(Object attackerId, Runnable attackSequence) {
    public MirroredAttackCommand { Objects.requireNonNull(attackerId); Objects.requireNonNull(attackSequence); }
    public void execute() { attackSequence.run(); }
}

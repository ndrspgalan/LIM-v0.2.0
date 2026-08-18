package domain.combat.ai.declarative;

import domain.combat.moveset.ModeAttackRef;
import domain.inventory.item.WeaponActionMode;
import domain.inventory.item.GripMode;
import java.util.Objects;
import java.util.Optional;

/** Estado cinético mínimo persistido por LIM entre ciclos de decisión. */
public record MeleeDecisionState(
        WeaponActionMode currentMode,
        GripMode currentGrip,
        Optional<ModeAttackRef> previousMove,
        int nextLightOrdinal,
        int lightChainLengthSoFar,
        boolean lightComboActive,
        boolean convergentTrajectoryUnlocked
) {
    public MeleeDecisionState {
        Objects.requireNonNull(currentMode); Objects.requireNonNull(currentGrip); previousMove=Objects.requireNonNull(previousMove);
        if (nextLightOrdinal < 1 || lightChainLengthSoFar < 0) throw new IllegalArgumentException("Estado LIGHT inválido.");
    }
    public static MeleeDecisionState initial(WeaponActionMode mode, GripMode grip) {
        return new MeleeDecisionState(mode, grip, Optional.empty(), 1, 0, false, false);
    }
}

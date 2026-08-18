package domain.combat.runic;

import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.runic.RunicMarkId;
import java.util.Objects;

public final class SilencePolicy {
    public double emittedSoundIntensity(double ordinaryIntensity, CharacterSheet sheet, EquipmentState equipment) {
        if (!Double.isFinite(ordinaryIntensity) || ordinaryIntensity < 0) throw new IllegalArgumentException("Intensidad inválida.");
        Objects.requireNonNull(sheet); Objects.requireNonNull(equipment);
        return equipment.hasAwakenedRunicMark(RunicMarkId.SILENCIO, sheet) ? 0.0 : ordinaryIntensity;
    }
    public boolean retainsTargetLockAfterFeint(boolean previouslyLocked, CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(sheet); Objects.requireNonNull(equipment);
        return previouslyLocked && !equipment.hasAwakenedRunicMark(RunicMarkId.SILENCIO, sheet);
    }
}

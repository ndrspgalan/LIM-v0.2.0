package domain.combat.runic;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.runic.RunicMarkId;
import domain.runic.RunicMarkActivityPolicy;
import domain.ability.NullificationPolicy;
import java.util.Set;
import java.util.Objects;

/** Bonificaciones malditas de ataques primarios; los impactos secundarios no se realimentan. */
public final class RunicAttackAugmentationPolicy {
    public double bindingVowCurseDamage(CharacterSheet sheet, EquipmentState equipment, ImpactOrigin origin) {
        Objects.requireNonNull(sheet); Objects.requireNonNull(equipment); Objects.requireNonNull(origin);
        return origin.triggersRunicOffense()
                && equipment.hasAwakenedRunicMark(RunicMarkId.VOTO_VINCULANTE, sheet)
                ? sheet.valueOf(Attribute.FE) : 0.0;
    }
    public double bindingVowCurseDamage(CharacterSheet sheet, EquipmentState equipment, Set<RunicMarkId> exceptionalMarks,
                                         NullificationPolicy.SuppressionState suppression, ImpactOrigin origin) {
        Objects.requireNonNull(sheet); Objects.requireNonNull(origin);
        return origin.triggersRunicOffense()
                && RunicMarkActivityPolicy.active(RunicMarkId.VOTO_VINCULANTE, sheet, equipment, exceptionalMarks, suppression)
                ? sheet.valueOf(Attribute.FE) : 0.0;
    }

}

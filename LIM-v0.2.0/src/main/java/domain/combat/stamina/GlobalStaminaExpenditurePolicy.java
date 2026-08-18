package domain.combat.stamina;

import domain.ability.NullificationPolicy;
import domain.character.sheet.CharacterSheet;
import domain.environment.time.DayPhase;
import domain.inventory.equipment.EquipmentState;
import domain.runic.RunicMarkActivityPolicy;
import domain.runic.RunicMarkId;

import java.util.Set;

/** Punto único  para aplicar modificadores globales al gasto de PA. */
public final class GlobalStaminaExpenditurePolicy {
    public static final double PARHELIO_NIGHT_MULTIPLIER = 0.75;

    public double resolve(double canonicalCost, CharacterSheet sheet, EquipmentState equipment, DayPhase phase) {
        return resolve(canonicalCost, sheet, equipment, phase, Set.of(), NullificationPolicy.SuppressionState.none());
    }

    public double resolve(double canonicalCost, CharacterSheet sheet, EquipmentState equipment, DayPhase phase,
                          Set<RunicMarkId> exceptionalMarks, NullificationPolicy.SuppressionState suppression) {
        if (!Double.isFinite(canonicalCost) || canonicalCost < 0) throw new IllegalArgumentException("Coste de PA inválido.");
        boolean parhelio = phase == DayPhase.NIGHT && RunicMarkActivityPolicy.active(
                RunicMarkId.PARHELIO, sheet, equipment, exceptionalMarks, suppression);
        return canonicalCost * (parhelio ? PARHELIO_NIGHT_MULTIPLIER : 1.0);
    }
    /** capa final de consumibles. La inyección domina cualquier gasto; Hidromiel normaliza sólo el multiplicador técnico del ataque. */
    public double resolveWithConsumables(double baseCost, double attackMultiplier, boolean stimulantInjectionActive, boolean meadActive,
                                         CharacterSheet sheet, EquipmentState equipment, DayPhase phase,
                                         Set<RunicMarkId> exceptionalMarks, NullificationPolicy.SuppressionState suppression) {
        if (!Double.isFinite(baseCost) || baseCost < 0 || !Double.isFinite(attackMultiplier) || attackMultiplier <= 0)
            throw new IllegalArgumentException("Coste/multiplicador de PA inválido.");
        if (stimulantInjectionActive) return 0.0;
        double normalized = meadActive && attackMultiplier > 1.0 ? 1.0 : attackMultiplier;
        return resolve(baseCost * normalized, sheet, equipment, phase, exceptionalMarks, suppression);
    }

}

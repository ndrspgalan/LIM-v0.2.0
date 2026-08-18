package domain.combat.ai.declarative;

import domain.combat.PhysicalDamage;
import domain.combat.moveset.BodyAdvance;
import domain.inventory.item.*;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Acción melee legal y materializada por LIM. No contiene score, prioridad ni recomendación.
 * MDPAR recibe estos hechos y decide entre alternativas.
 */
public record MeleeActionCandidate(
        String weaponName,
        WeaponActionMode mode,
        GripMode grip,
        WeaponCombatAction action,
        String motionId,
        int lightOrdinal,
        boolean lightFinisher,
        double staminaCost,
        double reachMeters,
        PhysicalDamage physicalDamage,
        BodyAdvance bodyAdvance,
        Optional<TransitionFact> transitionFromPrevious,
        OptionalDouble chargedPreparationSeconds,
        boolean releaseDrivenCharged,
        String trajectory,
        String endState
) {
    public MeleeActionCandidate {
        if (weaponName==null||weaponName.isBlank()||motionId==null||motionId.isBlank()) throw new IllegalArgumentException("Identidad ofensiva incompleta.");
        Objects.requireNonNull(mode); Objects.requireNonNull(grip); Objects.requireNonNull(action); Objects.requireNonNull(physicalDamage); Objects.requireNonNull(bodyAdvance);
        transitionFromPrevious=Objects.requireNonNull(transitionFromPrevious); chargedPreparationSeconds=Objects.requireNonNull(chargedPreparationSeconds);
        if (!Double.isFinite(staminaCost)||staminaCost<0||!Double.isFinite(reachMeters)||reachMeters<0) throw new IllegalArgumentException("Coste/alcance inválido.");
        if (trajectory==null||trajectory.isBlank()||endState==null||endState.isBlank()) throw new IllegalArgumentException("Trayectoria/estado final obligatorios.");
    }
}

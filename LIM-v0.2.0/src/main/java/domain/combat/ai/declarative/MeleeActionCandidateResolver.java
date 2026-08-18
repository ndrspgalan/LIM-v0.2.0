package domain.combat.ai.declarative;

import domain.ability.AttackKind;
import domain.character.sheet.Attribute;
import domain.combat.*;
import domain.combat.moveset.*;
import domain.inventory.item.*;

import java.util.*;

/**
 *  — enumera acciones melee materialmente válidas. No ordena, puntúa ni recomienda.
 * La elección racional queda fuera de LIM.
 */
public final class MeleeActionCandidateResolver {
    private final CombatStaminaCostPolicy stamina = new CombatStaminaCostPolicy();
    private final HeavyAttackImpactPolicy heavy = new HeavyAttackImpactPolicy();
    private final ChargedAttackImpactPolicy charged = new ChargedAttackImpactPolicy();
    private final ChargedAttackSpecializationPolicy chargedSpecialization = new ChargedAttackSpecializationPolicy();

    public List<MeleeActionCandidate> resolve(
            CombatActorDecisionState actor, WeaponItem weapon, MeleeDecisionState state) {
        return resolve(actor, weapon, state, false);
    }

    /** las pasivas modifican consecuencias reales, nunca la elección. */
    public List<MeleeActionCandidate> resolve(
            CombatActorDecisionState actor, WeaponItem weapon, MeleeDecisionState state, boolean auraPulsionActive
    ) {
        Objects.requireNonNull(actor); Objects.requireNonNull(weapon); Objects.requireNonNull(state);
        List<MeleeActionCandidate> result = new ArrayList<>();
        for (WeaponConfiguration configuration : weapon.availableConfigurations()) {
            if (!meetsRequirements(actor, weapon, configuration)) continue;
            WeaponActionMode mode = configuration.actionMode();
            Set<WeaponCombatAction> allowed = weapon.combatActionsFor(mode);
            Optional<MeleeMovesetProfile> moveset = weapon.hasTrait(WeaponTrait.UNARMED) && weapon.name().equals("DESARMADO")
                    ? Optional.of(mode==WeaponActionMode.PRIMARY ? UnarmedMovesetCatalog.rightLead(actor.sheet().valueOf(Attribute.DESTREZA)) : UnarmedMovesetCatalog.leftLead(actor.sheet().valueOf(Attribute.DESTREZA)))
                    : weapon.offensiveMovesetFor(mode);
            if (moveset.isPresent()) {
                appendMovesetCandidates(result, actor, weapon, configuration, state, moveset.orElseThrow(), allowed, auraPulsionActive);
            } else {
                appendFallbackCandidates(result, actor, weapon, configuration, state, allowed, auraPulsionActive);
            }
        }
        return List.copyOf(result);
    }

    private void appendMovesetCandidates(List<MeleeActionCandidate> out, CombatActorDecisionState actor,
                                         WeaponItem weapon, WeaponConfiguration configuration, MeleeDecisionState state,
                                         MeleeMovesetProfile profile, Set<WeaponCombatAction> allowed, boolean auraPulsionActive) {
        int lightCount = profile.lightAttackCount();
        for (MeleeAttackMotion motion : profile.motions()) {
            if (!allowed.contains(motion.action())) continue;
            if (motion.action() == WeaponCombatAction.LIGHT_ATTACK) {
                int requested = state.lightComboActive() ? state.nextLightOrdinal() : 1;
                int executable = requested <= lightCount ? requested : 1;
                if (motion.lightOrdinal() != executable) continue;
            }
            out.add(candidate(actor, weapon, configuration, state, motion, lightCount, auraPulsionActive));
        }
        // BLOCK/PARRY pueden existir sin trayectoria ofensiva  y siguen siendo acciones legales.
        for (WeaponCombatAction action : List.of(WeaponCombatAction.BLOCK, WeaponCombatAction.PARRY)) {
            if (allowed.contains(action) && profile.motions().stream().noneMatch(m -> m.action() == action)) {
                out.add(nonOffensiveCandidate(weapon, configuration, action, state));
            }
        }
    }

    private void appendFallbackCandidates(List<MeleeActionCandidate> out, CombatActorDecisionState actor,
                                          WeaponItem weapon, WeaponConfiguration configuration, MeleeDecisionState state,
                                          Set<WeaponCombatAction> allowed, boolean auraPulsionActive) {
        for (WeaponCombatAction action : allowed) {
            if (action == WeaponCombatAction.BLOCK || action == WeaponCombatAction.PARRY) {
                out.add(nonOffensiveCandidate(weapon, configuration, action, state));
                continue;
            }
            int ordinal = action == WeaponCombatAction.LIGHT_ATTACK ? 1 : 0;
            MeleeAttackMotion synthetic = new MeleeAttackMotion(
                    "DECLARATIVE_" + configuration.actionMode() + "_" + action,
                    action, ordinal,
                    "Estado físico actual no especializado.",
                    "Trayectoria legal del repertorio existente; geometría fina aún no formalizada.",
                    "Superficie ofensiva canónica del arma.",
                    "Estado final pendiente de formalización cinética específica.",
                    BodyAdvance.NONE);
            out.add(candidate(actor, weapon, configuration, state, synthetic, action == WeaponCombatAction.LIGHT_ATTACK ? 1 : 0, auraPulsionActive));
        }
    }

    private MeleeActionCandidate candidate(CombatActorDecisionState actor, WeaponItem weapon,
                                           WeaponConfiguration configuration, MeleeDecisionState state,
                                           MeleeAttackMotion motion, int lightCount, boolean auraPulsionActive) {
        boolean restartFinisher = motion.action() == WeaponCombatAction.LIGHT_ATTACK
                && state.lightComboActive() && state.nextLightOrdinal() > Math.max(1, lightCount)
                && state.lightChainLengthSoFar() >= 3;
        boolean ordinaryFinisher = motion.action() == WeaponCombatAction.LIGHT_ATTACK
                && lightCount >= 3 && motion.lightOrdinal() == lightCount;
        boolean finisher = restartFinisher || ordinaryFinisher;
        double cost = offensiveCost(actor, weapon, motion.action(), finisher);
        PhysicalDamage damage = physicalDamage(actor, weapon, motion.action(), finisher, state.convergentTrajectoryUnlocked(), auraPulsionActive);
        Optional<TransitionFact> transition = transitionFromPrevious(weapon, state.previousMove(), configuration.actionMode(), motion.id());
        boolean releaseDriven = motion.action() == WeaponCombatAction.CHARGED_ATTACK && chargedSpecialization.releaseDriven(weapon);
        OptionalDouble preparation = motion.action() == WeaponCombatAction.CHARGED_ATTACK
                ? (releaseDriven ? OptionalDouble.empty() : OptionalDouble.of(ChargedAttackTimingPolicy.preparationSeconds(weapon, configuration.actionMode())))
                : OptionalDouble.empty();
        return new MeleeActionCandidate(weapon.name(), configuration.actionMode(), configuration.gripMode(), motion.action(), motion.id(),
                motion.lightOrdinal(), finisher, cost, weapon.reachMeters(), damage, motion.bodyAdvance(), transition,
                preparation, releaseDriven, motion.trajectory(), motion.endState());
    }

    private MeleeActionCandidate nonOffensiveCandidate(WeaponItem weapon, WeaponConfiguration configuration,
                                                       WeaponCombatAction action, MeleeDecisionState state) {
        String id = configuration.actionMode() + "_" + action;
        return new MeleeActionCandidate(weapon.name(), configuration.actionMode(), configuration.gripMode(), action, id, 0,
                false, 0.0, weapon.reachMeters(), new PhysicalDamage(0,0,0), BodyAdvance.NONE,
                transitionFromPrevious(weapon, state.previousMove(), configuration.actionMode(), id), OptionalDouble.empty(), false,
                action == WeaponCombatAction.BLOCK ? "Cobertura defensiva mantenida." : "Intercepción defensiva dentro de su ventana física.",
                "Estado defensivo resultante.");
    }

    private double offensiveCost(CombatActorDecisionState actor, WeaponItem weapon, WeaponCombatAction action, boolean finisher) {
        if (action == WeaponCombatAction.BLOCK || action == WeaponCombatAction.PARRY) return 0.0;
        if (action == WeaponCombatAction.DESTABILIZE) return stamina.cost(weapon, action);
        return stamina.cost(weapon, action, finisher);
    }

    private PhysicalDamage physicalDamage(CombatActorDecisionState actor, WeaponItem weapon, WeaponCombatAction action,
                                          boolean finisher, boolean convergent, boolean auraPulsionActive) {
        WeaponMode mode = weapon.modes().getFirst();
        PhysicalDamage base = MeleeWeaponImpactPolicy.baseImpact(weapon, mode);
        return switch (action) {
            case LIGHT_ATTACK -> finisher ? base.scaledBy(LightComboFinisherPolicy.offensiveMultiplier(convergent)) : base;
            case HEAVY_ATTACK -> heavy.resolve(weapon, mode, false, convergent);
            case CHARGED_ATTACK -> charged.resolve(weapon, mode, auraPulsionActive);
            case JUMP_ATTACK -> new PhysicalDamage(base.piercing(), base.slashing(), base.blunt() * AttackKind.JUMP.bluntMultiplier());
            case DESTABILIZE -> new PhysicalDamage(0, 0, actor.sheet().valueOf(Attribute.FUERZA));
            case BLOCK, PARRY -> new PhysicalDamage(0,0,0);
        };
    }

    private Optional<TransitionFact> transitionFromPrevious(WeaponItem weapon, Optional<ModeAttackRef> previous,
                                                            WeaponActionMode destinationMode, String destinationId) {
        if (previous.isEmpty()) return Optional.empty();
        ModeAttackRef from = previous.orElseThrow();
        if (from.mode() == destinationMode) {
            Optional<MeleeMovesetProfile> profile = weapon.offensiveMovesetFor(destinationMode);
            if (profile.isPresent()) {
                Optional<MeleeAttackTransition> t = profile.orElseThrow().transition(from.motionId(), destinationId);
                if (t.isPresent()) {
                    MeleeAttackTransition x=t.orElseThrow();
                    return Optional.of(new TransitionFact(x.continuity(), x.executionTimeMultiplier(), x.rationale()));
                }
            }
        } else {
            Optional<CrossModeAttackTransition> t = weapon.crossModeTransitionProfile().transition(from, new ModeAttackRef(destinationMode, destinationId));
            if (t.isPresent()) {
                CrossModeAttackTransition x=t.orElseThrow();
                return Optional.of(new TransitionFact(x.continuity(), x.executionTimeMultiplier(), x.rationale()));
            }
        }
        return Optional.of(TransitionFact.neutral("No existe una transición  especializada; LIM sólo declara ausencia de ventaja/desventaja cinética conocida."));
    }

    private boolean meetsRequirements(CombatActorDecisionState actor, WeaponItem weapon, WeaponConfiguration configuration) {
        if (weapon.hasTrait(WeaponTrait.UNARMED)) return true;
        for (AttributeRequirement requirement : WeaponRequirementPolicy.calculate(
                weapon.reachMeters(), weapon.weightKg(), configuration.gripMode(), weapon.traits())) {
            if (actor.sheet().valueOf(requirement.attribute()) < requirement.minimumValue()) return false;
        }
        return true;
    }
}

package domain.combat.runic;

import domain.character.sheet.CharacterSheet;
import domain.combat.PhysicalDamage;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.WeaponMode;
import domain.runic.RunicMarkId;
import domain.runic.RunicMarkActivityPolicy;
import domain.ability.NullificationPolicy;
import java.util.Set;
import java.util.Objects;

/** Fachada de  para composición ofensiva sin acoplar el núcleo a nombres de objetos. */
public final class RunicCombatService {
    private final RunicAttackAugmentationPolicy augmentation = new RunicAttackAugmentationPolicy();
    private final ResonanceCombatMemory resonanceMemory;
    private final CompositeImpactResolver impactResolver = new CompositeImpactResolver();
    private final RunicCoatingDamagePolicy coatingPolicy = new RunicCoatingDamagePolicy();

    public RunicCombatService(ResonanceCombatMemory resonanceMemory) {
        this.resonanceMemory = Objects.requireNonNull(resonanceMemory);
    }

    public Resolution resolvePrimaryAttack(Object attackerId, Object targetId,
                                           AttackSignature signature, WeaponMode impactMode,
                                           PhysicalDamage physicalNet,
                                           CharacterSheet attackerSheet, EquipmentState attackerEquipment,
                                           double targetCurseResistancePercent,
                                           double targetPhysicalStability, double targetSanity) {
        Objects.requireNonNull(attackerSheet); Objects.requireNonNull(attackerEquipment);
        Objects.requireNonNull(impactMode, "El perfil de impacto no puede ser nulo.");
        double physicalNetTotal = physicalNet.piercing() + physicalNet.slashing() + physicalNet.blunt();
        ResonanceResult resonance = resonanceMemory.register(attackerId, targetId, signature,
                physicalNetTotal,
                attackerEquipment.hasAwakenedRunicMark(RunicMarkId.RESONANCIA, attackerSheet),
                ImpactOrigin.PRIMARY_ATTACK);
        double vow = augmentation.bindingVowCurseDamage(attackerSheet, attackerEquipment, ImpactOrigin.PRIMARY_ATTACK);
        double coating = coatingPolicy.rawCurseDamage(signature.weaponIdentity().weapon(), impactMode, true);
        CompositeImpact impact = impactResolver.resolve(physicalNet, resonance.rawCurseDamage() + vow + coating,
                targetCurseResistancePercent, targetPhysicalStability, targetSanity);
        return new Resolution(impact, resonance, vow, coating);
    }

    /** Resolución excepcional para criaturas como el Doppelgänger, capaces de mantener varias marcas activas. */
    public Resolution resolvePrimaryAttack(Object attackerId, Object targetId,
                                           AttackSignature signature, WeaponMode impactMode,
                                           PhysicalDamage physicalNet,
                                           CharacterSheet attackerSheet, EquipmentState attackerEquipment,
                                           Set<RunicMarkId> exceptionalActiveMarks,
                                           NullificationPolicy.SuppressionState attackerSuppression,
                                           double targetCurseResistancePercent,
                                           double targetPhysicalStability, double targetSanity) {
        Objects.requireNonNull(attackerSheet); Objects.requireNonNull(impactMode);
        double physicalNetTotal = physicalNet.piercing() + physicalNet.slashing() + physicalNet.blunt();
        boolean resonanceActive = RunicMarkActivityPolicy.active(RunicMarkId.RESONANCIA, attackerSheet, attackerEquipment,
                exceptionalActiveMarks, attackerSuppression);
        ResonanceResult resonance = resonanceMemory.register(attackerId, targetId, signature,
                physicalNetTotal, resonanceActive, ImpactOrigin.PRIMARY_ATTACK);
        double vow = augmentation.bindingVowCurseDamage(attackerSheet, attackerEquipment, exceptionalActiveMarks,
                attackerSuppression, ImpactOrigin.PRIMARY_ATTACK);
        double coating = coatingPolicy.rawCurseDamage(signature.weaponIdentity().weapon(), impactMode, true);
        CompositeImpact impact = impactResolver.resolve(physicalNet, resonance.rawCurseDamage() + vow + coating,
                targetCurseResistancePercent, targetPhysicalStability, targetSanity);
        return new Resolution(impact, resonance, vow, coating);
    }

    public record Resolution(CompositeImpact impact, ResonanceResult resonance,
                             double bindingVowRawDamage, double coatingRawDamage) {}
}

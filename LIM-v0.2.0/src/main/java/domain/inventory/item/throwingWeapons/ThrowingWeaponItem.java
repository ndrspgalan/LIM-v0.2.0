package domain.inventory.item.throwingWeapons;

import domain.combat.DamageType;
import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.combat.StaggerResult;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.misc.MiscellaneousCategory;
import domain.inventory.item.misc.StackableMiscellaneousItem;
import domain.inventory.item.misc.UseAnimation;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import domain.throwing.ThrowProfile;
import domain.throwing.ThrowResult;

import java.util.List;
import java.util.Objects;

/**
 * : cada instancia representa exactamente un arma arrojadiza física.
 * Se equipa en acceso rápido, no utiliza AIMING y el lanzamiento consume esa instancia.
 */
public final class ThrowingWeaponItem extends StackableMiscellaneousItem {
    private final ThrowingWeaponEffect effect;
    private final ThrowProfile throwProfile;

    public ThrowingWeaponItem(
            String name,
            String description,
            int currentUnits,
            int maximumStack,
            double unitWeightKg,
            InventoryFootprint footprint,
            ThrowProfile throwProfile,
            ThrowingWeaponEffect effect,
            List<String> statistics,
            List<ItemProperty> properties
    ) {
        super(name, description, MiscellaneousCategory.OBJECT, currentUnits, maximumStack,
                unitWeightKg, footprint,
                new UseAnimation(0.35, List.of("Preparar unidad", "Lanzar")),
                statistics, properties);
        this.throwProfile = Objects.requireNonNull(throwProfile, "El perfil de lanzamiento no puede ser nulo.");
        this.effect = Objects.requireNonNull(effect, "El efecto arrojadizo no puede ser nulo.");
        if (Math.abs(throwProfile.massKg() - unitWeightKg) > 1.0e-9) {
            throw new IllegalArgumentException("La masa de la unidad y la masa balística arrojable deben coincidir.");
        }
    }

    public ThrowingWeaponEffect effect() { return effect; }
    public ThrowProfile throwProfile() { return throwProfile; }
    public boolean recoverable() { return throwProfile.recoverable(); }
    public boolean supportsAiming() { return false; }
    public double throwIntervalSeconds() { return ThrowingCadencePolicy.intervalSeconds(this); }

    public ThrowingWeaponImpactResult resolveSpecialImpact(
            ThrowResult thrown,
            ArmorHitLocation hitLocation,
            EquipmentState targetEquipment,
            double targetStamina,
            ElementalHealthRegenerationPolicy healthRegeneration,
            HostileEncounterState encounter
    ) {
        return resolveSpecialImpact(thrown, hitLocation, targetEquipment, targetStamina,
                CombatTechniqueUnlockPolicy.DEFLECTION_DEXTERITY_REQUIREMENT, healthRegeneration, encounter);
    }

    public ThrowingWeaponImpactResult resolveSpecialImpact(
            ThrowResult thrown,
            ArmorHitLocation hitLocation,
            EquipmentState targetEquipment,
            double targetStamina,
            int attackerDexterity,
            ElementalHealthRegenerationPolicy healthRegeneration,
            HostileEncounterState encounter
    ) {
        Objects.requireNonNull(thrown, "El impacto físico no puede ser nulo.");
        Objects.requireNonNull(hitLocation, "La hitbox no puede ser nula.");
        Objects.requireNonNull(targetEquipment, "El equipamiento objetivo no puede ser nulo.");
        Objects.requireNonNull(healthRegeneration, "La política de PV REGEN no puede ser nula.");
        Objects.requireNonNull(encounter, "El encuentro no puede ser nulo.");
        if (targetStamina < 0 || !Double.isFinite(targetStamina)) {
            throw new IllegalArgumentException("Los PA actuales deben ser finitos y no negativos.");
        }

        double poison = 0;
        double burn = 0;
        boolean virulent = false;
        boolean suffocating = false;
        double staminaAfter = targetStamina;
        StaggerResult stagger = new StaggerResult(0, 0);
        boolean coup = false;
        double regenInhibitionSeconds = 0.0;
        double controlStunSeconds = new CombatTechniqueUnlockPolicy().controlStunDurationSeconds(attackerDexterity);

        switch (effect) {
            case AMMONIA_CAPSULE -> {
                poison = 100;
                virulent = true;
                stagger = new StaggerResult(0.0, controlStunSeconds);
                healthRegeneration.registerDirectDamage(DamageType.POISON, poison, encounter);
            }
            case INCENDIARY_TERRACOTTA -> {
                burn = 100;
                suffocating = true;
                stagger = new StaggerResult(0.0, controlStunSeconds);
                healthRegeneration.registerDirectDamage(DamageType.BURN, burn, encounter);
            }
            case PHOSPHORUS_SULFUR_EGG -> {
                staminaAfter = 0;
                stagger = new StaggerResult(0.0, controlStunSeconds);
                regenInhibitionSeconds = controlStunSeconds;
                healthRegeneration.inhibitForSeconds(controlStunSeconds);
            }
            case THROWING_KNIFE -> {
                double coveragePercent = targetEquipment.equippedArmor().stream()
                        .filter(a -> a.protects(ArmorHitLocation.HEAD))
                        .mapToDouble(a -> a.coverageRatio(ArmorHitLocation.HEAD) * 100.0).max().orElse(0.0);
                double piercingProtection = targetEquipment.equippedArmor().stream()
                        .filter(a -> a.protects(ArmorHitLocation.HEAD))
                        .mapToDouble(a -> a.currentProtection().piercing()).max().orElse(0.0);
                coup = CoupDeGracePolicy.isCoupDeGrace(hitLocation == ArmorHitLocation.HEAD, coveragePercent,
                        piercingProtection, throwProfile.lethalityProfile().map(p -> p.piercing()).orElse(0.0));
            }
        }

        return new ThrowingWeaponImpactResult(
                thrown.damage(), poison, burn, virulent, suffocating,
                targetStamina, staminaAfter, stagger, coup, regenInhibitionSeconds
        );
    }


    public boolean hasCoupDeGraceProperty() {
        return properties().stream().anyMatch(p -> p.id() == ItemPropertyId.COUP_DE_GRACE);
    }
}

package domain.character.sheet;

import domain.character.Gender;
import domain.environment.time.DayPhase;
import domain.inventory.InventoryState;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.AccessoryContext;
import domain.runic.EffectImmunity;
import domain.runic.EffectImmunitySet;
import domain.runic.RunicMarkId;
import domain.runic.RunicMarkActivityPolicy;
import domain.ability.NullificationPolicy;
import java.util.Set;
import domain.status.ActiveTherapeuticEffects;

import java.util.Objects;
import java.util.OptionalDouble;

public final class DerivedStatisticsCalculator {
    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, double currentLoadKg) {
        return calculate(sheet, gender, currentLoadKg, 1.0, 0.0, ResistanceModifierSet.none());
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, InventoryState inventory) {
        return calculate(sheet, gender, inventory, RunicStatisticsContext.day(), ResistanceModifierSet.none());
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, InventoryState inventory,
                                           ResistanceModifierSet resistanceModifiers) {
        return calculate(sheet, gender, inventory, RunicStatisticsContext.day(), resistanceModifiers);
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, InventoryState inventory,
                                           DayPhase dayPhase) {
        return calculate(sheet, gender, inventory, new RunicStatisticsContext(dayPhase), ResistanceModifierSet.none());
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, InventoryState inventory,
                                           RunicStatisticsContext context,
                                           ResistanceModifierSet resistanceModifiers) {
        Objects.requireNonNull(inventory, "El inventario no puede ser nulo.");
        Objects.requireNonNull(context, "El contexto estadístico no puede ser nulo.");
        Objects.requireNonNull(resistanceModifiers, "Los modificadores de resistencia no pueden ser nulos.");
        EquipmentState equipment = inventory.equipment();
        AccessoryContext accessoryContext = switch (context.dayPhase()) {
            case DAY -> AccessoryContext.day();
            case AFTERNOON -> AccessoryContext.afternoon();
            case NIGHT -> AccessoryContext.night();
        };
        return calculateInternal(sheet, gender, inventory.totalCarriedWeightKg(),
                equipment.healthRegenerationMultiplier(sheet),
                equipment.sanityBonus(sheet, accessoryContext),
                new ResistanceModifierSet(resistanceModifiers.characterClass(),
                        DamageResistanceProfile.combine(resistanceModifiers.equipment(), equipment.accessoryResistanceBonus(sheet)),
                        resistanceModifiers.status(), resistanceModifiers.species()),
                ActiveTherapeuticEffects.none(), equipment, context.dayPhase(), null, NullificationPolicy.SuppressionState.none());
    }

    /** Estadísticas con set rúnico excepcional (Doppelgänger) y posible supresión por ANULACIÓN. */
    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, InventoryState inventory,
                                           RunicStatisticsContext context, ResistanceModifierSet resistanceModifiers,
                                           Set<RunicMarkId> exceptionalActiveRunicMarks,
                                           NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(context); Objects.requireNonNull(resistanceModifiers);
        Objects.requireNonNull(suppression);
        EquipmentState equipment = inventory.equipment();
        AccessoryContext accessoryContext = switch (context.dayPhase()) {
            case DAY -> AccessoryContext.day();
            case AFTERNOON -> AccessoryContext.afternoon();
            case NIGHT -> AccessoryContext.night();
        };
        return calculateInternal(sheet, gender, inventory.totalCarriedWeightKg(),
                equipment.healthRegenerationMultiplier(sheet, suppression),
                equipment.sanityBonus(sheet, accessoryContext, suppression),
                new ResistanceModifierSet(resistanceModifiers.characterClass(),
                        DamageResistanceProfile.combine(resistanceModifiers.equipment(), equipment.accessoryResistanceBonus(sheet, suppression)),
                        resistanceModifiers.status(), resistanceModifiers.species()),
                ActiveTherapeuticEffects.none(), equipment, context.dayPhase(),
                exceptionalActiveRunicMarks == null ? Set.of() : Set.copyOf(exceptionalActiveRunicMarks), suppression);
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, double currentLoadKg,
                                            double healthRegenerationMultiplier, double sanityBonus,
                                            ResistanceModifierSet resistanceModifiers) {
        return calculateBase(sheet, gender, currentLoadKg, healthRegenerationMultiplier,
                sanityBonus, resistanceModifiers, ActiveTherapeuticEffects.none());
    }

    public CurrentCharacterStats calculate(CharacterSheet sheet, Gender gender, double currentLoadKg,
                                            double healthRegenerationMultiplier, double sanityBonus,
                                            ResistanceModifierSet resistanceModifiers,
                                            ActiveTherapeuticEffects therapeuticEffects) {
        return calculateBase(sheet, gender, currentLoadKg, healthRegenerationMultiplier,
                sanityBonus, resistanceModifiers, therapeuticEffects);
    }

    private CurrentCharacterStats calculateBase(CharacterSheet sheet, Gender gender, double currentLoadKg,
                                                double healthRegenerationMultiplier, double sanityBonus,
                                                ResistanceModifierSet resistanceModifiers,
                                                ActiveTherapeuticEffects therapeuticEffects) {
        return calculateInternal(sheet, gender, currentLoadKg, healthRegenerationMultiplier,
                sanityBonus, resistanceModifiers, therapeuticEffects, null, DayPhase.DAY, null, NullificationPolicy.SuppressionState.none());
    }

    private CurrentCharacterStats calculateInternal(CharacterSheet sheet, Gender gender, double currentLoadKg,
                                                    double healthRegenerationMultiplier, double sanityBonus,
                                                    ResistanceModifierSet resistanceModifiers,
                                                    ActiveTherapeuticEffects therapeuticEffects,
                                                    EquipmentState equipment, DayPhase dayPhase,
                                                    Set<RunicMarkId> exceptionalActiveRunicMarks,
                                                    NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        Objects.requireNonNull(resistanceModifiers, "Los modificadores de resistencia no pueden ser nulos.");
        Objects.requireNonNull(therapeuticEffects, "Los efectos terapéuticos no pueden ser nulos.");

        int vitality = sheet.valueOf(Attribute.VITALIDAD);
        int endurance = sheet.valueOf(Attribute.AGUANTE);
        int adaptability = sheet.valueOf(Attribute.ADAPTABILIDAD);
        int intelligence = sheet.valueOf(Attribute.INTELIGENCIA);
        int clairvoyance = sheet.valueOf(Attribute.CLARIVIDENCIA);

        EffectImmunitySet immunities = equipment == null ? EffectImmunitySet.none() : equipment.effectImmunities(sheet, suppression);
        boolean parhelio = RunicMarkActivityPolicy.active(RunicMarkId.PARHELIO, sheet, equipment, exceptionalActiveRunicMarks, suppression);
        boolean compassRose = RunicMarkActivityPolicy.active(RunicMarkId.ROSA_DE_LOS_VIENTOS, sheet, equipment, exceptionalActiveRunicMarks, suppression);
        boolean bindingVow = RunicMarkActivityPolicy.active(RunicMarkId.VOTO_VINCULANTE, sheet, equipment, exceptionalActiveRunicMarks, suppression);

        double runicHealthMultiplier = 1.0;
        double runicPhysicalStability = 0.0;
        double runicSanity = compassRose ? clairvoyance : 0.0;
        if (parhelio) {
            switch (dayPhase) {
                case DAY -> runicHealthMultiplier = 3.0;
                case AFTERNOON -> runicHealthMultiplier = 2.2;
                case NIGHT -> { /* : Parhelio nocturno actúa sobre el gasto de PA, no sobre estadísticas derivadas. */ }
            }
        }
        if (immunities.contains(EffectImmunity.HEALTH_REGEN_PENALTIES) && runicHealthMultiplier < 1.0) {
            runicHealthMultiplier = 1.0;
        }

        double totalHealth = vitality * 10.0;
        double healthRegeneration = Math.pow(totalHealth, 1.0 / 6.0)
                * healthRegenerationMultiplier
                * therapeuticEffects.healthRegenerationMultiplier()
                * runicHealthMultiplier;
        double maximumLoad = Math.min(endurance, gender == Gender.HOMBRE ? 40.0 : 30.0)
                * therapeuticEffects.carryingCapacityMultiplier();
        StaminaRecovery baseRecovery = staminaRecovery(endurance, currentLoadKg, maximumLoad,
                immunities.contains(EffectImmunity.STAMINA_REGEN_PENALTIES));
        StaminaRecovery staminaRecovery = new StaminaRecovery(
                baseRecovery.pointsPerSecond() * therapeuticEffects.staminaRegenerationMultiplier(),
                baseRecovery.fullRecoverySeconds(), baseRecovery.immobilized());
        DamageResistanceProfile adaptabilityResistances = resistanceProfileFromAdaptability(adaptability, gender);
        DamageResistanceProfile resolvedResistances = resistanceModifiers.applyTo(adaptabilityResistances);
        if (bindingVow) resolvedResistances = resolvedResistances.replaceFrenzy(0.0);
        if (immunities.contains(EffectImmunity.POISON)) resolvedResistances = resolvedResistances.replacePoison(100.0);
        if (immunities.contains(EffectImmunity.FRENZY)) resolvedResistances = resolvedResistances.replaceFrenzy(100.0);

        return new CurrentCharacterStats(OptionalDouble.of(totalHealth), OptionalDouble.of(healthRegeneration),
                OptionalDouble.of(Math.max(0, vitality + therapeuticEffects.physicalStabilityModifier() + runicPhysicalStability)),
                OptionalDouble.of(Math.max(0, intelligence + sanityBonus
                        + therapeuticEffects.sanityModifier() + runicSanity)),
                OptionalDouble.of(endurance), OptionalDouble.of(staminaRecovery.pointsPerSecond()),
                OptionalDouble.of(currentLoadKg), OptionalDouble.of(maximumLoad), resolvedResistances);
    }

    public StaminaRecovery staminaRecovery(double totalStamina, double currentLoadKg, double maximumLoadKg) {
        return staminaRecovery(totalStamina, currentLoadKg, maximumLoadKg, false);
    }

    public StaminaRecovery staminaRecovery(double totalStamina, double currentLoadKg, double maximumLoadKg,
                                           boolean immuneToPenalties) {
        if (maximumLoadKg <= 0) return new StaminaRecovery(0, 0, true);
        return new domain.combat.stamina.StaminaLoadRecoveryPolicy().resolve(totalStamina,currentLoadKg,maximumLoadKg,false);
    }

    public DamageResistanceProfile resistanceProfileFromAdaptability(int adaptability, Gender gender) {
        if (adaptability < 0) throw new IllegalArgumentException("La ADAPTABILIDAD no puede ser negativa.");
        Objects.requireNonNull(gender, "El género no puede ser nulo.");

        int ordinaryLevels = Math.min(adaptability, 75);
        int extraordinaryLevels = Math.max(0, adaptability - 75);
        double extraordinary = extraordinaryLevels * 0.7;

        double piercing = ordinaryLevels * (gender == Gender.HOMBRE ? 0.2 : 0.1) + extraordinary;
        double slashing = ordinaryLevels * (gender == Gender.HOMBRE ? 0.2 : 0.1) + extraordinary;
        double blunt = ordinaryLevels * (gender == Gender.HOMBRE ? 0.2 : 0.1) + extraordinary;
        double poison = ordinaryLevels * 0.1 + extraordinary;
        double burn = ordinaryLevels * 0.1 + extraordinary;
        double frost = ordinaryLevels * 0.1 + extraordinary;
        double curse = ordinaryLevels * (gender == Gender.MUJER ? 0.25 : 0.1) + extraordinary;
        double electricity = extraordinary;
        double frenzy = ordinaryLevels * (gender == Gender.MUJER ? 0.05 : 0.1) + extraordinary;

        return new DamageResistanceProfile(
                OptionalDouble.of(Math.min(100.0, piercing)), OptionalDouble.of(Math.min(100.0, slashing)),
                OptionalDouble.of(Math.min(100.0, blunt)), OptionalDouble.of(Math.min(100.0, poison)),
                OptionalDouble.of(Math.min(100.0, burn)), OptionalDouble.of(Math.min(100.0, frost)),
                OptionalDouble.of(Math.min(100.0, curse)), OptionalDouble.of(Math.min(100.0, electricity)),
                OptionalDouble.of(Math.min(100.0, frenzy)));
    }

}

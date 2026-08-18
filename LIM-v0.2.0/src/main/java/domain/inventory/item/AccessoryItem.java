package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.runic.EffectImmunitySet;

import java.util.List;
import java.util.Objects;

public class AccessoryItem extends InventoryEntry {
    private final List<AccessoryEffect> effects;
    private final EffectImmunitySet immunities;

    public AccessoryItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            List<String> statistics,
            List<ItemProperty> properties,
            List<AccessoryEffect> effects
    ) {
        this(name, narrativeDescription, weightKg, footprint, statistics, properties, effects, EffectImmunitySet.none());
    }

    public AccessoryItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            List<String> statistics,
            List<ItemProperty> properties,
            List<AccessoryEffect> effects,
            EffectImmunitySet immunities
    ) {
        super(name, narrativeDescription, weightKg, footprint, statistics, properties);
        Objects.requireNonNull(effects, "Los efectos del abalorio no pueden ser nulos.");
        if (effects.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Los efectos del abalorio no pueden contener valores nulos.");
        }
        this.effects = List.copyOf(effects);
        this.immunities = Objects.requireNonNull(immunities, "Las inmunidades del abalorio no pueden ser nulas.");
    }

    /* Constructores heredados para conservar compatibilidad con las iteraciones previas. */
    public AccessoryItem(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                         double healthRegenerationMultiplier, Attribute conditionalAttribute, int conditionalMinimum,
                         double conditionalSanityBonus, List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, healthRegenerationMultiplier, conditionalAttribute,
                conditionalMinimum, conditionalSanityBonus,
                SanityBonusPolicy.conditionalFixed(conditionalAttribute, conditionalMinimum,
                        conditionalSanityBonus), false, statistics, properties, EffectImmunitySet.none());
    }

    public AccessoryItem(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                         double healthRegenerationMultiplier, Attribute conditionalAttribute, int conditionalMinimum,
                         double conditionalSanityBonus, SanityBonusPolicy policy, boolean embodiedAnchor,
                         List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, healthRegenerationMultiplier, conditionalAttribute,
                conditionalMinimum, conditionalSanityBonus, policy, embodiedAnchor, statistics, properties,
                EffectImmunitySet.none());
    }

    public AccessoryItem(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                         double healthRegenerationMultiplier, Attribute conditionalAttribute, int conditionalMinimum,
                         double conditionalSanityBonus, List<String> statistics, List<ItemProperty> properties,
                         EffectImmunitySet immunities) {
        this(name, narrativeDescription, weightKg, footprint, healthRegenerationMultiplier, conditionalAttribute,
                conditionalMinimum, conditionalSanityBonus,
                SanityBonusPolicy.conditionalFixed(conditionalAttribute, conditionalMinimum,
                        conditionalSanityBonus), false, statistics, properties, immunities);
    }

    public AccessoryItem(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                         double healthRegenerationMultiplier, Attribute conditionalAttribute, int conditionalMinimum,
                         double conditionalSanityBonus, SanityBonusPolicy policy, boolean embodiedAnchor,
                         List<String> statistics, List<ItemProperty> properties, EffectImmunitySet immunities) {
        this(name, narrativeDescription, weightKg, footprint, statistics, properties,
                effectsFromConstructorArguments(healthRegenerationMultiplier, conditionalAttribute, conditionalMinimum,
                        conditionalSanityBonus, policy, embodiedAnchor), immunities);
    }

    private static List<AccessoryEffect> effectsFromConstructorArguments(double healthMultiplier, Attribute attribute, int minimum,
                                                       double mentalBonus, SanityBonusPolicy policy,
                                                       boolean embodiedAnchor) {
        if (healthMultiplier <= 0) throw new IllegalArgumentException("El multiplicador de regeneración debe ser positivo.");
        Objects.requireNonNull(attribute, "El atributo condicional no puede ser nulo.");
        if (minimum < 1 || minimum > 120) throw new IllegalArgumentException("El umbral condicional debe estar entre 1 y 120.");
        java.util.ArrayList<AccessoryEffect> result = new java.util.ArrayList<>();
        if (Math.abs(healthMultiplier - 1.0) > 1e-9) {
            result.add(AccessoryEffect.always("EFECTO HEREDADO", AccessoryEffectType.HEALTH_REGENERATION_MULTIPLIER,
                    healthMultiplier));
        }
        if (embodiedAnchor) {
            result.add(AccessoryEffect.always("FRÍO LUNAR", AccessoryEffectType.INTERSTICE_SANITY_BONUS, 0));
        } else {
            result.add(new AccessoryEffect("EFECTO HEREDADO", AccessoryEffectType.SANITY_BONUS, null,
                    mentalBonus, false, attribute, minimum));
        }
        if (embodiedAnchor) {
            result.add(AccessoryEffect.hidden("ÁNCORA ENCARNADA", AccessoryEffectType.VEIL_RIFT_NAVIGATION,
                    Attribute.CLARIVIDENCIA, 33, 1));
        }
        return result;
    }

    public List<AccessoryEffect> effects() { return effects; }

    public Attribute conditionalAttribute() {
        return effects.stream().map(AccessoryEffect::activationAttribute).filter(Objects::nonNull)
                .findFirst().orElse(Attribute.FE);
    }

    public int conditionalMinimum() {
        return effects.stream().filter(effect -> effect.activationAttribute() != null)
                .mapToInt(AccessoryEffect::activationMinimum).findFirst().orElse(1);
    }

    public double conditionalSanityBonus() {
        return effects.stream().filter(effect -> effect.type() == AccessoryEffectType.SANITY_BONUS)
                .mapToDouble(AccessoryEffect::amount).findFirst().orElse(0.0);
    }

    public double healthRegenerationMultiplier() {
        return effects.stream()
                .filter(effect -> effect.type() == AccessoryEffectType.HEALTH_REGENERATION_MULTIPLIER)
                .mapToDouble(AccessoryEffect::amount)
                .reduce(1.0, (left, right) -> left * right);
    }

    public double healthRegenerationMultiplier(CharacterSheet sheet) {
        return effects.stream()
                .filter(effect -> effect.type() == AccessoryEffectType.HEALTH_REGENERATION_MULTIPLIER)
                .filter(effect -> effect.isActiveFor(sheet))
                .mapToDouble(AccessoryEffect::amount)
                .reduce(1.0, (left, right) -> left * right);
    }

    public double sanityBonus(CharacterSheet sheet, AccessoryContext context) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(context, "El contexto del abalorio no puede ser nulo.");
        double fixed = effects.stream()
                .filter(effect -> effect.type() == AccessoryEffectType.SANITY_BONUS)
                .filter(effect -> effect.isActiveFor(sheet))
                .mapToDouble(AccessoryEffect::amount).sum();
        double interstice = context.inInterstice()
                ? effects.stream()
                        .filter(effect -> effect.type() == AccessoryEffectType.INTERSTICE_SANITY_BONUS)
                        .filter(effect -> effect.isActiveFor(sheet))
                        .mapToDouble(AccessoryEffect::amount).sum()
                : 0.0;
        double night = context.dayPhase() == DayPhase.NIGHT
                ? effects.stream()
                        .filter(effect -> effect.type() == AccessoryEffectType.NIGHT_SANITY_BONUS)
                        .filter(effect -> effect.isActiveFor(sheet))
                        .mapToDouble(AccessoryEffect::amount).sum()
                : 0.0;
        return fixed + interstice + night;
    }

    public double attributeBonus(Attribute attribute, CharacterSheet sheet) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        return effects.stream()
                .filter(effect -> effect.type() == AccessoryEffectType.ATTRIBUTE_BONUS)
                .filter(effect -> effect.affectedAttribute() == attribute)
                .filter(effect -> effect.isActiveFor(sheet))
                .mapToDouble(AccessoryEffect::amount).sum();
    }

    public double allResistancesBonus(CharacterSheet sheet) {
        return effects.stream()
                .filter(effect -> effect.type() == AccessoryEffectType.ALL_RESISTANCES_BONUS)
                .filter(effect -> effect.isActiveFor(sheet))
                .mapToDouble(AccessoryEffect::amount).sum();
    }

    public EffectImmunitySet immunities() { return immunities; }

    public EffectImmunitySet activeImmunities(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return sheet.valueOf(conditionalAttribute()) >= conditionalMinimum()
                ? immunities : EffectImmunitySet.none();
    }

    public boolean enablesVeilRiftNavigation(CharacterSheet sheet) {
        return effects.stream().anyMatch(effect -> effect.type() == AccessoryEffectType.VEIL_RIFT_NAVIGATION
                && effect.isActiveFor(sheet));
    }
}

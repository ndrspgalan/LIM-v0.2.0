package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.status.TherapeuticEffectProfile;
import java.util.List;
import java.util.Objects;

public final class TherapeuticItem extends StackableMiscellaneousItem {
    private final SurvivalConsumptionEffect survivalEffect;
    private final TherapeuticEffectProfile therapeuticEffect;

    public TherapeuticItem(String name, String description, MiscellaneousCategory category, int quantity,
            int maximumStack, double structuralWeightKg, double contentWeightPerUseKg,
            InventoryFootprint footprint, double seconds, List<String> animationSteps,
            List<String> statistics, List<ItemProperty> properties, TherapeuticEffectProfile therapeuticEffect) {
        this(name, description, category, quantity, maximumStack, structuralWeightKg, contentWeightPerUseKg,
                footprint, seconds, animationSteps, statistics, properties,
                SurvivalConsumptionEffect.none(), therapeuticEffect);
    }

    public TherapeuticItem(String name, String description, MiscellaneousCategory category, int quantity,
            int maximumStack, double structuralWeightKg, double contentWeightPerUseKg,
            InventoryFootprint footprint, double seconds, List<String> animationSteps,
            List<String> statistics, List<ItemProperty> properties, SurvivalConsumptionEffect survivalEffect,
            TherapeuticEffectProfile therapeuticEffect) {
        super(name, description, category, quantity, maximumStack, structuralWeightKg, contentWeightPerUseKg,
                UseResourceKind.CHARGES, footprint, new UseAnimation(seconds, animationSteps), statistics, properties);
        this.survivalEffect = Objects.requireNonNull(survivalEffect, "El efecto de supervivencia no puede ser nulo.");
        this.therapeuticEffect = Objects.requireNonNull(therapeuticEffect, "El efecto terapéutico no puede ser nulo.");
    }

    public SurvivalConsumptionEffect survivalEffect() { return survivalEffect; }
    public TherapeuticEffectProfile therapeuticEffect() { return therapeuticEffect; }
}

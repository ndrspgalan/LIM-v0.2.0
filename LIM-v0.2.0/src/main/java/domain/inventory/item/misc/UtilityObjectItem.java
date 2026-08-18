package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.throwing.ThrowProfile;
import domain.inventory.item.ItemProperty;

public final class UtilityObjectItem extends StackableMiscellaneousItem {
    private final List<UtilityAction> actions;
    private final Optional<ThrowProfile> throwProfile;

    public UtilityObjectItem(String name, String description, int currentUses, int maximumUses,
            double structuralWeight, double perUseWeight, UseResourceKind kind,
            InventoryFootprint footprint, UseAnimation useAnimation, List<UtilityAction> actions) {
        this(name, description, currentUses, maximumUses, structuralWeight, perUseWeight, kind,
                footprint, useAnimation, actions, Optional.empty());
    }

    public UtilityObjectItem(String name, String description, int currentUses, int maximumUses,
            double structuralWeight, double perUseWeight, UseResourceKind kind,
            InventoryFootprint footprint, UseAnimation useAnimation, List<UtilityAction> actions,
            Optional<ThrowProfile> throwProfile) {
        this(name, description, currentUses, maximumUses, structuralWeight, perUseWeight, kind, footprint,
                useAnimation, actions, throwProfile, List.of());
    }

    public UtilityObjectItem(String name, String description, int currentUses, int maximumUses,
            double structuralWeight, double perUseWeight, UseResourceKind kind,
            InventoryFootprint footprint, UseAnimation useAnimation, List<UtilityAction> actions,
            Optional<ThrowProfile> throwProfile, List<ItemProperty> properties) {
        super(name, description, MiscellaneousCategory.OBJECT, currentUses, maximumUses,
                structuralWeight, perUseWeight, kind, footprint, Objects.requireNonNull(useAnimation),
                List.of(), properties);
        this.actions = List.copyOf(Objects.requireNonNull(actions, "Las acciones no pueden ser nulas."));
        this.throwProfile = Objects.requireNonNull(throwProfile, "El perfil de lanzamiento opcional no puede ser nulo.");
        if (this.throwProfile.isPresent() && !this.actions.contains(UtilityAction.THROW)) {
            throw new IllegalArgumentException("Un objeto con perfil de lanzamiento debe admitir la acción THROW.");
        }
    }

    public List<UtilityAction> actions() { return actions; }
    public Optional<ThrowProfile> throwProfile() { return throwProfile; }
    /** Los objetos arrojadizos improvisados tampoco utilizan AIMING. */
    public boolean supportsAiming() { return false; }
}

package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/** linterna mecánica autoalimentada que debe accionarse desde Quick CHEST. */
public final class MechanicalLampItem extends InventoryEntry {
    public enum Mechanism { PULL_CORD, SQUEEZE_DYNAMO }
    private final Mechanism mechanism;
    private final double illuminationMeters;
    private final double lightSecondsPerAction;
    private final UseAnimation useAnimation;

    public MechanicalLampItem(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                              Mechanism mechanism, double illuminationMeters, double lightSecondsPerAction,
                              UseAnimation useAnimation) {
        super(name, narrativeDescription, weightKg, footprint,
                List.of("ILUMINACIÓN | " + illuminationMeters + " m", "LUZ | Amarilla", "MONTAJE | Coraza / Quick CHEST"));
        if (illuminationMeters <= 0 || lightSecondsPerAction < 0) throw new IllegalArgumentException("Perfil luminoso inválido.");
        this.mechanism = java.util.Objects.requireNonNull(mechanism);
        this.illuminationMeters = illuminationMeters;
        this.lightSecondsPerAction = lightSecondsPerAction;
        this.useAnimation = java.util.Objects.requireNonNull(useAnimation);
    }
    public Mechanism mechanism(){ return mechanism; }
    public double illuminationMeters(){ return illuminationMeters; }
    /** 0 significa que sólo permanece encendida mientras se acciona mecánicamente. */
    public double lightSecondsPerAction(){ return lightSecondsPerAction; }
    public UseAnimation useAnimation(){ return useAnimation; }
}

package domain.inventory.item.misc;

import domain.character.sheet.Attribute;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.AccessoryEffect;
import domain.inventory.item.AccessoryEffectType;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;

import java.util.List;

/**  — el Astrolabio pasa de MISC a ACCESSORY y se convierte en artefacto activable. */
public final class AstrolabeItem extends AccessoryItem implements ArtifactAccessory {
    public static final double WEIGHT_KG = 0.9;
    public static final InventoryFootprint FOOTPRINT = new InventoryFootprint(2, 2);
    public static final int CLARITY_REQUIREMENT = 22;
    public static final String DESCRIPTION = "Instrumento V881 de orientación construido con anillos graduados, agujas y discos móviles de latón. Su adaptación moderna interpreta las referencias registradas en la Memoria del Mundo y señala con precisión la dirección del destino seleccionado. No revela el camino: únicamente indica hacia dónde avanzar.";

    public AstrolabeItem() {
        super("Astrolabio", DESCRIPTION, WEIGHT_KG, FOOTPRINT,
                List.of(),
                List.of(ItemProperty.hiddenWithHiddenRequirement(ItemPropertyId.GENERIC, "ARTEFACTO DE ORIENTACIÓN",
                        "Los anillos y referencias del instrumento sólo pueden interpretarse plenamente con CLARIVIDENCIA 22.",
                        Attribute.CLARIVIDENCIA, CLARITY_REQUIREMENT, "ACTIVACIÓN | E")),
                List.of(AccessoryEffect.hidden("ACTIVACIÓN DEL ASTROLABIO", AccessoryEffectType.ARTIFACT_ACTIVATION,
                        Attribute.CLARIVIDENCIA, CLARITY_REQUIREMENT, 1)));
    }

    @Override public String artifactId() { return "ASTROLABE"; }
    @Override public Attribute activationAttribute() { return Attribute.CLARIVIDENCIA; }
    @Override public int activationMinimum() { return CLARITY_REQUIREMENT; }
}

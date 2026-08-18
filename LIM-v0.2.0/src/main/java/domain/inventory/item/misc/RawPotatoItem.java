package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/**
 * Materia prima agrícola . No implementa FoodItem ni política de consumo:
 * Kenan no puede comerla cruda; su uso canónico aquí es material de fermentación para etanol.
 */
public final class RawPotatoItem extends InventoryEntry {
    public static final double UNIT_WEIGHT_KG = 0.180;

    public RawPotatoItem() {
        super("Patata cruda",
                "Tubérculo recién cosechado, todavía crudo y sin preparación alimentaria. Su pulpa rica en almidón permite convertirla en materia fermentable después de triturarla y tratarla, pero en este estado no se ofrece como consumible: Kenan la conserva como materia prima cuando necesita producir combustible por sus propios medios.",
                UNIT_WEIGHT_KG, new InventoryFootprint(1,1),
                List.of("ESTADO | Cruda · no comestible", "MASA | 0,180 kg por unidad",
                        "USO | Materia prima para etanol", "TAMAÑO | 1x1"));
    }
}

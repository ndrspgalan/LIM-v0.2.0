package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;

import java.util.List;

/**
 * Agregado físico persistente de mucus blanco transpuesto.
 * Cada mL añadido se fusiona con la misma Lágrima, aumenta 1 uso y añade 1 g.
 * No representa un stack de objetos independientes.
 */
public final class MucusTearItem extends StackableMiscellaneousItem {
    public static final int MAXIMUM_STACK = 100;
    public static final int MAXIMUM_AGGREGATE_USES = 100;
    public static final double UNIT_WEIGHT_KG = 0.001;

    public static final String NARRATIVE_DESCRIPTION =
            "Ninguna superficie viva permanece expuesta durante mucho tiempo. Allí donde el cuerpo entra en contacto con el mundo, " +
            "el mucus aparece formando películas invisibles capaces de preservar aquello que, de otro modo, terminaría por desgastarse. " +
            "La transposición concentra esa misma capacidad protectora en una única lágrima cristalina. Cuando su contenido se extiende " +
            "sobre el filo de un arma, la materia deja de comportarse como un metal desnudo y adquiere, durante un tiempo, la facultad " +
            "de transportar una memoria que nunca perteneció al acero.";

    public static final String FORM_DESCRIPTION =
            "Lágrima translúcida de contorno orgánico y superficie lisa. En su interior permanece suspendida una película blanquecina " +
            "que se desplaza con lentitud al inclinarla, como una capa lagrimal separada de toda superficie viva.";

    public MucusTearItem(int quantity) {
        super(
                "Lágrima de Mucus Blanco",
                NARRATIVE_DESCRIPTION + " La secreción refleja puede multiplicar transitoriamente la producción basal hasta unas cien veces; por eso una Lágrima transpuesta nunca supera 100 mL. Al saturarse, cualquier mucus blanco adicional nuclea automáticamente otra Lágrima independiente.",
                MiscellaneousCategory.OBJECT,
                quantity,
                MAXIMUM_AGGREGATE_USES,
                UNIT_WEIGHT_KG,
                new InventoryFootprint(1, 1),
                new UseAnimation(1.0, List.of("Extender una lágrima sobre el arma", "Dejar que la película maldita se adhiera")),
                List.of("FORMA | " + FORM_DESCRIPTION,
                        "MUCUS TRANSPUESTO | " + quantity + " mL · " + quantity + " usos",
                        "USO | 1 Lágrima recubre por completo un arma cortante",
                        "RECUBRIMIENTO | Energía Maldita = contundencia vigente del perfil de impacto"),
                List.<ItemProperty>of()
        );
    }

    @Override public double weightKg() { return currentUses()*UNIT_WEIGHT_KG; }

    @Override public domain.inventory.logistics.InventoryPhysicalDimensions physicalDimensions() {
        // 1 uso = 1 mL = 1 cm³. Envolvente elipsoidal orgánica D = 1,6k × 1,0k × 0,7k.
        double volumeCm3=Math.max(1,currentUses());
        double kCm=Math.cbrt(volumeCm3 / ((4.0/3.0)*Math.PI*0.8*0.5*0.35));
        return domain.inventory.logistics.InventoryPhysicalDimensions.fromMetricDimensions(
                1.6*kCm/100.0, kCm/100.0, 0.7*kCm/100.0);
    }

    @Override public domain.inventory.InventoryFootprint footprint() {
        return domain.inventory.logistics.InventoryVolumeProjectionPolicy.footprint(physicalDimensions());
    }

    public String formDescription() { return FORM_DESCRIPTION; }
}

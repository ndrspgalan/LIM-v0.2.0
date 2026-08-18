package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

public final class CoolantBottleItem extends RepairResourceContainer {
    public CoolantBottleItem(int currentUses) {
        super("Botella de Líquido Refrigerante",
                "Una botella de dos litros destinada a la mezcla técnica de agua destilada y etanol que mantiene operativo el Compuesto Electromecánico. Para producir un uso se necesita un odre con al menos un uso de agua, hidromiel con al menos un uso como fuente de etanol y la Maletín profesional de Alicia e Iván, cuyo instrumental permite destilar, medir y estabilizar la mezcla antes de introducirla en el circuito.",
                currentUses, 5, 0.30, 0.34, new InventoryFootprint(2,3),
                new UseAnimation(10.0, List.of("Retirar el conjunto equipado", "Abrir el circuito electromecánico", "Reponer el líquido refrigerante", "Cerrar y volver a equipar")),
                List.of("Capacidad | 5 usos", "Volumen | 2 litros", "Composición | Agua destilada y etanol",
                        "MANUFACTURA | 1 uso de Odre + 120 mL de Petaca de hidromiel + Maletín profesional de Alicia e Iván -> 1 uso",
                        "FUNCIÓN | Refrigeración, purga y estabilización del Compuesto Electromecánico"));
    }
}

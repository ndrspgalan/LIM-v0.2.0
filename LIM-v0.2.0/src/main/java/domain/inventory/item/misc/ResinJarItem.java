package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

public final class ResinJarItem extends RepairResourceContainer {
    public ResinJarItem(int currentUses) {
        super("Tarro de Resina",
                "Un tarro robusto preparado para conservar resina útil en el mantenimiento de arcos, ballestas y otros mecanismos de madera. Puede adquirirse con facilidad en poblaciones y puestos de camino, aunque su precio no es barato; por eso suele resultar más rentable rellenarlo extrayendo resina directamente de los árboles.",
                currentUses, 3, 0.22, 0.09, new InventoryFootprint(2,2),
                new UseAnimation(7.0, List.of("Retirar el arma equipada", "Aplicar la resina", "Reajustar la tensión y volver a equiparla")),
                List.of("Capacidad | 3 usos", "Recargable | Sí", "Reparación | Armas a distancia degradables"));
    }
}

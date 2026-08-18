package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/** Herramienta persistente  para producir etanol cuando no hay acceso comercial a queroseno. */
public final class ImprovisedFuelConverterItem extends InventoryEntry {
    public ImprovisedFuelConverterItem() {
        super("Conversor de combustible improvisado",
                "Conjunto portátil ensamblado con un triturador manual, recipiente de fermentación sellable, serpentín de condensación, cámara de calentamiento y racores recuperados. No produce combustible de la nada: permite sacar partido a materias amiláceas accesibles cuando Kenan no puede comprar queroseno ligero o no conoce a nadie que lo venda. Las patatas se trituran, su almidón se vuelve fermentable y el alcohol obtenido se concentra por destilación hasta una carga utilizable como etanol. El calificativo improvisado describe su construcción fuera de una instalación industrial; sigue siendo una herramienta material, pesada y reutilizable.",
                4.80, new InventoryFootprint(5,4),
                List.of("USOS | Ilimitados", "PROCESO | Trituración · fermentación · destilación",
                        "ENTRADA CANÓNICA | Patata cruda", "SALIDA CANÓNICA | Etanol",
                        "FUNCIÓN | Alternativa persistente cuando el queroseno comercial no está disponible",
                        "TAMAÑO | 5x4"));
    }
}

package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;

import java.util.List;

/**  — instrumento óptico portátil de observación, telemetría y marcado espacial. */
public final class ReconnaissanceMonocularItem extends InventoryEntry {
    public static final double WEIGHT_KG = 0.48;
    public static final InventoryFootprint FOOTPRINT = new InventoryFootprint(2, 1);
    public static final int STACK_MAX = 1;
    public static final double BASE_OBSERVATION_RANGE_METERS = 250.0;

    public static final String DESCRIPTION = """
El Monocular de Reconocimiento V881 apareció cuando el aumento del alcance efectivo de las armas dejó de estar limitado únicamente por la capacidad de alcanzar un blanco. La pólvora sin humo, la balística experimental, los nuevos instrumentos de medición y las ópticas de precisión habían permitido disparar a distancias en las que identificar correctamente aquello sobre lo que se disparaba empezaba a convertirse en un problema independiente.

Los primeros intentos consistieron en trasladar ópticas de arma al observador. El resultado era innecesariamente pesado, obligaba a mantener geometrías diseñadas para soportar retroceso y mezclaba dos funciones distintas: observar y disparar. Valerian terminó desarrollando un instrumento específico. Un cuerpo telescópico compacto reunía aumento óptico, enfoque y estimación de distancia sin necesidad de permanecer unido a ningún arma.

Su utilidad acabó extendiéndose más allá de los tiradores. Exploradores, aeronautas, ingenieros y unidades de reconocimiento podían examinar terreno, distinguir equipamiento y estimar distancias antes de comprometerse con una aproximación. El instrumento no revelaba aquello que el observador no pudiera conocer: ampliaba y medía lo que ya estaba delante de él.

El Monocular de Reconocimiento V881 representa así una consecuencia indirecta de la misma infraestructura científica que transformó el armamento valeriano. Cuando las armas aprendieron a alcanzar más lejos, la observación tuvo que aprender a hacerlo antes.
""";

    public ReconnaissanceMonocularItem() {
        super("Monocular de Reconocimiento V881", DESCRIPTION, WEIGHT_KG, FOOTPRINT, List.of(
                "STACK MÁXIMO | 1",
                "OBSERVACIÓN | ×3 / ×4 / ×5",
                "ALCANCE BASE DE OBSERVACIÓN | 250 m",
                "TELEMETRÍA | TIEMPO REAL",
                "MARCA DE OBSERVACIÓN | MÁXIMO 1 · MEMORIA DEL MUNDO",
                "USO | ACCESO RÁPIDO · ENVAINA EL ARMA ACTIVA"
        ));
    }
}

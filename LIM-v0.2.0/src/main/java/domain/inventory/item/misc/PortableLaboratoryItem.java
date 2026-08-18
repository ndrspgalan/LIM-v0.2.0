package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/**
 * Instrumental persistente único de Alicia e Iván.
 * No aporta materia prima: habilita diagnosis, manufactura, calibración y reparación especializada.
 */
public final class PortableLaboratoryItem extends InventoryEntry {
    public PortableLaboratoryItem() {
        super("Maletín profesional de Alicia e Iván",
                "Maletín rígido de taller y laboratorio concebido para intervenir sobre mecanismos que una caja de herramientas ordinaria sólo podría desmontar. Su interior está distribuido en bandejas amortiguadas y alojamientos mecanizados para que cada útil conserve una posición fija durante el transporte. La sección de Alicia reúne micrómetros, calibres, galgas de espesores y roscas, llaves de precisión, extractores, pequeñas mordazas, punzones, botadores, matrices de remachado, terrajas, machos, útiles de engaste, útiles para racores y conducciones hidráulicas, manómetros, una bomba manual de purga, jeringas técnicas, depósitos graduados, herramientas para válvulas, retenes, muelles y servomecanismos, lubricadores, cortadores, limas finas y repuestos normalizados de fijación. La sección de Iván contiene comprobadores de continuidad y aislamiento, puentes de medida, devanadores y útiles de bobinado, terminales, cableado, fusibles, casquillos dieléctricos, soldadura y estañado de precisión, lentes de inspección, matraces, probetas, pipetas, pequeños alambiques desmontables, filtros, morteros, reactivos de limpieza y desoxidación y recipientes estancos para mezclar o estabilizar fluidos. Entre ambas secciones existe una bandeja común de interfaces: racores, juntas, sellos, tornillería, conectores, segmentos de conducción flexible y útiles de alineación que permiten convertir materiales canónicos separados en un módulo verificable. El maletín no fabrica materia de la nada y no sustituye una fragua, un laminador ni una reserva de material: permite cortar, ajustar, unir, medir, aislar, purgar, destilar, recalibrar y comprobar piezas ya disponibles. Por eso es requisito para fabricar Compuesto Electromecánico, para preparar Líquido Refrigerante y para reparar el Conjunto del Ingeniero. Alicia construyó el principio corporal y portante de ese conjunto para sí misma; Iván convirtió su taller improvisado en un procedimiento reproducible de instrumentación y control. El resultado conserva la historia de ambos, pero su valor funcional no depende de conocerla: cualquier operario capaz de comprender el instrumental puede reconstruir paso a paso qué debe medir antes de cerrar un circuito.",
                12.0, new InventoryFootprint(6,4),
                List.of(
                        "USOS | Ilimitados",
                        "CLASE | Laboratorio portátil de precisión · reparación y manufactura especializada",
                        "INSTRUMENTAL MECÁNICO | Micrómetros · calibres · galgas · extractores · matrices · terrajas · machos · mordazas · punzones",
                        "HIDROMECÁNICA | Racores · manómetros · bomba de purga · jeringas técnicas · válvulas · retenes · juntas",
                        "ELECTRICIDAD | Continuidad · aislamiento · bobinado · terminales · fusibles · casquillos dieléctricos · soldadura",
                        "LABORATORIO | Matraces · probetas · pipetas · alambiques · filtros · morteros · reactivos",
                        "MANUFACTURA | Habilita Compuesto Electromecánico",
                        "FLUIDOS | Habilita Líquido Refrigerante",
                        "REPARACIÓN | Requisito del Conjunto del Ingeniero y de materiales V881 de manufactura intrincada",
                        "OBJETO ÚNICO | Alicia e Iván"
                ));
    }
}

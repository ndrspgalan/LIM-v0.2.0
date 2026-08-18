package domain.inventory.item.accessory;

import domain.inventory.item.AccessoryItem;
import domain.inventory.item.misc.AstrolabeItem;
import java.util.List;

/**  — artefactos activables canónicos. */
public final class ArtifactAccessoryCatalog {
 private ArtifactAccessoryCatalog(){}
 public static AccessoryItem astrolabe(){return new AstrolabeItem();}
 public static AccessoryItem tokkosho(){return a("TOKKOSHO_V881","Tokkosho V881","Tokkosho de bronce y aleación conductora con una punta en cada extremo. Su geometría axial encierra captación, aislamiento y recomposición de campo electroatmosférico; para quien no comprende esa arquitectura sigue siendo una pieza extraordinariamente trabajada.",0.42,2,1);}
 public static AccessoryItem heliograph(){return a("HELIOGRAPH_V881","Espejo heliográfico V881","Espejo militar de señalización montado sobre una articulación de precisión. Sus superficies y marcas recuerdan campañas en las que una línea de visión podía sostener un frente sin tender un cable.",0.55,2,2);}
 public static AccessoryItem tuningFork(){return a("RESONANT_TUNING_FORK_V881","Diapasón resonante V881","Diapasón de laboratorio construido para excitar respuestas mecánicas extremadamente estrechas. Las pequeñas muescas de calibración documentan décadas de trabajo sobre materia, ocultación y resonancia.",0.31,2,1);}
 public static AccessoryItem seismoscope(){return a("SEISMOSCOPE_V881","Sismoscopio V881","Sensor portátil de vibraciones con masa suspendida y lectura mecánica amplificada. Fue diseñado para escuchar estructuras antes que conversaciones.",0.85,2,2);}
 public static AccessoryItem nocturlabe(){return a("NOCTURLABE_V881","Nocturlabio V881","Instrumento nocturno de aros, índices y referencias astronómicas. Conserva la tradición de fechar la noche mediante el cielo y la combina con reconstrucción local de evidencia temporal.",0.62,2,2);}
 private static AccessoryItem a(String id,String n,String d,double w,int v,int h){return new V881ArtifactAccessoryItem(id,n,d,w,v,h);}
 public static List<AccessoryItem> all(){return List.of(astrolabe(),tokkosho(),heliograph(),tuningFork(),seismoscope(),nocturlabe());}
}

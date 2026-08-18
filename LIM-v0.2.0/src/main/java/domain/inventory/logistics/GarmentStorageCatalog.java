package domain.inventory.logistics;

import domain.inventory.item.armor.ArmorInventoryCategory;
import domain.inventory.item.armor.ArmorPiece;
import java.util.*;

/** canon cuantitativo de las 38 prendas que superaron la auditoría de inventariabilidad. */
public final class GarmentStorageCatalog {
    private static final Map<String, GarmentStorageProfile> PROFILES = build();
    private GarmentStorageCatalog() {}

    public static Optional<GarmentStorageProfile> profileFor(ArmorPiece piece) {
        Objects.requireNonNull(piece);
        return Optional.ofNullable(PROFILES.get(piece.name()));
    }
    public static Optional<GarmentStorageProfile> profileForName(String name) { return Optional.ofNullable(PROFILES.get(name)); }
    public static Set<String> inventoryGarmentNames() { return PROFILES.keySet(); }
    public static int profileCount() { return PROFILES.size(); }

    private static Map<String, GarmentStorageProfile> build() {
        LinkedHashMap<String, GarmentStorageProfile> m = new LinkedHashMap<>();
        // CHEST LIGHT: bolsillos sastreados/laborales. Z=1 mantiene la ropa por debajo de contenedores dedicados profundos.
        chest(m,"Camisa de trabajo V881", mod("Bolsillo pectoral izquierdo",2,2), mod("Bolsillo pectoral derecho",2,2)); // 8
        chest(m,"Chaleco V881", mod("Bolsillos inferiores",2,2), mod("Bolsillos superiores",2,2)); // 8
        chest(m,"Chaleco largo V881", mod("Bolsillo inferior izquierdo",2,3), mod("Bolsillo inferior derecho",2,3)); // 12
        chest(m,"Chaleco de trabajo V881", mod("Bolsillo herramienta izquierdo",2,3), mod("Bolsillo herramienta derecho",2,3), mod("Bolsillos pectorales",2,2)); // 16
        chest(m,"Chaleco de montar V881", mod("Bolsillo delantero izquierdo",2,2), mod("Bolsillo delantero derecho",2,2)); // 8
        chest(m,"Levita V881", mod("Bolsillo interior",2,2), mod("Bolsillo exterior izquierdo",2,2), mod("Bolsillo exterior derecho",2,2)); // 12
        chest(m,"Frac V881", mod("Bolsillos de faldón",2,2), mod("Bolsillos interiores",2,2)); // 8
        chest(m,"Chaqué V881", mod("Bolsillos de faldón",2,2), mod("Bolsillos interiores",2,2)); // 8
        chest(m,"Americana V881", mod("Bolsillo interior",2,2), mod("Bolsillo exterior izquierdo",2,2), mod("Bolsillo exterior derecho",2,2)); // 12
        chest(m,"Chaqueta Norfolk V881", mod("Bolsillo parche izquierdo",2,3), mod("Bolsillo parche derecho",2,3), mod("Bolsillos superiores",2,2)); // 16
        chest(m,"Blusón de trabajo V881", mod("Bolsillo laboral izquierdo",2,3), mod("Bolsillo laboral derecho",2,3), mod("Bolsillos pectorales",2,2)); // 16
        chest(m,"Gabán V881", mod("Bolsillo profundo izquierdo",2,3), mod("Bolsillo profundo derecho",2,3), mod("Bolsillos interiores",2,2), mod("Bolsillos auxiliares",2,2)); // 20
        chest(m,"Sobretodo V881", mod("Bolsillo profundo izquierdo",2,3), mod("Bolsillo profundo derecho",2,3), mod("Bolsillos interiores",2,2), mod("Bolsillos auxiliares",2,2)); // 20
        chest(m,"Ulster V881", mod("Bolsillo de viaje izquierdo",2,3), mod("Bolsillo de viaje derecho",2,3), mod("Bolsillos interiores",2,3), mod("Bolsillos auxiliares",2,3)); // 24
        chest(m,"Guardapolvo V881", mod("Bolsillo utilitario izquierdo",2,3), mod("Bolsillo utilitario derecho",2,3), mod("Bolsillos interiores",2,2)); // 16
        chest(m,"Gabardina V881", mod("Bolsillo exterior izquierdo",2,3), mod("Bolsillo exterior derecho",2,3), mod("Bolsillos interiores",2,2), mod("Bolsillos auxiliares",2,2)); // 20
        chest(m,"Chaqueta de montar V881", mod("Bolsillo seguro izquierdo",2,2), mod("Bolsillo seguro derecho",2,2), mod("Bolsillo interior",2,1)); // 10
        chest(m,"Capa Inverness V881", mod("Bolsillo profundo izquierdo",2,3), mod("Bolsillo profundo derecho",2,3), mod("Bolsillos interiores",2,2)); // 16
        chest(m,"Dolman V881", mod("Bolsillo izquierdo",2,2), mod("Bolsillo derecho",2,2)); // 8
        // CHEST MEDIUM.
        chest(m,"Chaqueta de Viaje V881", mod("Bolsillo de viaje izquierdo",2,3), mod("Bolsillo de viaje derecho",2,3), mod("Bolsillos interiores",2,2)); // 16
        chest(m,"Chaqueta de Aeronauta V881", mod("Bolsillo frontal izquierdo",2,2), mod("Bolsillo frontal derecho",2,2), mod("Bolsillos interiores",2,2)); // 12
        chest(m,"Chaqueta cruzada de motorista V881", mod("Bolsillo cerrado izquierdo",2,2), mod("Bolsillo cerrado derecho",2,2), mod("Bolsillos interiores",2,2)); // 12
        chest(m,"Delantal de Taller V881", mod("Bolsillo herramienta izquierdo",2,3), mod("Bolsillo herramienta derecho",2,3), mod("Portaherramientas central",2,2), mod("Bolsillos auxiliares",2,2)); // 20
        integral(m,"Mono Ignífugo V881", mod("Bolsillo pectoral",2,2), mod("Bolsillo cadera izquierdo",2,3), mod("Bolsillo cadera derecho",2,3)); // 16 CHEST provider

        // LEGGINGS LIGHT/MIDDLE + MEDIUM. Bolsillos de montar se mantienen deliberadamente pequeños.
        legs(m,"Pantalón recto V881", mod("Bolsillo lateral izquierdo",2,2), mod("Bolsillo lateral derecho",2,2)); // 8
        legs(m,"Pantalón formal V881", mod("Bolsillo lateral izquierdo",2,2), mod("Bolsillo lateral derecho",2,2)); // 8
        legs(m,"Pantalón de trabajo V881", mod("Bolsillo cargo izquierdo",2,3), mod("Bolsillo cargo derecho",2,3), mod("Bolsillos superiores",2,2)); // 16
        legs(m,"Pantalón de cintura alta V881", mod("Bolsillo izquierdo",2,2), mod("Bolsillo derecho",2,2)); // 8
        legs(m,"Pantalón holgado V881", mod("Bolsillo lateral izquierdo",2,2), mod("Bolsillo lateral derecho",2,2), mod("Bolsillo trasero",2,1)); // 10
        legs(m,"Pantalón marinero V881", mod("Bolsillo lateral izquierdo",2,2), mod("Bolsillo lateral derecho",2,2), mod("Bolsillo laboral",2,1)); // 10
        legs(m,"Pantalón de montar V881", mod("Bolsillo seguro izquierdo",2,1), mod("Bolsillo seguro derecho",2,1), mod("Bolsillo auxiliar",2,1)); // 6
        legs(m,"Breeches V881", mod("Bolsillo vertical izquierdo",2,1), mod("Bolsillo vertical derecho",2,1), mod("Bolsillo auxiliar",2,1)); // 6
        legs(m,"Knickerbockers V881", mod("Bolsillo izquierdo",2,2), mod("Bolsillo derecho",2,2)); // 8
        legs(m,"Falda de paseo V881", mod("Faltriquera izquierda",2,3), mod("Faltriquera derecha",2,3)); // 12
        legs(m,"Falda de trabajo V881", mod("Bolsillo laboral izquierdo",2,3), mod("Bolsillo laboral derecho",2,3), mod("Bolsillos auxiliares",2,2)); // 16
        legs(m,"Falda de montar V881", mod("Bolsillo seguro izquierdo",2,2), mod("Bolsillo seguro derecho",2,2)); // 8
        legs(m,"Falda dividida V881", mod("Bolsillo izquierdo",2,3), mod("Bolsillo derecho",2,3)); // 12
        legs(m,"Pantalón de cuero endurecido V881", mod("Bolsillo reforzado izquierdo",2,3), mod("Bolsillo reforzado derecho",2,3)); // 12
        // prendas CHILD con dos bolsillos pequeños; permiten provisiones si caben físicamente.
        legs(m,"Pantalón infantil V881", mod("Bolsillo infantil izquierdo",2,2), mod("Bolsillo infantil derecho",2,2));
        legs(m,"Falda infantil V881", mod("Faltriquera infantil izquierda",2,2), mod("Faltriquera infantil derecha",2,2));
        if (m.size()!=40) throw new IllegalStateException("/ debe definir exactamente 40 prendas inventariables, no "+m.size());
        return Collections.unmodifiableMap(m);
    }

    private static InventoryStorageModule mod(String label,int x,int y){ return new InventoryStorageModule(label,new InventoryPhysicalDimensions(x,y,1)); }
    private static void chest(Map<String,GarmentStorageProfile> m,String n,InventoryStorageModule... mods){ put(m,n,ArmorInventoryCategory.CHEST,mods); }
    private static void legs(Map<String,GarmentStorageProfile> m,String n,InventoryStorageModule... mods){ put(m,n,ArmorInventoryCategory.LEGGINGS,mods); }
    private static void integral(Map<String,GarmentStorageProfile> m,String n,InventoryStorageModule... mods){ put(m,n,ArmorInventoryCategory.INTEGRAL_SUIT,mods); }
    private static void put(Map<String,GarmentStorageProfile> m,String n,ArmorInventoryCategory c,InventoryStorageModule... mods){
        if(m.put(n,new GarmentStorageProfile(c,List.of(mods)))!=null) throw new IllegalStateException("Prenda duplicada: "+n);
    }
}

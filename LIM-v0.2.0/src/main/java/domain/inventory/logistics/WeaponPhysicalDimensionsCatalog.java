package domain.inventory.logistics;

import domain.inventory.InventoryFootprint;
import java.util.Map;
import java.util.LinkedHashMap;

/** dimensiones físicas canónicas X(ancho) × Y(largo/alto) × Z(profundidad) de armamento. */
public final class WeaponPhysicalDimensionsCatalog {
    private static final Map<String, InventoryPhysicalDimensions> DIMS = build();
    private WeaponPhysicalDimensionsCatalog() {}

    public static InventoryPhysicalDimensions dimensionsFor(String name) {
        InventoryPhysicalDimensions d = DIMS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas XYZ para: " + name);
        return d;
    }

    public static InventoryFootprint footprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(dimensionsFor(name));
    }

    /** Compatibilidad para armas de prueba/no canónicas: Z=0,10 m hasta que declaren XYZ propio. */
    public static InventoryFootprint footprintForOrMetricFallback(String name, double xMeters, double yMeters) {
        InventoryPhysicalDimensions d = DIMS.get(name);
        if (d != null) return InventoryVolumeProjectionPolicy.footprint(d);
        return InventoryVolumeProjectionPolicy.footprint(InventoryPhysicalDimensions.fromMetricDimensions(xMeters, yMeters, 0.10));
    }

    private static InventoryPhysicalDimensions m(double x,double y,double z) {
        return InventoryPhysicalDimensions.fromMetricDimensions(x,y,z);
    }

    private static Map<String, InventoryPhysicalDimensions> build() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        // Melee / escudo.
        m.put("Pico", m(0.40,0.80,0.08));
        m.put("Zapapico", m(0.30,0.80,0.08));
        m.put("Piqueta", m(0.25,0.40,0.06));
        m.put("Cuchillo de Carnicero", m(0.12,0.40,0.04));
        m.put("Daga", m(0.07,0.40,0.04));
        m.put("Hacha de Leñador", m(0.28,0.60,0.07));
        m.put("Cimitarra", m(0.10,1.00,0.05));
        m.put("Espada Helicoidal", m(0.17,1.10,0.06));
        m.put("Espadón de Rotor", m(0.15,1.30,0.10));
        m.put("Espadón de Rotor [RETRAÍDO]", m(0.15,0.82,0.10));
        m.put("Katana Termo-mecánica V881", m(0.10,1.00,0.06));
        m.put("Maza Electro-mecánica V881", m(0.12,0.50,0.12));
        m.put("Martillo de bola", m(0.11,0.35,0.06));
        m.put("Hoz", m(0.22,0.40,0.04));
        m.put("Guadaña", m(0.65,1.60,0.08));
        m.put("Horca", m(0.30,1.60,0.08));
        m.put("Bō", m(0.04,1.80,0.04));
        m.put("Boathook", m(0.18,1.80,0.05));
        m.put("Pavesina Cementada de Asalto V881", m(0.42,0.55,0.055));

        // Firearms.
        m.put("Rifle Neumático de Repetición V881", m(0.20,1.20,0.08));
        m.put("Fusil Bifilar Electromagnético V881", m(0.20,1.50,0.10));
        m.put("Pistola Autocargadora V881", m(0.16,0.49,0.05));
        m.put("Subfusil Automático V881", m(0.24,0.83,0.08));
        m.put("Fusil de Repetición V881", m(0.20,1.10,0.08));
        m.put("Cañón Antimaterial V881", m(0.30,1.35,0.15));
        m.put("Cañón de Racimo V881", m(0.25,1.10,0.18));
        m.put("Lanza-Arcos Electrodinámico V881", m(0.22,0.88,0.14));
        m.put("Rociador de Cal Viva V881", m(0.28,0.48,0.18));

        // Ranged.
        m.put("Honda", m(0.10,0.15,0.05));
        m.put("Arco Simple Recurvo", m(0.12,1.20,0.05));
        m.put("Arco Compuesto", m(0.12,1.20,0.06));

        // Throwing.
        m.put("Cápsula de Gas Amonio V881", m(0.08,0.16,0.08));
        m.put("Granada Incendiaria de Terracota V881", m(0.12,0.18,0.12));
        m.put("Granada de Huevo con Fósforo y Azufre V881", m(0.05,0.07,0.05));
        m.put("Cuchillo Arrojadizo V881", m(0.04,0.20,0.02));
        return Map.copyOf(m);
    }
}

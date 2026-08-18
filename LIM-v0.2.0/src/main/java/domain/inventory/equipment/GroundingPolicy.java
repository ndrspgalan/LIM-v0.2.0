package domain.inventory.equipment;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.environment.time.TerrainWeatherPolicy;
import domain.environment.time.WeatherProfile;
import domain.worldmemory.spatial.TerrainSurface;

/**
 * : distingue contacto con tierra, aislamiento y una verdadera ruta protectora global.
 * AISLADO no equivale a TOMA A TIERRA.
 */
public final class GroundingPolicy {
    private GroundingPolicy() {}

    /**
     * Una ruta global sólo existe si FEET está acoplado al terreno y ninguna pieza conductora
     * deja al organismo dentro de una trayectoria peligrosa sin aislamiento/derivación propia.
     */
    public static boolean fullBodyGroundingPath(EquipmentState equipment) {
        if (equipment == null || !groundedByFeet(equipment)) return false;
        return equipment.equippedItems().values().stream().noneMatch(item -> {
            boolean conductor = item.properties().stream().anyMatch(p -> p.id() == ItemPropertyId.ELECTRICAL_CONDUCTOR);
            boolean grounded = item.properties().stream().anyMatch(p -> p.id() == ItemPropertyId.GROUNDING);
            boolean insulated = item.properties().stream().anyMatch(p -> p.id() == ItemPropertyId.INSULATING);
            if (item instanceof ArmorPiece armor) {
                conductor = conductor || armor.materials().contains(ArmorMaterial.STEEL)
                        || armor.materials().contains(ArmorMaterial.BRONZE)
                        || armor.materials().contains(ArmorMaterial.TUNGSTEN_PLATES_2_5_MM);
            }
            // INTEGRATED_CONDUCTIVE describe continuidad con el terreno,
            // no una derivación protectora. Un sabatón metálico sigue dejando al usuario
            // dentro de la trayectoria y no neutraliza la debilidad de ninguna pieza.
            return conductor && !grounded && !insulated;
        });
    }

    /** Compatibilidad nominal: significa acoplamiento físico al terreno por FEET, no inmunidad eléctrica. */
    public static boolean groundedByFeet(EquipmentState equipment) {
        if (equipment == null) return false;
        FeetElectricalContact contact = FeetElectricalContactPolicy.resolve(equipment);
        return contact == FeetElectricalContact.EARTH_COUPLED
                || contact == FeetElectricalContact.INTEGRATED_CONDUCTIVE;
    }


    /**
     * : ruta efectiva, ya incluyendo el veto ambiental canónico.
     * El equipamiento puede ser nominalmente apto y aun así no existir TOMA A TIERRA efectiva.
     */
    public static boolean fullBodyGroundingPath(EquipmentState equipment, TerrainSurface surface, WeatherProfile weather) {
        return TerrainWeatherPolicy.allowsGrounding(surface, weather) && fullBodyGroundingPath(equipment);
    }

    /** compatibilidad de FEET + entorno; no implica inmunidad ni elimina vulnerabilidades por pieza. */
    public static boolean groundedByFeet(EquipmentState equipment, TerrainSurface surface, WeatherProfile weather) {
        return TerrainWeatherPolicy.allowsGrounding(surface, weather) && groundedByFeet(equipment);
    }

    public static boolean insulatedByFeet(EquipmentState equipment) {
        return equipment != null
                && FeetElectricalContactPolicy.resolve(equipment) == FeetElectricalContact.INSULATED;
    }
}

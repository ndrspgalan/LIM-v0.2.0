package domain.inventory.item.firearmAccessories;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import java.util.List;

/** Catálogo canónico de accesorios desmontables de arma de fuego V881. */
public final class FirearmAccessoryCatalog {
    private FirearmAccessoryCatalog() {}

    public static FirearmAccessoryItem slingV881() {
        return new FirearmAccessoryItem(
                "Correa de Arma V881",
                "Correa de transporte y retención con herrajes normalizados V881.",
                0.18, new InventoryFootprint(2, 1), FirearmAccessoryMount.SLING, 0, 0,
                List.of(
                        "MONOMANUAL ASISTIDO | Permite sujetar a una mano un arma compatible que no cumple las condiciones ideales.",
                        "MEJOR ERGONOMÍA | El peso efectivo de manejo del arma equipada se multiplica ×0,75.",
                        "DESMONTABLE | Puede instalarse y retirarse de un arma compatible."),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.ASSISTED_ONE_HANDED,
                                "MONOMANUAL ASISTIDO", "La correa soporta y retiene parte del conjunto durante el manejo.",
                                "El arma puede sujetarse a una mano pese a no cumplir las condiciones ideales."),
                        ItemProperty.alwaysActive(ItemPropertyId.BETTER_ERGONOMICS,
                                "MEJOR ERGONOMÍA", "La correa redistribuye la carga del conjunto sobre el cuerpo.",
                                "El peso efectivo de manejo del arma se multiplica ×0,75 mientras la correa permanezca montada."),
                        ItemProperty.alwaysActive(ItemPropertyId.DETACHABLE,
                                "DESMONTABLE", "El accesorio utiliza anclajes normalizados V881.",
                                "Puede desmontarse y reutilizarse en otra plataforma compatible.")));
    }

    public static FirearmAccessoryItem bipodV881() {
        return new FirearmAccessoryItem(
                "Bípode de Arma V881", "Apoyo plegable normalizado para estabilizar armas largas o pesadas.",
                0.62, new InventoryFootprint(3, 2), FirearmAccessoryMount.BIPOD, 0, 0,
                List.of("ESTABILIZADOR ASISTIDO | Desplegado, el retroceso efectivo se vuelve nulo.",
                        "DESMONTABLE | Puede instalarse y retirarse de un arma compatible."),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.ASSISTED_STABILIZER,
                                "ESTABILIZADOR ASISTIDO", "El apoyo transmite la reacción del disparo a una superficie estable.",
                                "Con el bípode desplegado el retroceso efectivo del arma es 0."),
                        ItemProperty.alwaysActive(ItemPropertyId.DETACHABLE,
                                "DESMONTABLE", "El accesorio utiliza un anclaje inferior normalizado V881.",
                                "Puede desmontarse y reutilizarse en otra plataforma compatible.")));
    }

    private static FirearmAccessoryItem optic(String name, String narrative, double weightKg, InventoryFootprint footprint, double multiplier) {
        return new FirearmAccessoryItem(name, narrative, weightKg, footprint, FirearmAccessoryMount.OPTIC,
                multiplier, multiplier,
                List.of("AUMENTO ÓPTICO | ×" + (int) multiplier, "PRECISIÓN ASISTIDA | Facilita adquisición y colocación dentro del alcance propio del arma.", "DESMONTABLE | Interfaz óptica normalizada V881."),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.PRECISION_ASSISTANCE,
                                "PRECISIÓN ASISTIDA", "La óptica amplía la adquisición visual sin alterar la letalidad del proyectil.",
                                "Mejora la adquisición visual y la precisión práctica sin modificar el alcance efectivo propio del arma."),
                        ItemProperty.alwaysActive(ItemPropertyId.DETACHABLE,
                                "DESMONTABLE", "La óptica utiliza una interfaz normalizada V881.",
                                "Puede desmontarse y reutilizarse en otra plataforma compatible.")));
    }

    public static FirearmAccessoryItem fiedlerSightV881() {
        return optic("Mirilla Fiedler V881", "Óptica desmontable Fiedler adaptada al estándar V881.", 0.34, new InventoryFootprint(2, 1), 3.0);
    }
    public static FirearmAccessoryItem zeissSightV881() {
        return optic("Mirilla Zeiss V881", "Óptica desmontable Zeiss adaptada al estándar V881.", 0.46, new InventoryFootprint(2, 1), 4.0);
    }
    public static FirearmAccessoryItem winchesterA5SightV881() {
        return optic("Mirilla Winchester A5 V881", "Óptica desmontable Winchester A5 adaptada al estándar V881.", 0.58, new InventoryFootprint(3, 1), 5.0);
    }

    public static List<FirearmAccessoryItem> all() {
        return List.of(slingV881(), bipodV881(), fiedlerSightV881(), zeissSightV881(), winchesterA5SightV881());
    }
}

package domain.inventory.item.throwingWeapons;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.PersonalTransportUseProperties;
import domain.throwing.ThrowProfile;

import java.util.List;

/** Catálogo canónico  de armas arrojadizas no improvisadas. */
public final class ThrowingWeaponCatalog {
    private ThrowingWeaponCatalog() {}

    public static final String THROWING_KNIFE_NARRATIVE = String.join("\n\n",
            "Las armas arrojadizas convencionales sobrevivieron peor que las armas de fuego a la evolución del combate valeriano. La mejora de las protecciones, el aumento de las distancias de enfrentamiento y el valor creciente del espacio de inventario penalizaron especialmente aquellas piezas cuya masa y volumen solo podían justificarse durante el lanzamiento.",
            "Las hachas arrojadizas conservaron potencia de impacto, pero exigían transportar una masa considerable para una herramienta cuya trayectoria dependía demasiado de la rotación y de la distancia exacta al blanco. Frente a armas de fuego cada vez más fiables, dejaron de justificar su espacio como armamento militar especializado.",
            "Los pila ligeros y pesados sufrieron un problema todavía más evidente. Su longitud, dificultad de transporte y lentitud de preparación resultaban incompatibles con el equipo individual V881. Continuaron siendo funcionales como armas históricas, pero dejaron de tener sentido dentro de una doctrina donde un soldado debía transportar munición, protección, herramientas y equipo especializado.",
            "Los chakrams ofrecían una solución más compacta en masa, pero no en geometría. Su gran superficie plana ocupaba demasiado espacio, su comportamiento tras impactos oblicuos era difícil de predecir y su capacidad terminal frente a protección moderna resultaba insuficiente para justificar una doctrina propia.",
            "El Cuchillo Arrojadizo V881 sobrevivió precisamente porque abandonó cualquier pretensión de competir con esas armas en potencia. El modelo redujo el conjunto a una pieza estrecha de acero sin pomo, próxima en forma a un clavo fino de punta plana y angosta. Su masa de 0,100 kg permite transportar varias unidades, su tamaño apenas penaliza el inventario y cada unidad puede recuperarse cuando las condiciones lo permiten.",
            "Con 25 de letalidad perforante, no pretende derrotar las grandes protecciones militares. Su permanencia procede de silencio, simplicidad, recuperación y disponibilidad inmediata. Cuando alcanza la cabeza, GOLPE DE GRACIA se activa si sus 25 puntos perforantes superan la protección perforante efectiva de esa región; la cabeza debe conservar una fracción anatómica descubierta."
    );

    public static List<ThrowingWeaponItem> all() {
        return List.of(ammoniaGasCapsuleV881(), incendiaryTerracottaGrenadeV881(), phosphorusSulfurEggGrenadeV881(), throwingKnifeV881());
    }

    public static ThrowingWeaponItem ammoniaGasCapsuleV881() {
        return new ThrowingWeaponItem(
                "Cápsula de Gas Amonio V881",
                "Frasco de cristal grueso de uso incapacitante. La composición y el mecanismo de dispersión se mantienen CONFIDENCIAL; a efectos de LIM, el impacto libera inmediatamente su carga química.",
                1, 1, 0.35, domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor("Cápsula de Gas Amonio V881", new domain.inventory.InventoryFootprint(1,1)),
                ThrowProfile.improvised(0.35, false), ThrowingWeaponEffect.AMMONIA_CAPSULE,
                List.of("UNIDAD FÍSICA | Individual", "VENENO | 100", "TOXICIDAD VIRULENTA | activación inmediata", "ATURDIMIENTO | 2,0-2,7 s según DESTREZA", "AIMING | No utiliza"),
                PersonalTransportUseProperties.all()
        );
    }

    public static ThrowingWeaponItem incendiaryTerracottaGrenadeV881() {
        return new ThrowingWeaponItem(
                "Granada Incendiaria de Terracota V881",
                "Vasija de terracota texturizada concebida para romperse al impacto y liberar una carga incendiaria V881. Composición e iniciación: CONFIDENCIAL.",
                1, 1, 0.55, domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor("Granada Incendiaria de Terracota V881", new domain.inventory.InventoryFootprint(1,1)),
                ThrowProfile.improvised(0.55, false), ThrowingWeaponEffect.INCENDIARY_TERRACOTTA,
                List.of("UNIDAD FÍSICA | Individual", "QUEMADURA | 100", "QUEMADURA ASFIXIANTE | activación inmediata", "ATURDIMIENTO | 2,0-2,7 s según DESTREZA", "AIMING | No utiliza"),
                PersonalTransportUseProperties.all()
        );
    }

    public static ThrowingWeaponItem phosphorusSulfurEggGrenadeV881() {
        return new ThrowingWeaponItem(
                "Granada de Huevo con Fósforo y Azufre V881",
                "Arrojadiza mínima de cáscara frágil para desorganización inmediata. Su carga reactiva exacta y el método de preparación se mantienen CONFIDENCIAL.",
                1, 1, 0.06, domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor("Granada de Huevo con Fósforo y Azufre V881", new domain.inventory.InventoryFootprint(1,1)),
                ThrowProfile.improvised(0.06, false), ThrowingWeaponEffect.PHOSPHORUS_SULFUR_EGG,
                List.of("UNIDAD FÍSICA | Individual", "PA | vacía por completo al impactar", "ATURDIMIENTO | 2,0-2,7 s según DESTREZA", "AIMING | No utiliza"),
                PersonalTransportUseProperties.all()
        );
    }

    private static List<ItemProperty> withCoup(List<ItemProperty> base) {
        java.util.ArrayList<ItemProperty> all = new java.util.ArrayList<>(base);
        all.add(PersonalTransportUseProperties.coupDeGrace());
        return List.copyOf(all);
    }

    public static ThrowingWeaponItem throwingKnifeV881() {
        return new ThrowingWeaponItem(
                "Cuchillo Arrojadizo V881", THROWING_KNIFE_NARRATIVE,
                1, 1, 0.100, domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor("Cuchillo Arrojadizo V881", new domain.inventory.InventoryFootprint(1,1)),
                ThrowProfile.weapon(0.100, true, new LethalityProfile(25, 0, 0)), ThrowingWeaponEffect.THROWING_KNIFE,
                List.of("UNIDAD FÍSICA | 1 cuchillo", "PERFORANTE | 25", "RECUPERABLE | Sí", "AIMING | No utiliza"),
                withCoup(PersonalTransportUseProperties.all())
        );
    }
}

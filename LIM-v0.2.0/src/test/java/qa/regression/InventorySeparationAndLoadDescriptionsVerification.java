package qa.regression;

import domain.inventory.logistics.InventoryCompartmentType;
import presentation.menu.InventoryScreen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/** Inventario logístico puro + descripción técnica obligatoria de cada sistema de carga. */
public final class InventorySeparationAndLoadDescriptionsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        List<InventoryCompartmentType> transportLoadSystems = List.of(
                InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,
                InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,
                InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,
                InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,
                InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN
        );

        for (InventoryCompartmentType type : transportLoadSystems) {
            org.junit.jupiter.api.Assertions.assertTrue(type.narrativeDescription() != null && type.narrativeDescription().length() >= 100,
                    type.label() + " debe conservar una descripción técnica sustantiva.");
            org.junit.jupiter.api.Assertions.assertTrue(type.narrativeDescription().contains(";") || type.narrativeDescription().contains(":"),
                    type.label() + " debe explicar construcción y función, no limitarse a una etiqueta breve.");
        }

        org.junit.jupiter.api.Assertions.assertTrue(hasOnlyPresentationDependencies(),
                "InventoryScreen no debe depender de la calculadora de estadísticas derivadas ni almacenar datos de Hoja del personaje.");
        org.junit.jupiter.api.Assertions.assertTrue(hasLogisticsOnlyConstructor(),
                "InventoryScreen debe construirse sin DerivedStatisticsCalculator.");
        org.junit.jupiter.api.Assertions.assertTrue(noCharacterSheetRenderingMethods(),
                "InventoryScreen no debe conservar métodos de presentación de estadísticas, resistencias o carga derivada del personaje.");
    }

    private static boolean hasOnlyPresentationDependencies() {
        for (Field field : InventoryScreen.class.getDeclaredFields()) {
            String typeName = field.getType().getName();
            if (typeName.startsWith("domain.character.sheet.") || typeName.contains("DerivedStatistics")) return false;
        }
        return true;
    }

    private static boolean hasLogisticsOnlyConstructor() {
        Constructor<?>[] constructors = InventoryScreen.class.getDeclaredConstructors();
        if (constructors.length != 1 || constructors[0].getParameterCount() != 3) return false;
        for (Class<?> parameter : constructors[0].getParameterTypes()) {
            if (parameter.getName().startsWith("domain.character.sheet.") || parameter.getName().contains("DerivedStatistics")) return false;
        }
        return true;
    }

    private static boolean noCharacterSheetRenderingMethods() {
        for (Method method : InventoryScreen.class.getDeclaredMethods()) {
            String name = method.getName().toLowerCase();
            if (name.contains("currentstats") || name.contains("resistance") || name.equals("displayload") || name.equals("refreshstats")) return false;
        }
        return true;
    }

    
}

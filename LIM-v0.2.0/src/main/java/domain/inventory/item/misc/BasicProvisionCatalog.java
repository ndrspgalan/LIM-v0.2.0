package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import domain.survival.FoodType;

/** Provisiones canónicas ordinarias reutilizables fuera de bootstrap. */
public final class BasicProvisionCatalog {
    private BasicProvisionCatalog() {}
    public static FoodItem driedGrapes() {
        return new FoodItem("Uva deshidratada",
                "Bolsa pequeña con dos raciones de uvas secadas para reducir su contenido de agua. Cada uso reduce un nivel de hambre y puede activar o recibir la bonificación por variedad.",
                FoodType.DRIED_GRAPES,2,2,0.060,new InventoryFootprint(1,1),1.5,0,0);
    }
    /** Odre ordinario llevado con una sola carga al inicio CHILD para limitar masa. */
    public static BeverageItem childWaterskin() {
        return new BeverageItem("Odre",
                "Recipiente flexible de cuero cosido con boquilla de cobre y latón. En este estado inicial contiene una sola carga de agua; puede rellenarse después en una fuente.",
                1,5,0.250,0.400,new InventoryFootprint(2,2),1.8);
    }
}

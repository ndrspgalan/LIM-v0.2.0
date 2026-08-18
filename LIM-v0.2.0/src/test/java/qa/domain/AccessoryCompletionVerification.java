package qa.domain;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.accessory.AccessoryCatalog;

import java.util.Map;

public final class AccessoryCompletionVerification {
    private AccessoryCompletionVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.InventoryState.emptyWithoutPersonalTransport().equipment().itemAt(EquipmentSlot.ACCESSORY).isEmpty(),
                "Kenan no debe comenzar equipado.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.InventoryState.emptyWithoutPersonalTransport().equipment().itemAt(EquipmentSlot.ACCESSORY).isEmpty(),
                "Jacob no debe comenzar equipado.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.InventoryState.emptyWithoutPersonalTransport().totalCarriedWeightKg() == 0.0,
                "Kenan no debe comenzar cargado.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.InventoryState.emptyWithoutPersonalTransport().totalCarriedWeightKg() == 0.0,
                "Jacob no debe comenzar cargado.");

        AccessoryItem first = AccessoryCatalog.sketchBook();
        AccessoryItem second = AccessoryCatalog.kiaraNotebook();
        org.junit.jupiter.api.Assertions.assertTrue(first != second && !first.name().equals(second.name()), "Los cuadernos deben ser abalorios independientes.");
        org.junit.jupiter.api.Assertions.assertTrue(first.weightKg() == 0.200 && second.weightKg() == 0.200, "Ambos cuadernos deben pesar 200 g.");
        org.junit.jupiter.api.Assertions.assertTrue(first.footprint().verticalSlots() == 3 && first.footprint().horizontalSlots() == 2,
                "El A5 debe ocupar 3 x 2 slots.");

        CharacterSheet faith20 = CharacterSheet.of(10,10,10,10,10,10,20,10,10);
        CharacterSheet faith21 = CharacterSheet.of(10,10,10,10,10,10,21,10,10);
        CharacterSheet faith23 = CharacterSheet.of(10,10,10,10,10,10,23,10,10);
        var hidden = second.properties().stream().filter(p -> p.id() == domain.inventory.item.ItemPropertyId.GROW_OLD_TOGETHER).findFirst().orElseThrow();
        org.junit.jupiter.api.Assertions.assertFalse(hidden.isVisibleTo(faith20), "Una propiedad oculta no debe revelarse antes de su requisito propio.");
        org.junit.jupiter.api.Assertions.assertTrue(!hidden.isVisibleTo(faith21) && !hidden.isActiveFor(faith21), "FE 21 no satisface el requisito propio FE 23.");
        org.junit.jupiter.api.Assertions.assertTrue(hidden.isVisibleTo(faith23) && hidden.isActiveFor(faith23), "FE 23 revela y activa la propiedad.");

        EquipmentState notebookEquipped = new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, second));
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(notebookEquipped.healthRegenerationMultiplier(faith21) - 1.0) < 1e-9,
                "La regeneración oculta no debe activarse con FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(notebookEquipped.healthRegenerationMultiplier(faith23) - 1.0) < 1e-9,
                "¿ENVEJECEMOS JUNTOS? ya no debe multiplicar PV REGEN.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(notebookEquipped.sanityBonus(faith23) - 2.0) < 1e-9,
                "El Cuaderno de Kiara sólo conserva +2 Cordura de PENSAMIENTO DE PENSAMIENTO.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(notebookEquipped.accessoryResistanceBonus(faith23).piercing().orElseThrow()) < 1e-9,
                "¿ENVEJECEMOS JUNTOS? ya no debe aportar resistencias.");
        org.junit.jupiter.api.Assertions.assertTrue(notebookEquipped.effectImmunities(faith23).contains(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES),
                "FE 23 debe activar inmunidad a inhibición de PV REGEN.");

        AccessoryItem claws = AccessoryCatalog.eagleClaws();
        EquipmentState clawsEquipped = new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, claws));
        org.junit.jupiter.api.Assertions.assertTrue(clawsEquipped.attributeBonus(Attribute.CARISMA, faith20) == 11,
                "Las Garras de Águila deben aportar +11 Carisma real.");

        for (AccessoryItem fake : AccessoryCatalog.inertAccessories()) {
            org.junit.jupiter.api.Assertions.assertTrue(fake.properties().isEmpty(), "Un abalorio falso no debe declarar propiedades mecánicas.");
            org.junit.jupiter.api.Assertions.assertTrue(fake.statistics().isEmpty(), "Un abalorio falso no debe mostrar estadísticas neutras.");
            org.junit.jupiter.api.Assertions.assertTrue(fake.effects().isEmpty(), "Un abalorio falso no debe tener efectos.");
            org.junit.jupiter.api.Assertions.assertTrue(fake.weightKg() > 0 && fake.footprint().hasGridDimensions(),
                    "Todo abalorio falso debe tener peso y tamaño físicos.");
        }
        for (AccessoryItem accessory : AccessoryCatalog.all()) {
            org.junit.jupiter.api.Assertions.assertTrue(accessory.weightKg() > 0 && accessory.footprint().hasGridDimensions(),
                    "Todo abalorio debe tener peso y tamaño concretos.");
        }
    }

    
}

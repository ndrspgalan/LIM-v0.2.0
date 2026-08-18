package qa.integration;

import domain.combat.ArmorCoverageResolver;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

import java.util.Map;

public final class TextileArmorVerification {
    
    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1.0e-9) throw new AssertionError(message + ": " + actual);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        ArmorPiece workShirt = ArmorCatalog.innerWorkShirt();
        close(workShirt.bodyRegionCoverageRatio(BodyArmorRegion.CHEST), 0.50, "Camisa de trabajo");
        close(workShirt.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), 0.10, "Cobertura de brazos de la camisa de trabajo");
        org.junit.jupiter.api.Assertions.assertTrue(workShirt.inventoryCategory().orElseThrow() == ArmorInventoryCategory.CHEST,
                "La pieza sigue ocupando solamente CHEST.");
        org.junit.jupiter.api.Assertions.assertTrue(workShirt.material() == ArmorMaterial.CLOTH,
                "La prenda moderna debe conservar TELA como material dominante.");

        ArmorPiece bracersFive = new ArmorPiece("Brazales 5%", "Prueba", 0.1,
                ArmorInventoryCategory.BRACERS, ArmorHitLocation.BODY, 0.05,
                new ArmorProtectionProfile(1, 1, 1), ArmorMaterial.CLOTH, ArmorForm.STANDARD,
                java.util.List.of(), java.util.List.of());
        EquipmentState capped = new EquipmentState(Map.of(
                EquipmentSlot.CHEST, workShirt, EquipmentSlot.BRACERS, bracersFive));
        new ArmorCoverageResolver().applicableArmor(ArmorHitLocation.BODY, capped);

        ArmorPiece bracersTooLarge = new ArmorPiece("Brazales 6%", "Prueba", 0.1,
                ArmorInventoryCategory.BRACERS, ArmorHitLocation.BODY, 0.06,
                new ArmorProtectionProfile(1, 1, 1), ArmorMaterial.CLOTH, ArmorForm.STANDARD,
                java.util.List.of(), java.util.List.of());
        EquipmentState overlapped = new EquipmentState(Map.of(EquipmentSlot.CHEST, ArmorCatalog.innerWorkShirt(),
                EquipmentSlot.BRACERS, bracersTooLarge));
        new ArmorCoverageResolver().applicableArmor(ArmorHitLocation.BODY, overlapped);
        close(BodyArmorCoverageCompositionPolicy.effectiveCoverage(overlapped.equippedArmor(), BodyArmorRegion.BRACERS),
                0.15, "La superposición de brazales se capa al 15%");

        ArmorPiece neck = ArmorCatalog.travelerNeckGaiter();
        close(neck.headCoverageRatio(), 0.20, "Cobertura Cubrecuellos");
        org.junit.jupiter.api.Assertions.assertTrue(neck.form() == ArmorForm.NECK_GAITER, "Forma Cubrecuellos");
        org.junit.jupiter.api.Assertions.assertTrue(neck.statistics().contains("MATERIAL | TELA x3"), "Capas Cubrecuellos");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerTravelerCloak().properties().isEmpty(),
                "La Capa del Viajero canónica debe ser cosmética y no depender del antiguo subsistema cloak.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.engineerSuit().hasProperty(ItemPropertyId.ONE_PIECE_SUIT),
                "El Ingeniero debe exponer MONO DE UNA PIEZA.");
        close(ArmorCatalog.engineerSuit().headCoverageRatio(), 1.0, "Ingeniero cabeza");
        close(ArmorCatalog.engineerSuit().bodyCoverageRatio(), 1.0, "Ingeniero cuerpo");
    }
}

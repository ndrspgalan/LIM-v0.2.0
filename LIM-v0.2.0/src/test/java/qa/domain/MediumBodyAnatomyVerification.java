package qa.domain;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

/** anatomía BODY 50/15/30/5, cobertura multirregional y catálogo MEDIUM modular. */
public final class MediumBodyAnatomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        anatomy();
        heavyCompatibility();
        paperAndSuits();
        leatherCatalog();
        overlapMath();
    }

    private static void anatomy() {
        close(BodyArmorRegion.CHEST.maximumCoverageRatio(), .50, "CHEST50");
        close(BodyArmorRegion.BRACERS.maximumCoverageRatio(), .15, "BRACERS15");
        close(BodyArmorRegion.LEGGINGS.maximumCoverageRatio(), .30, "LEGS30");
        close(BodyArmorRegion.FEET.maximumCoverageRatio(), .05, "FEET5");
        org.junit.jupiter.api.Assertions.assertTrue(BodyArmorRegion.FEET.contributesToErgonomics(), ": FEET participa en la progresión ergonómica LEGS+FEET");
        org.junit.jupiter.api.Assertions.assertTrue(EquipmentSlot.valueOf("FEET") == EquipmentSlot.FEET, "Debe existir ranura FEET");
    }

    private static void heavyCompatibility() {
        ArmorPiece historical = ArmorCatalog.historicalKnightLeggings();
        close(historical.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .30, "Caballero histórico LEGS30");
        close(historical.bodyRegionCoverageRatio(BodyArmorRegion.FEET), .05, "Caballero histórico FEET5");
        org.junit.jupiter.api.Assertions.assertTrue(historical.hasProperty(ItemPropertyId.INTEGRATED_FOOTWEAR), "Caballero histórico debe integrar calzado");
        ArmorPiece v881 = ArmorCatalog.knightV881Leggings();
        close(v881.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .12, "Caballero V881 LEGS12");
        close(v881.bodyRegionCoverageRatio(BodyArmorRegion.FEET), 0, "Caballero V881 deja FEET libre para TOMA A TIERRA");
        org.junit.jupiter.api.Assertions.assertTrue(!v881.hasProperty(ItemPropertyId.INTEGRATED_FOOTWEAR), "Caballero V881 no integra calzado");
    }

    private static void paperAndSuits() {
        close(ArmorCatalog.paperChestV881().bodyRegionCoverageRatio(BodyArmorRegion.CHEST), .50, "Papel chest50");
        close(ArmorCatalog.paperBracersV881().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), .05, "Papel bracers5");
        close(ArmorCatalog.paperLeggingsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .12, "Papel legs12");
        ArmorPiece ins = ArmorCatalog.insulatingSuit();
        org.junit.jupiter.api.Assertions.assertTrue(ins.hasProperty(ItemPropertyId.INTEGRATED_FOOTWEAR), "Mono aislante integra calzado");
        org.junit.jupiter.api.Assertions.assertTrue(ins.hasProperty(ItemPropertyId.INTEGRAL_WATERPROOF),
                "AISLANTE debe declarar inmunidad a EMPAPADO mientras esté operativo");
    }

    private static void leatherCatalog() {
        close(ArmorCatalog.hardenedLeatherBracers().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), .05, "Precisión5");
        close(ArmorCatalog.hardenedLeatherChest().bodyRegionCoverageRatio(BodyArmorRegion.CHEST), .50, "Chaqueta chest50");
        close(ArmorCatalog.hardenedLeatherChest().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), .10, "Chaqueta arms10");
        close(ArmorCatalog.hardenedLeatherAviatorJacketV881().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), .10, "Aviador arms10");
        close(ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), .10, "Motorista arms10");
        close(ArmorCatalog.leatherShotgunChapsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .30, "Shotgun30");
        close(ArmorCatalog.leatherBatwingChapsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .30, "Batwing30");
        close(ArmorCatalog.leatherCharroChapsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .30, "Charro30");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), .12, "Riding legs12");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.FEET), .05, "Riding feet5");
        close(ArmorCatalog.leatherHeavyWorkBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.FEET), .05, "Work feet5");
        close(ArmorCatalog.leatherOxfordBrogueShoesV881().bodyRegionCoverageRatio(BodyArmorRegion.FEET), .05, "Oxford feet5");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.workshopLeatherApronV881().bodyRegionCoverage().containsKey(BodyArmorRegion.CHEST)
                        && ArmorCatalog.workshopLeatherApronV881().bodyRegionCoverage().containsKey(BodyArmorRegion.LEGGINGS),
                "Delantal debe ser multirregional");
    }

    private static void overlapMath() {
        ArmorPiece pants = ArmorCatalog.hardenedLeatherLeggings();
        ArmorPiece boots = new ArmorPiece("Bota alta sintética de verificación", "Pieza de prueba para verificar la suma regional de protección.",
                1.0, new domain.inventory.InventoryFootprint(1,1), ArmorInventoryCategory.FEET,
                java.util.Map.of(BodyArmorRegion.FEET,.05, BodyArmorRegion.LEGGINGS,.12),
                new ArmorProtectionProfile(30,30,30), ArmorMaterial.HARDENED_LEATHER,
                java.util.Set.of(ArmorMaterial.HARDENED_LEATHER), ArmorForm.STANDARD, java.util.List.of(), java.util.List.of());
        ArmorEquipmentLayout layout = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, pants)
                .equip(EquipmentSlot.FEET, ArmorLayerPosition.OUTER, boots);
        close(layout.effectiveCoverage(BodyArmorRegion.LEGGINGS), .30, "Cobertura LEGS topada en30");
        close(layout.effectiveCoverage(BodyArmorRegion.FEET), .05, "Cobertura FEET5");
        org.junit.jupiter.api.Assertions.assertTrue(layout.effectiveProtection(BodyArmorRegion.LEGGINGS).piercing() > pants.currentProtection().piercing(),
                "La bota alta debe sumar protección en LEGS en vez de quedar ignorada");
        expectFailure(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, ArmorCatalog.historicalKnightLeggings())
                .equip(EquipmentSlot.FEET, ArmorLayerPosition.OUTER, ArmorCatalog.leatherOxfordBrogueShoesV881()),
                "Calzado integrado debe ocupar FEET");
    }

    private static void expectFailure(Runnable action, String message) {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new IllegalStateException(message);
    }
    private static void close(double a, double b, String message) {
        if (Math.abs(a-b) > 1e-9) throw new IllegalStateException(message + ": " + a + " != " + b);
    }
    
}

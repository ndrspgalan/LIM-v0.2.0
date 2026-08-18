package qa.integration;

import domain.combat.ArmorCoverageResolver;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.*;

import java.util.List;
import java.util.Map;

public final class LayeredArmorVerification {
    
    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1.0e-9) throw new AssertionError(message + ": " + actual);
    }
    private static void profile(ArmorProtectionProfile p, double a, double b, double c, String message) {
        close(p.piercing(), a, message + " P"); close(p.slashing(), b, message + " C"); close(p.blunt(), c, message + " Ct");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.LAMINATED_GLASS.label().equals("Vidrio laminado"), "Debe existir Vidrio laminado.");
        profile(ArmorMaterial.LAMINATED_GLASS.canonicalProtection(), 40, 85, 35, "Vidrio laminado");


        ArmorPiece jet = ArmorCatalog.hardenedLeatherJetHelmet();
        close(jet.headCoverageRatio(), 0.60, "Cobertura casco Jet");
        profile(jet.protection(), 33, 65, 35, "Casco Jet ponderado");
        org.junit.jupiter.api.Assertions.assertTrue(jet.containsMaterial(ArmorMaterial.HARDENED_LEATHER) && jet.containsMaterial(ArmorMaterial.LAMINATED_GLASS),
                "El Casco Jet debe declarar ambos materiales al 50%.");

        profile(ArmorCatalog.hardenedLeatherChest().protection(), 17, 31, 23, "Chaqueta de Viaje ");
        profile(ArmorCatalog.hardenedLeatherBracers().protection(), 20, 37, 28, "Guantes precisión ");
        profile(ArmorCatalog.hardenedLeatherLeggings().protection(), 16, 29, 22, "Pantalón cuero ");
        close(ArmorCatalog.hardenedLeatherBracers().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), 0.05, "Brazales cuero");
        close(ArmorCatalog.hardenedLeatherLeggings().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), 0.30, "Polainas cuero");

        ArmorPiece workshopApron = ArmorCatalog.workshopLeatherApronV881();
        ArmorPiece workshopGloves = ArmorCatalog.workshopBracers();
        close(workshopApron.bodyRegionCoverageRatio(BodyArmorRegion.CHEST), 0.25, "Delantal taller torso");
        close(workshopApron.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS), 0.10, "Delantal taller piernas");
        close(workshopGloves.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS), 0.05, "Guantes taller");
        profile(workshopApron.protection(), 25, 45, 35, "Delantal taller cuero x1");
        profile(workshopGloves.protection(), 25, 45, 35, "Guantes taller cuero x1");
        profile(ArmorCatalog.workshopGoggles().protection(), 40, 85, 35, "Gafas taller");
        close(ArmorCatalog.workshopGoggles().headCoverageRatio(), 0.05, "Cobertura gafas");
        new ArmorCoverageResolver().applicableArmor(ArmorHitLocation.BODY,
                new EquipmentState(Map.of(EquipmentSlot.CHEST, workshopApron, EquipmentSlot.BRACERS, workshopGloves)));

        ArmorPiece coif = ArmorCatalog.paddedCoif();
        close(coif.headCoverageRatio(), 0.50, "Cobertura cofia sin gafas integradas");
        profile(coif.protection(), 10, 25, 10, "Cofia acolchada sin gafas");
        profile(ArmorCatalog.paddedGambeson().protection(), 36, 90, 36, "Gambesón x18");

        profile(ArmorProtectionCompositionPolicy.weightedMaterials(List.of(
                new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER, 0.5),
                new ArmorMaterialShare(ArmorMaterial.LAMINATED_GLASS, 0.5))), 33, 65, 35, "Media 50/50");
    }
}

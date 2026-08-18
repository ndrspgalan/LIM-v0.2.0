package qa.integration;


import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.combat.DamageType;
import domain.combat.NonConventionalDamageResolver;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.*;
import domain.inventory.item.firearms.*;

public final class ArcAndHistoricalArmorVerification {
    private ArcAndHistoricalArmorVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyArcBatteryAndConvergence();
        verifyHistoricalKnightPiecesAndConductivity();
        verifyV881Exceptions();
    }

    private static void verifyArcBatteryAndConvergence() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        org.junit.jupiter.api.Assertions.assertTrue(!arc.beginManualCharge(), "Sin batería el Lanza-Arcos no entra en carga.");
        ElectromagneticPortableBatteryItem battery = new ElectromagneticPortableBatteryItem();
        org.junit.jupiter.api.Assertions.assertTrue(arc.installBattery(battery), "La batería portátil se instala en el Lanza-Arcos.");
        double before = battery.remainingEnergyJ();
        arc.advancePreferredManualCharge(3.0, true);
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.storedElectricalEnergyJ(), 1650.0), "Carga preferente completa en 3 s.");
        org.junit.jupiter.api.Assertions.assertTrue(close(battery.remainingEnergyJ(), before), "El uso ordinario del Lanza-Arcos no agota la batería ni depende del cargador portátil.");
        ArcDischargeProfile profile = arc.currentDischargeProfile();
        org.junit.jupiter.api.Assertions.assertTrue(close(profile.offensiveReserve(), 300.0), "Tres bobinas = E300 bruto.");
        org.junit.jupiter.api.Assertions.assertTrue(close(profile.electricalIntensityPerTarget(1), 300.0), "Tres bobinas convergen E300 en un objetivo.");
        org.junit.jupiter.api.Assertions.assertTrue(close(profile.electricalIntensityPerTarget(2), 150.0), "Dos objetivos reparten E300.");
        org.junit.jupiter.api.Assertions.assertTrue(close(profile.electricalIntensityPerTarget(3), 100.0), "Tres objetivos reciben E100 cada uno.");
        org.junit.jupiter.api.Assertions.assertTrue(close(profile.electricalIntensityPerTarget(10), 30.0), "Diez objetivos reciben E30 cada uno a carga máxima.");
    }

    private static void verifyHistoricalKnightPiecesAndConductivity() {
        java.util.List<ArmorPiece> pieces = java.util.List.of(ArmorCatalog.historicalKnightChest(), ArmorCatalog.historicalKnightBracers(), ArmorCatalog.historicalKnightLeggings(), ArmorCatalog.historicalKnightHelmet());
        org.junit.jupiter.api.Assertions.assertTrue(pieces.size() == 4, "Caballero histórico separado en cuatro piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(close(pieces.stream().mapToDouble(ArmorPiece::weightKg).sum(), 25.0), "El peso histórico total se conserva en 25 kg.");

        EquipmentState equipment = equipmentOf(pieces);
        var result = new NonConventionalDamageResolver().resolve(DamageType.ELECTRICITY, 10.0, ArmorHitLocation.BODY, equipment, 0.0, false);
        org.junit.jupiter.api.Assertions.assertTrue(close(result.netDamage(), 60.0), "Tres piezas BODY de acero => x6 electricidad; los sabatones no neutralizan la debilidad.");
    }

    private static void verifyV881Exceptions() {
        EquipmentState knight = equipmentOf(java.util.List.of(ArmorCatalog.knightV881Chest(), ArmorCatalog.knightV881Bracers(), ArmorCatalog.knightV881Leggings()));
        var e = new NonConventionalDamageResolver().resolve(DamageType.ELECTRICITY, 10.0, ArmorHitLocation.BODY, knight, 0.0, false);
        org.junit.jupiter.api.Assertions.assertTrue(close(e.materialAdjustedDamage(), 60.0), "Caballero V881 conserva x2 por cada una de sus tres piezas BODY de acero.");

        EquipmentState ebony = equipmentOf(java.util.List.of(ArmorCatalog.ebonyWarriorV881Chest(), ArmorCatalog.ebonyWarriorV881LeftBracer()));
        var b = new NonConventionalDamageResolver().resolve(DamageType.BURN, 10.0, ArmorHitLocation.BODY, ebony, 0.0, false);
        org.junit.jupiter.api.Assertions.assertTrue(close(b.materialAdjustedDamage(), 10.0), "Ébano V881 no es INFLAMABLE.");
    }

    private static EquipmentState equipmentOf(java.util.List<ArmorPiece> pieces) {
        java.util.EnumMap<EquipmentSlot, domain.inventory.InventoryEntry> map = new java.util.EnumMap<>(EquipmentSlot.class);
        for (ArmorPiece piece : pieces) {
            ArmorInventoryCategory cat = piece.inventoryCategory().orElseThrow();
            EquipmentSlot slot = switch (cat) {
                case HEAD -> EquipmentSlot.HEAD;
                case CHEST -> EquipmentSlot.CHEST;
                case BRACERS -> EquipmentSlot.BRACERS;
                case LEGGINGS -> EquipmentSlot.LEGGINGS;
                case FEET -> EquipmentSlot.FEET;
                case INTEGRAL_SUIT -> EquipmentSlot.CHEST;
            };
            map.put(slot, piece);
        }
        return new EquipmentState(map);
    }

    private static boolean close(double a, double b) { return Math.abs(a-b) < 1e-6; }
    
}

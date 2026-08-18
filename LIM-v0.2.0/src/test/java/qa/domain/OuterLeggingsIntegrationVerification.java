package qa.domain;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** integración canónica de la capa protectora OUTER LEGGINGS. */
public final class OuterLeggingsIntegrationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        List<ArmorPiece> outer = ArmorCatalog.allOuterLeggings();
        org.junit.jupiter.api.Assertions.assertTrue(outer.size() == 12, " debe publicar 12 protecciones OUTER canónicas");
        Set<String> names = new HashSet<>();
        for (ArmorPiece p : outer) {
            org.junit.jupiter.api.Assertions.assertTrue(names.add(p.name()), "No puede haber duplicados en OUTER LEGGINGS: " + p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.inventoryCategory().orElseThrow() == ArmorInventoryCategory.LEGGINGS,
                    p.name() + " debe pertenecer a LEGGINGS");
            org.junit.jupiter.api.Assertions.assertTrue(p.materialClass() == ArmorMaterialClass.MEDIUM || p.materialClass() == ArmorMaterialClass.HEAVY,
                    p.name() + " debe ser MEDIUM/HEAVY");
            ArmorEquipmentLayout.empty().equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, p);
        }

        // El Guerrero de Ébano V881 no tiene polainas canónicas.
        org.junit.jupiter.api.Assertions.assertTrue(java.util.List.of(ArmorCatalog.ebonyWarriorV881Chest(), ArmorCatalog.ebonyWarriorV881LeftBracer()).stream()
                        .noneMatch(p -> p.inventoryCategory().orElse(null) == ArmorInventoryCategory.LEGGINGS),
                "Guerrero de Ébano V881 no puede contener polainas");
        org.junit.jupiter.api.Assertions.assertTrue(outer.stream().noneMatch(p -> p.name().contains("Ébano V881")),
                "OUTER no puede inventar polainas del Guerrero de Ébano V881");


        // Pila completa: INNER BASE + COVER + MIDDLE + OUTER.
        ArmorPiece outerHeavy = ArmorCatalog.historicalHeavyLamellarLeggings();
        ArmorEquipmentLayout full = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerLongDrawersV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPetticoatV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleStraightTrousersV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, outerHeavy);
        org.junit.jupiter.api.Assertions.assertTrue(full.piecesAt(EquipmentSlot.LEGGINGS).size() == 4, "La pila completa debe admitir cuatro piezas");
        fail(() -> full.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, ArmorCatalog.paperLeggingsV881()));
        fail(() -> ArmorEquipmentLayout.empty().equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER,
                ArmorCatalog.middleStraightTrousersV881()));
        fail(() -> ArmorEquipmentLayout.empty().equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE,
                ArmorCatalog.paperLeggingsV881()));

        // Cobertura: MIDDLE 30 + OUTER parcial no crea más pierna, pero sí añade protección.
        ArmorPiece trousers = ArmorCatalog.middleStraightTrousersV881();
        ArmorPiece knightV881 = ArmorCatalog.knightV881Leggings();
        double cov = BodyArmorCoverageCompositionPolicy.effectiveCoverage(List.of(trousers, knightV881), BodyArmorRegion.LEGGINGS);
        close(cov, .30, "MIDDLE30 + OUTER12 sigue siendo LEGGINGS30 anatómico");
        ArmorProtectionProfile base = BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(trousers), BodyArmorRegion.LEGGINGS);
        ArmorProtectionProfile layered = BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(trousers, knightV881), BodyArmorRegion.LEGGINGS);
        org.junit.jupiter.api.Assertions.assertTrue(layered.piercing() > base.piercing() && layered.slashing() > base.slashing() && layered.blunt() > base.blunt(),
                "OUTER parcial debe añadir protección sobre la zona solapada");

        // Una prenda larga de CHEST añade un tercer estrato regional sin consumir LEGGINGS.
        ArmorEquipmentLayout regional = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.OUTER, ArmorCatalog.outerUlsterV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, trousers)
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, knightV881);
        close(regional.effectiveCoverage(BodyArmorRegion.LEGGINGS), .30,
                "Ulster + MIDDLE + OUTER no puede superar LEGGINGS30");
        org.junit.jupiter.api.Assertions.assertTrue(regional.effectiveProtection(BodyArmorRegion.LEGGINGS).piercing() > layered.piercing(),
                "Ulster debe aportar protección adicional en su solapamiento");

        // FEET multirregional: las botas altas añaden protección a LEGGINGS pero no ocupan OUTER LEGGINGS.
        ArmorEquipmentLayout boots = fullWithoutOuter()
                .equip(EquipmentSlot.FEET, ArmorLayerPosition.OUTER, ArmorCatalog.leatherHighRidingBootsV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, ArmorCatalog.paperLeggingsV881());
        org.junit.jupiter.api.Assertions.assertTrue(boots.piecesAt(EquipmentSlot.FEET).size() == 1, "Las botas altas deben ocupar FEET");
        org.junit.jupiter.api.Assertions.assertTrue(boots.piecesAt(EquipmentSlot.LEGGINGS).size() == 4, "Las botas no consumen una plaza LEGGINGS");

        // Calzado integrado: Caballero histórico reserva FEET; Caballero V881 lo deja libre.
        ArmorPiece historical = ArmorCatalog.historicalKnightLeggings();
        org.junit.jupiter.api.Assertions.assertTrue(historical.hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR), "Caballero histórico integra FEET");
        ArmorEquipmentLayout historicalLayout = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, historical);
        fail(() -> historicalLayout.equip(EquipmentSlot.FEET, ArmorLayerPosition.OUTER,
                ArmorCatalog.leatherOxfordBrogueShoesV881()));

        ArmorPiece v881 = ArmorCatalog.knightV881Leggings();
        org.junit.jupiter.api.Assertions.assertTrue(!v881.hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR), "Caballero V881 debe dejar FEET libre");
        ArmorEquipmentLayout groundedCandidate = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, v881)
                .equip(EquipmentSlot.FEET, ArmorLayerPosition.OUTER, ArmorCatalog.leatherOxfordBrogueShoesV881());
        org.junit.jupiter.api.Assertions.assertTrue(groundedCandidate.piecesAt(EquipmentSlot.FEET).size() == 1, "Caballero V881 admite FEET independiente");

        // PAPER conserva su propia lógica WET y no se vuelve textil genérico.
        ArmorPiece paper = ArmorCatalog.paperLeggingsV881();
        org.junit.jupiter.api.Assertions.assertTrue(paper.material() == ArmorMaterial.PAPER, "Papel debe conservar PAPER como material principal");
        org.junit.jupiter.api.Assertions.assertTrue(paper.materialClass() == ArmorMaterialClass.MEDIUM, "Papel debe seguir siendo MEDIUM");
    }

    private static ArmorEquipmentLayout fullWithoutOuter() {
        return ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerLongDrawersV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPetticoatV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleStraightTrousersV881());
    }

    
    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1.0e-9) throw new AssertionError(message + ": " + actual + " != " + expected);
    }
    private static void fail(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("La operación debía ser rechazada");
    }
}

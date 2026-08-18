package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;

public final class ArmorMaterialTaxonomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.CLOTH.materialClass() == ArmorMaterialClass.LIGHT, "Tela debe ser LIGHT");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.DIELECTRIC_CLOTH.materialClass() == ArmorMaterialClass.LIGHT, "Tela dieléctrica debe ser LIGHT");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.PAPER.materialClass() == ArmorMaterialClass.MEDIUM, "Papel debe ser MEDIUM");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.HARDENED_LEATHER.materialClass() == ArmorMaterialClass.MEDIUM, "Cuero endurecido debe ser MEDIUM");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.BRONZE.materialClass() == ArmorMaterialClass.MEDIUM, "Bronce debe ser MEDIUM");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.STEEL.materialClass() == ArmorMaterialClass.HEAVY, "Acero debe ser HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.EBONY_WOOD.materialClass() == ArmorMaterialClass.HEAVY, "Ébano debe ser HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.TUNGSTEN.materialClass() == ArmorMaterialClass.HEAVY, "Wolframio debe ser HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE.materialClass() == ArmorMaterialClass.HEAVY,
                "Compuesto Electromecánico debe ser HEAVY");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.paddedGambeson().materialClass() == ArmorMaterialClass.LIGHT,
                "Una pieza confeccionada exclusivamente en tela debe derivar LIGHT");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.hardenedLeatherChest().materialClass() == ArmorMaterialClass.MEDIUM,
                "Tela+cuero debe derivar MEDIUM");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.historicalKnightChest().materialClass() == ArmorMaterialClass.HEAVY,
                "Acero+tela debe derivar HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.engineerSuit().materialClass() == ArmorMaterialClass.HEAVY,
                "El compuesto estructural debe dominar la clasificación de la pieza");

        ArmorEquipmentLayout chest = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerWorkShirt())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.historicalKnightChest())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.OUTER, ArmorCatalog.outerWorkSmockV881());
        org.junit.jupiter.api.Assertions.assertTrue(chest.piecesAt(EquipmentSlot.CHEST).size() == 3, "CHEST debe admitir LIGHT+HEAVY+LIGHT");

        expectFailure(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.historicalKnightChest())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.OUTER, ArmorCatalog.hardenedLeatherChest()),
                "HEAVY y MEDIUM deben ser mutuamente excluyentes y ocupar MIDDLE");

        expectFailure(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.BRACERS, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.hardenedLeatherBracers())
                .equip(EquipmentSlot.BRACERS, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.workshopBracers()),
                "BRACERS debe admitir una sola pieza");
        expectFailure(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.hardenedLeatherLeggings())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.middleStraightTrousersV881()),
                "LEGGINGS debe admitir una sola pieza");

        ArmorEquipmentLayout panopticon = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.enlightenedPanopticon());
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(panopticon.headWeightKg() - 3.5) < 1e-9, "Panóptico debe consumir los 3,5 kg de HEAD");
        expectFailure(() -> panopticon.equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.beretV881()),
                "HEAD no puede superar 3,5 kg aunque aún quede una segunda posición");

        ArmorEquipmentLayout threeHeadFunctions = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.headScarfV881())
                .equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.normalVisionGlassesV881())
                .equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.beretV881());
        org.junit.jupiter.api.Assertions.assertTrue(threeHeadFunctions.piecesAt(EquipmentSlot.HEAD).size() == 3, ": HEAD admite LOWER + TACTICAL + UPPER");
        expectFailure(() -> threeHeadFunctions.equip(EquipmentSlot.HEAD, ArmorLayerPosition.UNSPECIFIED, ArmorCatalog.workshopGoggles()),
                "TACTICAL HEAD ya está ocupado por las gafas de visión");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.paperChestV881().armorRegion() == ArmorRegion.BODY, "Coraza de papel debe ser BODY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.paperHelmetV881().armorRegion() == ArmorRegion.HEAD, "Casco de papel debe ser HEAD");
    }

    private static void expectFailure(Runnable action, String message) {
        try {
            action.run();
            throw new IllegalStateException(message);
        } catch (IllegalArgumentException expected) {
            // correcto
        }
    }

    
}

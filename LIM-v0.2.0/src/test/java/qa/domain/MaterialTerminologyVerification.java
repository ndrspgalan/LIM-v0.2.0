package qa.domain;

import domain.character.sheet.CharacterSheet;
import domain.environment.EnvironmentalAdversity;
import domain.environment.EnvironmentalSetBonusPolicy;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.*;

import java.util.List;
import java.util.Map;

/** Verifica la limpieza semántica de materiales introducida. */
public final class MaterialTerminologyVerification {
    private MaterialTerminologyVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyElectromechanicalCompositeTerminology();
        verifyClothPropertyHasNoFaithGate();
    }

    private static void verifyElectromechanicalCompositeTerminology() {
        ArmorMaterial material = ArmorMaterial.ELECTROMECHANICAL_COMPOSITE;
        org.junit.jupiter.api.Assertions.assertTrue("Compuesto Electromecánico".equals(material.label()),
                "El identificador técnico debe estar en inglés sin alterar la etiqueta visible en español.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.engineerSuit().containsMaterial(material),
                "El traje del Ingeniero debe usar ELECTROMECHANICAL_COMPOSITE.");
    }

    private static void verifyClothPropertyHasNoFaithGate() {
        EquipmentState clothBody = new EquipmentState(Map.of(
                EquipmentSlot.CHEST, cloth("Coraza de tela", .50),
                EquipmentSlot.BRACERS, cloth("Brazales de tela", .15),
                EquipmentSlot.LEGGINGS, cloth("Polainas de tela", .35)));
        EnvironmentalSetBonusPolicy policy = new EnvironmentalSetBonusPolicy();
        CharacterSheet minimumFaith = CharacterSheet.of(10, 10, 10, 10, 10, 10, 1, 10, 10);

        org.junit.jupiter.api.Assertions.assertTrue(policy.clothSetVisible(clothBody),
                "La inmunidad física de la tela no puede tratarse como conocimiento oculto.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.immuneTo(minimumFaith, clothBody, EnvironmentalAdversity.BITING_FROST),
                "La inmunidad del conjunto de tela no puede depender de FE 21.");
    }

    private static ArmorPiece cloth(String name, double coverage) {
        return new ArmorPiece(name, name, 1.0, InventoryFootprint.equipmentOnly(), ArmorHitLocation.BODY,
                coverage, ArmorMaterial.CLOTH.canonicalProtection(), ArmorMaterial.CLOTH,
                ArmorForm.STANDARD, List.of(), List.of());
    }

    
}

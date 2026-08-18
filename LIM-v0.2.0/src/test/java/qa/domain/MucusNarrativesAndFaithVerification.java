package qa.domain;

import domain.character.sheet.CharacterSheet;
import domain.combat.coating.WeaponCoatingService;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponActionMode;
import domain.inventory.item.WeaponConfiguration;
import domain.inventory.item.WeaponConfigurationPolicy;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;
import domain.inventory.item.GripMode;
import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.inventory.item.misc.MucusCrystalItem;
import domain.inventory.item.misc.MucusTearItem;
import domain.runic.EffectImmunity;
import domain.runic.RunicMarkCatalog;
import domain.runic.RunicMarkId;
import presentation.menu.CharacterSheetInspectionEntry;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

public final class MucusNarrativesAndFaithVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyNarratives();
        verifyUniversalFaithRequirementRemoved();
        verifyCrystalEffectsWithoutUniversalFaith();
        verifyTearEffectWithoutUniversalFaith();
        verifyRunicMarkWithoutUniversalFaith();
    }

    private static void verifyNarratives() {
        var entries = CharacterSheetInspectionEntry.canonicalEntries();
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus disponible").contains("memoria biológica"),
                "La descripción general debe definir el mucus como memoria biológica de la adaptación.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus blanco").contains("adaptación primaria"),
                "El Mucus Blanco debe representar la conservación orgánica primaria.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus amarillento").contains("reservas energéticas"),
                "El Mucus Amarillento debe representar la administración del esfuerzo prolongado.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus verdoso").contains("reorganizarse frente a condiciones variables"),
                "El Mucus Verdoso debe representar plasticidad adaptativa.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus marrón").contains("esfuerzo físico continuado"),
                "El Mucus Marrón debe representar adaptación estructural al esfuerzo.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus ensangrentado").contains("reorganizar tejidos"),
                "El Mucus Ensangrentado debe representar reconstrucción tras el daño.");
        org.junit.jupiter.api.Assertions.assertTrue(description(entries, "Mucus negruzco").contains("fenómenos que escapan a una percepción ordinaria"),
                "El Mucus Negruzco debe representar adaptación ante fenómenos ocultos.");
    }

    private static void verifyUniversalFaithRequirementRemoved() {
        for (MucusCrystalItem crystal : crystals()) {
            org.junit.jupiter.api.Assertions.assertTrue(crystal.conditionalAttribute() == domain.character.sheet.Attribute.CLARIVIDENCIA && crystal.conditionalMinimum() == 33,
                    "Los Cristales deben activarse exclusivamente con CLARIVIDENCIA 33.");
        }
    }

    private static void verifyCrystalEffectsWithoutUniversalFaith() {
        EquipmentState equipment = new EquipmentState(Map.of(
                EquipmentSlot.ACCESSORY, MucusCrystalCatalog.yellow()));
        org.junit.jupiter.api.Assertions.assertTrue(!equipment.effectImmunities(sheetWithClairvoyance(1,32)).contains(EffectImmunity.POISON),
                "CLARIVIDENCIA 32 no debe activar el Cristal.");
        org.junit.jupiter.api.Assertions.assertTrue(equipment.effectImmunities(sheetWithClairvoyance(1,33)).contains(EffectImmunity.POISON),
                "CLARIVIDENCIA 33 debe activar el Cristal sin requisito adicional de FE.");
    }

    private static void verifyTearEffectWithoutUniversalFaith() {
        WeaponCoatingService service = new WeaponCoatingService();
        WeaponItem weapon = cuttingWeapon();
        MucusTearItem tear = new MucusTearItem(1);
        org.junit.jupiter.api.Assertions.assertTrue(service.applyCurse(sheet(1), tear, weapon).successful(),
                "Una Lágrima no debe exigir FE 21.");
    }

    private static void verifyRunicMarkWithoutUniversalFaith() {
        var mark = RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION);
        org.junit.jupiter.api.Assertions.assertTrue(mark.isAwakenedFor(sheet(1)), "Una Marca Rúnica no debe exigir FE 21.");
    }

    private static String description(List<CharacterSheetInspectionEntry> entries, String label) {
        return entries.stream().filter(entry -> entry.label().equals(label)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Falta la entrada: " + label)).description();
    }

    private static List<MucusCrystalItem> crystals() {
        return List.of(MucusCrystalCatalog.yellow(), MucusCrystalCatalog.greenish(),
                MucusCrystalCatalog.brown(), MucusCrystalCatalog.bloodied(), MucusCrystalCatalog.blackish());
    }

    private static CharacterSheet sheet(int faith) {
        return CharacterSheet.of(30, 30, 30, 30, 30, 30, faith, 30, 30);
    }
    private static CharacterSheet sheetWithClairvoyance(int faith,int clairvoyance) {
        return CharacterSheet.of(30,30,30,30,30,30,faith,30,clairvoyance);
    }

    private static WeaponItem cuttingWeapon() {
        return new WeaponItem("Arma cortante", "Arma de verificación.", 1,
                new InventoryFootprint(1, 1), 1,
                List.of(new WeaponMode("Filo", new LethalityProfile(0, 10, 3))),
                List.of(), List.of(), List.of(), OptionalDouble.empty(), 0, false,
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))), Set.of());
    }

    
}

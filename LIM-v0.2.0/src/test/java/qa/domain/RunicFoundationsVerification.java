package qa.domain;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.CurrentCharacterStats;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.environment.time.DayPhase;
import domain.inventory.InventoryState;
import domain.inventory.QuickAccessBar;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.LogisticsState;
import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.runic.RunicMarkCatalog;
import domain.runic.RunicMarkId;
import domain.runic.RunicMarkItem;

import java.util.Map;

public final class RunicFoundationsVerification {
    private static final double EPSILON = 1.0e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyCatalogAndLatentVisibility();
        verifySlotCompatibility();
        verifyParhelio();
        verifyCompassRose();
        verifyBindingVowAndBlackCrystal();
        verifyBloodiedCrystalCancelsParhelioPenalty();
        verifyBrownCrystalCancelsOverloadRegenPenalty();
    }

    private static void verifyCatalogAndLatentVisibility() {
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.all().size() == 7, "El catálogo debe contener siete marcas.");
        RunicMarkItem parhelio = RunicMarkCatalog.require(RunicMarkId.PARHELIO);
        CharacterSheet latent = sheet(20, 30, 40);
        CharacterSheet awakened = sheet(21, 30, 40);
        org.junit.jupiter.api.Assertions.assertTrue(parhelio.visibleNarrative(latent).isPresent(), "La narrativa no debe exigir FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(!parhelio.visibleStatistics(latent).isEmpty(), "Los efectos no deben exigir FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(parhelio.visibleNarrative(awakened).isPresent(), "FE 21 debe revelar la narrativa.");
        org.junit.jupiter.api.Assertions.assertTrue(!parhelio.visibleStatistics(awakened).isEmpty(), "FE 21 debe revelar efectos.");
    }

    private static void verifySlotCompatibility() {
        RunicMarkItem parhelio = RunicMarkCatalog.require(RunicMarkId.PARHELIO);
        new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK, parhelio));
        expectFailure(() -> new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, parhelio)),
                "Una marca no puede ocupar Abalorio.");
        expectFailure(() -> new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK, MucusCrystalCatalog.blackish())),
                "Un abalorio no puede ocupar Marca Rúnica.");
    }

    private static void verifyParhelio() {
        CharacterSheet sheet = sheet(21, 30, 40);
        DerivedStatisticsCalculator calculator = new DerivedStatisticsCalculator();
        InventoryState inventory = inventory(Map.of(EquipmentSlot.RUNIC_MARK,
                RunicMarkCatalog.require(RunicMarkId.PARHELIO)));
        CurrentCharacterStats base = calculator.calculate(sheet, Gender.HOMBRE, InventoryState.emptyWithoutPersonalTransport(), DayPhase.DAY);
        CurrentCharacterStats day = calculator.calculate(sheet, Gender.HOMBRE, inventory, DayPhase.DAY);
        CurrentCharacterStats afternoon = calculator.calculate(sheet, Gender.HOMBRE, inventory, DayPhase.AFTERNOON);
        CurrentCharacterStats night = calculator.calculate(sheet, Gender.HOMBRE, inventory, DayPhase.NIGHT);
        close(day.healthRegeneration().orElseThrow(), base.healthRegeneration().orElseThrow() * 3.0, "Parhelio día PV REGEN");
        close(afternoon.healthRegeneration().orElseThrow(), base.healthRegeneration().orElseThrow() * 2.2, "Parhelio tarde PV REGEN");
        close(night.healthRegeneration().orElseThrow(), base.healthRegeneration().orElseThrow(), "Parhelio noche no altera PV REGEN");
        close(day.physicalStability().orElseThrow(), sheet.valueOf(domain.character.sheet.Attribute.VITALIDAD),
                "Parhelio ya no altera estabilidad física");
        close(night.sanity().orElseThrow(), sheet.valueOf(domain.character.sheet.Attribute.INTELIGENCIA),
                "Parhelio ya no penaliza cordura");
    }

    private static void verifyCompassRose() {
        CharacterSheet sheet = sheet(21, 30, 75);
        InventoryState inventory = inventory(Map.of(EquipmentSlot.RUNIC_MARK,
                RunicMarkCatalog.require(RunicMarkId.ROSA_DE_LOS_VIENTOS)));
        CurrentCharacterStats stats = new DerivedStatisticsCalculator().calculate(sheet, Gender.HOMBRE, inventory, DayPhase.DAY);
        close(stats.sanity().orElseThrow(), 30 + 75, "Rosa de los Vientos debe sumar Clarividencia.");
    }

    private static void verifyBindingVowAndBlackCrystal() {
        CharacterSheet sheet = sheet(21, 30, 40);
        DerivedStatisticsCalculator calculator = new DerivedStatisticsCalculator();
        InventoryState vow = inventory(Map.of(EquipmentSlot.RUNIC_MARK,
                RunicMarkCatalog.require(RunicMarkId.VOTO_VINCULANTE)));
        close(calculator.calculate(sheet, Gender.HOMBRE, vow, DayPhase.DAY).resistances().frenzy().orElseThrow(),
                0.0, "Voto Vinculante debe fijar Frenesí a 0%.");
        InventoryState combined = inventory(Map.of(
                EquipmentSlot.RUNIC_MARK, RunicMarkCatalog.require(RunicMarkId.VOTO_VINCULANTE),
                EquipmentSlot.ACCESSORY, MucusCrystalCatalog.blackish()));
        close(calculator.calculate(sheet, Gender.HOMBRE, combined, DayPhase.DAY).resistances().frenzy().orElseThrow(),
                100.0, "El Cristal Negruzco debe prevalecer mediante inmunidad.");
    }

    private static void verifyBloodiedCrystalCancelsParhelioPenalty() {
        CharacterSheet sheet = sheet(21, 30, 40);
        InventoryState combined = inventory(Map.of(EquipmentSlot.ACCESSORY, MucusCrystalCatalog.bloodied()));
        org.junit.jupiter.api.Assertions.assertTrue(combined.equipment().effectImmunities(sheet).contains(domain.runic.EffectImmunity.CURSE_DAMAGE),
                "El Cristal Ensangrentado debe otorgar inmunidad a Maldición.");
    }

    private static void verifyBrownCrystalCancelsOverloadRegenPenalty() {
        CharacterSheet sheet = sheet(21, 30, 40);
        InventoryState inventory = inventory(Map.of(EquipmentSlot.ACCESSORY, MucusCrystalCatalog.brown()));
        org.junit.jupiter.api.Assertions.assertTrue(inventory.equipment().effectImmunities(sheet).contains(domain.runic.EffectImmunity.STAMINA_REGEN_DELAY),
                "El Cristal Marrón debe exponer recuperación instantánea de PA REGEN.");
    }

    private static InventoryState inventory(Map<EquipmentSlot, domain.inventory.InventoryEntry> items) {
        return new InventoryState(new EquipmentState(items), QuickAccessBar.empty(), LogisticsState.emptyWithoutPersonalTransport());
    }

    private static CharacterSheet sheet(int faith, int intelligence, int clairvoyance) {
        return CharacterSheet.of(40, 30, 30, 20, 20, intelligence, faith, 20, clairvoyance);
    }

    private static void expectFailure(Runnable action, String message) {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new IllegalStateException(message);
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPSILON) {
            throw new IllegalStateException(message + ": esperado=" + expected + ", actual=" + actual);
        }
    }

    
}

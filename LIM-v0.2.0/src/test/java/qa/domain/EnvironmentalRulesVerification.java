package qa.domain;

import domain.animation.CanonicalAnimation;
import domain.animation.CharacterAnimationState;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.character.sheet.StaminaRecovery;
import domain.combat.*;
import domain.environment.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.MucusCrystalCatalog;
import presentation.menu.CharacterSheetInspectionEntry;

import java.util.List;
import java.util.Map;

public final class EnvironmentalRulesVerification {
    private static final CharacterSheet FE21 = CharacterSheet.of(30, 40, 10, 30, 30, 30, 21, 30, 33);
    private static final StaminaRecovery RECOVERY = new DerivedStatisticsCalculator().staminaRecovery(40, 0, 40);

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        simultaneousAdversities();
        crystalsAndMetamorphosis();
        custodyForcesRecovery();
        thermalAdaptationFreezesFrostBuildUp();
        maskingUsesOperationalNeckGaiter();
        directElementalDamageInhibitsHealthRegen();
        narrativesExist();
    }

    private static void simultaneousAdversities() {
        EnvironmentalExposure exposure = new EnvironmentalExposure();
        exposure.enter(EnvironmentalAdversity.VIRULENT_TOXICITY, 10, FE21, EquipmentState.empty(), RECOVERY);
        exposure.enter(EnvironmentalAdversity.SUFFOCATING_HEAT, 10, FE21, EquipmentState.empty(), RECOVERY);
        org.junit.jupiter.api.Assertions.assertTrue(exposure.insideSource(EnvironmentalAdversity.VIRULENT_TOXICITY), "Toxicidad debe conservar su estado.");
        org.junit.jupiter.api.Assertions.assertTrue(exposure.insideSource(EnvironmentalAdversity.SUFFOCATING_HEAT), "Calor debe coexistir con Toxicidad.");
    }

    private static void crystalsAndMetamorphosis() {
        EnvironmentalExposure yellow = new EnvironmentalExposure();
        EquipmentState yellowCrystal = new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, MucusCrystalCatalog.yellow()));
        org.junit.jupiter.api.Assertions.assertTrue(yellow.enter(EnvironmentalAdversity.VIRULENT_TOXICITY, 20, FE21, yellowCrystal, RECOVERY).exposureSeconds() == 0,
                "El Cristal Amarillento debe impedir Toxicidad Virulenta.");

        EnvironmentalExposure green = new EnvironmentalExposure();
        EquipmentState greenCrystal = new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, MucusCrystalCatalog.greenish()));
        org.junit.jupiter.api.Assertions.assertTrue(green.enter(EnvironmentalAdversity.SUFFOCATING_HEAT, 20, FE21, greenCrystal, RECOVERY).exposureSeconds() == 0,
                "El Cristal Verdoso debe impedir Calor Asfixiante.");

        domain.combat.MetamorphosisDamagePolicy metamorphosis = new domain.combat.MetamorphosisDamagePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(metamorphosis.transform(DamageType.POISON, true) == DamageType.CURSE,
                "METAMORPHOSIS sostenida debe convertir Veneno en Maldición.");
        org.junit.jupiter.api.Assertions.assertTrue(metamorphosis.transform(DamageType.CURSE, true) == DamageType.POISON,
                "METAMORPHOSIS sostenida debe convertir Maldición en Veneno.");
    }

    private static void custodyForcesRecovery() {
        EnvironmentalExposure exposure = new EnvironmentalExposure();
        exposure.enter(EnvironmentalAdversity.SOAKED, 10, FE21, EquipmentState.empty(), RECOVERY);
        exposure.enter(EnvironmentalAdversity.BITING_FROST, 10, FE21, EquipmentState.empty(), RECOVERY);
        exposure.setCustodyActive(true, RECOVERY);
        org.junit.jupiter.api.Assertions.assertTrue(exposure.snapshot(EnvironmentalAdversity.SOAKED, FE21).recovering(), "CUSTODIA debe recuperar Empapado.");
        org.junit.jupiter.api.Assertions.assertTrue(exposure.snapshot(EnvironmentalAdversity.BITING_FROST, FE21).recovering(), "CUSTODIA debe recuperar Frío.");
    }

    private static void thermalAdaptationFreezesFrostBuildUp() {
        EnvironmentalExposure exposure = new EnvironmentalExposure();
        exposure.enter(EnvironmentalAdversity.BITING_FROST, 4, FE21, EquipmentState.empty(), RECOVERY);
        exposure.setThermalAdaptationActive(true);
        double before = exposure.snapshot(EnvironmentalAdversity.BITING_FROST, FE21).exposureSeconds();
        double after = exposure.enter(EnvironmentalAdversity.BITING_FROST, 5, FE21, EquipmentState.empty(), RECOVERY).exposureSeconds();
        org.junit.jupiter.api.Assertions.assertTrue(before == after, "ADAPTACIÓN TÉRMICA debe paralizar el build-up ya iniciado.");
    }

    private static void maskingUsesOperationalNeckGaiter() {
        ArmorPiece gaiter = ArmorCatalog.travelerNeckGaiter();
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.HEAD, gaiter));
        CharacterAnimationState animation = new CharacterAnimationState();
        new EnvironmentalExposure().enter(EnvironmentalAdversity.SUFFOCATING_HEAT, 1, FE21, equipment, RECOVERY, animation);
        org.junit.jupiter.api.Assertions.assertTrue(animation.currentAnimation() != CanonicalAnimation.MASK, ": el cubrecuellos ya no activa ENMASCARAR automáticamente al eliminar FILTRO NATURAL.");
    }

    private static void directElementalDamageInhibitsHealthRegen() {
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy policy = new ElementalHealthRegenerationPolicy();
        policy.registerDirectDamage(DamageType.BURN, 1, encounter);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.healthRegenerationAllowed(encounter, false), "La quemadura directa debe inhibir PV REGEN durante el encuentro.");
        encounter.conclude();
        org.junit.jupiter.api.Assertions.assertTrue(policy.healthRegenerationAllowed(encounter, false), "PV REGEN debe restablecerse al terminar el encuentro.");
    }

    private static void narrativesExist() {
        var entries = CharacterSheetInspectionEntry.canonicalEntries();
        for (String label : List.of("Toxicidad Virulenta", "Quemadura Asfixiante", "Frío Escarchante", "Empapado")) {
            org.junit.jupiter.api.Assertions.assertTrue(entries.stream().anyMatch(e -> e.label().equals(label)), "Falta narrativa ambiental: " + label);
        }
    }

    
}

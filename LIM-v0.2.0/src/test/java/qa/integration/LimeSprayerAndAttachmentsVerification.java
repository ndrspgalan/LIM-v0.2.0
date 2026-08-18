package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.combat.DamageType;
import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.environment.EnvironmentalAdversity;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.QuickAccessBar;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.ammunition.LimeCartridgeCase;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.firearms.*;
import domain.inventory.item.misc.TherapeuticItem;
import domain.inventory.item.firearmAccessories.FirearmAccessoryMount;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.LogisticsState;
import domain.inventory.logistics.PersonalTransportState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Verificación ejecutable. */
public final class LimeSprayerAndAttachmentsVerification {
    private LimeSprayerAndAttachmentsVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyAttachmentCatalogAndDynamicMass();
        verifyAssistedStabilizer();
        verifyLimeSprayerCanonicalDefinition();
        verifyLimeCartridgeCaseAndReload();
        verifyCorrosionAndEnvironment();
        verifyElementalRegenInhibition();
        verifyConsumableStacks();
        verifyNarrativeAndCatalog();
    }

    private static void verifyAttachmentCatalogAndDynamicMass() {
        var sling = FirearmAccessoryCatalog.slingV881();
        var bipod = FirearmAccessoryCatalog.bipodV881();
        var medium = FirearmAccessoryCatalog.fiedlerSightV881();
        var precision = FirearmAccessoryCatalog.winchesterA5SightV881();
        org.junit.jupiter.api.Assertions.assertTrue(sling.grantsAssistedOneHanded() && sling.detachable(), "La correa debe aportar MONOMANUAL ASISTIDO y DESMONTABLE.");
        org.junit.jupiter.api.Assertions.assertTrue(bipod.grantsAssistedStabilizer() && bipod.detachable(), "El bípode debe aportar ESTABILIZADOR ASISTIDO y DESMONTABLE.");
        org.junit.jupiter.api.Assertions.assertTrue(close(medium.maxMagnification(), 3.0) && close(medium.effectiveRangeMultiplier(), 1.0), "Fiedler x3 óptico sin ampliar alcance.");
        org.junit.jupiter.api.Assertions.assertTrue(close(precision.maxMagnification(), 5.0) && close(precision.effectiveRangeMultiplier(), 1.0), "Winchester A5 x5 óptico sin ampliar alcance.");

        var bifilar = FirearmCatalog.bifilarElectromagneticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(bifilar.baseWeightKg(), 8.70), "El Bifilar debe excluir correa y bípode de la masa base.");
        org.junit.jupiter.api.Assertions.assertTrue(bifilar.admitsAttachment(FirearmAccessoryMount.SLING) && bifilar.admitsAttachment(FirearmAccessoryMount.BIPOD)
                && bifilar.admitsAttachment(FirearmAccessoryMount.OPTIC), "El Bifilar debe admitir los tres tipos.");
        org.junit.jupiter.api.Assertions.assertTrue(bifilar.mountAttachment(sling), "Debe montar correa.");
        org.junit.jupiter.api.Assertions.assertTrue(bifilar.mountAttachment(bipod), "Debe montar bípode.");
        org.junit.jupiter.api.Assertions.assertTrue(bifilar.mountAttachment(precision), "Debe montar mirilla.");
        org.junit.jupiter.api.Assertions.assertTrue(close(bifilar.weightKg(), 8.70 + 0.18 + 0.62 + 0.58), "La masa equipada debe sumar accesorios.");
        org.junit.jupiter.api.Assertions.assertTrue(bifilar.unmountAttachment(FirearmAccessoryMount.OPTIC).isPresent(), "La mirilla debe desmontarse.");

        var auto = FirearmCatalog.submachineGunV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(auto.baseWeightKg(), 3.85), "El Subfusil  conserva masa propia separada de la correa.");
        org.junit.jupiter.api.Assertions.assertTrue(auto.admitsAttachment(FirearmAccessoryMount.SLING) && !auto.admitsAttachment(FirearmAccessoryMount.BIPOD)
                && !auto.admitsAttachment(FirearmAccessoryMount.OPTIC), "El Subfusil solo admite correa.");

        var pneumatic = FirearmCatalog.repeatingPneumaticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(pneumatic.admitsAttachment(FirearmAccessoryMount.SLING) && pneumatic.admitsAttachment(FirearmAccessoryMount.BIPOD)
                && pneumatic.admitsAttachment(FirearmAccessoryMount.OPTIC), "El neumático debe admitir correa, bípode y mirilla.");
    }

    private static void verifyAssistedStabilizer() {
        var rifle = FirearmCatalog.repeatingPneumaticRifleV881();
        var bipod = FirearmAccessoryCatalog.bipodV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.mountAttachment(bipod), "El Bifilar debe aceptar bípode.");
        org.junit.jupiter.api.Assertions.assertTrue(bipod.deploy(), "El bípode debe poder desplegarse.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.assistedStabilizerActive(), "El estabilizador asistido debe estar activo.");
        int before = rifle.ammunitionRemaining();
        new FirearmInputResolutionPolicy().resolve(FirearmInput.RIGHT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.ammunitionRemaining() == before - 1, "Debe disparar.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.recoilState().accumulatedVelocityMps(), 0.0), "El bípode desplegado vuelve nulo el retroceso efectivo.");
    }

    private static void verifyLimeSprayerCanonicalDefinition() {
        LimeSprayerItem sprayer = FirearmCatalog.limeSprayerV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.baseWeightKg(), 1.35), "Masa base vacía del rociador.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.weightKg(), 4.55), "Sin correa, el cartucho lleno debe elevar la masa dinámica a 4,55 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.lengthMeters(), 0.48) && close(sprayer.widthMeters(), 0.28), "Dimensiones canónicas.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.footprint().verticalSlots() == 6 && sprayer.footprint().horizontalSlots() == 10, ": footprint XYZ 6x10.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.effectiveRangeMeters(), 2.5), "Alcance 2,5 m.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.capacityLiters(), 3.0) && close(sprayer.fullSpraySeconds(), 28.0), "Capacidad y duración.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.hitIntervalSeconds(), 0.5) && sprayer.cartridgeDefinition().capacity() == 56, "56 hits por cartucho completo.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.burnDamage() == 67 && sprayer.poisonDamage() == 100, "Daño no convencional canónico.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.supportsOneHanded() && !sprayer.supportsTwoHanded(), "El rociador solo admite control monomanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!sprayer.operationalForSpraying(), "Sin correa no debe activarse MONOMANUAL ASISTIDO.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.mountAttachment(FirearmAccessoryCatalog.slingV881()), "Debe admitir correa.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.operationalForSpraying(), "La correa debe habilitar MONOMANUAL ASISTIDO.");
        org.junit.jupiter.api.Assertions.assertTrue(close(sprayer.weightKg(), 4.73), "Cargado y con correa debe pesar 4,73 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.confidentialCalibration().startsWith("CONFIDENCIAL"), "La calibración sensible debe constar como CONFIDENCIAL.");
    }

    private static void verifyLimeCartridgeCaseAndReload() {
        LimeSprayerItem sprayer = FirearmCatalog.limeSprayerV881();
        sprayer.mountAttachment(FirearmAccessoryCatalog.slingV881());
        LimeCartridgeCase caseItem = AmmunitionCatalog.limeCartridgeCase();
        org.junit.jupiter.api.Assertions.assertTrue(caseItem.footprint().verticalSlots() == 3 && caseItem.footprint().horizontalSlots() == 2, "La geometría métrica canónica proyecta el estuche a 3x2 slots.");
        org.junit.jupiter.api.Assertions.assertTrue(caseItem.maxUnits() == 5 && caseItem.remainingUnits() == 5, "Debe admitir cinco cartuchos.");
        org.junit.jupiter.api.Assertions.assertTrue(close(caseItem.weightKg(), 16.3), "El peso del estuche debe crecer con sus cinco cartuchos.");

        // Vacía el cartucho inicial de 56 ticks.
        var input = new FirearmInputResolutionPolicy();
        for (int i = 0; i < 56; i++) {
            if (i == 0) org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_PRESS, sprayer).allowed(), "Debe iniciar rociado.");
            else org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_HOLD, sprayer).allowed(), "AA del rociador debe mantener el rociado.");
        }
        input.resolve(FirearmInput.RIGHT_RELEASE, sprayer);
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.ammunitionRemaining() == 0, "El cartucho debe agotarse tras 56 hits.");

        InventoryState inventory = inventoryWithRightHand(sprayer, List.of(caseItem));
        var reload = input.resolve(FirearmInput.RELOAD_PRESS, sprayer, inventory);
        org.junit.jupiter.api.Assertions.assertTrue(reload.allowed() && sprayer.ammunitionRemaining() == 56, "R debe consumir un cartucho del estuche.");
        org.junit.jupiter.api.Assertions.assertTrue(caseItem.remainingUnits() == 4 && close(caseItem.weightKg(), 13.1), "El estuche debe perder masa al consumir un cartucho.");
    }

    private static void verifyCorrosionAndEnvironment() {
        LimeSprayerItem sprayer = FirearmCatalog.limeSprayerV881();
        sprayer.mountAttachment(FirearmAccessoryCatalog.slingV881());
        var armor = ArmorCatalog.ebonyWarriorChest();
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.CHEST, armor));
        double before = armor.currentBluntProtection();
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy regen = new ElementalHealthRegenerationPolicy();

        var dry = sprayer.resolveHit(ArmorHitLocation.BODY, equipment, 0, 0, false, regen, encounter);
        org.junit.jupiter.api.Assertions.assertTrue(close(before - armor.currentBluntProtection(), 1.0), "CORROSIVO debe retirar 1 Ct por hit.");
        org.junit.jupiter.api.Assertions.assertTrue(!dry.corrodedArmorPieces().isEmpty(), "Debe registrar la pieza corroída.");
        double afterDry = armor.currentBluntProtection();
        sprayer.resolveHit(ArmorHitLocation.BODY, equipment, 0, 0, true, regen, encounter);
        org.junit.jupiter.api.Assertions.assertTrue(close(afterDry - armor.currentBluntProtection(), 2.0), "EMPAPADO debe elevar CORROSIVO a -2 Ct.");
        org.junit.jupiter.api.Assertions.assertTrue(regen.inhibited(), "Quemadura/Veneno directos deben inhibir PV REGEN durante el encuentro.");

        var surface = sprayer.contaminateSurface();
        org.junit.jupiter.api.Assertions.assertTrue(close(surface.remainingSeconds(), 30.0), "Contaminación de superficie 30 s.");
        org.junit.jupiter.api.Assertions.assertTrue(surface.adversities().contains(EnvironmentalAdversity.VIRULENT_TOXICITY)
                && surface.adversities().contains(EnvironmentalAdversity.SUFFOCATING_HEAT),
                "La superficie debe activar Toxicidad Virulenta y Quemadura Asfixiante.");
        org.junit.jupiter.api.Assertions.assertTrue(sprayer.environmentalStateLabels().equals(List.of("Toxicidad Virulenta", "Quemadura Asfixiante")), "Etiquetas ambientales canónicas.");
    }

    private static void verifyElementalRegenInhibition() {
        for (DamageType type : List.of(DamageType.POISON, DamageType.BURN, DamageType.FROST, DamageType.ELECTRICITY)) {
            HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
            ElementalHealthRegenerationPolicy policy = new ElementalHealthRegenerationPolicy();
            policy.registerDirectDamage(type, 1.0, encounter);
            org.junit.jupiter.api.Assertions.assertTrue(policy.inhibited(), type + " directo debe inhibir PV REGEN.");
            org.junit.jupiter.api.Assertions.assertTrue(!policy.healthRegenerationAllowed(encounter, false), type + " debe mantener PV REGEN inhibida.");
            encounter.conclude();
            org.junit.jupiter.api.Assertions.assertTrue(policy.healthRegenerationAllowed(encounter, false), "Al concluir el encuentro se libera la inhibición.");
        }
    }

    private static void verifyConsumableStacks() {
        var injection=MiscellaneousItemCatalog.stimulantInjection();
        var lucidity=MiscellaneousItemCatalog.lucidityEssence();
        org.junit.jupiter.api.Assertions.assertTrue(injection.maximumStack()==1 && lucidity.maximumStack()==1,
                ": autoinyector y ampolla son unidades físicas independientes.");
        org.junit.jupiter.api.Assertions.assertTrue(!injection.addUnits(1) && !lucidity.addUnits(1),
                "Una instancia no puede absorber otra unidad física.");
        org.junit.jupiter.api.Assertions.assertTrue(close(injection.weightKg(),0.080),"Peso unitario de inyección.");
        org.junit.jupiter.api.Assertions.assertTrue(close(lucidity.weightKg(),0.040),"Peso unitario de esencia.");
    }

    private static void verifyNarrativeAndCatalog() {
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.all().stream().anyMatch(i -> i.name().equals("Rociador de Cal Viva V881")), "El rociador debe estar en el catálogo.");
        String n = FirearmCatalog.LIME_SPRAYER_NARRATIVE;
        org.junit.jupiter.api.Assertions.assertTrue(n.contains("armaduras de escamas") && n.contains("cotas de malla"), "Narrativa: obsolescencia de escamas y malla.");
        org.junit.jupiter.api.Assertions.assertTrue(n.contains("protección lamelar") && n.contains("Conjunto del Caballero V881"), "Narrativa: evolución lamelar y milanesa.");
        org.junit.jupiter.api.Assertions.assertTrue(n.contains("armadura de ébano") && n.contains("pólvora sin humo"), "Narrativa: ébano y segunda presión selectiva.");
        org.junit.jupiter.api.Assertions.assertTrue(n.contains("Toxicidad Virulenta") && n.contains("Quemadura Asfixiante"), "Narrativa: estados ambientales.");
        org.junit.jupiter.api.Assertions.assertTrue(n.contains("MONOMANUAL ASISTIDO") && n.contains("CORROSIVO"), "Narrativa: propiedades .");
    }

    private static InventoryState inventoryWithRightHand(InventoryEntry weapon, List<InventoryEntry> bodyEntries) {
        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.RIGHT_HAND, weapon));
        EnumMap<InventoryCompartmentType, InventoryCompartment> compartments = new EnumMap<>(InventoryCompartmentType.class);
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            compartments.put(type, InventoryCompartment.empty(type, type == InventoryCompartmentType.BACKPACK));
        }
        compartments.put(InventoryCompartmentType.BACKPACK,
                new InventoryCompartment(InventoryCompartmentType.BACKPACK, true, bodyEntries));
        return new InventoryState(equipment, QuickAccessBar.empty(), new LogisticsState(compartments, PersonalTransportState.none()));
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < 0.000001; }
    
}

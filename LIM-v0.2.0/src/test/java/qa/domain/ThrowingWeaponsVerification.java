package qa.domain;

import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;
import domain.throwing.StackThrowPolicy;
import domain.throwing.ThrowRequest;
import domain.throwing.ThrownRecoveryPolicy;

public final class ThrowingWeaponsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyCatalogReplacement();
        verifyAmmoniaCapsule();
        verifyTerracotta();
        verifyEggGrenade();
        verifyThrowingKnife();
    }

    private static ThrowRequest request() { return new ThrowRequest(40, 40, 1.72, 20); }

    private static void verifyCatalogReplacement() {
        var all = ThrowingWeaponCatalog.all();
        org.junit.jupiter.api.Assertions.assertTrue(all.size() == 4, "Solo deben quedar las cuatro arrojadizas canónicas .");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().noneMatch(i -> i.name().contains("Hacha") || i.name().contains("Pilum") || i.name().contains("Chakram") || i.name().contains("Venablo")),
                "Los placeholders históricos deben haber desaparecido del catálogo.");
        all.forEach(i -> {
            org.junit.jupiter.api.Assertions.assertTrue(!i.supportsAiming(), "Ninguna arrojadiza utiliza AIMING.");
            org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(i), "Toda arrojadiza canónica debe requerir acceso rápido para uso directo.");
        });
    }

    private static void verifyAmmoniaCapsule() {
        ThrowingWeaponItem item = ThrowingWeaponCatalog.ammoniaGasCapsuleV881();
        org.junit.jupiter.api.Assertions.assertTrue(item.maximumStack() == 1 && item.footprint().verticalSlots() == 1 && item.footprint().horizontalSlots() == 1, "Cápsula: stack/slots.");
        close(item.weightKg(), 0.35, "Cápsula: peso.");
        var thrown = new StackThrowPolicy().throwOne(item, request());
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy regen = new ElementalHealthRegenerationPolicy();
        var result = item.resolveSpecialImpact(thrown, ArmorHitLocation.BODY, EquipmentState.empty(), 100, regen, encounter);
        close(result.poisonDamage(), 100, "Cápsula: Veneno 100.");
        org.junit.jupiter.api.Assertions.assertTrue(result.virulentToxicityActivated(), "Cápsula: Toxicidad Virulenta completa.");
        close(result.stagger().staggerDurationSeconds(), new CombatTechniqueUnlockPolicy().controlStunDurationSeconds(20), "Cápsula: aturdimiento por DESTREZA.");
        org.junit.jupiter.api.Assertions.assertTrue(regen.inhibited(), "Veneno directo debe inhibir PV REGEN.");
    }

    private static void verifyTerracotta() {
        ThrowingWeaponItem item = ThrowingWeaponCatalog.incendiaryTerracottaGrenadeV881();
        org.junit.jupiter.api.Assertions.assertTrue(item.maximumStack() == 1 && item.footprint().verticalSlots() == 2 && item.footprint().horizontalSlots() == 1, "Terracota: stack/slots.");
        close(item.weightKg(), 0.55, "Terracota: peso.");
        var thrown = new StackThrowPolicy().throwOne(item, request());
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy regen = new ElementalHealthRegenerationPolicy();
        var result = item.resolveSpecialImpact(thrown, ArmorHitLocation.BODY, EquipmentState.empty(), 100, regen, encounter);
        close(result.burnDamage(), 100, "Terracota: Quemadura 100.");
        org.junit.jupiter.api.Assertions.assertTrue(result.suffocatingBurnActivated(), "Terracota: Quemadura Asfixiante completa.");
        close(result.stagger().staggerDurationSeconds(), 2.0, "Terracota: DEX20 produce 2 s.");
        org.junit.jupiter.api.Assertions.assertTrue(regen.inhibited(), "Quemadura directa debe inhibir PV REGEN.");
    }

    private static void verifyEggGrenade() {
        ThrowingWeaponItem item = ThrowingWeaponCatalog.phosphorusSulfurEggGrenadeV881();
        org.junit.jupiter.api.Assertions.assertTrue(item.maximumStack() == 1 && item.footprint().verticalSlots() == 1 && item.footprint().horizontalSlots() == 1, "Huevo: stack/slots.");
        close(item.weightKg(), 0.06, "Huevo: peso.");
        var thrown = new StackThrowPolicy().throwOne(item, request());
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy regen = new ElementalHealthRegenerationPolicy();
        var result = item.resolveSpecialImpact(thrown, ArmorHitLocation.BODY, EquipmentState.empty(), 87, regen, encounter);
        close(result.staminaAfter(), 0, "Huevo: vacía PA.");
        close(result.staminaDrained(), 87, "Huevo: drena todos los PA actuales.");
        org.junit.jupiter.api.Assertions.assertTrue(!result.virulentToxicityActivated(), ": el huevo ya no inicia build-up tóxico.");
        close(result.stagger().staggerDurationSeconds(), 2.0, "Huevo: DEX20 produce 2 s.");
        close(result.healthRegenerationInhibitionSeconds(), 2.0, "Huevo: inhibe PV REGEN durante el aturdimiento.");
        org.junit.jupiter.api.Assertions.assertTrue(regen.inhibited(), "Huevo: PV REGEN debe quedar temporalmente inhibida.");
    }

    private static void verifyThrowingKnife() {
        ThrowingWeaponItem knife = ThrowingWeaponCatalog.throwingKnifeV881();
        org.junit.jupiter.api.Assertions.assertTrue(knife.maximumStack() == 1 && knife.currentUses() == 1, "Cuchillo: una instancia física.");
        close(knife.unitWeightKg(), 0.100, "Cuchillo: 0,100 kg por unidad.");
        close(knife.weightKg(), 0.100, "Cuchillo: masa unitaria.");
        org.junit.jupiter.api.Assertions.assertTrue(knife.footprint().verticalSlots() == 2 && knife.footprint().horizontalSlots() == 1, "Cuchillo: 2x1.");
        org.junit.jupiter.api.Assertions.assertTrue(knife.throwProfile().lethalityProfile().orElseThrow().piercing() == 25, "Cuchillo: 25 perforante.");
        org.junit.jupiter.api.Assertions.assertTrue(knife.recoverable() && knife.hasCoupDeGraceProperty(), "Cuchillo: recuperable + GOLPE DE GRACIA.");

        var policy = new StackThrowPolicy();
        var thrown = policy.throwOne(knife, request());
        org.junit.jupiter.api.Assertions.assertTrue(knife.currentUses() == 0 && Math.abs(knife.weightKg()) < 1e-9, "Lanzar consume esa instancia.");
        var result = knife.resolveSpecialImpact(thrown, ArmorHitLocation.HEAD, EquipmentState.empty(), 100,
                new ElementalHealthRegenerationPolicy(), new HostileEncounterState());
        org.junit.jupiter.api.Assertions.assertTrue(result.coupDeGrace(), "Cabeza sin protección debe activar GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(new ThrownRecoveryPolicy().recover(thrown, knife) && knife.currentUses() == 1, "El cuchillo físico debe poder recuperarse como la misma instancia.");
        org.junit.jupiter.api.Assertions.assertTrue(ThrowingWeaponCatalog.THROWING_KNIFE_NARRATIVE.contains("hachas arrojadizas")
                && ThrowingWeaponCatalog.THROWING_KNIFE_NARRATIVE.contains("pila ligeros y pesados")
                && ThrowingWeaponCatalog.THROWING_KNIFE_NARRATIVE.contains("chakrams"),
                "La narrativa debe justificar la obsolescencia de los placeholders históricos.");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(message + " actual=" + actual + " expected=" + expected);
    }
    
}

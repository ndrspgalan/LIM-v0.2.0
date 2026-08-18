package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.combat.coating.MercuryCoatingService;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.throwing.*;

/** Regresión : lanzamiento, control por DESTREZA, PV REGEN y GOLPE DE GRACIA. */
public final class ThrowingBallisticsAndControlVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyCoupDeGraceSources();
        verifyMercuryLead46();
        verifyCanonicalThrowingFormula();
        verifyAngularBallistics();
        verifyDexterityStunCurveAndEggRegen();
    }

    private static void verifyCoupDeGraceSources() {
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.sling().hasProperty(ItemPropertyId.COUP_DE_GRACE), "Honda conserva GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.simpleRecurveBow().hasProperty(ItemPropertyId.COUP_DE_GRACE), "Arco recurvo conserva GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.compositeBow().hasProperty(ItemPropertyId.COUP_DE_GRACE), "Arco compuesto conserva GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(ThrowingWeaponCatalog.throwingKnifeV881().hasCoupDeGraceProperty(), "Cuchillo arrojadizo conserva GOLPE DE GRACIA.");
    }

    private static void verifyMercuryLead46() {
        MercuryCoatingService mercury = new MercuryCoatingService();
        var stone = MiscellaneousItemCatalog.mercuryStone();
        var lead46 = AmmunitionCatalog.pneumaticLead46Cartridge();
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.sling().accepts(lead46.ammunitionDescriptor()), "La Honda debe aceptar la bala .46 de plomo.");
        org.junit.jupiter.api.Assertions.assertTrue(mercury.rub(stone, lead46, lead46.remainingUnits()), "La bala/cartucho .46 de plomo debe admitir Piedra de Mercurio.");
        org.junit.jupiter.api.Assertions.assertTrue(mercury.isCoated(lead46), "La munición .46 debe conservar el recubrimiento de mercurio.");
    }

    private static void verifyCanonicalThrowingFormula() {
        ThrowPolicy policy = new ThrowPolicy();
        var payload = ThrownPayload.item("Objeto de 2 kg", ThrowProfile.improvised(2.0, true));
        var result = policy.resolve(new ThrowRequest(30, 20, 1.80, 35), payload);
        close(result.horizontalDistanceMeters(), 20.0, "A 35 grados, DEX20 = 20 m.");
        close(result.damage().blunt(), 32.0, "FUE30 + 2 kg = B32.");
    }

    private static void verifyAngularBallistics() {
        ThrowPolicy policy = new ThrowPolicy();
        var payload = ThrownPayload.item("Guijarro", ThrowProfile.improvised(0.05, true));
        var optimal = policy.resolve(new ThrowRequest(30, 30, 1.80, 35), payload);
        var horizontal = policy.resolve(new ThrowRequest(30, 30, 1.80, 0), payload);
        var downward = policy.resolve(new ThrowRequest(30, 30, 1.80, -30), payload);
        var up = policy.resolve(new ThrowRequest(30, 30, 1.80, 90), payload);
        var down = policy.resolve(new ThrowRequest(30, 30, 1.80, -90), payload);
        close(optimal.horizontalDistanceMeters(), 30.0, "35 grados debe ser el alcance óptimo canónico.");
        org.junit.jupiter.api.Assertions.assertTrue(horizontal.horizontalDistanceMeters() > 0 && horizontal.horizontalDistanceMeters() < 30, "A 0 grados debe existir alcance horizontal menor al óptimo.");
        org.junit.jupiter.api.Assertions.assertTrue(downward.horizontalDistanceMeters() >= 0 && downward.horizontalDistanceMeters() < horizontal.horizontalDistanceMeters(), "Lanzar hacia abajo debe acortar la trayectoria.");
        close(up.horizontalDistanceMeters(), 0.0, "+90 grados no recorre distancia horizontal.");
        close(down.horizontalDistanceMeters(), 0.0, "-90 grados no recorre distancia horizontal.");
    }

    private static void verifyDexterityStunCurveAndEggRegen() {
        CombatTechniqueUnlockPolicy curve = new CombatTechniqueUnlockPolicy();
        close(curve.controlStunDurationSeconds(20), 2.0, "DEX20 = 2 s.");
        close(curve.controlStunDurationSeconds(70), 2.7, "DEX70 = 2,7 s.");

        var egg = ThrowingWeaponCatalog.phosphorusSulfurEggGrenadeV881();
        var thrown = new StackThrowPolicy().throwOne(egg, new ThrowRequest(30, 70, 1.80, 35));
        HostileEncounterState encounter = new HostileEncounterState(); encounter.begin();
        ElementalHealthRegenerationPolicy regen = new ElementalHealthRegenerationPolicy();
        var result = egg.resolveSpecialImpact(thrown, ArmorHitLocation.BODY, EquipmentState.empty(), 100, 70, regen, encounter);
        close(result.stagger().staggerDurationSeconds(), 2.7, "El huevo comparte curva de DESTREZA con Parry/Mirror Parry.");
        close(result.healthRegenerationInhibitionSeconds(), 2.7, "PV REGEN se inhibe durante exactamente el stun.");
        org.junit.jupiter.api.Assertions.assertTrue(!regen.healthRegenerationAllowed(encounter, false), "PV REGEN debe estar bloqueada durante el stun.");
        regen.advanceTime(2.69);
        org.junit.jupiter.api.Assertions.assertTrue(!regen.healthRegenerationAllowed(encounter, false), "La inhibición debe persistir hasta completar el stun.");
        regen.advanceTime(0.01);
        org.junit.jupiter.api.Assertions.assertTrue(regen.healthRegenerationAllowed(encounter, false), "PV REGEN debe volver al terminar el stun del huevo.");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1e-6) throw new IllegalStateException(message + " actual=" + actual + " expected=" + expected);
    }
    
}

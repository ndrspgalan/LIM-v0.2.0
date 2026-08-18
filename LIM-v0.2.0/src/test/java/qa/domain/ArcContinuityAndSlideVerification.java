package qa.domain;


import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.control.ControlAction;
import domain.control.PcControlScheme;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.firearms.*;
import domain.movement.SlidingPolicy;

public final class ArcContinuityAndSlideVerification {
    private ArcContinuityAndSlideVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyUnboundedArcDistribution();
        verifyContinuousPreferredCharge();
        verifyBatteryIsNotGameplayLimitedByPortableCharger();
        verifySpecialHelmetsAreNotConductors();
        verifyRunningSlide();
    }

    private static void verifyUnboundedArcDistribution() {
        org.junit.jupiter.api.Assertions.assertTrue(close(ArcChargePolicy.resolve(ArcChargePolicy.MODULE_I_TURNS).offensiveReserve(), 100), "Módulo I = E100.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArcChargePolicy.resolve(ArcChargePolicy.MODULE_I_TURNS).electricalIntensityPerTarget(10), 10), "E100 / 10 = E10.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArcChargePolicy.resolve(ArcChargePolicy.MODULE_III_TURNS).electricalIntensityPerTarget(10), 30), "E300 / 10 = E30.");
    }

    private static void verifyContinuousPreferredCharge() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        ElectromagneticPortableBatteryItem battery = new ElectromagneticPortableBatteryItem();
        org.junit.jupiter.api.Assertions.assertTrue(arc.installBattery(battery), "La batería se acopla.");
        arc.advancePreferredManualCharge(1.2, true);
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.crankTurns(), 3), "1,2 s = 3 vueltas equivalentes.");
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.currentDischargeProfile().offensiveReserve(), 100), "A 1,2 s ya puede disparar E100.");
        arc.advancePreferredManualCharge(0.9, true);
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.crankTurns(), 5), "2,1 s acumulados = 5 vueltas.");
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.currentDischargeProfile().offensiveReserve(), 200), "2,1 s = E200.");
        arc.advancePreferredManualCharge(0.9, true);
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.crankTurns(), 6), "3 s acumulados = 6 vueltas.");
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.currentDischargeProfile().offensiveReserve(), 300), "3 s = E300.");
        org.junit.jupiter.api.Assertions.assertTrue(close(arc.currentDischargeProfile().thermalLockSeconds(), 3), "Carga máxima bloquea 3 s.");
    }

    private static void verifyBatteryIsNotGameplayLimitedByPortableCharger() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        ElectromagneticPortableBatteryItem battery = new ElectromagneticPortableBatteryItem(1.0);
        arc.installBattery(battery);
        arc.advancePreferredManualCharge(3.0, true);
        org.junit.jupiter.api.Assertions.assertTrue(close(battery.remainingEnergyJ(), 1.0), "El Lanza-Arcos no consume la reserva modelada del Bifilar.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.chargeFull(), "Una batería acoplada y la manivela permiten explotar el banco completo.");
    }

    private static void verifySpecialHelmetsAreNotConductors() {
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.enlightenedPanopticon().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR), "Panóptico sin CONDUCTOR ELÉCTRICO.");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.retractableAeronautHelmet().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR), "Casco del Aeronauta sin CONDUCTOR ELÉCTRICO.");
    }

    private static void verifyRunningSlide() {
        SlidingPolicy policy = new SlidingPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(policy.distanceMeters(1.80, true), 2.70), "Slide = 1,5 × altura.");
        org.junit.jupiter.api.Assertions.assertTrue(close(policy.distanceMeters(1.80, false), 0.0), "C fuera de carrera no desliza.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.SLIDE && b.input().equals("C")), "PC publica C para SLIDE contextual.");
    }

    private static boolean close(double a, double b) { return Math.abs(a-b) < 1e-6; }
    
}

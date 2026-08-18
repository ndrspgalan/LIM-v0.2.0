package qa.domain;


import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.control.ControlAction;
import domain.control.InputGesture;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;
import domain.inventory.item.firearms.ArcChargePolicy;
import domain.inventory.item.firearms.ArcInductionFirearmItem;
import domain.inventory.item.firearms.FirearmAction;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.firearms.FirearmHandlingState;
import domain.inventory.item.firearms.FirearmInput;
import domain.inventory.item.firearms.FirearmInputResolutionPolicy;
import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.inventory.item.firearms.LimeSprayerItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.item.firearmAccessories.FirearmAccessoryMount;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;

import java.util.Set;

public final class ArcControlsSynchronizationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyArcCanonicalShape();
        verifyArcChargeAndDischarge();
        verifyNoAimingPlatforms();
        verifyDestabilizeKicks();
        verifyPcPs4FirearmParity();
        verifyArcDetachableSling();
    }

    private static void verifyArcCanonicalShape() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        close(arc.baseWeightKg(), 2.62, "La masa base debe excluir la correa desmontable de 0,18 kg.");
        close(arc.lengthMeters(), 0.88, "Longitud del Lanza-Arcos.");
        close(arc.effectiveRangeMeters(), 3.0, "Alcance del Lanza-Arcos.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.supportsUnboundedArcDistribution(), "El arco puede repartirse entre cualquier número de objetivos válidos.");
        org.junit.jupiter.api.Assertions.assertTrue(!arc.supportsAiming(), "El Lanza-Arcos no utiliza AIMING.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.fireModes().size() == 1, "La descarga no usa selector balístico de cadencia.");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.all().stream().anyMatch(i -> i.name().equals("Lanza-Arcos Electrodinámico V881")),
                "El Lanza-Arcos debe formar parte de FirearmCatalog.all().");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.ARC_INDUCTION_LANCE_NARRATIVE.contains("Tres vueltas equivalentes")
                        && FirearmCatalog.ARC_INDUCTION_LANCE_NARRATIVE.contains("seis representan la capacidad completa"),
                "La narrativa canónica del dossier debe estar integrada.");
    }

    private static void verifyArcChargeAndDischarge() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        org.junit.jupiter.api.Assertions.assertTrue(arc.installBattery(new ElectromagneticPortableBatteryItem()), "El Lanza-Arcos requiere la batería portátil V881.");
        FirearmInputResolutionPolicy input = new FirearmInputResolutionPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!input.resolve(FirearmInput.RIGHT_PRESS, arc).allowed(), "Carga 0 no puede descargar.");
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RELOAD_HOLD, arc).action() == FirearmAction.ENTER_ARC_MANUAL_CHARGE,
                "HOLD R debe entrar en carga manual del Lanza-Arcos.");
        for (int i = 0; i < 3; i++) org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_PRESS, arc).allowed(), "Cada giro debe cargar.");
        close(arc.currentDischargeProfile().offensiveReserve(), 100.0, "3 vueltas deben producir reserva 100.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.currentDischargeProfile().fullyActiveModules() == 1, "3 vueltas deben habilitar un módulo.");
        close(arc.currentDischargeProfile().thermalLockSeconds(), 1.2, "Bloqueo I.");
        close(arc.currentDischargeProfile().shockUnits(), 25.0, "SACUDIDA I.");
        input.resolve(FirearmInput.RELOAD_PRESS, arc);
        org.junit.jupiter.api.Assertions.assertTrue(arc.handlingState() == FirearmHandlingState.NORMAL, "R debe cancelar la manivela sin perder carga.");
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_PRESS, arc).allowed(), "Debe descargar con carga parcial I.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.lastDischargeProfile() != null, "Debe persistir el último perfil descargado.");
        close(arc.lastDischargeProfile().offensiveReserve(), 100.0, "La descarga I conserva reserva 100.");
        close(arc.thermalLockRemainingSeconds(), 1.2, "La descarga I activa bloqueo térmico de 1,2 s.");
        arc.advanceThermalTime(1.2);
        org.junit.jupiter.api.Assertions.assertTrue(!arc.triggerThermallyLocked(), "El bloqueo térmico debe finalizar.");

        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RELOAD_HOLD, arc).allowed(), "Debe volver a entrar en carga.");
        for (int i = 0; i < 6; i++) input.resolve(FirearmInput.RIGHT_HOLD, arc);
        close(arc.crankTurns(), ArcChargePolicy.MAX_TURNS, "6 vueltas = carga máxima.");
        close(arc.currentDischargeProfile().offensiveReserve(), 300.0, "Reserva máxima 300.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.currentDischargeProfile().fullyActiveModules() == 3, "Tres módulos a carga máxima.");
        close(arc.currentDischargeProfile().thermalLockSeconds(), 3.0, "Bloqueo III.");
        close(arc.currentDischargeProfile().shockUnits(), 75.0, "SACUDIDA III.");
        close(arc.electricalIntensityPerTarget(1), 300.0, "Un blanco recibe la convergencia E300 de las tres bobinas.");
        close(arc.electricalIntensityPerTarget(10), 30.0, "E300 se reparte entre diez blancos como E30 por objetivo.");
    }

    private static void verifyNoAimingPlatforms() {
        FirearmInputResolutionPolicy input = new FirearmInputResolutionPolicy();
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        LimeSprayerItem lime = FirearmCatalog.limeSprayerV881();
        org.junit.jupiter.api.Assertions.assertTrue(!input.resolve(FirearmInput.LEFT_PRESS, arc).allowed(), "Lanza-Arcos no AIMING.");
        org.junit.jupiter.api.Assertions.assertTrue(!input.resolve(FirearmInput.LEFT_PRESS, lime).allowed(), "Rociador no AIMING.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.handlingState() == FirearmHandlingState.NORMAL && lime.handlingState() == FirearmHandlingState.NORMAL,
                "LEFT PRESS no debe alterar el estado de plataformas sin AIMING.");
        ThrowingWeaponCatalog.all().forEach(w -> org.junit.jupiter.api.Assertions.assertTrue(!w.supportsAiming(), "Ningún throwing weapon utiliza AIMING."));
    }

    private static void verifyDestabilizeKicks() {
        FirearmInputResolutionPolicy input = new FirearmInputResolutionPolicy();
        String arc = input.resolve(FirearmInput.DESTABILIZE_PRESS, FirearmCatalog.arcInductionLanceV881()).reason();
        String lime = input.resolve(FirearmInput.DESTABILIZE_PRESS, FirearmCatalog.limeSprayerV881()).reason();
        org.junit.jupiter.api.Assertions.assertTrue(arc.toLowerCase().contains("patada frontal"), "Lanza-Arcos: DESTABILIZE = patada frontal.");
        org.junit.jupiter.api.Assertions.assertTrue(lime.toLowerCase().contains("patada frontal"), "Rociador: DESTABILIZE = patada frontal.");
    }

    private static void verifyPcPs4FirearmParity() {
        Set<ControlAction> required = Set.of(
                ControlAction.TOGGLE_FIREARM_AIM, ControlAction.FIRE_FIREARM, ControlAction.RELOAD_FIREARM,
                ControlAction.ENTER_PNEUMATIC_PRESSURIZATION, ControlAction.ENTER_ELECTROMAGNETIC_MANUAL_CHARGE,
                ControlAction.ENTER_ARC_MANUAL_CHARGE, ControlAction.CYCLE_FIRE_MODE,
                ControlAction.HEAVY_ATTACK, ControlAction.CHARGED_ATTACK
        );
        for (ControlAction action : required) {
            org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == action),
                    "PC debe publicar " + action + ".");
            org.junit.jupiter.api.Assertions.assertTrue(Ps4ControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == action),
                    "PS4 debe publicar " + action + ".");
        }
        org.junit.jupiter.api.Assertions.assertTrue(Ps4ControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.FIRE_FIREARM
                        && b.gesture() == InputGesture.HOLD),
                "PS4 debe soportar HOLD para 3A/AA/rociado.");
        org.junit.jupiter.api.Assertions.assertTrue(Ps4ControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.CALL_PERSONAL_TRANSPORT)
                        && Ps4ControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.OPEN_PERSONAL_TRANSPORT_WHEEL),
                "PS4 debe conservar paridad del contrato de transporte de PC.");
    }

    private static void verifyArcDetachableSling() {
        ArcInductionFirearmItem arc = FirearmCatalog.arcInductionLanceV881();
        org.junit.jupiter.api.Assertions.assertTrue(arc.admitsAttachment(FirearmAccessoryMount.SLING), "El Lanza-Arcos debe admitir correa.");
        close(arc.weightKg(), 2.62, "Sin correa pesa 2,62 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(arc.mountAttachment(FirearmAccessoryCatalog.slingV881()), "Debe montar la correa desmontable.");
        close(arc.weightKg(), 2.80, "Con correa recupera los 2,80 kg del dossier.");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 0.000001) throw new AssertionError(message + " actual=" + actual + " expected=" + expected);
    }

    
}

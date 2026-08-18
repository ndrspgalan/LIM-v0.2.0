package qa.domain;

import domain.control.ControlAction;
import domain.control.PcControlScheme;
import domain.inventory.item.GripMode;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponGripEligibility;
import domain.inventory.item.WeaponGripEligibilityPolicy;
import domain.inventory.item.firearms.*;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;

import java.util.List;
import java.util.Set;

public final class FirearmsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyCanonicalRifle();
        verifyPneumaticCycle();
        verifyRecoilContract();
        verifyFireModes();
        verifyGripStrengthPolicy();
        verifyPackageSplitAndControls();
        verifyNarrative();
    }

    private static void verifyCanonicalRifle() {
        PneumaticFirearmItem rifle = FirearmCatalog.repeatingPneumaticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.name().equals("Rifle Neumático de Repetición V881"), "Nombre canónico incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.weightKg(), 4.50), "Peso incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.lengthMeters(), 1.20) && close(rifle.widthMeters(), 0.20), "Dimensiones incorrectas.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.footprint().verticalSlots() == 2 && rifle.footprint().horizontalSlots() == 12,
                "1 slot = 0,1 m debe producir 12 x 2.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.effectiveRangeMeters(), 150.0), "Alcance incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.caliber().equals(".46"), "Calibre incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.cartridgeDefinition().capacity() == 20 && rifle.ammunitionRemaining() == 20,
                "El cartucho debe contener 20 proyectiles.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.cartridgeDefinition().footprint().verticalSlots() == 3
                        && rifle.cartridgeDefinition().footprint().horizontalSlots() == 1
                        && rifle.cartridgeDefinition().maxStackSize() == 1,
                "El cartucho tubular debe ocupar 3x1 por XYZ y seguir siendo único.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.pneumaticCapacityShots() == 20 && rifle.pressureRemaining() == 20,
                "Una carga neumática debe proporcionar exactamente 20 disparos.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.pressureGaugePresent(), "El rifle debe incorporar medidor de presión.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.lethalityProfile().equals(new LethalityProfile(55, 0, 0)), "Letalidad incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.recoilVelocityPerShotMps(), 0.64), "Retroceso incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.fireModes().equals(List.of(FireMode.ONE_A)), "El rifle solo debe disponer de 1A.");
        org.junit.jupiter.api.Assertions.assertTrue(!rifle.supportsOneHanded() && rifle.supportsTwoHanded(), "El rifle es exclusivamente bimanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!rifle.wearsOut(), "Las armas de fuego no se desgastan.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.twoHandedRequirements().stream().anyMatch(r -> r.attribute().name().equals("FUERZA") && r.minimumValue() == 34),
                "4,50 kg a dos manos debe requerir FUERZA 34.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.twoHandedRequirements().stream().anyMatch(r -> r.attribute().name().equals("DESTREZA") && r.minimumValue() == 12),
                "1,20 m debe requerir DESTREZA 12.");
    }

    private static void verifyPneumaticCycle() {
        PneumaticFirearmItem rifle = FirearmCatalog.repeatingPneumaticRifleV881();
        FirearmInputResolutionPolicy policy = new FirearmInputResolutionPolicy();

        policy.resolve(FirearmInput.LEFT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.handlingState() == FirearmHandlingState.AIMING, "LEFT CLICK debe activar apuntado.");
        policy.resolve(FirearmInput.LEFT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.handlingState() == FirearmHandlingState.NORMAL, "LEFT CLICK debe desactivar apuntado.");

        for (int i = 0; i < 20; i++) {
            FirearmActionResult fired = policy.resolve(FirearmInput.RIGHT_PRESS, rifle);
            org.junit.jupiter.api.Assertions.assertTrue(fired.allowed() && fired.shotsFired() == 1, "1A debe disparar una bala por pulsación.");
            policy.resolve(FirearmInput.RIGHT_RELEASE, rifle);
        }
        org.junit.jupiter.api.Assertions.assertTrue(rifle.ammunitionRemaining() == 0 && rifle.pressureRemaining() == 0,
                "Veinte disparos deben consumir cartucho y carga neumática completos.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.effectiveRangeMeters(), 150.0) && close(rifle.lethalityProfile().piercing(), 55.0),
                "La presión no degrada alcance ni letalidad.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(FirearmInput.RIGHT_PRESS, rifle).allowed(), "Con presión 0 el arma no dispara.");

        policy.resolve(FirearmInput.RELOAD_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.ammunitionRemaining() == 20 && rifle.pressureRemaining() == 0,
                "R cambia solo el cartucho, no presuriza el depósito.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(FirearmInput.RIGHT_PRESS, rifle).allowed(),
                "Munición sin presión no debe permitir disparar.");

        FirearmActionResult enter = policy.resolve(FirearmInput.RELOAD_HOLD, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(enter.allowed() && rifle.handlingState() == FirearmHandlingState.PNEUMATIC_PRESSURIZATION,
                "HOLD R debe hacer que el personaje sujete el mecanismo neumático.");
        for (int i = 0; i < 19; i++) {
            policy.resolve(FirearmInput.RIGHT_PRESS, rifle);
        }
        org.junit.jupiter.api.Assertions.assertTrue(rifle.pressureRemaining() == 19
                        && rifle.handlingState() == FirearmHandlingState.PNEUMATIC_PRESSURIZATION,
                "A mitad de carga debe mantenerse el estado de presurización.");
        policy.resolve(FirearmInput.RELOAD_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.handlingState() == FirearmHandlingState.NORMAL && rifle.pressureRemaining() == 19,
                "R debe cancelar la presurización sin perder la presión obtenida.");

        policy.resolve(FirearmInput.RELOAD_HOLD, rifle);
        policy.resolve(FirearmInput.RIGHT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.pressureRemaining() == 20 && rifle.handlingState() == FirearmHandlingState.NORMAL,
                "Al alcanzar presión máxima debe producirse fallback automático al agarre normal.");
        FirearmActionResult fullFallback = policy.resolve(FirearmInput.RELOAD_HOLD, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(fullFallback.allowed() && rifle.handlingState() == FirearmHandlingState.NORMAL,
                "HOLD R con presión máxima debe volver al agarre normal como fallback.");

        FirearmActionResult destabilize = policy.resolve(FirearmInput.DESTABILIZE_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(destabilize.action() == FirearmAction.DESTABILIZE,
                "DESTABILIZE debe conservar su semántica y materializarse como culatazo.");
    }

    private static void verifyRecoilContract() {
        PneumaticFirearmItem rifle = FirearmCatalog.repeatingPneumaticRifleV881();
        FirearmInputResolutionPolicy policy = new FirearmInputResolutionPolicy();
        policy.resolve(FirearmInput.RIGHT_PRESS, rifle);
        policy.resolve(FirearmInput.RIGHT_RELEASE, rifle);
        policy.resolve(FirearmInput.RIGHT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.recoilState().accumulatedVelocityMps(), 1.28),
                "El retroceso debe acumular 0,64 m/s por cada disparo.");
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.recoilState().accumulatedVelocityMps(), 1.28),
                "No existe recuperación automática del retroceso.");
        rifle.recoilState().stabilizeByPlayer(0.50);
        org.junit.jupiter.api.Assertions.assertTrue(close(rifle.recoilState().accumulatedVelocityMps(), 0.78),
                "Solo una compensación explícita del jugador debe estabilizar el arma.");
    }

    private static void verifyFireModes() {
        FirearmInputResolutionPolicy policy = new FirearmInputResolutionPolicy();
        FirearmItem burst = firearmWithMode(FireMode.THREE_A);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_PRESS, burst).shotsFired() == 1, "3A: primer disparo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_HOLD, burst).shotsFired() == 1, "3A: segundo disparo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_HOLD, burst).shotsFired() == 1, "3A: tercer disparo.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(FirearmInput.RIGHT_HOLD, burst).allowed(),
                "3A debe exigir una nueva pulsación después de tres balas.");
        policy.resolve(FirearmInput.RIGHT_RELEASE, burst);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_PRESS, burst).shotsFired() == 1,
                "3A debe reiniciarse con una nueva pulsación.");

        FirearmItem automatic = firearmWithMode(FireMode.AUTO_A);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_PRESS, automatic).shotsFired() == 1, "AA: primer disparo.");
        for (int i = 0; i < 5; i++) {
            org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(FirearmInput.RIGHT_HOLD, automatic).shotsFired() == 1,
                    "AA debe continuar mientras RIGHT CLICK permanezca pulsado.");
        }
        policy.resolve(FirearmInput.RIGHT_RELEASE, automatic);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(FirearmInput.RIGHT_HOLD, automatic).allowed(),
                "AA debe detenerse al soltar RIGHT CLICK.");
    }

    private static void verifyGripStrengthPolicy() {
        org.junit.jupiter.api.Assertions.assertTrue(WeaponGripEligibilityPolicy.resolve(4.50, true, true, 45, Set.of())
                        == WeaponGripEligibility.ONE_OR_TWO_HANDED,
                "Con FUERZA 45 un arma de 4,5 kg compatible debe admitir 1H/2H.");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponGripEligibilityPolicy.resolve(4.50, true, true, 34, Set.of())
                        == WeaponGripEligibility.FORCED_TWO_HANDED,
                "Con FUERZA 34 debe forzarse el agarre a dos manos.");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponGripEligibilityPolicy.resolve(4.50, true, true, 33, Set.of())
                        == WeaponGripEligibility.CANNOT_WIELD,
                "Con FUERZA 33 no debe poder empuñarse.");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.repeatingPneumaticRifleV881().gripEligibilityForStrength(34)
                        == WeaponGripEligibility.TWO_HANDED_ONLY,
                "El rifle V881 debe poder empuñarse exclusivamente a dos manos con FUERZA 34.");
    }

    private static void verifyPackageSplitAndControls() {
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.all().stream().noneMatch(item ->
                        item.name().contains("Girandoni") || item.name().equals("Fasce") || item.name().contains("Relé")),
                "rangedWeapons no debe conservar armas de fuego mal clasificadas.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("LEFT CLICK") && binding.action() == ControlAction.TOGGLE_FIREARM_AIM),
                "LEFT CLICK debe alternar apuntado para armas de fuego.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("RIGHT CLICK") && binding.action() == ControlAction.FIRE_FIREARM),
                "RIGHT CLICK debe disparar armas de fuego.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("R") && binding.action() == ControlAction.RELOAD_FIREARM),
                "R debe recargar cartucho.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("R") && binding.action() == ControlAction.ENTER_PNEUMATIC_PRESSURIZATION),
                "HOLD R debe entrar en manipulación neumática.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("MOUSE WHEEL") && binding.action() == ControlAction.CYCLE_FIRE_MODE),
                "La rueda debe cambiar cadencia en armas de fuego.");
    }

    private static void verifyNarrative() {
        String narrative = FirearmCatalog.REPEATING_PNEUMATIC_RIFLE_NARRATIVE;
        org.junit.jupiter.api.Assertions.assertTrue(narrative.contains("guerras napoleónicas"), "La narrativa debe conservar el origen histórico acordado.");
        org.junit.jupiter.api.Assertions.assertTrue(narrative.contains("cartucho tubular lateral contiene veinte proyectiles calibre .46 de plomo"),
                "La narrativa debe conservar la especificación canónica.");
        org.junit.jupiter.api.Assertions.assertTrue(!narrative.contains("El estándar V881 no sustituyó"),
                "La frase descartada no puede reaparecer en el código.");
    }

    private static FirearmItem firearmWithMode(FireMode mode) {
        FirearmCartridge cartridge = new FirearmCartridge("Cartucho de prueba", ".46", 20);
        return new FirearmItem(
                "Arma de prueba", "Arma de prueba para verificar cadencias.", 2.0, 0.8, 0.2,
                50, ".46", cartridge, new LethalityProfile(10, 0, 0), 0.5,
                List.of(mode), true, true, Set.of()
        );
    }

    private static boolean close(double a, double b) {
        return Math.abs(a - b) < 0.000001;
    }

    
}

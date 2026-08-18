package qa.integration;

import domain.character.sheet.Attribute;
import domain.combat.MirrorParryPolicy;
import domain.combat.ParryTargetEligibilityPolicy;
import domain.control.ControlAction;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.*;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public final class WeaponsShieldsControlsVerification {
    private WeaponsShieldsControlsVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifySingleHandSymmetryAndOneHandLimits();
        verifyDualWieldAndShields();
        verifyParryAndMirrorParryEligibility();
        verifyUnarmedFallback();
        verifyControls();
    }

    private static void verifySingleHandSymmetryAndOneHandLimits() {
        WeaponItem weapon = shortWeapon("Daga", false);
        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();

        ResolvedWeaponHandling leftOnly = new ResolvedWeaponHandling(
                ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                ResolvedHand.active(EquipmentSlot.LEFT_HAND, weapon, weapon.currentConfiguration()),
                WieldingState.SINGLE_WIELD
        );
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.RIGHT_PRESS, leftOnly, false).action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "Un arma única en la izquierda debe atacar con esa mano.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.DESTABILIZE_PRESS, leftOnly, false).action().orElseThrow() == WeaponCombatAction.DESTABILIZE,
                "Un arma a una mano debe disponer de golpe desestabilizador.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(WeaponInput.HEAVY_PRESS, leftOnly, false).allowed(),
                "Un arma a una mano no puede ejecutar ataque fuerte.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(WeaponInput.CHARGED_HOLD, leftOnly, false).allowed(),
                "Un arma a una mano no puede ejecutar ataque cargado.");
    }

    private static void verifyDualWieldAndShields() {
        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();
        WeaponItem right = shortWeapon("Derecha", false);
        WeaponItem left = shortWeapon("Izquierda", false);
        ResolvedWeaponHandling dual = dual(right, left);

        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.RIGHT_PRESS, dual, false).action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "La derecha debe usar ataque ligero principal.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.LEFT_PRESS, dual, false).action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "La izquierda debe usar ataque ligero alternativo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.DESTABILIZE_PRESS, dual, false).action().orElseThrow() == WeaponCombatAction.DESTABILIZE,
                "ALT IZQ debe resolver golpe desestabilizador con la derecha.");

        WeaponItem shield = shield("Escudo");
        ResolvedWeaponHandling shieldLeft = dual(right, shield);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.LEFT_HOLD, shieldLeft, false).action().orElseThrow() == WeaponCombatAction.BLOCK,
                "El escudo izquierdo debe bloquear en modo alternativo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.LEFT_PRESS, shieldLeft, true).action().orElseThrow() == WeaponCombatAction.BLOCK,
                "Desde  el escudo dedicado bloquea dentro de ventana; no hace PARRY.");

        ResolvedWeaponHandling shieldRight = dual(shield, left);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.RIGHT_PRESS, shieldRight, false).action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "El escudo derecho debe arrollar en modo principal.");
    }

    private static void verifyParryAndMirrorParryEligibility() {
        ParryTargetEligibilityPolicy eligibility = new ParryTargetEligibilityPolicy();
        WeaponItem ordinary = shortWeapon("Sable", false);
        WeaponItem rotor = shortWeapon("Rotor", true);
        WeaponItem shield = shield("Escudo rival");
        WeaponItem twoHanded = twoHandedWeapon("Mandoble");
        WeaponItem bluntOnly = bluntWeapon("Maza");

        org.junit.jupiter.api.Assertions.assertTrue(eligibility.isEligible(ordinary), "Un arma cortante no exclusiva a dos manos debe admitir parry.");
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.isEligible(rotor), "DE ROTOR debe impedir parry.");
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.isEligible(shield), "Un escudo no admite parry como objetivo.");
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.isEligible(twoHanded), "Un arma exclusivamente bimanual no admite parry.");
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.isEligible(bluntOnly), "Sin perfil cortante no puede haber parry.");

        WeaponItem helicoidal = helicoidal();
        MirrorParryPolicy mirror = new MirrorParryPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(mirror.canMirrorParry(helicoidal, GripMode.ONE_HANDED, WeaponCombatAction.LIGHT_ATTACK,
                        ordinary, GripMode.ONE_HANDED, WeaponCombatAction.JUMP_ATTACK, true),
                "MirrorParry debe depender de colisión ofensiva y elegibilidad, no de ataques idénticos.");
        org.junit.jupiter.api.Assertions.assertTrue(!mirror.canMirrorParry(helicoidal, GripMode.ONE_HANDED, WeaponCombatAction.LIGHT_ATTACK,
                        rotor, GripMode.ONE_HANDED, WeaponCombatAction.LIGHT_ATTACK, true),
                "MirrorParry debe compartir la exclusión DE ROTOR.");
        org.junit.jupiter.api.Assertions.assertTrue(!helicoidal.combatActionsFor(WeaponActionMode.PRIMARY).contains(WeaponCombatAction.PARRY),
                "La Espada Helicoidal no debe tener PARRY defensivo ordinario por su MirrorParry.");
        org.junit.jupiter.api.Assertions.assertTrue(helicoidal.combatActionsFor(WeaponActionMode.PRIMARY).contains(WeaponCombatAction.DESTABILIZE)
                        && helicoidal.combatActionsFor(WeaponActionMode.ALTERNATIVE).contains(WeaponCombatAction.DESTABILIZE),
                "La Espada Helicoidal debe desestabilizar a una y dos manos.");
    }

    private static void verifyUnarmedFallback() {
        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();
        ResolvedWeaponHandling unarmed = new ResolvedWeaponHandling(
                ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                ResolvedHand.empty(EquipmentSlot.LEFT_HAND),
                WieldingState.UNARMED
        );
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.LEFT_PRESS, unarmed, false).action().orElseThrow() == WeaponCombatAction.DESTABILIZE,
                "DESARMADO debe actuar como fallback defensivo cuando ambas manos están libres.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(WeaponInput.LEFT_HOLD, unarmed, false).action().orElseThrow() == WeaponCombatAction.BLOCK,
                "DESARMADO bloquea con guardia facial mantenida.");
        WeaponItem unarmedItem = UnarmedWeaponFactory.create();
        org.junit.jupiter.api.Assertions.assertTrue(unarmedItem.currentConfiguration().gripMode() == GripMode.TWO_HANDED
                        && unarmedItem.availableConfigurations().size() == 2
                        && unarmedItem.availableConfigurations().stream().allMatch(c -> c.gripMode() == GripMode.TWO_HANDED),
                "DESARMADO conserva dos guardias lógicas PRIMARY/ALTERNATIVE, ambas bimanuales.");
    }

    private static void verifyControls() {
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("ALT IZQ") && binding.action() == ControlAction.HEAVY_ATTACK),
                "PC debe asignar ALT IZQ al ataque fuerte.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("LEFT CLICK") && binding.action() == ControlAction.PARRY),
                "PC debe separar PARRY de GOLPE DESESTABILIZADOR.");
        org.junit.jupiter.api.Assertions.assertTrue(Ps4ControlScheme.canonicalBindings().stream().anyMatch(binding ->
                        binding.input().equals("R2") && binding.action() == ControlAction.HEAVY_ATTACK),
                "PS4 debe disponer de equivalente para ataque fuerte.");
    }

    private static ResolvedWeaponHandling dual(WeaponItem right, WeaponItem left) {
        return new ResolvedWeaponHandling(
                ResolvedHand.active(EquipmentSlot.RIGHT_HAND, right,
                        DualWieldConfigurationPolicy.rightHandConfiguration(right)),
                ResolvedHand.active(EquipmentSlot.LEFT_HAND, left,
                        DualWieldConfigurationPolicy.leftHandConfiguration(left)),
                WieldingState.DUAL_WIELD
        );
    }

    private static WeaponItem shortWeapon(String name, boolean rotor) {
        WeaponItem item = weapon(name, .4, .5,
                new LethalityProfile(10, 20, 5),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
                )), rotor ? Set.of(WeaponTrait.DE_ROTOR) : Set.of());
        return item.withCombatPolicy(WeaponCombatPolicy.dagger());
    }

    private static WeaponItem shield(String name) {
        return weapon(name, .45, .9, new LethalityProfile(0, 0, 20),
                WeaponConfigurationPolicy.shield(), Set.of(WeaponTrait.SHIELD))
                .withCombatPolicy(WeaponCombatPolicy.shield());
    }

    private static WeaponItem twoHandedWeapon(String name) {
        return weapon(name, 1.2, 3, new LethalityProfile(10, 50, 30),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY)
                )), Set.of());
    }

    private static WeaponItem bluntWeapon(String name) {
        return weapon(name, .4, .8, new LethalityProfile(0, 0, 40),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY)
                )), Set.of());
    }

    private static WeaponItem helicoidal() {
        return weapon("Espada Helicoidal", 1.48, 2.9, new LethalityProfile(35, 90, 25),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.ALTERNATIVE)
                )), Set.of(WeaponTrait.HELICOIDAL_CONTROL))
                .withCombatActionsFor(WeaponActionMode.PRIMARY, Set.of(
                        WeaponCombatAction.LIGHT_ATTACK,
                        WeaponCombatAction.JUMP_ATTACK,
                        WeaponCombatAction.DESTABILIZE
                ))
                .withCombatActionsFor(WeaponActionMode.ALTERNATIVE, Set.of(
                        WeaponCombatAction.LIGHT_ATTACK,
                        WeaponCombatAction.HEAVY_ATTACK,
                        WeaponCombatAction.CHARGED_ATTACK,
                        WeaponCombatAction.JUMP_ATTACK,
                        WeaponCombatAction.DESTABILIZE
                ));
    }

    private static WeaponItem weapon(
            String name, double reach, double weight, LethalityProfile lethality,
            WeaponConfigurationPolicy configurations, Set<WeaponTrait> traits
    ) {
        return new WeaponItem(
                name, name, weight, new InventoryFootprint(1, 1), reach,
                List.of(new WeaponMode("Modo", lethality)),
                List.of(new AttributeRequirement(Attribute.FUERZA, 1)),
                List.of(), List.of(), OptionalDouble.empty(), 0, false,
                configurations, traits
        );
    }

    
}

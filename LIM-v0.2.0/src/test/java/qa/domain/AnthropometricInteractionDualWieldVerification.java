package qa.domain;

import domain.interaction.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public final class AnthropometricInteractionDualWieldVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        anthropometricInteraction();
        contextualActionCycling();
        interdependentDualWieldCombo();
        limitedSecondaryIdentity();
    }

    private static void anthropometricInteraction() {
        AnthropometricInteractionReachPolicy policy = new AnthropometricInteractionReachPolicy();
        double kenanReach = policy.reachMeters(1.72);
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(kenanReach - 0.7568) < 0.00001,
                "El alcance de Kenan debe ser proporcional a sus 1,72 m de altura.");
        SpatialPoint actor = new SpatialPoint(0, 0, 1.2);
        ForwardDirection forward = new ForwardDirection(0, 1, 0);
        org.junit.jupiter.api.Assertions.assertTrue(policy.canReach(1.72, actor, forward, new SpatialPoint(0, .70, 1.2)),
                "Una hitbox frontal dentro del brazo debe ser interactuable.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canReach(1.72, actor, forward, new SpatialPoint(0, .90, 1.2)),
                "Una hitbox fuera del alcance antropométrico no debe ser interactuable.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canReach(1.72, actor, forward, new SpatialPoint(0, -.30, 1.2)),
                "Una hitbox situada detrás no debe estar disponible aunque esté cerca.");
    }

    private static void contextualActionCycling() {
        InteractionHitbox campfire = new InteractionHitbox("fogata", new SpatialPoint(0, .5, 0),
                List.of(InteractionAction.USE, InteractionAction.SLEEP));
        ContextualInteractionState state = new ContextualInteractionState();
        org.junit.jupiter.api.Assertions.assertTrue(state.refresh(campfire, true), "La interacción debe abrirse cuando la hitbox es alcanzable.");
        org.junit.jupiter.api.Assertions.assertTrue(state.selectedAction().orElseThrow() == InteractionAction.USE,
                "La primera acción debe quedar seleccionada.");
        org.junit.jupiter.api.Assertions.assertTrue(state.cycle().orElseThrow() == InteractionAction.SLEEP,
                "Q debe ciclar únicamente las acciones de la hitbox alcanzable.");
        state.refresh(campfire, false);
        org.junit.jupiter.api.Assertions.assertTrue(!state.interactionAvailable() && state.cycle().isEmpty(),
                "Al salir del alcance deben desaparecer Interactuar y el ciclado con Q.");
    }

    private static void interdependentDualWieldCombo() {
        WeaponItem primary = weapon("Principal").withLightAttackComboFor(WeaponActionMode.PRIMARY, 4);
        WeaponItem secondary = weapon("Secundaria").withLightAttackComboFor(WeaponActionMode.ALTERNATIVE, 3);
        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();
        DualWieldComboState state = new DualWieldComboState();

        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.RIGHT_PRESS, primary, secondary, true, false, state)) == 1,
                "La principal debe abrir el combo global en 1.");
        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.LEFT_PRESS, primary, secondary, true, false, state)) == 2,
                "La secundaria debe heredar el ordinal global 2.");
        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.RIGHT_PRESS, primary, secondary, true, false, state)) == 3,
                "La principal debe continuar por el ordinal 3.");
        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.LEFT_PRESS, primary, secondary, true, false, state)) == 1,
                "La secundaria debe reiniciar en 1 si carece de ataque ligero 4.");
        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.RIGHT_PRESS, primary, secondary, true, false, state)) == 2,
                "Tras el reinicio efectivo, la secuencia compartida debe continuar en 2.");
    }

    private static void limitedSecondaryIdentity() {
        WeaponItem primary = weapon("Principal").withLightAttackComboFor(WeaponActionMode.PRIMARY, 5);
        WeaponItem secondary = weapon("Secundaria").withLightAttackComboFor(WeaponActionMode.ALTERNATIVE, 5);
        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();
        DualWieldComboState state = new DualWieldComboState();
        policy.resolve(WeaponInput.RIGHT_PRESS, primary, secondary, true, false, state);

        secondary.withCombatActionsFor(WeaponActionMode.ALTERNATIVE, Set.of(WeaponCombatAction.PARRY));
        var parry = policy.resolve(WeaponInput.LEFT_PRESS, primary, secondary, true, true, state);
        org.junit.jupiter.api.Assertions.assertTrue(parry.action().orElseThrow() == WeaponCombatAction.PARRY,
                "El repertorio exclusivo de la secundaria debe conservarse.");
        org.junit.jupiter.api.Assertions.assertTrue(state.nextLightAttackOrdinal() == 2,
                "Una acción secundaria no ligera no debe consumir el combo ligero compartido.");

        org.junit.jupiter.api.Assertions.assertTrue(ordinal(policy.resolve(WeaponInput.RIGHT_PRESS, primary, secondary, true, false, state)) == 2,
                "La principal debe conservar la continuidad ligera tras una acción exclusiva secundaria.");
    }

    private static int ordinal(WeaponInputResolution result) {
        org.junit.jupiter.api.Assertions.assertTrue(result.allowed() && result.action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "Se esperaba un ataque ligero permitido.");
        return result.lightAttackOrdinal().orElseThrow();
    }

    private static WeaponItem weapon(String name) {
        return new WeaponItem(name, "Arma de verificación.", 1, new InventoryFootprint(1, 1), 1,
                List.of(new WeaponMode("Base", new LethalityProfile(1, 1, 1))),
                List.of(), List.of(), List.of(), OptionalDouble.empty(), 0, false,
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE))), Set.of())
                .withCombatPolicy(new WeaponCombatPolicy(Set.of(
                        WeaponCombatAction.LIGHT_ATTACK,
                        WeaponCombatAction.HEAVY_ATTACK,
                        WeaponCombatAction.CHARGED_ATTACK,
                        WeaponCombatAction.JUMP_ATTACK)));
    }

    
}

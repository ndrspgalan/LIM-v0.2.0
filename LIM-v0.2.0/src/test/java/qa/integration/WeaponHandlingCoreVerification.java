package qa.integration;

import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

public final class WeaponHandlingCoreVerification {
    private WeaponHandlingCoreVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        slotsRepresentPhysicalHands();
        leftHandLimitsAreConjunctive();
        singleWieldPreservesSelectedModeInEitherHand();
        dualWieldForcesCanonicalModes();
        dualWieldExitStowsLeftAndTransformsRight();
    }

    private static void slotsRepresentPhysicalHands() {
        org.junit.jupiter.api.Assertions.assertTrue(EquipmentSlot.valueOf("RIGHT_HAND") == EquipmentSlot.RIGHT_HAND,
                "Debe existir RIGHT_HAND.");
        org.junit.jupiter.api.Assertions.assertTrue(EquipmentSlot.valueOf("LEFT_HAND") == EquipmentSlot.LEFT_HAND,
                "Debe existir LEFT_HAND.");
        org.junit.jupiter.api.Assertions.assertTrue(java.util.Arrays.stream(EquipmentSlot.values())
                        .noneMatch(slot -> slot.name().contains("PRIMARY_WEAPON") || slot.name().contains("SECONDARY_WEAPON")),
                "No deben sobrevivir las ranuras jerárquicas antiguas.");
    }

    private static void leftHandLimitsAreConjunctive() {
        WeaponItem valid = weapon("Válida", .50, 1.0, dualPolicy());
        new EquipmentState(Map.of(EquipmentSlot.LEFT_HAND, valid));

        expectFailure(() -> new EquipmentState(Map.of(
                EquipmentSlot.LEFT_HAND, weapon("Demasiado larga", .51, .5, dualPolicy()))));
        expectFailure(() -> new EquipmentState(Map.of(
                EquipmentSlot.LEFT_HAND, weapon("Demasiado pesada", .4, 1.01, dualPolicy()))));

        WeaponItem exceptional = weapon("Excepción tipada", 2.0, 4.0, dualPolicy())
                .allowLeftHandLimitException();
        new EquipmentState(Map.of(EquipmentSlot.LEFT_HAND, exceptional));
    }

    private static void singleWieldPreservesSelectedModeInEitherHand() {
        WeaponItem right = weapon("Diestra", .4, .5, dualPolicy());
        right.selectActionMode(WeaponActionMode.ALTERNATIVE);
        ResolvedWeaponHandling rightState = WeaponHandlingResolver.resolve(
                new EquipmentState(Map.of(EquipmentSlot.RIGHT_HAND, right)), false);
        org.junit.jupiter.api.Assertions.assertTrue(rightState.wieldingState() == WieldingState.SINGLE_WIELD,
                "Una única arma derecha debe resolverse como SINGLE_WIELD.");
        org.junit.jupiter.api.Assertions.assertTrue(rightState.rightHand().effectiveConfiguration().orElseThrow().actionMode()
                        == WeaponActionMode.ALTERNATIVE,
                "El arma única derecha debe conservar el modo elegido.");

        WeaponItem left = weapon("Zurda", .4, .5, dualPolicy());
        left.selectActionMode(WeaponActionMode.ALTERNATIVE);
        ResolvedWeaponHandling leftState = WeaponHandlingResolver.resolve(
                new EquipmentState(Map.of(EquipmentSlot.LEFT_HAND, left)), false);
        org.junit.jupiter.api.Assertions.assertTrue(leftState.leftHand().effectiveConfiguration().orElseThrow().actionMode()
                        == WeaponActionMode.ALTERNATIVE,
                "El arma única izquierda debe conservar el modo elegido.");
    }

    private static void dualWieldForcesCanonicalModes() {
        WeaponItem right = weapon("Derecha", .4, .5, dualPolicy());
        WeaponItem left = weapon("Izquierda", .4, .5, dualPolicy());
        right.selectActionMode(WeaponActionMode.ALTERNATIVE);
        left.selectActionMode(WeaponActionMode.PRIMARY);

        EquipmentState equipment = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, right,
                EquipmentSlot.LEFT_HAND, left));
        DualWieldConfigurationPolicy.activate(right, left);
        ResolvedWeaponHandling state = WeaponHandlingResolver.resolve(equipment, true);

        org.junit.jupiter.api.Assertions.assertTrue(state.rightHand().effectiveConfiguration().orElseThrow().equals(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY)),
                "La mano derecha debe quedar forzada a modo principal a una mano.");
        org.junit.jupiter.api.Assertions.assertTrue(state.leftHand().effectiveConfiguration().orElseThrow().equals(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)),
                "La mano izquierda debe quedar forzada a modo alternativo a una mano.");
    }

    private static void dualWieldExitStowsLeftAndTransformsRight() {
        WeaponItem right = weapon("Derecha", .4, .5, dualPolicy());
        WeaponItem left = weapon("Izquierda", .4, .5, dualPolicy());
        DualWieldConfigurationPolicy.activate(right, left);

        DualWieldExitTransition transition = DualWieldConfigurationPolicy.exitToSingleRight(right, left);
        org.junit.jupiter.api.Assertions.assertTrue(transition.leftHandDisposition() == HandDisposition.STOWED,
                "La salida debe guardar el objeto izquierdo, sea arma o escudo.");
        org.junit.jupiter.api.Assertions.assertTrue(transition.rightHandConfiguration().actionMode() == WeaponActionMode.ALTERNATIVE,
                "La pieza derecha debe pasar a modo alternativo.");
    }

    private static WeaponConfigurationPolicy dualPolicy() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }

    private static WeaponItem weapon(
            String name,
            double reach,
            double weight,
            WeaponConfigurationPolicy policy
    ) {
        return new WeaponItem(
                name,
                "Arma de verificación.",
                weight,
                new InventoryFootprint(1, 1),
                reach,
                List.of(new WeaponMode("Prueba", new LethalityProfile(1, 1, 1))),
                List.of(),
                List.of(),
                List.of(),
                OptionalDouble.empty(),
                0,
                false,
                policy,
                Set.of()
        );
    }

    private static void expectFailure(Runnable operation) {
        try {
            operation.run();
            throw new AssertionError("Se esperaba una validación fallida.");
        } catch (IllegalArgumentException expected) {
            // Esperado.
        }
    }

    
}

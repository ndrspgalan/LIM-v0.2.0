package qa.architecture;

import domain.combat.ai.loadout.CombatLoadoutResolver;
import domain.combat.ai.loadout.VisibleLoadout;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

public final class ConsoleAndAiVerification {
    private ConsoleAndAiVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        aiResolvesEitherPhysicalHand();
        aiResolvesCanonicalDualWield();
        handlingIgnoresStowedLeftObject();
        consoleUsesResolvedHandlingAndCanonicalInputs();
    }

    private static void aiResolvesEitherPhysicalHand() {
        CombatLoadoutResolver resolver = new CombatLoadoutResolver();
        WeaponItem left = weapon("Zurda");
        var resolved = resolver.resolve(VisibleLoadout.of(null, left));
        org.junit.jupiter.api.Assertions.assertTrue(resolved.handling().leftHand().weapon().orElseThrow() == left,
                "La IA debe resolver un arma situada únicamente en LEFT_HAND.");
        org.junit.jupiter.api.Assertions.assertTrue(resolved.attackingWeapon() == left,
                "La pieza atacante debe ser la única arma activa, aunque esté en la izquierda.");
    }

    private static void aiResolvesCanonicalDualWield() {
        CombatLoadoutResolver resolver = new CombatLoadoutResolver();
        WeaponItem right = weapon("Diestra");
        WeaponItem left = weapon("Zurda");
        var resolved = resolver.resolve(VisibleLoadout.of(right, left));
        org.junit.jupiter.api.Assertions.assertTrue(resolved.dualWielding(), "Dos armas compatibles deben resolverse como dual wielding.");
        org.junit.jupiter.api.Assertions.assertTrue(resolved.handling().rightHand().effectiveConfiguration().orElseThrow().actionMode()
                        == WeaponActionMode.PRIMARY,
                "La derecha debe operar en modo principal.");
        org.junit.jupiter.api.Assertions.assertTrue(resolved.handling().leftHand().effectiveConfiguration().orElseThrow().actionMode()
                        == WeaponActionMode.ALTERNATIVE,
                "La izquierda debe operar en modo alternativo.");
    }

    private static void handlingIgnoresStowedLeftObject() {
        WeaponItem right = weapon("Activa");
        WeaponItem left = weapon("Guardada");
        left.stowForHandlingTransition();
        EquipmentState equipment = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, right,
                EquipmentSlot.LEFT_HAND, left
        ));
        var handling = WeaponHandlingResolver.resolve(equipment, false);
        org.junit.jupiter.api.Assertions.assertTrue(handling.wieldingState() == WieldingState.SINGLE_WIELD,
                "Un objeto izquierdo guardado no debe contar como mano activa.");
        org.junit.jupiter.api.Assertions.assertTrue(handling.leftHand().disposition() == HandDisposition.EMPTY,
                "La proyección de combate debe dejar libre la mano cuyo objeto está guardado.");
    }

    private static void consoleUsesResolvedHandlingAndCanonicalInputs() throws Exception {
        String source = Files.readString(Path.of("src/main/java/presentation/console/GameplayConsole.java"));
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("WeaponHandlingResolver.resolve"),
                "GameplayConsole debe consumir el resolvedor central de manejo.");
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("WeaponInput.DESTABILIZE_PRESS"),
                "GameplayConsole debe exponer la entrada canónica de golpe desestabilizador.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("activeWeaponSlot"),
                "GameplayConsole no debe conservar una ranura de arma activa jerárquica.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("PARRY_OR_DESTABILIZE"),
                "GameplayConsole no debe conservar el adaptador no canónico.");
    }

    private static WeaponItem weapon(String name) {
        return new WeaponItem(
                name,
                "Arma de verificación.",
                .8,
                new InventoryFootprint(1, 1),
                .4,
                List.of(new WeaponMode("Prueba", new LethalityProfile(2, 2, 2))),
                List.of(),
                List.of(),
                List.of(),
                OptionalDouble.of(.5),
                1,
                false,
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
                )),
                Set.of()
        );
    }

    
}

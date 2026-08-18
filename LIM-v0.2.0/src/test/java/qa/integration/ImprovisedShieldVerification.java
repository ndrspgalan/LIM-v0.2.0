package qa.integration;

import domain.combat.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorPiece;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

public final class ImprovisedShieldVerification {
    private ImprovisedShieldVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyActivationContract();
        verifyMeleeTwelveEightyEightResolution();
        verifyProjectileInterception();
        verifyStableAccessoryIdentity();
    }

    private static void verifyActivationContract() {
        ArmorPiece bracers = ArmorCatalog.historicalEbonyWarriorBracers();
        org.junit.jupiter.api.Assertions.assertTrue(bracers.supportsImprovisedBlock(), "Los brazales de ébano deben declarar ESCUDO IMPROVISADO.");
        org.junit.jupiter.api.Assertions.assertTrue(bracers.properties().stream().anyMatch(p -> p.id() == ItemPropertyId.IMPROVISED_SHIELD),
                "La propiedad debe tener identidad mecánica estable.");

        WeaponItem oneHanded = weapon(GripMode.ONE_HANDED, false);
        EquipmentState valid = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, oneHanded,
                EquipmentSlot.BRACERS, bracers));
        org.junit.jupiter.api.Assertions.assertTrue(new ImprovisedBracerBlockPolicy().canBlock(valid),
                "Una mano y secundaria libre deben habilitar BLOCK.");

        EquipmentState twoHanded = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, weapon(GripMode.TWO_HANDED, false),
                EquipmentSlot.BRACERS, ArmorCatalog.historicalEbonyWarriorBracers()));
        org.junit.jupiter.api.Assertions.assertTrue(!new ImprovisedBracerBlockPolicy().canBlock(twoHanded),
                "Un arma a dos manos debe impedir el bloqueo.");

        EquipmentState occupied = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, weapon(GripMode.ONE_HANDED, false),
                EquipmentSlot.LEFT_HAND, weapon(GripMode.ONE_HANDED, false),
                EquipmentSlot.BRACERS, ArmorCatalog.historicalEbonyWarriorBracers()));
        org.junit.jupiter.api.Assertions.assertTrue(!new ImprovisedBracerBlockPolicy().canBlock(occupied),
                "Una secundaria desenvainada debe impedir el bloqueo.");
    }

    private static void verifyMeleeTwelveEightyEightResolution() {
        ArmorPiece bracers = ArmorCatalog.historicalEbonyWarriorBracers();
        EquipmentState equipment = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, weapon(GripMode.ONE_HANDED, false),
                EquipmentSlot.BRACERS, bracers));
        double before = bracers.currentBluntProtection();
        var result = new ImprovisedBracerMeleeBlockResolver().resolve(new PhysicalDamage(100, 100, 100), equipment);
        org.junit.jupiter.api.Assertions.assertTrue(close(result.armorCoveredRatio(), 0.05) && close(result.resistancePendingRatio(), 0.95),
                "El bloqueo cuerpo a cuerpo debe conservar la división 5/95.");
        org.junit.jupiter.api.Assertions.assertTrue(close(result.damageAfterArmor().piercing(), 96.25)
                        && close(result.damageAfterArmor().slashing(), 97.25)
                        && close(result.damageAfterArmor().blunt(), 97.0),
                "La rama cubierta debe aplicar mitigación porcentual P75/C55/B60 y conservar el 95 % descubierto.");
        org.junit.jupiter.api.Assertions.assertTrue(close(bracers.currentBluntProtection(), before), "El bloqueo no debe degradar el ébano.");
    }

    private static void verifyProjectileInterception() {
        ArmorPiece bracers = ArmorCatalog.historicalEbonyWarriorBracers();
        EquipmentState equipment = new EquipmentState(Map.of(
                EquipmentSlot.RIGHT_HAND, weapon(GripMode.ONE_HANDED, false),
                EquipmentSlot.BRACERS, bracers));
        var resolver = new ImprovisedBracerProjectileResolver();
        var stopped = resolver.resolve(new PhysicalDamage(60, 20, 30), true, equipment);
        org.junit.jupiter.api.Assertions.assertTrue(stopped.intercepted() && !stopped.stopped(), "El brazal debe interceptar sin convertir P75/C55/B60 en invulnerabilidad absoluta.");
        org.junit.jupiter.api.Assertions.assertTrue(close(stopped.residualDamage().piercing(), 58.875)
                        && close(stopped.residualDamage().slashing(), 19.725)
                        && close(stopped.residualDamage().blunt(), 29.55)
                        && stopped.appliedWear() == 0,
                ": ESCUDO IMPROVISADO mitiga sólo su +2,5 pp de cobertura HEAD con el perfil real del brazal.");
        var missed = resolver.resolve(new PhysicalDamage(60, 20, 30), false, equipment);
        org.junit.jupiter.api.Assertions.assertTrue(!missed.intercepted() && missed.residualDamage().equals(new PhysicalDamage(60, 20, 30)),
                "Fuera de la hitbox, el proyectil debe continuar intacto.");
    }

    private static void verifyStableAccessoryIdentity() {
        var property = domain.inventory.item.accessory.AccessoryCatalog.kiaraNotebook().properties().stream()
                .filter(p -> p.id() == ItemPropertyId.GROW_OLD_TOGETHER).findFirst().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(property.name().equals("¿ENVEJECEMOS JUNTOS?"),
                "La identidad estable no debe alterar el texto canónico mostrado.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.item.accessory.AccessoryCatalog.inertAccessories().size() == 8,
                "Los ocho abalorios rudimentarios deben declararse de forma explícita como inertes.");
    }

    private static WeaponItem weapon(GripMode grip, boolean sheathed) {
        return new WeaponItem("Arma de prueba", "Arma de prueba para contratos defensivos.", 0.5,
                new InventoryFootprint(1, 1), 0.4,
                List.of(new WeaponMode("Prueba", new LethalityProfile(1, 1, 1))), List.of(), List.of(), List.of(),
                OptionalDouble.empty(), 0, sheathed,
                new WeaponConfigurationPolicy(List.of(new WeaponConfiguration(grip, WeaponActionMode.PRIMARY))), Set.of());
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < 1e-9; }
    
}

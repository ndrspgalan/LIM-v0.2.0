package qa.integration;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.CombatTechnique;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.combat.ImprovisedBracerBlockPolicy;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;
import domain.inventory.item.armor.ArmorCatalog;
import domain.movement.ExplorationTechnique;
import domain.movement.ExplorationTechniqueUnlockPolicy;
import domain.throwing.ThrowRequest;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;

public final class EngineerAndEbonyBracerVerification {
    private EngineerAndEbonyBracerVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyHeavyErgonomics();
        verifyEffectiveDexterityConsumers();
        verifyImprovisedShieldActivation();
        verifyImprovisedShieldParryEligibility();
        verifyDestabilizeCoexistence();
    }

    private static void verifyHeavyErgonomics() {
        var suit = ArmorCatalog.engineerSuit();
        org.junit.jupiter.api.Assertions.assertTrue(suit.hasProperty(ItemPropertyId.BIOMECHANICAL_RIGIDITY),
                "El Conjunto del Ingeniero debe declarar RIGIDEZ BIOMECÁNICA.");
        CharacterSheet sheet = sheet(75, 40, 40);
        EquipmentState equipped = new EquipmentState(Map.of(EquipmentSlot.CHEST, suit));
        org.junit.jupiter.api.Assertions.assertTrue(equipped.effectiveAttributeValue(Attribute.DESTREZA, sheet) == 20,
                "RIGIDEZ BIOMECÁNICA debe limitar DESTREZA efectiva a 20.");
        org.junit.jupiter.api.Assertions.assertTrue(EquipmentState.empty().effectiveAttributeValue(Attribute.DESTREZA, sheet) == 75,
                "Desequipar el traje debe restaurar la DESTREZA efectiva sin mutar la hoja.");
        org.junit.jupiter.api.Assertions.assertTrue(sheet.valueOf(Attribute.DESTREZA) == 75,
                "El límite no puede modificar el valor base confirmado.");
    }

    private static void verifyEffectiveDexterityConsumers() {
        CharacterSheet sheet = sheet(75, 40, 40);
        EquipmentState equipped = new EquipmentState(Map.of(
                EquipmentSlot.CHEST, ArmorCatalog.engineerSuit()));

        CombatTechniqueUnlockPolicy combat = new CombatTechniqueUnlockPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(combat.isUnlocked(CombatTechnique.DEFLECTION, sheet, equipped),
                "DESTREZA efectiva 20 debe conservar DESVIAR.");

        ExplorationTechniqueUnlockPolicy exploration = new ExplorationTechniqueUnlockPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(exploration.isUnlocked(ExplorationTechnique.RIDE, sheet, equipped),
                "La exploración debe consultar atributos efectivos contextuales.");

        ThrowRequest request = ThrowRequest.from(sheet, equipped, 1.72, 45);
        org.junit.jupiter.api.Assertions.assertTrue(request.dexterity() == 20,
                "Los lanzamientos deben recibir la DESTREZA efectiva limitada.");
    }

    private static void verifyImprovisedShieldActivation() {
        ImprovisedBracerBlockPolicy policy = new ImprovisedBracerBlockPolicy();
        WeaponItem rightPrimary = oneHandedWeapon("Espada", WeaponActionMode.PRIMARY, Set.of());
        EquipmentState valid = equipment(rightPrimary, null);
        org.junit.jupiter.api.Assertions.assertTrue(policy.canBlock(valid),
                "Arma ordinaria a una mano, modo principal, diestra y zurda libre debe activar el brazal.");

        WeaponItem rightAlternative = dualModeWeapon("Arma transformable");
        rightAlternative.selectActionMode(WeaponActionMode.ALTERNATIVE);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canBlock(equipment(rightAlternative, null)),
                "El modo alternativo derecho debe desactivar ESCUDO IMPROVISADO.");

        WeaponItem shield = oneHandedWeapon("Escudo", WeaponActionMode.PRIMARY, Set.of(WeaponTrait.SHIELD));
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canBlock(equipment(shield, null)),
                "Un escudo en la diestra no puede activar el brazal de ébano.");

        WeaponItem occupied = oneHandedWeapon("Daga zurda", WeaponActionMode.PRIMARY, Set.of());
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canBlock(equipment(rightPrimary, occupied)),
                "LEFT_HAND ocupada y activa debe impedir ESCUDO IMPROVISADO.");
    }

    private static void verifyImprovisedShieldParryEligibility() {
        ImprovisedBracerBlockPolicy policy = new ImprovisedBracerBlockPolicy();
        EquipmentState equipment = equipment(oneHandedWeapon("Espada", WeaponActionMode.PRIMARY, Set.of()), null);
        WeaponItem eligible = weapon("Sable rival", 0.4, 0.5,
                List.of(new WeaponMode("Filo", new LethalityProfile(1, 5, 1))),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))), Set.of());
        org.junit.jupiter.api.Assertions.assertTrue(policy.canParry(equipment, eligible),
                "El PARRY del brazal debe reutilizar la elegibilidad común.");

        WeaponItem rotor = weapon("Rotor", 0.4, 0.5,
                List.of(new WeaponMode("Filo", new LethalityProfile(1, 5, 1))),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))),
                Set.of(WeaponTrait.DE_ROTOR));
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canParry(equipment, rotor), "DE ROTOR debe ser inmune al PARRY del brazal.");

        WeaponItem blunt = weapon("Maza", 0.4, 0.5,
                List.of(new WeaponMode("Golpe", new LethalityProfile(0, 0, 8))),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))), Set.of());
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canParry(equipment, blunt),
                "Un arma sin perfil cortante no debe admitir PARRY.");
    }

    private static void verifyDestabilizeCoexistence() {
        WeaponItem right = oneHandedWeapon("Espada", WeaponActionMode.PRIMARY, Set.of())
                .withCombatPolicy(WeaponCombatPolicy.dagger());
        EquipmentState equipment = equipment(right, null);
        boolean bracer = new ImprovisedBracerBlockPolicy().canBlock(equipment);
        ResolvedWeaponHandling handling = WeaponHandlingResolver.resolve(equipment, false);
        WeaponInputResolutionPolicy inputs = new WeaponInputResolutionPolicy();

        var block = inputs.resolve(WeaponInput.LEFT_HOLD, handling, false, bracer, new DualWieldComboState());
        org.junit.jupiter.api.Assertions.assertTrue(block.allowed() && block.action().orElseThrow() == WeaponCombatAction.BLOCK,
                "El brazal debe ocupar la defensa izquierda virtual.");

        var destabilize = inputs.resolve(WeaponInput.DESTABILIZE_PRESS, handling, false, bracer,
                new DualWieldComboState());
        org.junit.jupiter.api.Assertions.assertTrue(destabilize.allowed() && destabilize.action().orElseThrow() == WeaponCombatAction.DESTABILIZE,
                "El arma derecha debe conservar GOLPE DESESTABILIZADOR mientras el brazal está activo.");
    }

    private static EquipmentState equipment(WeaponItem right, WeaponItem left) {
        Map<EquipmentSlot, domain.inventory.InventoryEntry> items = new java.util.EnumMap<>(EquipmentSlot.class);
        items.put(EquipmentSlot.RIGHT_HAND, right);
        if (left != null) items.put(EquipmentSlot.LEFT_HAND, left);
        items.put(EquipmentSlot.BRACERS, ArmorCatalog.historicalEbonyWarriorBracers());
        return new EquipmentState(items);
    }

    private static WeaponItem dualModeWeapon(String name) {
        return weapon(name, 0.4, 0.5,
                List.of(new WeaponMode("Filo", new LethalityProfile(1, 2, 1))),
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE))), Set.of());
    }

    private static WeaponItem oneHandedWeapon(String name, WeaponActionMode mode, Set<WeaponTrait> traits) {
        return weapon(name, 0.4, 0.5,
                List.of(new WeaponMode("Filo", new LethalityProfile(1, 2, 1))),
                new WeaponConfigurationPolicy(List.of(new WeaponConfiguration(GripMode.ONE_HANDED, mode))), traits);
    }

    private static WeaponItem weapon(String name, double reach, double weight, List<WeaponMode> modes,
                                     WeaponConfigurationPolicy configurations, Set<WeaponTrait> traits) {
        return new WeaponItem(name, "Objeto de verificación .", weight,
                new InventoryFootprint(1, 1), reach, modes, List.of(), List.of(), List.of(),
                OptionalDouble.empty(), 0, false, configurations, traits);
    }

    private static CharacterSheet sheet(int dexterity, int strength, int charisma) {
        return CharacterSheet.of(20, 20, 20, strength, dexterity, 20, 20, charisma, 20);
    }

    
}

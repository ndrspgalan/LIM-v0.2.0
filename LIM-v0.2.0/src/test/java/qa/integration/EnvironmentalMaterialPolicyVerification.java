package qa.integration;

import domain.character.Gender;
import domain.character.sheet.*;
import domain.combat.*;
import domain.environment.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;

import java.util.List;
import java.util.Map;

public final class EnvironmentalMaterialPolicyVerification {
    private static ArmorPiece armor(String name, ArmorHitLocation location, double coverage, ArmorMaterial material) {
        return new ArmorPiece(name, name, 1, InventoryFootprint.equipmentOnly(), location, coverage,
                new ArmorProtectionProfile(10, 10, 10), material, ArmorForm.STANDARD, List.of(), List.of());
    }
    private static void close(double actual, double expected, String label) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(label + ": " + actual + " != " + expected);
    }
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        EquipmentState steelChest = new EquipmentState(Map.of(
                EquipmentSlot.CHEST, armor("Coraza", ArmorHitLocation.BODY, .50, ArmorMaterial.STEEL),
                EquipmentSlot.FEET, ArmorCatalog.leatherHeavyWorkBootsV881()));
        NonConventionalImpactResult electric = new NonConventionalDamageResolver().resolve(
                DamageType.ELECTRICITY, 100, ArmorHitLocation.BODY, steelChest, 0, false);
        close(electric.netDamage(), 200, "Electricidad canalizada por una pieza de acero x2");

        NonConventionalImpactResult soaked = new NonConventionalDamageResolver().resolve(
                DamageType.ELECTRICITY, 100, ArmorHitLocation.BODY, EquipmentState.empty(), 75, true);
        close(soaked.effectiveResistancePercent(), 0, "Conductor natural");
        close(soaked.netDamage(), 100, "Electricidad empapado");

        EquipmentState clothBody = new EquipmentState(Map.of(
                EquipmentSlot.CHEST, armor("Coraza", ArmorHitLocation.BODY, .50, ArmorMaterial.CLOTH),
                EquipmentSlot.BRACERS, armor("Brazales", ArmorHitLocation.BODY, .15, ArmorMaterial.CLOTH),
                EquipmentSlot.LEGGINGS, armor("Polainas", ArmorHitLocation.BODY, .35, ArmorMaterial.CLOTH)));
        CharacterSheet faith1 = CharacterSheet.of(10, 10, 10, 10, 10, 10, 1, 10, 10);
        EnvironmentalSetBonusPolicy clothPolicy = new EnvironmentalSetBonusPolicy();
        if (!clothPolicy.clothSetVisible(clothBody)) {
            throw new AssertionError("La propiedad física del conjunto de tela debe ser visible sin requisito de FE.");
        }
        if (!clothPolicy.immuneTo(faith1, clothBody, EnvironmentalAdversity.BITING_FROST)) {
            throw new AssertionError("El conjunto corporal de tela debe inmunizar frente a Frío Escarchante sin requisito de FE.");
        }

        CharacterSheet sheet = CharacterSheet.of(10, 40, 30, 10, 10, 10, 21, 10, 10);
        StaminaRecovery recovery = new DerivedStatisticsCalculator().staminaRecovery(40, 0, 40);
        EnvironmentalExposure exposure = new EnvironmentalExposure();
        EnvironmentalTickResult buildup = exposure.enter(EnvironmentalAdversity.SUFFOCATING_HEAT, 3, sheet,
                EquipmentState.empty(), recovery);
        if (!buildup.active()) throw new AssertionError("ADAPTABILIDAD 30 debe completar Quemadura Asfixiante en 3 segundos.");
        close(buildup.rawHealthDamage(), 0, "El build-up no causa daño retroactivo");
        EnvironmentalTickResult normalized = exposure.leave(recovery.fullRecoverySeconds(), sheet,
                EquipmentState.empty(), recovery);
        if (normalized.adversity() != EnvironmentalAdversity.NORMAL) {
            throw new AssertionError("La recuperación ambiental debe durar lo mismo que la recuperación completa de PA.");
        }
    }
}

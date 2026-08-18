package qa.integration;

import domain.character.sheet.CharacterSheet;
import domain.combat.WeaponDurabilityResolver;
import domain.combat.coating.WeaponCoatingService;
import domain.combat.runic.RunicCoatingDamagePolicy;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.inventory.item.misc.MucusTearItem;
import domain.inventory.item.misc.UseAnimation;
import domain.inventory.item.misc.UseResourceKind;
import domain.inventory.item.misc.UtilityAction;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.inventory.item.misc.WhetstonePolicy;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public final class TranspositionCanonicalVerification {
    private static final double EPS = 1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifySingleTearAndCuttingRequirement();
        verifyDamageFollowsImpactProfile();
        verifyDamageFollowsCurrentWear();
        verifyCoatingTermination();
        verifyNarrativeAndFormSections();
    }

    private static void verifySingleTearAndCuttingRequirement() {
        WeaponItem cutting = weapon(new LethalityProfile(0, 1, 20));
        MucusTearItem tears = new MucusTearItem(3);
        var result = new WeaponCoatingService().applyCurse(sheet(33), tears, cutting);
        org.junit.jupiter.api.Assertions.assertTrue(result.successful() && result.tearUnitsConsumed() == 1 && tears.currentUses() == 2,
                "Un uso debe consumir exactamente una Lágrima.");

        WeaponItem bluntOnly = weapon(new LethalityProfile(0, 0, 20));
        var rejected = new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), bluntOnly);
        org.junit.jupiter.api.Assertions.assertTrue(!rejected.successful() && bluntOnly.coating().isEmpty(),
                "Un arma sin daño cortante no puede recubrirse.");
    }

    private static void verifyDamageFollowsImpactProfile() {
        WeaponMode zero = new WeaponMode("Perfil sin contundencia", new LethalityProfile(0, 5, 0));
        WeaponMode fifty = new WeaponMode("Perfil contundente", new LethalityProfile(0, 5, 50));
        WeaponItem weapon = weapon(zero, fifty);
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), weapon);
        RunicCoatingDamagePolicy policy = new RunicCoatingDamagePolicy();
        close(policy.rawCurseDamage(weapon, zero, true), 0, "El perfil con contundencia cero añade cero Maldición.");
        close(policy.rawCurseDamage(weapon, fifty, true), 50, "Debe respetar la contundencia del perfil efectivo.");
        close(policy.rawCurseDamage(weapon, fifty, false), 0, "Un impacto fallido no aplica el recubrimiento.");
    }

    private static void verifyDamageFollowsCurrentWear() {
        WeaponMode mode = new WeaponMode("Perfil", new LethalityProfile(0, 2, 3));
        WeaponItem weapon = weapon(mode);
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), weapon);
        RunicCoatingDamagePolicy policy = new RunicCoatingDamagePolicy();
        close(policy.rawCurseDamage(weapon, mode, true), 3, "Daño inicial incorrecto.");
        weapon.applyHeavyArmorWear(mode, new domain.inventory.item.armor.ArmorProtectionProfile(1,1,1));
        close(policy.rawCurseDamage(weapon, mode, true), 2, "El daño maldito debe disminuir con el desgaste.");
    }

    private static void verifyCoatingTermination() {
        WeaponMode zero = new WeaponMode("Cero", new LethalityProfile(0, 1, 0));
        WeaponMode one = new WeaponMode("Uno", new LethalityProfile(0, 1, 1));
        WeaponItem worn = weapon(zero, one);
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), worn);
        worn.applyHeavyArmorWear(zero, new domain.inventory.item.armor.ArmorProtectionProfile(1,1,1));
        org.junit.jupiter.api.Assertions.assertTrue(worn.coating().isPresent(), "Un perfil a cero no elimina el recubrimiento si otro conserva contundencia.");
        worn.applyHeavyArmorWear(one, new domain.inventory.item.armor.ArmorProtectionProfile(1,1,1));
        org.junit.jupiter.api.Assertions.assertTrue(worn.coating().isEmpty(), "El recubrimiento termina cuando todos los perfiles llegan a cero.");

        WeaponItem sharpened = weapon(new LethalityProfile(0, 1, 10));
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), sharpened);
        UtilityObjectItem whetstone = new UtilityObjectItem("Piedra de afilar", "Piedra de verificación.", 1, 1,
                0.2, 0, UseResourceKind.DURABILITY, new InventoryFootprint(1,1),
                new UseAnimation(1, List.of("Afilar")), List.of(UtilityAction.SHARPEN));
        org.junit.jupiter.api.Assertions.assertTrue(new WhetstonePolicy().sharpen(whetstone, sharpened), "El afilado debe ser válido.");
        org.junit.jupiter.api.Assertions.assertTrue(sharpened.coating().isEmpty(), "La Piedra de Afilar elimina el recubrimiento.");
    }

    private static void verifyNarrativeAndFormSections() {
        org.junit.jupiter.api.Assertions.assertTrue(MucusTearItem.NARRATIVE_DESCRIPTION.contains("películas invisibles"), "Falta la narrativa lagrimal.");
        org.junit.jupiter.api.Assertions.assertTrue(!new MucusTearItem(1).formDescription().isBlank(), "La Lágrima debe exponer Forma por separado.");
        var crystals = List.of(MucusCrystalCatalog.yellow(), MucusCrystalCatalog.greenish(),
                MucusCrystalCatalog.brown(), MucusCrystalCatalog.bloodied(), MucusCrystalCatalog.blackish());
        org.junit.jupiter.api.Assertions.assertTrue(crystals.stream().allMatch(c -> c.name().matches("Cristal de Mucus [A-ZÁÉÍÓÚÑ]+")),
                "Los colores deben escribirse en mayúsculas.");
        org.junit.jupiter.api.Assertions.assertTrue(crystals.stream().allMatch(c -> !c.narrativeDescription().isBlank() && !c.formDescription().isBlank()),
                "Cada cristal debe separar descripción narrativa y Forma.");
    }

    private static WeaponItem weapon(LethalityProfile... profiles) {
        List<WeaponMode> modes = java.util.stream.IntStream.range(0, profiles.length)
                .mapToObj(i -> new WeaponMode("Modo " + i, profiles[i])).toList();
        return weapon(modes.toArray(WeaponMode[]::new));
    }

    private static WeaponItem weapon(WeaponMode... modes) {
        return new WeaponItem("Arma", "Arma de verificación.", 1, new InventoryFootprint(1,1), 1,
                List.of(modes), List.of(), List.of(), List.of(), OptionalDouble.empty(), 0, false,
                new WeaponConfigurationPolicy(List.of(new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))), Set.of());
    }

    private static CharacterSheet sheet(int clairvoyance) { return CharacterSheet.of(30,30,30,30,30,30,30,30,clairvoyance); }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPS) throw new IllegalStateException(message + ": " + actual + " != " + expected);
    }
    
}

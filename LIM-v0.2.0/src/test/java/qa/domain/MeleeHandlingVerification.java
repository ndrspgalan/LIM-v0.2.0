package qa.domain;

import domain.combat.ChargedAttackPreparationPolicy;
import domain.combat.ChargedAttackPreparationState;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

/**  — saneamiento de configuraciones de agarre antes de definir movesets ofensivos. */
public final class MeleeHandlingVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyRotor();
        verifyKatana();
        verifyMace();
        verifyPitchfork();
        verifyChargedPreparation();
    }

    private static void verifyRotor() {
        WeaponItem rotor = MeleeWeaponCatalog.espadonDeRotor();
        org.junit.jupiter.api.Assertions.assertTrue(rotor.supportsTwoHandedUse() && rotor.supportsOneHandedUse(), ": Rotor debe admitir 2H PRIMARY y 1H ALTERNATIVE.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.currentConfiguration().equals(new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY)),
                "Rotor debe iniciar en 2H PRIMARY.");
    }

    private static void verifyKatana() {
        WeaponItem katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        org.junit.jupiter.api.Assertions.assertTrue(katana.isExclusivelyTwoHanded() && !katana.supportsOneHandedUse(),
                "Katana termo-mecánica debe ser exclusivamente 2H.");
        org.junit.jupiter.api.Assertions.assertTrue(katana.currentConfiguration().equals(new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY)),
                "Katana termo-mecánica debe ser 2H PRIMARY.");
        org.junit.jupiter.api.Assertions.assertTrue(!katana.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE),
                "Katana no debe conservar compatibilidad monomanual por ergonomía suficiente.");
        int strength = katana.requirements().stream()
                .filter(r -> r.attribute() == domain.character.sheet.Attribute.FUERZA)
                .findFirst().orElseThrow().minimumValue();
        org.junit.jupiter.api.Assertions.assertTrue(strength == 10, "La FUERZA de Katana debe calcularse desde agarre 2H.");
    }

    private static void verifyMace() {
        WeaponItem mace = MeleeWeaponCatalog.mazaElectroMecanicaV881();
        org.junit.jupiter.api.Assertions.assertTrue(mace.supportsOneHandedUse() && !mace.supportsTwoHandedUse(),
                "Maza electro-mecánica debe ser exclusivamente 1H.");
        org.junit.jupiter.api.Assertions.assertTrue(mace.currentConfiguration().equals(new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY)),
                "Maza electro-mecánica debe ser 1H PRIMARY.");
        WeaponInputResolution heavy = new WeaponInputResolutionPolicy().resolve(WeaponInput.HEAVY_PRESS, mace, null, false, false);
        org.junit.jupiter.api.Assertions.assertTrue(heavy.allowed() && heavy.action().orElseThrow() == WeaponCombatAction.HEAVY_ATTACK,
                "La excepción de fuerte monomanual de la Maza debe preservarse.");
    }

    private static void verifyPitchfork() {
        WeaponItem pitchfork = MeleeWeaponCatalog.horca();
        org.junit.jupiter.api.Assertions.assertTrue(pitchfork.isExclusivelyTwoHanded(), "Horca debe ser exclusivamente 2H.");
        org.junit.jupiter.api.Assertions.assertTrue(pitchfork.currentConfiguration().equals(new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY)),
                "Horca debe ser 2H PRIMARY.");
        org.junit.jupiter.api.Assertions.assertTrue(!pitchfork.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK), ": Horca no tiene HEAVY.");
        org.junit.jupiter.api.Assertions.assertTrue(!pitchfork.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK), ": Horca no tiene CHARGED.");
    }

    private static void verifyChargedPreparation() {
        ChargedAttackPreparationPolicy policy = new ChargedAttackPreparationPolicy();

        WeaponItem ordinary = MeleeWeaponCatalog.guadana();
        ChargedAttackPreparationState ordinaryState = new ChargedAttackPreparationState();
        ordinaryState.start(); ordinaryState.advance(1.19);
        org.junit.jupiter.api.Assertions.assertTrue(policy.canPrepare(ordinary) && policy.canCancel(ordinary, ordinaryState),
                "Un cargado ordinario debe poder cancelarse durante su ventana canónica de 1,20 s.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.cancel(ordinary, ordinaryState) && !ordinaryState.preparing(),
                "Cancelar el cargado ordinario debe abortar la preparación.");

        WeaponItem rotor = MeleeWeaponCatalog.espadonDeRotor();
        org.junit.jupiter.api.Assertions.assertTrue(rotor.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK) && policy.canPrepare(rotor),
                ": el Espadón de Rotor es la excepción DE_ROTOR que recupera CHARGED.");
    }

    
}

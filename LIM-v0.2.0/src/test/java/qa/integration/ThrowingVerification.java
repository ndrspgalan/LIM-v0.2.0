package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.character.sheet.CharacterSheet;
import domain.combat.PhysicalDamage;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.throwing.*;

public final class ThrowingVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyAttributesIncreasePerformance();
        verifyMassAndHeightAffectTrajectory();
        verifyImprovisedDamageIsOnlyBlunt();
        verifyThrownWeaponAddsItsIntegralLethalityProfile();
        verifyPebbleExtractionAndRecovery();
        verifyCurrencyExtractionIdentityAndRecovery();
        verifyInvalidRequestsDoNotConsumeUnits();
    }

    private static void verifyAttributesIncreasePerformance() {
        ThrowPolicy policy = new ThrowPolicy();
        ThrownPayload pebble = ThrownPayload.item("Guijarro", ThrowProfile.improvised(0.080, true));
        ThrowResult low = policy.resolve(new ThrowRequest(10, 10, 1.72, 45), pebble);
        ThrowResult strength = policy.resolve(new ThrowRequest(20, 10, 1.72, 45), pebble);
        ThrowResult dexterity = policy.resolve(new ThrowRequest(10, 20, 1.72, 45), pebble);
        org.junit.jupiter.api.Assertions.assertTrue(close(strength.horizontalDistanceMeters(), low.horizontalDistanceMeters()), "FUERZA ya no modifica el alcance: lo gobierna DESTREZA.");
        org.junit.jupiter.api.Assertions.assertTrue(dexterity.horizontalDistanceMeters() > low.horizontalDistanceMeters(), "Más DESTREZA debe aumentar el alcance.");
        org.junit.jupiter.api.Assertions.assertTrue(strength.damage().blunt() > low.damage().blunt(), "Más FUERZA debe aumentar el daño contundente.");
        org.junit.jupiter.api.Assertions.assertTrue(close(dexterity.damage().blunt(), low.damage().blunt()), "DESTREZA ya no modifica el daño contundente.");
    }

    private static void verifyMassAndHeightAffectTrajectory() {
        ThrowPolicy policy = new ThrowPolicy();
        ThrowRequest standard = new ThrowRequest(30, 30, 1.72, 45);
        ThrowResult light = policy.resolve(standard, ThrownPayload.item("Ligero", ThrowProfile.improvised(0.050, true)));
        ThrowResult heavy = policy.resolve(standard, ThrownPayload.item("Pesado", ThrowProfile.improvised(1.000, true)));
        ThrowResult tall = policy.resolve(new ThrowRequest(30, 30, 2.00, 45),
                ThrownPayload.item("Ligero", ThrowProfile.improvised(0.050, true)));
        org.junit.jupiter.api.Assertions.assertTrue(close(light.horizontalDistanceMeters(), heavy.horizontalDistanceMeters()), "La masa ya no modifica el alcance canónico del lanzamiento manual.");
        org.junit.jupiter.api.Assertions.assertTrue(heavy.damage().blunt() > light.damage().blunt(), "Cada kilogramo lanzado debe aportar contundencia adicional.");
        org.junit.jupiter.api.Assertions.assertTrue(tall.horizontalDistanceMeters() >= light.horizontalDistanceMeters(), "La altura no debe empeorar la trayectoria física.");
    }

    private static void verifyImprovisedDamageIsOnlyBlunt() {
        ThrowResult result = new ThrowPolicy().resolve(
                new ThrowRequest(40, 40, 1.72, 45),
                ThrownPayload.item("Guijarro", ThrowProfile.improvised(0.080, true)));
        PhysicalDamage damage = result.damage();
        org.junit.jupiter.api.Assertions.assertTrue(close(damage.piercing(), 0) && close(damage.slashing(), 0) && damage.blunt() > 0,
                "Un objeto improvisado debe causar únicamente contundencia cinética.");
    }

    private static void verifyThrownWeaponAddsItsIntegralLethalityProfile() {
        LethalityProfile lethality = new LethalityProfile(60, 15, 10);
        ThrowResult result = new ThrowPolicy().resolve(
                new ThrowRequest(50, 50, 1.72, 45),
                ThrownPayload.item("Arma arrojadiza", ThrowProfile.weapon(0.700, true, lethality)));
        org.junit.jupiter.api.Assertions.assertTrue(close(result.damage().piercing(), 60), "Debe conservarse la perforación del perfil integral.");
        org.junit.jupiter.api.Assertions.assertTrue(close(result.damage().slashing(), 15), "Debe conservarse el corte del perfil integral.");
        org.junit.jupiter.api.Assertions.assertTrue(result.damage().blunt() > 10, "La contundencia cinética basal debe sumarse a la contundencia del perfil.");
    }

    private static void verifyPebbleExtractionAndRecovery() {
        UtilityObjectItem pebbles = MiscellaneousItemCatalog.pebble();
        StackThrowPolicy throwing = new StackThrowPolicy();
        ThrowResult result = throwing.throwOne(pebbles, new ThrowRequest(20, 20, 1.72, 45));
        org.junit.jupiter.api.Assertions.assertTrue(pebbles.quantity() == 4, "Lanzar debe retirar exactamente un guijarro.");
        org.junit.jupiter.api.Assertions.assertTrue(result.payload().profile().recoverable(), "El guijarro lanzado debe ser recuperable.");
        org.junit.jupiter.api.Assertions.assertTrue(new ThrownRecoveryPolicy().recover(result, pebbles), "El guijarro debe poder volver a su stack.");
        org.junit.jupiter.api.Assertions.assertTrue(pebbles.quantity() == 5, "Recuperar debe restaurar exactamente una unidad.");
        org.junit.jupiter.api.Assertions.assertTrue(!new ThrownRecoveryPolicy().recover(result, pebbles), "Un stack completo no debe superar su capacidad.");
    }

    private static void verifyCurrencyExtractionIdentityAndRecovery() {
        CurrencyStack berylares = new CurrencyStack(CurrencyType.BERYLARE, 3);
        ThrowResult result = new StackThrowPolicy().throwOne(berylares, new ThrowRequest(20, 20, 1.72, 45));
        org.junit.jupiter.api.Assertions.assertTrue(berylares.quantity() == 2, "Lanzar una moneda debe retirar una sola unidad.");
        org.junit.jupiter.api.Assertions.assertTrue(result.payload().currencyType().orElseThrow() == CurrencyType.BERYLARE,
                "La moneda lanzada debe conservar su tipo monetario.");
        CurrencyStack wrong = new CurrencyStack(CurrencyType.SUELDO, 2);
        org.junit.jupiter.api.Assertions.assertTrue(!new ThrownRecoveryPolicy().recover(result, wrong), "Una moneda no puede recuperarse en un stack de otro tipo.");
        org.junit.jupiter.api.Assertions.assertTrue(new ThrownRecoveryPolicy().recover(result, berylares), "La moneda debe volver a un stack compatible.");
        org.junit.jupiter.api.Assertions.assertTrue(berylares.quantity() == 3, "La recuperación monetaria debe añadir exactamente una unidad.");
    }

    private static void verifyInvalidRequestsDoNotConsumeUnits() {
        UtilityObjectItem pebbles = MiscellaneousItemCatalog.pebble();
        int before = pebbles.quantity();
        try {
            new StackThrowPolicy().throwOne(pebbles, new ThrowRequest(20, 20, 1.72, 91));
            throw new IllegalStateException("El ángulo inválido debía ser rechazado.");
        } catch (IllegalArgumentException expected) {
            org.junit.jupiter.api.Assertions.assertTrue(pebbles.quantity() == before, "Una solicitud inválida no debe consumir unidades.");
        }
    }

    private static boolean close(double left, double right) { return Math.abs(left - right) < 0.000001; }
    
}

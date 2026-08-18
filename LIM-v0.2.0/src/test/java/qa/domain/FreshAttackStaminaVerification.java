package qa.domain;

import domain.combat.CombatAttackStaminaPolicy;

public final class FreshAttackStaminaVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var policy = new CombatAttackStaminaPolicy();

        org.junit.jupiter.api.Assertions.assertTrue(policy.canExecute(40, 40, 55), "Con PA completos debe poder ejecutarse un ataque que cuesta más que el máximo.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isFreshOverride(40, 40, 55), "El ataque supra-máximo con barra llena debe reconocerse como ataque fresco.");
        close(policy.staminaAfter(40, 40, 55), 0, "El ataque fresco supra-máximo debe vaciar la barra.");

        org.junit.jupiter.api.Assertions.assertTrue(!policy.canExecute(39, 40, 55), "Con la barra incompleta no debe existir bypass supra-máximo.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isFreshOverride(39, 40, 55), "Una barra incompleta nunca es ataque fresco.");

        org.junit.jupiter.api.Assertions.assertTrue(policy.canExecute(40, 40, 40), "Un ataque de coste exactamente igual al máximo se ejecuta normalmente.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isFreshOverride(40, 40, 40), "Coste igual al máximo no es supra-máximo.");
        close(policy.staminaAfter(40, 40, 40), 0, "Coste igual al máximo consume la barra completa.");

        org.junit.jupiter.api.Assertions.assertTrue(policy.canExecute(30, 40, 20), "Los ataques ordinarios siguen usando los PA actuales.");
        close(policy.staminaAfter(30, 40, 20), 10, "El gasto ordinario permanece intacto.");

        org.junit.jupiter.api.Assertions.assertTrue(!policy.canExecute(10, 40, 20), "PA insuficientes con barra incompleta siguen bloqueando el ataque.");
    }

    

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(message + " actual=" + actual + " expected=" + expected);
    }
}

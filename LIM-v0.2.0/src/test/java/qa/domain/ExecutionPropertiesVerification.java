package qa.domain;

import domain.combat.ArmorCombatHitbox;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import domain.inventory.item.firearms.FulminatingPolicy;

/** cobertura regional + protección perforante para GOLPE DE GRACIA/FULMINANTE. */
public final class ExecutionPropertiesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(CoupDeGracePolicy.isCoupDeGrace(true, 99.99, 54, 55), "GOLPE DE GRACIA: HEAD descubierta y P superior.");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true, 100, 0, 999), "HEAD 100% cubierta bloquea GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true, 99, 55, 55), "La igualdad perforante no basta.");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(false, 0, 0, 999), "GOLPE DE GRACIA sólo existe en HEAD.");

        org.junit.jupiter.api.Assertions.assertTrue(FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.HELMET, 99, 84, 85), "FULMINANTE por HEAD.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.HELMET, 100, 0, 999), "HEAD completa bloquea FULMINANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.CHEST, 49.9, 84, 85), "FULMINANTE por CHEST descubierta.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.CHEST, 50, 0, 999), "CHEST al 50% bloquea FULMINANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.BRACERS, 0, 0, 999), "FULMINANTE no se activa en otras regiones BODY.");
        org.junit.jupiter.api.Assertions.assertTrue(close(FulminatingPolicy.combinedPiercingProtection(50,50),75), "Dos capas P50 deben equivaler a P75 secuencial.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(ArmorCombatHitbox.CHEST, 49, 75, 75), "P igual no activa FULMINANTE.");
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;}
    
}

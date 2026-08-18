package qa.domain;

import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.*;
import domain.inventory.item.firearmAccessories.FirearmAccessoryMount;

/** Compatibilidad  actualizada:  transforma el antiguo fusil automático en Subfusil Automático. */
public final class AutomaticRifleVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var smg = FirearmCatalog.submachineGunV881();
        org.junit.jupiter.api.Assertions.assertTrue(smg.name().equals("Subfusil Automático V881"), "Nombre .");
        org.junit.jupiter.api.Assertions.assertTrue(smg.fireModes().equals(java.util.List.of(FireMode.AUTO_A)), "AA exclusivo.");
        org.junit.jupiter.api.Assertions.assertTrue(smg.cartridgeDefinition().capacity()==25, "25 cartuchos.");
        org.junit.jupiter.api.Assertions.assertTrue(smg.effectiveRangeMeters()==100, "100 m.");
        org.junit.jupiter.api.Assertions.assertTrue(smg.lethalityProfile().piercing()==85 && smg.lethalityProfile().blunt()==35, "85/0/35.");
        org.junit.jupiter.api.Assertions.assertTrue(!smg.supportsAiming(), "Sin AIMING.");
        org.junit.jupiter.api.Assertions.assertTrue(smg.admitsAttachment(FirearmAccessoryMount.SLING) && !smg.admitsAttachment(FirearmAccessoryMount.OPTIC), "Solo correa.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionCatalog.submachineGun9mmMagazine().capacity()==25, "Munición sincronizada.");
    }
    
}

package qa.domain;

import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.*;

/** Compatibilidad  actualizada: desde  la pistola ya no es selectiva. */
public final class SelectivePistolVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var pistol = FirearmCatalog.autoloadingPistolV881();
        org.junit.jupiter.api.Assertions.assertTrue(pistol.name().equals("Pistola Autocargadora V881"), "Nombre .");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(pistol.weightKg()-0.92)<1e-9, "Peso .");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.fireModes().equals(java.util.List.of(FireMode.ONE_A)), "1A exclusivo.");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.cartridgeDefinition().capacity()==8, "8 cartuchos.");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.cartridgeDefinition().material().equals("Plomo con camisa de cobre"), "Camisa de cobre.");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.lethalityProfile().piercing()==65 && pistol.lethalityProfile().blunt()==35, "65/0/35.");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.coupDeGracePropertyPresent(), "GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionCatalog.autoloadingPistol45Magazine().capacity()==8, "Catálogo de munición sincronizado.");
    }
    
}

package qa.domain;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.aeronautics.DisposableGliderItem;
import domain.inventory.item.aeronautics.DisposableGliderState;
import domain.inventory.item.armor.*;

public final class FinalArmorAndSanityVerification {
    private FinalArmorAndSanityVerification() {}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.MINERALIZED_EBONY.canonicalProtection().equals(new ArmorProtectionProfile(75,55,60)), "Ébano mineralizado.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.TUNGSTEN_PLATES_2_5_MM.canonicalProtection().equals(new ArmorProtectionProfile(20,45,25)), "Placas de wolframio.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.ebonyWarriorV881Chest().protection().equals(new ArmorProtectionProfile(95,100,85)), "Guerrero de Ébano V881.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.historicalEbonyWarriorChest().protection().equals(new ArmorProtectionProfile(75,55,60)), "Guerrero histórico.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.historicalKnightChest().protection().equals(new ArmorProtectionProfile(85,100,75)), "Caballero histórico por piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.knightV881Chest().protection().equals(new ArmorProtectionProfile(100,100,80)), "Caballero V881.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.retractableAeronautHelmet().protection().equals(new ArmorProtectionProfile(85,100,80)), "Aeronauta.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.enlightenedPanopticonCanonical().protection().equals(new ArmorProtectionProfile(40,100,100)), "Panóptico.");
        org.junit.jupiter.api.Assertions.assertTrue(new ArmorProtectionProfile(150,101,130).equals(new ArmorProtectionProfile(100,100,100)), "Guardarraíl defensivo.");
        org.junit.jupiter.api.Assertions.assertTrue(new LethalityProfile(150,101,130).equals(new LethalityProfile(100,100,100)), "Guardarraíl ofensivo.");
        DisposableGliderItem glider = new DisposableGliderItem(); glider.equipOverTorso();
        org.junit.jupiter.api.Assertions.assertTrue(glider.pressSpaceWhileFalling(true) && glider.state()==DisposableGliderState.DEPLOYED, "SPACE abre el planeador.");
        glider.resolveLanding(); org.junit.jupiter.api.Assertions.assertTrue(glider.state()==DisposableGliderState.CONSUMED, "Planeador desechable.");
    }
    
}

package qa.domain;

import domain.inventory.InventoryFootprint;
import domain.inventory.logistics.*;
import domain.inventory.item.meleeWeapons.*;
import domain.inventory.item.firearms.*;
import domain.inventory.item.rangedWeapons.*;
import domain.inventory.item.throwingWeapons.*;

public final class WeaponInventoryXyzVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(WeaponPhysicalDimensionsCatalog.footprintFor("Pavesina Cementada de Asalto V881").equals(new InventoryFootprint(5,6)), "Pavesina XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponPhysicalDimensionsCatalog.footprintFor("Cañón Antimaterial V881").equals(new InventoryFootprint(6,28)), "Antimaterial XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponPhysicalDimensionsCatalog.footprintFor("Rociador de Cal Viva V881").equals(new InventoryFootprint(6,10)), "Rociador XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor("Granada Incendiaria de Terracota V881",new InventoryFootprint(1,1)).equals(new InventoryFootprint(2,1)), "Terracota XYZ ");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponPhysicalDimensionsCatalog.footprintFor("Espadón de Rotor [RETRAÍDO]").equals(new InventoryFootprint(2,9)), "Rotor retraído XYZ");

        var anti = FirearmCatalog.antiMaterielCannonV881();
        org.junit.jupiter.api.Assertions.assertTrue(anti.fulminatingPropertyPresent(), "Antimaterial conserva FULMINANTE");
        org.junit.jupiter.api.Assertions.assertTrue(!anti.coupDeGracePropertyPresent(), "Antimaterial no duplica GOLPE DE GRACIA");
        org.junit.jupiter.api.Assertions.assertTrue(anti.footprint().equals(new InventoryFootprint(6,28)), "Antimaterial usa catálogo XYZ");

        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.pico().footprint().equals(new InventoryFootprint(4,8)), "Pico XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.mazaElectroMecanicaV881().footprint().equals(new InventoryFootprint(4,10)), "Maza XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().footprint().equals(new InventoryFootprint(2,9)), "Rotor retraído al crear");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().deployedFootprint().equals(new InventoryFootprint(2,13)), "Rotor desplegado");

        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.sling().footprint().equals(new InventoryFootprint(1,2)), "Honda XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.simpleRecurveBow().footprint().equals(new InventoryFootprint(2,12)), "Recurvo XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(ThrowingWeaponCatalog.incendiaryTerracottaGrenadeV881().footprint().equals(new InventoryFootprint(2,1)), "Granada XYZ ");

        var d = InventoryPhysicalDimensions.fromMetricDimensions(0.30,1.35,0.15);
        org.junit.jupiter.api.Assertions.assertTrue(d.equals(new InventoryPhysicalDimensions(3,14,2)), "Discretización métrica XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryVolumeProjectionPolicy.footprint(d).equals(new InventoryFootprint(6,28)), "Proyección XZ × YZ");
    }
    
}

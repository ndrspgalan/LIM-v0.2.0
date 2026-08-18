package qa.domain;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorPiece;

import java.util.Arrays;

/** Consolidación posterior a : elimina andamiaje de conjuntos/capas obsoleto. */
public final class DomainCanonicalVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(EquipmentSlot.values()).noneMatch(slot -> slot.name().equals("CLOAK")),
                "CLOAK no debe seguir siendo una ranura paralela: las capas son ArmorPiece OUTER CHEST.");
        ArmorPiece traveler = ArmorCatalog.outerTravelerCloak();
        org.junit.jupiter.api.Assertions.assertTrue(traveler.inventoryCategory().orElseThrow().name().equals("CHEST"),
                "La Capa del Viajero debe pertenecer al catálogo canónico CHEST.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(ArmorCatalog.class.getDeclaredMethods()).noneMatch(m -> m.getName().equals("allSets")),
                "ArmorCatalog no debe publicar allSets tras la migración por piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(ArmorCatalog.class.getDeclaredMethods()).noneMatch(m -> m.getName().endsWith("Set")),
                "ArmorCatalog no debe conservar agregadores nominales de conjuntos.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(ArmorPiece.class.getDeclaredMethods()).noneMatch(m -> m.getName().equals("isBroken")),
                "ArmorPiece debe usar isDepleted y no el alias obsoleto isBroken.");
    }

    
}

package qa.domain;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.armor.ArmorCatalog;

public final class ArmorCatalogIntegrityVerification {
    private ArmorCatalogIntegrityVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var panopticon = ArmorCatalog.enlightenedPanopticon();
        org.junit.jupiter.api.Assertions.assertTrue(panopticon.name().equals("Panóptico del Ilustrado"), "El Panóptico debe existir como pieza suelta.");
        org.junit.jupiter.api.Assertions.assertTrue(!panopticon.narrativeDescription().contains("Parte del conjunto"), "La narrativa no puede vincular el Panóptico a un conjunto eliminado.");
        org.junit.jupiter.api.Assertions.assertTrue(panopticon.coverageRatio(domain.inventory.item.armor.ArmorHitLocation.HEAD) == 1.0, "El Panóptico conserva cobertura integral de cabeza.");
        org.junit.jupiter.api.Assertions.assertTrue(panopticon.protection().piercing() == 40 && panopticon.protection().slashing() == 100 && panopticon.protection().blunt() == 100,
                "El Panóptico debe conservar su perfil protector canónico.");
        ItemProperty property = panopticon.properties().stream().filter(p -> p.name().equals("Panóptico del Ilustrado")).findFirst().orElseThrow();
        var fe20 = CharacterSheet.of(1,1,1,1,1,1,20,1,75);
        var fe21c21 = CharacterSheet.of(1,1,1,1,1,1,21,1,21);
        var fe21c22 = CharacterSheet.of(1,1,1,1,1,1,21,1,22);
        org.junit.jupiter.api.Assertions.assertTrue(property.isVisibleTo(fe20), "CLA 75 revela el Panóptico con independencia de FE.");
        org.junit.jupiter.api.Assertions.assertFalse(property.isVisibleTo(fe21c21), "CLA 21 aún no revela la función oculta del Panóptico.");
        org.junit.jupiter.api.Assertions.assertTrue(!property.isActiveFor(fe21c21) && property.isVisibleTo(fe21c22) && property.isActiveFor(fe21c22), "CLARIVIDENCIA 22 revela y activa el Panóptico.");
        org.junit.jupiter.api.Assertions.assertTrue(property.effectiveStatistic().equals("INMUNIDAD | Frenesí"), "El Panóptico conserva INMUNIDAD | Frenesí.");
    }

    
}

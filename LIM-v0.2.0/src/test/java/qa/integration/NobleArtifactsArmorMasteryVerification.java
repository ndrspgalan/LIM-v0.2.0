package qa.integration;

import domain.ability.CharacterMasteryCollection;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.AttributeCapPolicy;
import domain.character.progression.CharacterClassDefinition;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.misc.AstrolabeItem;
import domain.social.NobleCanonicalProfiles;
import domain.social.NobleStartingEquipmentCatalog;
import domain.social.Profession;
import domain.social.Subprofession;

import java.util.*;

/**  — barrera dura de Noble, artefactos, armadura y vínculo con maestrías. */
public final class NobleArtifactsArmorMasteryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        nobleTaxonomyAndCaps();
        nobleEquipmentAndArmor();
        astrolabeMigrationAndArtifactContract();
        masteryLinkage();
        forbiddenHistoricalSetsAbsentFromNoble();
    }

    private static void nobleTaxonomyAndCaps() {
        AttributeCapPolicy caps = new AttributeCapPolicy(GenderSoftcapProfile.canonical(), CharacterClassDefinition.canonicalDefinitions());
        for (var entry : NobleCanonicalProfiles.all().entrySet()) {
            org.junit.jupiter.api.Assertions.assertTrue(entry.getKey().profession() == Profession.NOBLE, "Subprofesión ajena a Noble: " + entry.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(!entry.getValue().isEmpty(), "Noble sin perfiles activos: " + entry.getKey());
            for (var p : entry.getValue().values()) {
                org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel() == p.attributes().totalAttributeLevel(), "Nivel no derivado: " + p.subprofession() + "/" + p.characterClass());
                for (Gender g : p.genders()) {
                    for (Attribute a : Attribute.values()) {
                        int max = caps.maximumFor(g, p.characterClass(), p.attributes(), a);
                        org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(a) <= max,
                                "Softcap excedido: " + p.subprofession().label() + " / " + p.characterClass().label() + " / " + g.label() + " / " + a.label() + "=" + p.attributes().valueOf(a) + " > " + max);
                    }
                }
                if (p.characterClass() == CharacterClass.MAESTRO) {
                    org.junit.jupiter.api.Assertions.assertTrue(p.genders().equals(Set.of(Gender.HOMBRE, Gender.MUJER)), "Maestro Noble debe existir en ambos sexos: " + p.subprofession());
                } else {
                    Set<Gender> expected = switch (p.characterClass()) {
                        case LUCHADOR, INTELECTUAL, INDOMITO -> Set.of(Gender.HOMBRE);
                        case ESPECIALISTA, APODERADO, HERALDO -> Set.of(Gender.MUJER);
                        case MAESTRO -> Set.of(Gender.HOMBRE, Gender.MUJER);
                    };
                    org.junit.jupiter.api.Assertions.assertTrue(p.genders().equals(expected), "Afinidad sexual incoherente: " + p.subprofession() + "/" + p.characterClass());
                }
            }
        }
    }

    private static void nobleEquipmentAndArmor() {
        for (var entry : NobleCanonicalProfiles.all().entrySet()) {
            for (CharacterClass c : entry.getValue().keySet()) {
                var e = NobleStartingEquipmentCatalog.equipment(entry.getKey(), c);
                NobleStartingEquipmentCatalog.placement(entry.getKey(), c);
                org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().isPresent(), "Noble sin abalorio equipado: " + entry.getKey() + "/" + c);
                org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().get() instanceof ArtifactAccessory, "El abalorio canónico Noble debe ser artefacto: " + entry.getKey() + "/" + c);
                org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().stream().filter("Inyección estimulante"::equals).count() >= 3,
                        "Noble debe disponer de reserva de inyecciones: " + entry.getKey() + "/" + c);
                org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().contains("Esencia de lucidez"), "Falta Esencia de lucidez: " + entry.getKey() + "/" + c);
                org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().contains("Frasco de I-RND"), "Falta I-RND: " + entry.getKey() + "/" + c);
                for (ArmorPiece armor : e.wornGarments()) {
                    String n = armor.name();
                    org.junit.jupiter.api.Assertions.assertTrue(!n.contains("Históric") && !n.contains("Ébano"), "Armadura histórica/Ébano prohibida en Noble: " + n);
                }
                if (entry.getKey() == Subprofession.ENLIGHTENED_PATRON) {
                    org.junit.jupiter.api.Assertions.assertTrue(e.wornGarments().stream().anyMatch(a -> a.name().equals("Panóptico del Ilustrado")), "El Mecenas ilustrado debe llevar el Panóptico.");
                }
            }
        }
    }

    private static void astrolabeMigrationAndArtifactContract() {
        AstrolabeItem astrolabe = new AstrolabeItem();
        org.junit.jupiter.api.Assertions.assertTrue(astrolabe instanceof AccessoryItem, "El Astrolabio debe ser ACCESSORY.");
        org.junit.jupiter.api.Assertions.assertTrue(astrolabe instanceof ArtifactAccessory, "El Astrolabio debe implementar el contrato de artefacto.");
        org.junit.jupiter.api.Assertions.assertTrue(astrolabe.activationMinimum() == 22 && astrolabe.activationAttribute() == Attribute.CLARIVIDENCIA,
                "El Astrolabio requiere CLARIVIDENCIA 22.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.item.accessory.ArtifactAccessoryCatalog.all().stream().anyMatch(a -> a.name().equals("Astrolabio")), "Astrolabio ausente del catálogo de artefactos.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.catalog.PhysicalObjectCatalog.definitionForName("Astrolabio").family().equals("accessory"), "Astrolabio debe registrarse físicamente como ACCESSORY, no MISC.");
    }

    private static void masteryLinkage() {
        for (var entry : NobleCanonicalProfiles.all().entrySet()) {
            for (var p : entry.getValue().values()) {
                for (Gender g : p.genders()) {
                    var collection = CharacterMasteryCollection.forClass(p.characterClass(), g);
                    org.junit.jupiter.api.Assertions.assertTrue(!collection.knownMasteries(p.attributes()).isEmpty(),
                            "Clase sin maestrías resonantes: " + entry.getKey() + "/" + p.characterClass() + "/" + g);
                }
            }
        }
    }

    private static void forbiddenHistoricalSetsAbsentFromNoble() {
        for (var entry : NobleCanonicalProfiles.all().entrySet()) {
            for (CharacterClass c : entry.getValue().keySet()) {
                var e = NobleStartingEquipmentCatalog.equipment(entry.getKey(), c);
                String joined = e.wornGarments().stream().map(ArmorPiece::name).reduce("", (a,b)->a+"|"+b);
                org.junit.jupiter.api.Assertions.assertTrue(!joined.contains("Armadura de Caballero Histórica") && !joined.contains("Armadura de Ébano Histórica") && !joined.contains("Ébano V881"),
                        "Conjunto histórico/Ébano detectado: " + entry.getKey() + "/" + c);
            }
        }
    }

    
}

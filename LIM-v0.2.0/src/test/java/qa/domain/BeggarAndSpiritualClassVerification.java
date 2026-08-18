package qa.domain;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.CharacterClassDefinition;
import domain.character.sheet.Attribute;
import domain.social.*;

import java.util.Map;

public final class BeggarAndSpiritualClassVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyClasses();
        verifyBeggar();
    }

    private static void verifyClasses(){
        Map<CharacterClass,CharacterClassDefinition> defs=CharacterClassDefinition.canonicalDefinitions();
        org.junit.jupiter.api.Assertions.assertTrue(defs.size()==7,"Deben existir siete clases canónicas.");
        for(CharacterClass c:CharacterClass.values()){
            org.junit.jupiter.api.Assertions.assertTrue(!c.spiritualSeal().isBlank(),"Cada clase necesita sello espiritual: "+c);
            org.junit.jupiter.api.Assertions.assertTrue(c.narrativeDescription().length()>180,"Cada clase necesita doctrina narrativa suficiente: "+c);
            org.junit.jupiter.api.Assertions.assertTrue(c.narrativeDescription().contains("afinidad atributiva") || c.narrativeDescription().contains("afinidad"),
                    "La descripción debe distinguir sello y expresión atributiva: "+c);
        }
        org.junit.jupiter.api.Assertions.assertTrue(defs.get(CharacterClass.LUCHADOR).isAvailableFor(Gender.HOMBRE),"Luchador masculino.");
        org.junit.jupiter.api.Assertions.assertTrue(!defs.get(CharacterClass.LUCHADOR).isAvailableFor(Gender.MUJER),"Luchador no femenino.");
        org.junit.jupiter.api.Assertions.assertTrue(defs.get(CharacterClass.HERALDO).isAvailableFor(Gender.MUJER),"Heraldo femenino.");
        org.junit.jupiter.api.Assertions.assertTrue(!defs.get(CharacterClass.HERALDO).isAvailableFor(Gender.HOMBRE),"Heraldo no masculino.");
        org.junit.jupiter.api.Assertions.assertTrue(defs.get(CharacterClass.MAESTRO).isAvailableFor(Gender.HOMBRE) && defs.get(CharacterClass.MAESTRO).isAvailableFor(Gender.MUJER),
                "Maestro debe ser común a ambos sexos.");
    }

    private static void verifyBeggar(){
        org.junit.jupiter.api.Assertions.assertTrue(BeggarCanonicalProfiles.all().keySet().equals(new java.util.HashSet<>(Subprofession.forProfession(Profession.BEGGAR))),"Mendigo debe cubrir exactamente sus estados adultos canónicos.");
        for(var entry:BeggarCanonicalProfiles.all().entrySet()){
            org.junit.jupiter.api.Assertions.assertTrue(entry.getValue().size()==7,"Cada estado Mendigo conserva siete sellos históricos: "+entry.getKey());
            for(var p:entry.getValue().values()) org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel derivado: "+entry.getKey());
        }
    }

    
}

package qa.integration;
import domain.character.*; import domain.character.sheet.Attribute; import domain.social.*;
public final class KenanAndBeggarEquipmentVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){var k=KenanCanonicalProfile.initialSheet();org.junit.jupiter.api.Assertions.assertTrue(KenanCanonicalProfile.AGE_YEARS==6,"Kenan 6 años");org.junit.jupiter.api.Assertions.assertTrue(KenanCanonicalProfile.HEIGHT_METERS==1.16,"Kenan 1,16 m");org.junit.jupiter.api.Assertions.assertTrue(k.totalAttributeLevel()==9,"Kenan nivel 9");for(Attribute at:Attribute.values())org.junit.jupiter.api.Assertions.assertTrue(k.valueOf(at)==1,"Atributo CHILD=1");for(Subprofession s:Subprofession.forProfession(Profession.BEGGAR)){org.junit.jupiter.api.Assertions.assertTrue(BeggarCanonicalProfiles.isDeprecated(s,CharacterClass.MAESTRO),"Maestro deprecated");for(CharacterClass c:BeggarCanonicalProfiles.activeProfiles(s).keySet()){var e=BeggarStartingEquipmentCatalog.equipment(s,c);org.junit.jupiter.api.Assertions.assertTrue(e.weaponNames().isEmpty()&&e.ammunitionNames().isEmpty()&&e.personalTransport().isEmpty(),"Mendigo sin arsenal/transporte");}}}
 
}

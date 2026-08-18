package qa.domain;
import domain.ability.*; import domain.character.Gender; import domain.character.sheet.CharacterSheet; import java.util.*;
public final class MasteryRuntimeAndCollectionVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 CharacterSheet s=CharacterSheet.of(76,75,76,50,70,30,60,50,75); CharacterMasteryCollection c=CharacterMasteryCollection.allCanonical();
 org.junit.jupiter.api.Assertions.assertTrue(c.selectableManifestations(MasteryType.ACTIVE,s).stream().noneMatch(m->m.name().equals("EXPLOSIÓN CINÉTICA")),"Explosión ya no activa");
 org.junit.jupiter.api.Assertions.assertTrue(c.selectableManifestations(MasteryType.SUSTAINED,s).stream().anyMatch(m->m.name().equals("EXPLOSIÓN CINÉTICA")),"Explosión sostenida");
 CharacterMasteryCollection ap=CharacterMasteryCollection.forClass(domain.character.CharacterClass.APODERADO,Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(ap.knowledgeState(MasteryId.ESPIRITU_INFATIGABLE)==MasteryKnowledgeState.REVEALED,"Afinidad revela, no desbloquea"); ap.unlock(MasteryId.ESPIRITU_INFATIGABLE); org.junit.jupiter.api.Assertions.assertTrue(ap.isPassiveActive("ESPÍRITU INFATIGABLE",s),"pasiva desbloqueada activa");
 ap.unlockStage(MasteryId.SANAR,"DRENAR"); org.junit.jupiter.api.Assertions.assertTrue(ap.isStageUnlocked(MasteryId.SANAR,"DRENAR"),"Drenar progresivo");}
 }

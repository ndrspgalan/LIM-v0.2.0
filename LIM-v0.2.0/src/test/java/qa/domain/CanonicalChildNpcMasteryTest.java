package qa.domain;

import domain.ability.CharacterMasteryCollection;
import domain.ability.MasteryId;
import domain.ability.MasteryKnowledgeState;
import domain.character.canonical.CanonicalCharacterTimelineCatalog;
import domain.character.canonical.CanonicalLifeStage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("domain") @Tag("gold-smoke")
final class CanonicalChildNpcMasteryTest {
    @Test void allSevenCanonicalNpcChildrenMaterializeBothChildMasteriesUnlocked(){
        var children=CanonicalCharacterTimelineCatalog.all().stream()
                .filter(p->p.stage()==CanonicalLifeStage.CHILD)
                .filter(p->!p.name().equalsIgnoreCase("Kenan"))
                .toList();
        assertEquals(7,children.size());
        for(var profile:children){
            var collection=CharacterMasteryCollection.forCanonicalChild(profile);
            assertEquals(MasteryKnowledgeState.UNLOCKED,collection.knowledgeState(MasteryId.REGENERACION_THETA),profile.name());
            assertEquals(MasteryKnowledgeState.UNLOCKED,collection.knowledgeState(MasteryId.ESPIRITU_INFATIGABLE),profile.name());
        }
    }
}

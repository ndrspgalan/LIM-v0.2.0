package qa.domain;
import domain.ability.*; import domain.character.sheet.CharacterSheet; import domain.combat.*;
public final class CombatKitIntegrityVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 CharacterSheet s=CharacterSheet.of(30,50,20,30,50,30,20,20,20); CharacterMasteryCollection c=CharacterMasteryCollection.allCanonical(); org.junit.jupiter.api.Assertions.assertTrue(c.selectableManifestations(MasteryType.SUSTAINED,s).stream().anyMatch(m->m.name().equals("EXPLOSIÓN CINÉTICA")),"Explosión en sostenidas"); org.junit.jupiter.api.Assertions.assertTrue(!c.selectableManifestations(MasteryType.ACTIVE,s).stream().anyMatch(m->m.name().equals("EXPLOSIÓN CINÉTICA")),"Explosión fuera de activas"); org.junit.jupiter.api.Assertions.assertTrue(AttackKind.CHARGED.staminaMultiplier()==1.3&&AttackKind.CHARGED.bluntMultiplier()==1.3,"charged x1,3");}
 }

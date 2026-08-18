package qa.domain;

import domain.ability.*;
import domain.ability.progression.*;
import domain.character.CharacterClass;
import domain.character.sheet.CharacterSheet;
import domain.runic.*;

public final class MasteryDiscoveryAndRunicMarksVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyIndomitableDiscovery();
        verifyProvocationExactConjunction();
        verifyEvolutivesAndTransmutation();
        verifyAnimalEmpathyIrreversible();
        verifyRunicProgression();
    }

    private static CharacterSheet sheet(int vit,int agu,int ada,int fue,int des,int inte,int fe,int car,int cla) {
        return CharacterSheet.of(vit,agu,ada,fue,des,inte,fe,car,cla);
    }

    private static void verifyIndomitableDiscovery() {
        var c=CharacterMasteryCollection.forClass(CharacterClass.MAESTRO);
        var p=new MasteryProgressState();
        p.registerSleep(true,true);
        new MasteryProgressionPolicy().evaluate(c,sheet(1,60,1,1,1,1,1,1,1),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.PULSION).isVisible(),"PULSIÓN debe revelarse al dormir con penalizadores máximos simultáneos.");
    }

    private static void verifyProvocationExactConjunction() {
        var c=CharacterMasteryCollection.forClass(CharacterClass.MAESTRO); var p=new MasteryProgressState(); var policy=new MasteryProgressionPolicy();
        policy.evaluate(c,sheet(1,1,1,24,1,1,1,75,1),p, domain.character.Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.INCITAR)==MasteryKnowledgeState.UNKNOWN,"PROVOCAR depende solo de FUERZA 25.");
        policy.evaluate(c,sheet(1,1,1,25,1,1,1,1,1),p, domain.character.Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.INCITAR)==MasteryKnowledgeState.UNLOCKED && c.isStageUnlocked(MasteryId.INCITAR, "PROVOCAR"),"PROVOCAR se revela y desbloquea en FUERZA 25.");
        policy.evaluate(c,sheet(1,1,1,50,1,1,1,1,1),p, domain.character.Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(c.isStageUnlocked(MasteryId.INCITAR, "GRITO DE GUERRA"),"GRITO DE GUERRA se revela y desbloquea en FUERZA 50.");
    }

    private static void verifyEvolutivesAndTransmutation() {
        var c=CharacterMasteryCollection.forClass(CharacterClass.LUCHADOR); var p=new MasteryProgressState(); var policy=new MasteryProgressionPolicy();
        policy.evaluate(c,sheet(75,1,75,1,1,1,1,1,1),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.ELECTROGENESIS)==MasteryKnowledgeState.REVEALED,"ELECTROGÉNESIS narrativa en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.TRIBOGENESIS)==MasteryKnowledgeState.REVEALED,"TRIBOGÉNESIS narrativa en 75.");
        p.registerRealFrenzyDamage(); policy.evaluate(c,sheet(76,1,76,1,1,1,1,1,75),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.TRANSMUTACION)==MasteryKnowledgeState.UNLOCKED,"TRANSMUTACIÓN debe abrirse tras Frenesí real y CLARIVIDENCIA suficiente.");
        org.junit.jupiter.api.Assertions.assertTrue(c.unlockedTransmutationNodes().size()==5,"Los cinco nodos canónicos 11/22/33/66/75 deben desbloquearse en cascada.");
    }

    private static void verifyAnimalEmpathyIrreversible() {
        var c=CharacterMasteryCollection.forClass(CharacterClass.LUCHADOR); var p=new MasteryProgressState();
        for(String t:MasteryProgressionPolicy.REQUIRED_FEARE_TROPHIES)p.addFeareTrophy(t);
        new MasteryProgressionPolicy().evaluate(c,sheet(1,1,1,1,1,12,1,12,1),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.EMPATIA_ANIMAL)==MasteryKnowledgeState.UNKNOWN,"EMPATÍA ANIMAL exige acariciar al menos una Fera CARISMA.");
        p.registerCharismaFeraPet("perro");
        new MasteryProgressionPolicy().evaluate(c,sheet(1,1,1,1,1,12,1,12,1),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.EMPATIA_ANIMAL)==MasteryKnowledgeState.UNLOCKED,"EMPATÍA ANIMAL debe desbloquearse con trofeos y una Fera CARISMA acariciada."); p.removeFeareTrophy("cola de rata");
        new MasteryProgressionPolicy().evaluate(c,sheet(1,1,1,1,1,12,1,12,1),p);
        org.junit.jupiter.api.Assertions.assertTrue(c.knowledgeState(MasteryId.EMPATIA_ANIMAL)==MasteryKnowledgeState.UNLOCKED,"El desbloqueo debe ser irreversible.");
    }

    private static void verifyRunicProgression() {
        var c=CharacterMasteryCollection.forClass(CharacterClass.INDOMITO);
        for(MasteryId id:MasteryId.values()) if(MasteryCatalog.require(id).resonanceClass()==CharacterClass.INDOMITO)c.unlock(id);
        var policy=new RunicMarkProgressPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(CharacterClass.INDOMITO,c,sheet(1,75,1,1,1,1,1,1,1))==RunicMarkProgressState.COSMETIC,"La rama afín completa sólo debe manifestar la Marca como cosmético.");
        var completed=CharacterMasteryCollection.allCanonical();
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(CharacterClass.INDOMITO,completed,sheet(1,75,1,1,1,1,1,1,1))==RunicMarkProgressState.AWAKENED,"La Marca debe despertar al completar todas las maestrías.");
    }

    
}

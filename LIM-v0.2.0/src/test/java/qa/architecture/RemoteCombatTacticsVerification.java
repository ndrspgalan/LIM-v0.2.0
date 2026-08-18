package qa.architecture;

import domain.combat.ai.declarative.*;
import domain.combat.ai.remote.*;
import domain.inventory.item.firearms.FirearmCatalog;
import java.util.List;

/** la antigua táctica remota se verifica como hechos/candidatos sin scoring. */
public final class RemoteCombatTacticsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        var pistol=RemoteCombatOptionFactory.firearm(FirearmCatalog.autoloadingPistolV881());
        var arsenal=new RemoteArsenalSnapshot(List.of(pistol));
        var candidates=new RemoteActionCandidateResolver().resolve(arsenal,Math.max(pistol.minimumAdequateDistanceMeters(),1));
        org.junit.jupiter.api.Assertions.assertTrue(!candidates.isEmpty(),"El arsenal remoto debe producir candidatos declarativos.");
        org.junit.jupiter.api.Assertions.assertTrue(candidates.stream().allMatch(c->c.sourceName().equals(pistol.name())),"La identidad física de la fuente debe conservarse.");
        org.junit.jupiter.api.Assertions.assertTrue(java.util.Arrays.stream(RemoteActionCandidate.class.getRecordComponents()).noneMatch(c->c.getName().equals("score")||c.getName().equals("priority")),
                "RemoteActionCandidate no puede contener scoring.");
        org.junit.jupiter.api.Assertions.assertTrue(!java.nio.file.Files.exists(java.nio.file.Path.of("src/main/java/domain/combat/ai/remote/RemoteTacticalScorePolicy.java")),
                "RemoteTacticalScorePolicy debe haber desaparecido.");
    }
    
}

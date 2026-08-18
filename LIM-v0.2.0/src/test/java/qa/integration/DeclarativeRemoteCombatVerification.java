package qa.integration;

import domain.combat.ai.declarative.*;
import domain.combat.ai.remote.*;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import java.util.List;

/**  — contrato declarativo remoto. No ejecutar en el ciclo normal. */
public final class DeclarativeRemoteCombatVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyLead46Identity(); verifyRangedAmmoIdentity(); verifyThrownEffects(); verifyNoScoringSurface();
    }
    private static void verifyLead46Identity() {
        var option=RemoteCombatOptionFactory.firearm(FirearmCatalog.repeatingPneumaticRifleV881());
        var c=new RemoteActionCandidateResolver().resolve(new RemoteArsenalSnapshot(List.of(option)),10).getFirst();
        org.junit.jupiter.api.Assertions.assertTrue(c.ammunition().isPresent(),"El firearm declara identidad material de munición");
        org.junit.jupiter.api.Assertions.assertTrue(".46".equals(c.ammunition().orElseThrow().caliber()),"Calibre .46");
        org.junit.jupiter.api.Assertions.assertTrue("Plomo".equalsIgnoreCase(c.ammunition().orElseThrow().material()),"Material plomo");
        org.junit.jupiter.api.Assertions.assertTrue(c.relations().stream().anyMatch(r->r.relation().equals("HELICOIDAL_INTERACTION")),"Relación helicoidal factual");
    }
    private static void verifyRangedAmmoIdentity() {
        var bow=RangedWeaponCatalog.simpleRecurveBow();
        var arrow=AmmunitionCatalog.piercingArrow();
        var option=RemoteCombatOptionFactory.ranged(bow,arrow.ammunitionDescriptor(),true,0);
        var c=new RemoteActionCandidateResolver().resolve(new RemoteArsenalSnapshot(List.of(option)),12).getFirst();
        org.junit.jupiter.api.Assertions.assertTrue(c.action()==RemoteActionType.FIRE,"Ranged listo materializa FIRE");
        org.junit.jupiter.api.Assertions.assertTrue(c.ammunition().isPresent(),"Flecha explícita");
    }
    private static void verifyThrownEffects() {
        var ammonia=ThrowingWeaponCatalog.ammoniaGasCapsuleV881();
        var option=RemoteCombatOptionFactory.thrown(ammonia,15,ammonia.throwProfile().lethalityProfile().orElse(new domain.inventory.item.LethalityProfile(0,0,0)));
        var c=new RemoteActionCandidateResolver().resolve(new RemoteArsenalSnapshot(List.of(option)),8).getFirst();
        org.junit.jupiter.api.Assertions.assertTrue(c.action()==RemoteActionType.THROW,"Arrojadizo listo materializa THROW");
        org.junit.jupiter.api.Assertions.assertTrue(c.relations().stream().anyMatch(r->r.detail().contains("detona")||r.detail().contains("rompe")),"Carga frágil no se neutraliza contra Helicoidal");
    }
    private static void verifyNoScoringSurface() {
        for (var m: RemoteActionCandidate.class.getDeclaredMethods()) {
            String n=m.getName().toLowerCase(); org.junit.jupiter.api.Assertions.assertTrue(!n.contains("score")&&!n.contains("priority"),"El candidato declarativo no expone scoring");
        }
    }
    
}

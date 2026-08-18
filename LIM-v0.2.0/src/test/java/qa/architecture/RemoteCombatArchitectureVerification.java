package qa.architecture;

import domain.combat.ai.declarative.*;
import domain.combat.ai.remote.*;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.LethalityProfile;
import java.util.List;

/**  conserva la arquitectura material remota y retira el selector táctico obsoleto. */
public final class RemoteCombatArchitectureVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(RangedDistancePolicy.classify(2,3,50)==RangedDistanceState.TOO_CLOSE,"too close");
        org.junit.jupiter.api.Assertions.assertTrue(RangedDistancePolicy.classify(3,3,50)==RangedDistanceState.ADEQUATE,"adequate lower");
        org.junit.jupiter.api.Assertions.assertTrue(RangedDistancePolicy.classify(51,3,50)==RangedDistanceState.TOO_FAR,"too far");
        var pistol=RemoteCombatOptionFactory.firearm(FirearmCatalog.autoloadingPistolV881());
        org.junit.jupiter.api.Assertions.assertTrue(pistol.family()==RemoteOffenseFamily.FIREARM,"firearm adapter");
        var bow=RangedWeaponCatalog.simpleRecurveBow();
        var arrow=AmmunitionCatalog.piercingArrow().ammunitionDescriptor();
        org.junit.jupiter.api.Assertions.assertTrue(RemoteCombatOptionFactory.ranged(bow,arrow,true,0).family()==RemoteOffenseFamily.RANGED_WEAPON,"ranged adapter");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteCombatOptionFactory.thrown(ThrowingWeaponCatalog.throwingKnifeV881(),20,new LethalityProfile(25,0,30.1)).family()==RemoteOffenseFamily.THROWN,"thrown adapter");
        var facts=new RemoteActionCandidateResolver().resolve(new RemoteArsenalSnapshot(List.of(pistol)),10);
        org.junit.jupiter.api.Assertions.assertTrue(!facts.isEmpty(),"/ debe materializar el arsenal como acciones declarativas.");
        org.junit.jupiter.api.Assertions.assertTrue(!java.nio.file.Files.exists(java.nio.file.Path.of("src/main/java/domain/combat/ai/remote/RemoteTacticalPolicy.java")),"El decisor remoto obsoleto debe eliminarse.");
    }
    
}

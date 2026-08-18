package qa.domain;
import domain.ability.*; import domain.combat.*;
public final class FinalCombatAndNarrativeVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 PhysicalDamage d=new PhysicalDamage(10,20,30); PhysicalDamage out=new AuraPulsionProjectilePolicy().mitigateGrossProjectile(d,true); org.junit.jupiter.api.Assertions.assertTrue(out.equals(d),"Aura ya no mitiga proyectiles");
 org.junit.jupiter.api.Assertions.assertTrue(new PulsionCombatPolicy().multiplier(50)==1.0,"Reciclaje no amplifica B global");
 var n=NullificationPolicy.incidentalContact(75); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.runicMarkUsable(n)&&NullificationPolicy.masteryUsable(n),"Anulación no apaga runas/maestrías");}
 }

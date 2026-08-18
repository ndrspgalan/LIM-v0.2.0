package qa.domain;
import domain.ability.*; import domain.combat.*;
public final class PulsionTrajectoryAndKineticVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 PulsionCombatPolicy p=new PulsionCombatPolicy(); org.junit.jupiter.api.Assertions.assertTrue(eq(p.feintStaminaCost(35,true),5)&&eq(p.feintStaminaCost(50,true),3.5),"finta PA"); org.junit.jupiter.api.Assertions.assertTrue(eq(p.jumpHeightMultiplier(35,true),1)&&eq(p.jumpHeightMultiplier(50,true),1.5),"salto"); org.junit.jupiter.api.Assertions.assertTrue(eq(p.multiplier(50),1),"sin B global");
 PhysicalDamage base=new PhysicalDamage(10,20,30); org.junit.jupiter.api.Assertions.assertTrue(new AuraPulsionProjectilePolicy().mitigateGrossProjectile(base,true).equals(base),"Aura no mitiga");
 ConvergentTrajectoryPolicy t=new ConvergentTrajectoryPolicy(); org.junit.jupiter.api.Assertions.assertTrue(eq(t.onLightAttack(true,3,3,false,true),1.4),"remate x1,4"); org.junit.jupiter.api.Assertions.assertTrue(eq(t.onLightAttack(true,1,3,false,true),1.4),"Flow x1,4");
 org.junit.jupiter.api.Assertions.assertTrue(new KineticExplosionPolicy().resolveWhenStaminaEmpty(1.8,20,true,0).isPresent()&&!new KineticExplosionPolicy().resolveWhenStaminaEmpty(1.8,20,true,1).isPresent(),"Explosión sólo PA0");}
 static boolean eq(double a,double b){return Math.abs(a-b)<1e-9;} }

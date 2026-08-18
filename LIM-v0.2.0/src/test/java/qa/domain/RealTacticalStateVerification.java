package qa.domain;
import domain.ability.*; import domain.character.Gender; import domain.environment.*; import domain.combat.stamina.*;
public final class RealTacticalStateVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 org.junit.jupiter.api.Assertions.assertTrue(!HalfStaminaMasteryPolicy.canUse("EXPLOSIÓN CINÉTICA",50,100),"ya no se activa al 50%"); org.junit.jupiter.api.Assertions.assertTrue(new MalignantEnergyRefinementPolicy().canTrigger(0),"trigger PA0"); org.junit.jupiter.api.Assertions.assertTrue(!new MalignantEnergyRefinementPolicy().canTrigger(1),"no trigger con PA");
 var f=new StaminaLoadRecoveryPolicy(); org.junit.jupiter.api.Assertions.assertTrue(eq(f.resolveFrost(100,0,60,false).fullRecoverySeconds(),5)&&eq(f.resolveFrost(100,0,60,true).fullRecoverySeconds(),3),"Frío 5/3");}
 static boolean eq(double a,double b){return Math.abs(a-b)<1e-9;} }

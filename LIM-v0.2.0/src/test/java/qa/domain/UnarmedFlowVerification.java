package qa.domain;

import domain.ability.ConvergentTrajectoryPolicy;
import domain.inventory.item.LightAttackComboProfile;

public final class UnarmedFlowVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(LightAttackComboProfile.PROVISIONAL_STANDARD_ATTACK_COUNT == 3,
                "El combo estándar de 3 golpes debe quedar marcado como placeholder provisional.");
        ConvergentTrajectoryPolicy p = new ConvergentTrajectoryPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,1,3,false,true),1.0),"apertura 1");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,2,3,false,true),1.0),"apertura 2");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,3,3,false,true),1.4),"primer remate abre flow");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,1,3,false,true),1.4),"flow x1,4");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,2,3,false,true),1.4),"flow fijo x1,4");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,3,3,false,true),1.4),"otro remate conserva x1,4");
        org.junit.jupiter.api.Assertions.assertTrue(!p.unarmedChainOpen(),"completar otro combo debe cerrar flow");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,1,3,false,true),1.0),"tras cerrar flow vuelve a basal");

        p.reset(); p.onLightAttack(true,3,3,false,true); p.onLightAttack(true,1,3,false,true);
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,1,3,true,true),1.0),"PA REGEN cierra flow");
        p.onLightAttack(true,3,3,false,true);
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true,1,3,false,true),1.4),"El estado de Flow debe seguir siendo compatible con OVERDRIVE");
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;}
    
}

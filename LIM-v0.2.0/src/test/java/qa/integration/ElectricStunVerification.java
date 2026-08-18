package qa.integration;

import domain.combat.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorHitLocation;

public final class ElectricStunVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        close(ElectricStunPolicy.stunSeconds(33),0.33,"E33");
        close(ElectricStunPolicy.stunSeconds(100),1.0,"E100");
        var half = new NonConventionalDamageResolver().resolve(
                DamageType.ELECTRICITY,100,ArmorHitLocation.BODY,EquipmentState.empty(),50,false);
        close(half.netDamage(),50,"E100 con 50 % de resistencia");
        close(half.stunSeconds(),0.5,"El stun se calcula sobre electricidad neta");
    }
    private static void close(double a,double b,String m){if(Math.abs(a-b)>EPS)throw new AssertionError(m+": "+a+" != "+b);}
}

package qa.domain;
import domain.ability.*; import domain.runic.*;
public final class RunicNavigationAndRecoveryVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 var n=NullificationPolicy.incidentalContact(75); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.runicMarkUsable(n),"Anulación ya no suprime marcas"); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.masteryUsable(n),"Anulación ya no suprime maestrías"); org.junit.jupiter.api.Assertions.assertTrue(!NullificationPolicy.accessoryPropertyUsable(n),"sí suprime abalorio capturado");}
 }

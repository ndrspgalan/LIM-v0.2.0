package qa.domain;
import domain.ability.*;
public final class TacticalKitVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 PairMastery n=(PairMastery)MasteryCatalog.require(MasteryId.ANULACION); org.junit.jupiter.api.Assertions.assertTrue(n.original().type()==MasteryType.PASSIVE&&n.refined().type()==MasteryType.PASSIVE,"Anulación pasiva"); org.junit.jupiter.api.Assertions.assertTrue(n.original().scalingStart()==6&&n.refined().scalingStart()==6,"Anulación AGUANTE6");}
 }

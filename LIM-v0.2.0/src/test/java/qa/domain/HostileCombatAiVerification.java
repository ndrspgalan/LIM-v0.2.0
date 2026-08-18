package qa.domain;
import domain.ability.*; import domain.social.RelationshipType;
public final class HostileCombatAiVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 org.junit.jupiter.api.Assertions.assertTrue(Double.isInfinite(NullificationPolicy.suppressionSeconds(6)),"Anulación persiste hasta fin encuentro"); var s=NullificationPolicy.apply(RelationshipType.HOSTILE,10,6,true,"Abalorio",true); org.junit.jupiter.api.Assertions.assertTrue(s.suppressed()&&s.insideField(),"fundacional aplica en campo"); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.masteryUsable(s)&&NullificationPolicy.runicMarkUsable(s),"no suprime maestría/runa"); org.junit.jupiter.api.Assertions.assertTrue(s.endHostileEncounter().equals(NullificationPolicy.SuppressionState.none()),"fin encuentro limpia");}
 }

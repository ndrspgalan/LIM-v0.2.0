package qa.domain;
import domain.ability.*; import domain.character.sheet.Attribute;
public final class MasterySynchronizationVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 PairMastery e=(PairMastery)MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA); org.junit.jupiter.api.Assertions.assertTrue(e.original().scalingAttribute()==Attribute.AGUANTE&&e.refined().scalingAttribute()==Attribute.AGUANTE,"Refinamiento usa AGUANTE"); org.junit.jupiter.api.Assertions.assertTrue(e.name().equals("REFINAMIENTO DE ENERGÍA MALDITA"),"nombre");
 StructuredMastery t=(StructuredMastery)MasteryCatalog.require(MasteryId.TRAYECTORIA_CONVERGENTE); String x=t.stages().get(0).mechanicalDescription(); org.junit.jupiter.api.Assertions.assertTrue(x.contains("x1,40")&&!x.contains("x2"),"Trayectoria fija x1,40");
 StructuredMastery h=(StructuredMastery)MasteryCatalog.require(MasteryId.SANAR); org.junit.jupiter.api.Assertions.assertTrue(h.stages().get(1).name().equals("RESTAURAR")&&h.stages().get(2).name().equals("CUSTODIA")&&h.stages().get(2).natures().contains(MasteryType.PASSIVE),"Restaurar/Custodia"); org.junit.jupiter.api.Assertions.assertTrue(h.stages().get(2).mechanicalDescription().contains("No repele"),"Custodia sin repulsión");}
 }

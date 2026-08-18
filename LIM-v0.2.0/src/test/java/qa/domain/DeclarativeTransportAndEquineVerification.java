package qa.domain;

import domain.ability.*;
import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.equine.*;
import domain.inventory.logistics.*;

/** QA acumulado . No se ejecuta en el ciclo normal. */
public final class DeclarativeTransportAndEquineVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  org.junit.jupiter.api.Assertions.assertTrue(EquineFeraeCatalog.canonical().size()==6,"Deben existir tres caballos y sus tres yeguas.");
  for(var v:EquineMountVariant.values()){
   var h=EquineFeraeCatalog.stallion(v); var m=EquineFeraeCatalog.mare(v);
   org.junit.jupiter.api.Assertions.assertTrue(h.species().branch()==FeraeBranch.INTELIGENCIA,"Caballo debe pertenecer a INTELIGENCIA.");
   org.junit.jupiter.api.Assertions.assertTrue(m.species().branch()==FeraeBranch.CARISMA,"Yegua debe pertenecer a CARISMA.");
   org.junit.jupiter.api.Assertions.assertTrue(h.canonicalLevel()==h.attributes().totalAttributeLevel() && m.canonicalLevel()==m.attributes().totalAttributeLevel(),"Nivel ecuestre debe derivar de atributos.");
   var ctx=new AnimalEmpathyContext(true,MasteryKnowledgeState.REVEALED,120,120,java.util.Set.of(HuntingTrophy.CERDA_DE_CABALLO),true);
   org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.companionEligibility(m.species(),ctx).eligible(),"Yegua debe conectar con EMPATÍA ANIMAL por CARISMA.");
  }
  org.junit.jupiter.api.Assertions.assertTrue(MotorcycleFuelState.CONSUMPTION_L_PER_100_KM>0,"Motocicleta debe conservar economía de combustible.");
 }
 
}

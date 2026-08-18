package qa.domain;

import domain.bestiarium.BestiaryTaxon;
import domain.bestiarium.ExistencePlane;
import domain.bestiarium.interstice.transcended.*;
import java.util.Map;

/** ontología y política causal TRANSCENDED, sin contenido de level design. */
public final class TranscendedPolicyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        ontology(); canonicalLaws(); discreteState(); uniqueCausality(); multiLawAndNeutralMeasurement(); tendencyOnlyOpportunity(); saturation();
    }

    private static void ontology(){
        org.junit.jupiter.api.Assertions.assertTrue(TranscendedDoctrine.taxon()==BestiaryTaxon.TRANSCENDED,"Taxón TRANSCENDED.");
        org.junit.jupiter.api.Assertions.assertTrue(TranscendedDoctrine.plane()==ExistencePlane.INTERSTICE,"TRANSCENDED pertenece al Intersticio.");
        org.junit.jupiter.api.Assertions.assertTrue(!TranscendedDoctrine.hasPhysicalForm(),"TRANSCENDED carece de forma física.");
        org.junit.jupiter.api.Assertions.assertTrue(TranscendedDoctrine.canonicalNarrative().contains("No imponen la voluntad de Kenan"),"La doctrina preserva agencia.");
        org.junit.jupiter.api.Assertions.assertTrue(TranscendedDoctrine.canonicalNarrative().contains("No trabajan con probabilidades"),"La doctrina usa oportunidades deterministas, no RNG.");
    }

    private static void canonicalLaws(){
        org.junit.jupiter.api.Assertions.assertTrue(TranscendedLaw.values().length==7," canoniza siete leyes.");
        for(TranscendedLaw law:TranscendedLaw.values()){
            org.junit.jupiter.api.Assertions.assertTrue(!law.poleZeroLabel().isBlank()&&!law.poleOneLabel().isBlank(),"Cada ley tiene dos polos narrativos.");
            org.junit.jupiter.api.Assertions.assertTrue(law.canonicalNarrative().length()>120,"Cada ley posee descripción narrativa canónica suficiente: "+law);
        }
    }

    private static void discreteState(){
        TranscendedState state=new TranscendedState();
        for(TranscendedLaw law:TranscendedLaw.values()){
            org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(law).step()==5,"Toda ley nace exactamente en 0.5.");
            close(state.valueOf(law).normalized(),.5,"Normalización neutral.");
            org.junit.jupiter.api.Assertions.assertTrue(state.tendencyOf(law)==TranscendedTendency.NEUTRAL,"0.5 no ejerce influencia.");
        }
        state.apply(TranscendedLaw.SCARCITY_ABUNDANCE,TranscendedShift.TOWARD_ONE);
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.SCARCITY_ABUNDANCE).step()==6,"Una acción significativa desplaza exactamente +0.1.");
        org.junit.jupiter.api.Assertions.assertTrue(state.tendencyOf(TranscendedLaw.SCARCITY_ABUNDANCE)==TranscendedTendency.POLE_ONE,"0.6 sólo significa tendencia al polo uno.");
        state.apply(TranscendedLaw.SCARCITY_ABUNDANCE,TranscendedShift.TOWARD_ZERO);
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.SCARCITY_ABUNDANCE).step()==5,"Una acción contraria devuelve a neutralidad.");
    }

    private static void uniqueCausality(){
        TranscendedState state=new TranscendedState();
        TranscendedCausalMemory memory=new TranscendedCausalMemory();
        TranscendedCausalityPolicy policy=new TranscendedCausalityPolicy();
        CausalEvent firstTrade=CausalEvent.of("first-trade","merchant-14:first-trade",TranscendedLaw.SCARCITY_ABUNDANCE,TranscendedShift.TOWARD_ONE,"Primer intercambio que abre una relación comercial.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.apply(firstTrade,state,memory),"La primera causalidad se aplica.");
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.SCARCITY_ABUNDANCE).step()==6,"Primera causalidad +0.1.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.apply(firstTrade,state,memory),"Repetir la misma acción causal no vuelve a puntuar.");
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.SCARCITY_ABUNDANCE).step()==6,"La unicidad impide farmear causalidad.");
        CausalEvent familiarity=CausalEvent.of("merchant-familiarity","merchant-14:familiarity",TranscendedLaw.APPROPRIATION_RECIPROCITY,TranscendedShift.TOWARD_ONE,"La repetición física materializa después un hecho social distinto.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.apply(familiarity,state,memory),"La misma conducta física puede materializar otra causalidad con otra unicidad.");
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.APPROPRIATION_RECIPROCITY).step()==6,"La nueva causalidad modifica otra ley.");
    }

    private static void multiLawAndNeutralMeasurement(){
        TranscendedState state=new TranscendedState();
        TranscendedCausalMemory memory=new TranscendedCausalMemory();
        TranscendedCausalityPolicy policy=new TranscendedCausalityPolicy();
        CausalEvent event=new CausalEvent("complex","complex:1",Map.of(
                TranscendedLaw.COMPETITION_COOPERATION,TranscendedShift.TOWARD_ONE,
                TranscendedLaw.CONCENTRATION_DISTRIBUTION,TranscendedShift.TOWARD_ZERO,
                TranscendedLaw.CONTINGENCY_DETERMINATION,TranscendedShift.NEUTRAL),"Un hecho puede tocar varias leyes sin pesos arbitrarios.");
        policy.apply(event,state,memory);
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.COMPETITION_COOPERATION).step()==6,"+0.1 discreto.");
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.CONCENTRATION_DISTRIBUTION).step()==4,"-0.1 discreto.");
        org.junit.jupiter.api.Assertions.assertTrue(state.valueOf(TranscendedLaw.CONTINGENCY_DETERMINATION).step()==5,"Efecto explícitamente neutral = 0.");
        CausalEvent measured=CausalEvent.measuredNeutral("measured","measured:1","Hecho parametrizado sin inclinación TRANSCENDED.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.apply(measured,state,memory)&&memory.hasConsumed("measured:1"),"Un hecho relevante puede medirse y memorizarse aunque no incline ninguna ley.");
    }

    private static void tendencyOnlyOpportunity(){
        TranscendedState state=new TranscendedState();
        TranscendedOpportunityPolicy policy=new TranscendedOpportunityPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(state,TranscendedLaw.SCARCITY_ABUNDANCE,"ordinario","escasez","abundancia").equals("ordinario"),"0.5 deja actuar al world state ordinario.");
        state.apply(TranscendedLaw.SCARCITY_ABUNDANCE,TranscendedShift.TOWARD_ONE);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(state,TranscendedLaw.SCARCITY_ABUNDANCE,"ordinario","escasez","abundancia").equals("abundancia"),"0.6 selecciona tendencia, no umbral de premio.");
        for(int i=0;i<4;i++)state.apply(TranscendedLaw.SCARCITY_ABUNDANCE,TranscendedShift.TOWARD_ONE);
        org.junit.jupiter.api.Assertions.assertTrue(policy.resolve(state,TranscendedLaw.SCARCITY_ABUNDANCE,"ordinario","escasez","abundancia").equals("abundancia"),"1.0 ofrece la misma familia causal que 0.6; sólo cambia la inercia almacenada.");
    }

    private static void saturation(){
        TranscendedValue v=TranscendedValue.neutral();
        for(int i=0;i<20;i++)v=v.shifted(TranscendedShift.TOWARD_ONE);
        org.junit.jupiter.api.Assertions.assertTrue(v.step()==10,"El estado satura en 1.0.");
        for(int i=0;i<30;i++)v=v.shifted(TranscendedShift.TOWARD_ZERO);
        org.junit.jupiter.api.Assertions.assertTrue(v.step()==0,"El estado satura en 0.0.");
    }

    private static void close(double a,double b,String m){org.junit.jupiter.api.Assertions.assertTrue(Math.abs(a-b)<1e-9,m+" ["+a+" != "+b+"]");}
    
}

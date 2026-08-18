package qa.domain;

import domain.inventory.item.armor.ArmorCatalog;
import domain.knowledge.*;
import domain.social.*;
import java.util.*;

public final class EliteContinuityKnowledgeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        shape();
        masterKnowledgeChain();
        institutionalCustody();
        permanencePretender();
        enlightenedAndPanopticon();
        internalContinuityDoctrine();
        previousContractsRemain();
    }

    private static void shape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=97,"– deben sumar 97 subprofesiones.");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,"Toda subprofesión canónica debe tener perfil.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.values().length==19,"No se crean profesiones madre nuevas.");
    }

    private static void masterKnowledgeChain(){
        check(Subprofession.REGENERATIONIST,Profession.TEACHER,2200);
        check(Subprofession.CONTINUITY_EPIGENETICIST,Profession.TEACHER,2350);
        check(Subprofession.NEUROARCHITECT,Profession.TEACHER,2400);
        check(Subprofession.SOUL_RESEARCHER,Profession.TEACHER,2500);
        check(Subprofession.SOUL_TRANSFUSIONIST,Profession.TEACHER,2750);
        check(Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,Profession.TEACHER,3100);
        check(Subprofession.PERMANENCE_RESEARCHER,Profession.TEACHER,3200);
        check(Subprofession.ENLIGHTENED,Profession.TEACHER,3500);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.REGENERATIONIST.narrativeDescription().toLowerCase(java.util.Locale.ROOT)
                        .contains("no confunde regeneración con rejuvenecimiento"),
                "Regeneracionista debe separar restauración de rejuvenecimiento.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.CONTINUITY_EPIGENETICIST.narrativeDescription().contains("biomáquina basada en carbono-12"),
                "Epigenetista debe fijar la base material mutable.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.NEUROARCHITECT.narrativeDescription().contains("autobiografía"),
                "Neuroarquitecto debe conectar patrones neuronales y anatomía.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.SOUL_RESEARCHER.narrativeDescription().contains("No trabaja con fe"),
                "Investigador álmico debe ser empírico.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.SOUL_TRANSFUSIONIST.narrativeDescription().contains("no reinicia"),
                "Trasvasista debe negar el reinicio de identidad.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER.narrativeDescription().contains("sustituir progresivamente ese régimen mutable"),
                "Metamorfosis debe cambiar régimen material.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PERMANENCE_RESEARCHER.narrativeDescription().contains("longevidad, inmortalidad material y permanencia"),
                "Permanencia debe diferenciar los tres estados.");
    }

    private static void institutionalCustody(){
        check(Subprofession.CONTINUITY_JURIST,Profession.JURIST,2200);
        check(Subprofession.DOCTRINE_CUSTODIAN,Profession.JURIST,2450);
        check(Subprofession.FREQUENCY_INSTRUMENT_MAKER,Profession.BLACKSMITH,2000);
        check(Subprofession.MATRIX_ARCHITECT,Profession.BLACKSMITH,2600);
        check(Subprofession.EXCEPTIONAL_ASSET_RECOVERER,Profession.MERCENARY,2250);
        check(Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN,Profession.SOLDIER,1650);
        check(Subprofession.RESTRICTED_MATERIALS_BROKER,Profession.MERCHANT,2400);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.DOCTRINE_CUSTODIAN.narrativeDescription().contains("Esfera del Progreso"),
                "Custodio debe explicar la frontera del discurso público.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FREQUENCY_INSTRUMENT_MAKER.narrativeDescription().contains("Osciladores"),
                "Instrumentista debe materializar la ciencia frecuencial.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EXCEPTIONAL_ASSET_RECOVERER.narrativeDescription().contains("no preguntar"),
                "Recuperador debe ocupar la logística excepcional.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN.narrativeDescription().contains("compartimentada"),
                "Custodio militar debe permitir secreto sin omnisciencia.");
    }

    private static void permanencePretender(){
        Subprofession p=Subprofession.PERMANENCE_PRETENDER;
        org.junit.jupiter.api.Assertions.assertTrue(p.profession()==Profession.NOBLE,"Pretendiente debe ser Noble.");
        org.junit.jupiter.api.Assertions.assertTrue(p.profile().incomeKind()==ProfessionIncomeKind.PATRIMONIAL_RENT,
                "Pretendiente debe usar renta patrimonial.");
        org.junit.jupiter.api.Assertions.assertTrue(p.monthlyReferenceValeritas()==420000,"Pretendiente = 2 Berylares/mes de referencia.");
        org.junit.jupiter.api.Assertions.assertTrue(p.monthlyReferenceLabel().equals("2 Berylare/mes"),"Etiqueta de renta del Pretendiente.");

        String n=p.narrativeDescription();
        for(String term:List.of("mucus negruzco","VITALIDAD","ADAPTABILIDAD","doppelgänger","Convergieron")){
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(term),"Pretendiente debe conocer: "+term);
        }
    }

    private static void enlightenedAndPanopticon(){
        String enlightened=Subprofession.ENLIGHTENED.narrativeDescription();
        for(String term:List.of("Intersticio","líneas telúricas","Velo","teletransporte","Panóptico del Ilustrado")){
            org.junit.jupiter.api.Assertions.assertTrue(enlightened.contains(term),"Ilustrado debe explicar: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(enlightened.contains("no llama al Velo un mundo de espíritus")
                        || enlightened.contains("No llama al Velo un mundo de espíritus"),
                "Ilustrado no puede confundir Velo con mundo espiritual.");

        String panopticon=ArmorCatalog.enlightenedPanopticon().narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(panopticon.contains("tradición de los Ilustrados"),
                "El Panóptico debe reconectar explícitamente con la subprofesión Ilustrado.");
    }

    private static void internalContinuityDoctrine(){
        org.junit.jupiter.api.Assertions.assertTrue(ContinuityScienceDoctrine.visibility()==CanonVisibility.INTERNAL_CANON,
                "La cadena completa es canon interno.");
        String truth=ContinuityScienceDoctrine.truth();
        for(String term:List.of("memoria epigenética","arquitectura neuronal","alma","trasvase",
                "carbono-12","milenio","metamorfosis silícica","permanencia","Intersticio")){
            org.junit.jupiter.api.Assertions.assertTrue(truth.contains(term),"Doctrina de continuidad debe contener: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(WorldOrderCanon.all().containsKey("ciencia-de-continuidad"),
                "WorldOrderCanon debe exponer el núcleo interno de continuidad.");
    }

    private static void previousContractsRemain(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EBONY_WARRIOR_V881.contemporaryHolder().orElseThrow().equals("Kenan"),
                "Kenan continúa único.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PERMANENCE_PRETENDER.profession()==Profession.NOBLE,
                " no crea una profesión nueva para los pretendientes.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,
                "La referencia madre de Noble permanece en 1 Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(IntersticeTopologyDoctrine.veil().contains("hendiduras des-veladas del Intersticio"),
                "Velo/Intersticio  intacto.");
    }

    private static void check(Subprofession s,Profession p,int value){
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==p,s.label()+" parent incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==value,s.label()+" paga incorrecta.");
    }

    
}

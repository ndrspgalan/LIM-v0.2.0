package qa.domain;

import domain.social.*;
import java.util.*;

public final class CivilNormalizationSubprofessionsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        shape();
        wagesAndParents();
        analogueCivilSociety();
        medicineAndAnimalCare();
        reconstructionAndConsumption();
        institutionalRecovery();
        previousContractsRemain();
    }

    private static void shape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=33,"El catálogo debe conservar las 33 subprofesiones acumuladas hasta .");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,
                "Toda subprofesión actualmente canónica debe tener perfil.");
        
        org.junit.jupiter.api.Assertions.assertTrue(Profession.EBONY_WARRIOR.authoredSubprofessions().size()==1,
                "Kenan continúa siendo la única subprofesión de Guerrero de Ébano.");
    }

    private static void wagesAndParents(){
        check(Subprofession.STONEWORK_MASTER,Profession.STONEMASON,1200);
        check(Subprofession.PRECISION_STONECUTTER,Profession.STONEMASON,1100);
        check(Subprofession.STRUCTURAL_CARPENTER,Profession.CARPENTER,1100);
        check(Subprofession.CABINETMAKER,Profession.CARPENTER,1250);
        check(Subprofession.INDUSTRIAL_TANNER,Profession.TANNER,950);
        check(Subprofession.WORK_TAILOR,Profession.DRESSMAKER,1000);
        check(Subprofession.SALON_DRESSMAKER,Profession.DRESSMAKER,1300);
        check(Subprofession.BARBER,Profession.HAIRDRESSER,800);
        check(Subprofession.SALON_HAIRDRESSER,Profession.HAIRDRESSER,1050);
        check(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,Profession.FAIRGROUND_WORKER,700);
        check(Subprofession.FAIRGROUND_ENTREPRENEUR,Profession.FAIRGROUND_WORKER,1100);
        check(Subprofession.KINGDOM_MESSENGER,Profession.TEACHER,1150);
        check(Subprofession.SURGEON,Profession.TEACHER,1600);
        check(Subprofession.VETERINARIAN,Profession.TEACHER,1250);
        check(Subprofession.PUBLIC_SCRIBE,Profession.JURIST,1250);
        check(Subprofession.MAGISTRATE,Profession.JURIST,1800);
        check(Subprofession.SHOPKEEPER,Profession.MERCHANT,1100);
        check(Subprofession.RAILWAY_GUARD,Profession.SOLDIER,1150);
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FAIRGROUND_ENTREPRENEUR.profile().incomeKind()==ProfessionIncomeKind.VARIABLE_INCOME,
                "La paga del empresario de feria debe ser explícitamente variable.");
    }

    private static void analogueCivilSociety(){
        String messenger=Subprofession.KINGDOM_MESSENGER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(messenger.contains("red digital universal") && messenger.contains("papel"),
                "Mensajero debe explicar custodia documental en una sociedad analógica.");
        String barber=Subprofession.BARBER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(barber.contains("sociedad casi enteramente analógica") && barber.contains("conversación"),
                "Barbero debe representar circulación informal de información.");
        String storyteller=Subprofession.ITINERANT_PUPPETEER_STORYTELLER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(storyteller.contains("transporta imaginario") && storyteller.contains("red digital universal"),
                "Narrador debe transportar imaginario, no documentación oficial.");
        String scribe=Subprofession.PUBLIC_SCRIBE.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(scribe.contains("sociedad analógica") && scribe.contains("documentos"),
                "Escribano debe representar sofisticación documental analógica.");
    }

    private static void medicineAndAnimalCare(){
        String surgeon=Subprofession.SURGEON.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(surgeon.contains("frecuencia") && surgeon.contains("anatomía"),
                "Cirujano debe impedir que la medicina frecuencial sustituya la intervención material.");
        String vet=Subprofession.VETERINARIAN.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(vet.contains("ganado") && vet.contains("monturas") && vet.contains("ciencia frecuencial"),
                "Veterinario debe unir economía animal y paradigma frecuencial.");
    }

    private static void reconstructionAndConsumption(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.STONEWORK_MASTER.narrativeDescription().contains("Primera Marcha Exaltada"),
                "Maestro pétreo debe conectar reconstrucción y permanencia.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PRECISION_STONECUTTER.narrativeDescription().contains("V881"),
                "Tallista debe mostrar convivencia entre oficio tradicional y tolerancia V881.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.STRUCTURAL_CARPENTER.narrativeDescription().contains("mundo enteramente metálico"),
                "Carpintero debe justificar continuidad material de la madera.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.CABINETMAKER.narrativeDescription().contains("indicador económico"),
                "Ebanista debe revelar consumo discrecional.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.SALON_DRESSMAKER.narrativeDescription().contains("dinero nuevo"),
                "Modista debe mostrar movilidad y nuevas fortunas.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.SALON_HAIRDRESSER.narrativeDescription().contains("recuperación urbana"),
                "Peluquero de salón debe mostrar normalización urbana.");
    }

    private static void institutionalRecovery(){
        String magistrate=Subprofession.MAGISTRATE.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(magistrate.contains("violencia había sustituido al procedimiento")
                        && magistrate.contains("Valerian todavía está averiguando"),
                "Magistrado debe mostrar recuperación institucional todavía incompleta.");
        String guard=Subprofession.RAILWAY_GUARD.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(guard.contains("locomotoras electromagnéticas") && guard.contains("sabotaje"),
                "Guardia ferroviario debe unir infraestructura y seguridad.");
        String shop=Subprofession.SHOPKEEPER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(shop.contains("jabón el martes") && shop.contains("Primera Marcha"),
                "Tendero debe mostrar normalidad cotidiana y cadenas de suministro.");
    }

    private static void previousContractsRemain(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EBONY_WARRIOR_V881.contemporaryHolder().orElseThrow().equals("Kenan"),
                " no puede alterar la unicidad de Kenan.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER.monthlyReferenceValeritas()==1800,
                " debe permanecer intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.SOLDIER.monthlyReferenceValeritas()==1100,
                " debe permanecer intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,
                "Noble no se toca.");
    }

    private static void check(Subprofession s,Profession p,int value){
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==p,s.label()+" parent incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==value,s.label()+" paga incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(!s.narrativeDescription().isBlank(),s.label()+" sin descripción.");
    }

    
}

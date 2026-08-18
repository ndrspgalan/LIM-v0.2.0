package qa.domain;

import domain.social.*;
import domain.inventory.logistics.PersonalTransportType;
import java.util.*;

public final class DomesticMobilityMarginalityAndLeisureVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        shape();
        beggarStates();
        courtesanDualism();
        personalTransport();
        domesticComfortAndTerritorialImbalance();
        analogueLeisure();
        nobleRemainsClosed();
        previousIterationsRemainStable();
    }

    private static void shape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=54,"El catálogo debe conservar las 54 subprofesiones acumuladas hasta .");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,
                "Toda subprofesión actualmente canónica necesita perfil.");
        for(Subprofession s:Subprofession.values()){
            org.junit.jupiter.api.Assertions.assertTrue(!s.narrativeDescription().isBlank(),"Sin narrativa: "+s.label());
        }
    }

    private static void beggarStates(){
        List<Subprofession> states=List.of(
                Subprofession.PRISONER,
                Subprofession.UNEMPLOYED,
                Subprofession.WORK_DISABLED,
                Subprofession.INDIGENT,
                Subprofession.DISPLACED_RESIDENT);
        for(Subprofession s:states){
            org.junit.jupiter.api.Assertions.assertTrue(s.profession()==Profession.BEGGAR,s.label()+" debe pertenecer a Mendigo.");
            org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==0,s.label()+" debe tener referencia profesional 0.");
            org.junit.jupiter.api.Assertions.assertTrue(s.profile().incomeKind()==ProfessionIncomeKind.NONE,s.label()+" no debe fingir salario.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.UNEMPLOYED.narrativeDescription().contains("luz doméstica")
                        && Subprofession.UNEMPLOYED.narrativeDescription().contains("alcantarillado"),
                "Desempleado debe mostrar bienestar material urbano.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.INDIGENT.narrativeDescription().contains("aguas fecales")
                        && Subprofession.INDIGENT.narrativeDescription().contains("locomotora silenciosa"),
                "Indigente debe expresar la paradoja de abundancia técnica y exclusión.");
        String displaced=Subprofession.DISPLACED_RESIDENT.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(displaced.contains("yacimiento se agota") && displaced.contains("estación nunca llega")
                        && displaced.contains("Caballos y bicicletas"),
                "Habitante desplazado debe representar desigualdad territorial no tecnológica.");
    }

    private static void courtesanDualism(){
        check(Subprofession.SEX_WORKER,Profession.COURTESAN,1050);
        check(Subprofession.SALON_COURTESAN,Profession.COURTESAN,1700);
        check(Subprofession.PROFESSIONAL_COMPANION,Profession.COURTESAN,1350);

        String worker=Subprofession.SEX_WORKER.narrativeDescription();
        for(String term:List.of("independiente","drogas","contrabando","explotación","microvigilancia")){
            org.junit.jupiter.api.Assertions.assertTrue(worker.contains(term),"Trabajadora sexual debe mostrar el dualismo: "+term);
        }

        String salon=Subprofession.SALON_COURTESAN.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(salon.contains("no cabe bien en la palabra prostitución")
                        && salon.contains("ausencia de fricción social"),
                "Cortesana de salón debe ser compañía, capital social y presencia.");
        org.junit.jupiter.api.Assertions.assertTrue(salon.contains("red produciría perfiles"),
                "Debe reflejar intermediación humana en una sociedad presencial.");

        String companion=Subprofession.PROFESSIONAL_COMPANION.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(companion.contains("no esperan que el encuentro termine en una cama")
                        && companion.contains("esperanza de vida"),
                "Acompañante debe separar compañía profesional y sexo y relacionarla con longevidad.");
    }

    private static void personalTransport(){
        check(Subprofession.STABLE_HAND,Profession.DAY_LABORER,700);
        check(Subprofession.CYCLIST_MESSENGER,Profession.TEACHER,1000);
        check(Subprofession.MOTORCYCLE_COURIER,Profession.MERCENARY,1350);
        check(Subprofession.ROAD_GUIDE,Profession.HUNTER,1100);

        String stable=Subprofession.STABLE_HAND.narrativeDescription();
        for(PersonalTransportType t:List.of(PersonalTransportType.HORSE_LEISURE,
                PersonalTransportType.HORSE_RACING,PersonalTransportType.HORSE_DRAFT)){
            org.junit.jupiter.api.Assertions.assertTrue(stable.contains(t.label()),"Mozo debe mencionar "+t.label());
        }

        String cyclist=Subprofession.CYCLIST_MESSENGER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(cyclist.contains(PersonalTransportType.BICYCLE_FOLDING_V881.label())
                        && cyclist.contains(PersonalTransportType.BICYCLE_MILITARY_V881.label()),
                "Mensajero ciclista debe explicar ambas bicicletas.");

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.MOTORCYCLE_COURIER.narrativeDescription()
                        .contains(PersonalTransportType.MOTORCYCLE_CARDAN_V881.label()),
                "Correo motociclista debe explicar la motocicleta Cardán.");

        String guide=Subprofession.ROAD_GUIDE.narrativeDescription();
        for(PersonalTransportType t:PersonalTransportType.values())
            org.junit.jupiter.api.Assertions.assertTrue(guide.contains(t.label()),"Guía debe conocer los seis transportes: "+t.label());
    }

    private static void domesticComfortAndTerritorialImbalance(){
        check(Subprofession.DOMESTIC_V881_INSTALLER,Profession.BLACKSMITH,1250);
        check(Subprofession.SANITATION_OPERATOR,Profession.DAY_LABORER,750);
        check(Subprofession.SANITARY_MASTER,Profession.TEACHER,1450);

        String home=Subprofession.DOMESTIC_V881_INSTALLER.narrativeDescription();
        for(String term:List.of("iluminarse","agua caliente","factura energética","Descargas estáticas",
                "incendios localizados","tormentas eléctricas")){
            org.junit.jupiter.api.Assertions.assertTrue(home.contains(term),"Instalador doméstico debe contener: "+term);
        }

        String sanitation=Subprofession.SANITATION_OPERATOR.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(sanitation.contains("alcantarillado sano") && sanitation.contains("tratamientos vibracionales"),
                "Saneamiento debe mostrar alcantarillado y tratamiento vibracional.");

        String master=Subprofession.SANITARY_MASTER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(master.contains("agua limpia") && master.contains("vibraciones controladas"),
                "Maestro sanitario debe presentar la ciencia civil.");
        org.junit.jupiter.api.Assertions.assertTrue(master.contains("no para convertirla en una receta"),
                "La narrativa debe mantener la dualidad sin procedimiento operativo.");
    }

    private static void analogueLeisure(){
        check(Subprofession.TAVERN_KEEPER,Profession.MERCHANT,1150);
        check(Subprofession.BOOKSELLER,Profession.MERCHANT,1200);
        check(Subprofession.TAVERN_MUSICIAN,Profession.FAIRGROUND_WORKER,750);
        check(Subprofession.GAME_MASTER,Profession.FAIRGROUND_WORKER,900);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.TAVERN_KEEPER.narrativeDescription().contains("infraestructura social"),
                "Taberna = infraestructura social presencial.");
        String books=Subprofession.BOOKSELLER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(books.contains("red digital universal") && books.contains("difícil de borrar a distancia"),
                "Librero debe revelar la persistencia analógica.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.TAVERN_MUSICIAN.narrativeDescription().contains("plataforma invisible")
                        && Subprofession.TAVERN_MUSICIAN.narrativeDescription().contains("anonimato compartido"),
                "Músico debe representar ocio presencial sin perfilado digital.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.GAME_MASTER.narrativeDescription().contains("historial universal")
                        && Subprofession.GAME_MASTER.narrativeDescription().contains("mercados clandestinos"),
                "Juegos deben expresar privacidad y dualismo clandestino.");
    }

    private static void nobleRemainsClosed(){

        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,
                "La renta noble de  no cambia.");
        for(Subprofession s:Subprofession.values()){
            org.junit.jupiter.api.Assertions.assertTrue(!s.narrativeDescription().contains("biomáquina basada en el carbono 12"),
                    " no debe revelar todavía la causalidad corporal de Noble.");
            org.junit.jupiter.api.Assertions.assertTrue(!s.narrativeDescription().contains("conmoción del Intersticio"),
                    " no debe revelar todavía la conmoción del Intersticio.");
        }
    }

    private static void previousIterationsRemainStable(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EBONY_WARRIOR_V881.contemporaryHolder().orElseThrow().equals("Kenan"),
                "Kenan continúa único.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER.monthlyReferenceValeritas()==1800,
                " intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.MAGISTRATE.monthlyReferenceValeritas()==1800,
                " intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.SOLDIER.monthlyReferenceValeritas()==1100,
                " intacta.");
    }

    private static void check(Subprofession s,Profession p,int value){
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==p,s.label()+" parent incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==value,s.label()+" paga incorrecta.");
    }

    
}

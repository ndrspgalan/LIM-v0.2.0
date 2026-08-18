package qa.domain;

import domain.social.*;
import java.util.*;

public final class PrimaryProductionPeripheralEconomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        shape();
        canonicalParents();
        ruralProduction();
        extractionAndTerritorialImbalance();
        maritimeAndPortEconomy();
        concentrationAndConvoySecurity();
        noDuplicateNavalRoles();
        previousContractsRemain();
    }

    private static void shape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=71,"El catálogo debe conservar las 71 subprofesiones acumuladas hasta .");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,
                "Toda subprofesión actualmente canónica debe tener perfil.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.values().length==19," no puede inventar nuevas profesiones madre.");
        
    }

    private static void canonicalParents(){
        for(Subprofession s:List.of(Subprofession.FARMER,Subprofession.LIVESTOCK_KEEPER,Subprofession.FOREST_LUMBERJACK,Subprofession.EXTRACTION_MINER,Subprofession.STEVEDORE,Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,Subprofession.HAULAGE_LABORER))
            org.junit.jupiter.api.Assertions.assertTrue(s.profession()==Profession.DAY_LABORER,s.label()+" debe integrarse en Jornalero.");

        for(Subprofession s:List.of(Subprofession.COASTAL_FISHER,Subprofession.OFFSHORE_FISHER,
                Subprofession.MERCHANT_SAILOR))
            org.junit.jupiter.api.Assertions.assertTrue(s.profession()==Profession.SAILOR,s.label()+" debe integrarse en Marinero.");

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PROFESSIONAL_HUNTER.profession()==Profession.HUNTER,"Cazador profesional parent.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.TRAPPER.profession()==Profession.HUNTER,"Trampero parent.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FORESTRY_MANAGER.profession()==Profession.TEACHER,"Gestor forestal parent.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PROSPECTOR.profession()==Profession.TEACHER,"Prospector parent.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.RURAL_AGGREGATOR.profession()==Profession.MERCHANT,"Acopiador parent.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.CONVOY_ESCORT.profession()==Profession.MERCENARY,"Escolta parent.");
    }

    private static void ruralProduction(){
        check(Subprofession.FARMER,800);
        check(Subprofession.LIVESTOCK_KEEPER,850);
        check(Subprofession.HORTICULTURIST,900);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FARMER.narrativeDescription().contains("mapa de intereses"),
                "Agricultor debe explicar distribución tecnológica no uniforme.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.LIVESTOCK_KEEPER.narrativeDescription().contains("medicina frecuencial"),
                "Ganadero debe conectarse con veterinaria V881.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.HORTICULTURIST.narrativeDescription().contains("estar conectado"),
                "Horticultor debe mostrar dependencia logística.");
    }

    private static void extractionAndTerritorialImbalance(){
        check(Subprofession.FOREST_LUMBERJACK,900);
        check(Subprofession.FORESTRY_MANAGER,1100);
        check(Subprofession.EXTRACTION_MINER,1100);
        check(Subprofession.PROSPECTOR,1250);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FOREST_LUMBERJACK.narrativeDescription().contains("asentamiento"),
                "Leñador debe mostrar dependencia territorial.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FORESTRY_MANAGER.narrativeDescription().contains("administración del tiempo"),
                "Gestor forestal debe aportar planificación.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EXTRACTION_MINER.narrativeDescription().contains("montaña"),
                "Minero debe conservar materialidad del riesgo.");
        String prospector=Subprofession.PROSPECTOR.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(prospector.contains("ferrocarril") && prospector.contains("yacimiento agotado"),
                "Prospector debe explicar auge y abandono territorial.");
    }

    private static void maritimeAndPortEconomy(){
        check(Subprofession.COASTAL_FISHER,850);
        check(Subprofession.OFFSHORE_FISHER,1150);
        check(Subprofession.MERCHANT_SAILOR,1100);
        check(Subprofession.STEVEDORE,850);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.COASTAL_FISHER.narrativeDescription().contains("captación del viento")
                        && Subprofession.COASTAL_FISHER.narrativeDescription().contains("superficies solares"),
                "Pesca costera debe pertenecer a la misma rama energética naval.");
        String merchant=Subprofession.MERCHANT_SAILOR.narrativeDescription();
        for(String term:List.of("energía del viento","velas solares","energía atmosférica"))
            org.junit.jupiter.api.Assertions.assertTrue(merchant.contains(term),"Marinero mercante debe contener "+term);
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.STEVEDORE.narrativeDescription().contains("contrabando"),
                "Puerto debe mostrar dualidad logística/clandestina.");
    }

    private static void concentrationAndConvoySecurity(){
        check(Subprofession.RURAL_AGGREGATOR,1100);
        check(Subprofession.CONVOY_ESCORT,1250);

        String aggregator=Subprofession.RURAL_AGGREGATOR.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(aggregator.contains("cien sacos") && aggregator.contains("poder económico"),
                "Acopiador debe introducir concentración sin abrir aún la burguesía completa.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.RURAL_AGGREGATOR.profile().incomeKind()==ProfessionIncomeKind.VARIABLE_INCOME,
                "Acopiador = ingreso variable.");

        String convoy=Subprofession.CONVOY_ESCORT.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(convoy.contains("No sustituye al Soldado") && convoy.contains("autoridad existe sobre el mapa"),
                "Escolta debe ocupar el hueco entre orden público y protección contractual.");
    }

    private static void noDuplicateNavalRoles(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.forProfession(Profession.SAILOR).contains(Subprofession.NAVAL_RAILGUN_GUNNER),
                "Debe conservarse Artillero de riel naval .");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.forProfession(Profession.SAILOR).contains(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER),
                "Debe conservarse Maquinista electroatmosférico naval .");
        long rail=Subprofession.forProfession(Profession.SAILOR).stream()
                .filter(s->s.label().equals("Artillero de riel naval")).count();
        long engine=Subprofession.forProfession(Profession.SAILOR).stream()
                .filter(s->s.label().equals("Maquinista electroatmosférico naval")).count();
        org.junit.jupiter.api.Assertions.assertTrue(rail==1 && engine==1," no debe duplicar roles navales ya canónicos.");
    }

    private static void previousContractsRemain(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EBONY_WARRIOR_V881.contemporaryHolder().orElseThrow().equals("Kenan"),"Kenan intacto.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.MAGISTRATE.monthlyReferenceValeritas()==1800," intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.INDIGENT.monthlyReferenceValeritas()==0," intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,"Noble intacto.");
    }

    private static void check(Subprofession s,int value){
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==value,s.label()+" paga incorrecta.");
    }

    
}

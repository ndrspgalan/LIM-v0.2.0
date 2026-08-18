package qa.domain;

import domain.character.CharacterClass;
import domain.knowledge.*;
import domain.social.*;

import java.util.*;

public final class PowerSphereNobilityAndIntersticeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        shape();
        bourgeoisie();
        nobleLayer();
        internalCanon();
        intersticeAndVeilAreNotConfused();
        protagonistClasses();
        previousContractsRemain();
    }

    private static void shape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=81,"El catálogo debe conservar las 81 subprofesiones acumuladas hasta .");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,
                "Toda subprofesión actualmente canónica necesita perfil.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.values().length==19,"Las profesiones madre siguen siendo 19.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.authoredSubprofessions().size()>=4,
                "El catálogo debe conservar al menos las cuatro formas de poder noble.");
    }

    private static void bourgeoisie(){
        check(Subprofession.V881_INDUSTRIALIST,Profession.MERCHANT,2500);
        check(Subprofession.SHIPOWNER,Profession.MERCHANT,3000);
        check(Subprofession.FINANCIER,Profession.MERCHANT,2750);
        check(Subprofession.INFRASTRUCTURE_CONCESSIONAIRE,Profession.MERCHANT,3500);
        check(Subprofession.GRAND_MERCHANT,Profession.MERCHANT,2250);
        check(Subprofession.MERCENARY_COMPANY_DIRECTOR,Profession.MERCENARY,3000);

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.V881_INDUSTRIALIST.narrativeDescription().contains("capacidad de producirla"),
                "Industrial debe representar control de producción.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.INFRASTRUCTURE_CONCESSIONAIRE.narrativeDescription().contains("mapa de concesiones"),
                "Concesionario debe explicar desigualdad territorial.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.FINANCIER.narrativeDescription().contains("futuros considera financiables"),
                "Financista debe representar selección de futuro económico.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.MERCENARY_COMPANY_DIRECTOR.narrativeDescription().contains("señores de la guerra"),
                "Director mercenario debe cerrar el ascenso de las compañías.");
    }

    private static void nobleLayer(){
        check(Subprofession.DYNASTIC_NOBLE,Profession.NOBLE,210000);
        check(Subprofession.CONCESSIONARY_NOBLE,Profession.NOBLE,315000);
        check(Subprofession.ENLIGHTENED_PATRON,Profession.NOBLE,262500);
        check(Subprofession.PATRIMONIAL_WARLORD,Profession.NOBLE,367500);

        for(Subprofession s:Profession.NOBLE.authoredSubprofessions())
            org.junit.jupiter.api.Assertions.assertTrue(s.profile().incomeKind()==ProfessionIncomeKind.PATRIMONIAL_RENT,
                    s.label()+" debe usar renta patrimonial.");

        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.DYNASTIC_NOBLE.monthlyReferenceLabel().equals("1 Berylare/mes"),
                "Noble de sangre debe conservar la referencia de un Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.CONCESSIONARY_NOBLE.monthlyReferenceLabel().equals("1,50 Berylares/mes"),
                "Noble concesionario 1,50 Berylares.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.ENLIGHTENED_PATRON.narrativeDescription().contains("Institución de la Esfera del Progreso"),
                "Mecenas debe conectar patronazgo e institución.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PATRIMONIAL_WARLORD.narrativeDescription().contains("probar diseños"),
                "Señor patrimonial debe mostrar utilidad política de la guerra.");
    }

    private static void internalCanon(){
        org.junit.jupiter.api.Assertions.assertTrue(WorldOrderCanon.visibility()==CanonVisibility.INTERNAL_CANON,
                "La verdad total no debe fingirse automáticamente como texto público.");
        org.junit.jupiter.api.Assertions.assertTrue(WorldOrderCanon.all().size()>=8,"El canon interno debe conservar los ocho núcleos .");
        String sphere=SphereOfProgressDoctrine.truth();
        org.junit.jupiter.api.Assertions.assertTrue(sphere.contains("Humanismo Secular")
                        && sphere.contains("Santo")
                        && sphere.contains("de Todos")
                        && sphere.contains("progreso circular"),
                "Esfera del Progreso incompleta.");

        String chaos=ControlledSocialChaosDoctrine.truth();
        for(String term:List.of("control macrocausal","agua","alimentos","vegetación",
                "incendios","tormentas estáticas","contrabando","drogas","prostitución coercitiva")){
            org.junit.jupiter.api.Assertions.assertTrue(chaos.contains(term),"Caos Social Controlado debe conocer: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(chaos.contains("no especifica parámetros"),
                "El dominio conoce la práctica sin convertirla en receta operacional.");

        String war=V881WarPoliticalEconomyDoctrine.truth();
        for(String term:List.of("encarecer o abaratar recursos","migraciones","probar lealtades",
                "concesiones","diseños","V881","Segunda Marcha Exaltada")){
            org.junit.jupiter.api.Assertions.assertTrue(war.contains(term),"Economía política de guerra incompleta: "+term);
        }
    }

    private static void intersticeAndVeilAreNotConfused(){
        String noble=NobleLongevityDoctrine.truth();
        for(String term:List.of("carbono-12","altamente mutable","formas corporales grotescas",
                "configuraciones mentales","conmociona el Intersticio")){
            org.junit.jupiter.api.Assertions.assertTrue(noble.contains(term),"Longevidad noble debe contener: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(!noble.contains("conmociona el Velo"),"La conmoción afecta al Intersticio, no al Velo.");

        String veil=IntersticeTopologyDoctrine.veil();
        org.junit.jupiter.api.Assertions.assertTrue(veil.contains("hendiduras des-veladas del Intersticio"),"Definición de Velo incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(veil.contains("líneas telúricas") && veil.contains("teletransporte"),
                "Velo debe admitir tránsito telúrico.");
        org.junit.jupiter.api.Assertions.assertTrue(veil.contains("filtro") && veil.contains("realidad"),
                "Velo debe admitir reproyección/percepción alternativa.");
        org.junit.jupiter.api.Assertions.assertTrue(veil.contains("no implica") && veil.contains("mundo espiritual"),
                "Velo no puede reducirse a mundo de espíritus.");

        String interstice=IntersticeTopologyDoctrine.interstice();
        org.junit.jupiter.api.Assertions.assertTrue(interstice.contains("No debe") && interstice.contains("mundo de espíritus"),
                "Intersticio tampoco debe definirse como plano espiritual.");
    }

    private static void protagonistClasses(){
        org.junit.jupiter.api.Assertions.assertTrue(ProtagonistSpiritDoctrine.kenan()==CharacterClass.INDOMITO,"Kenan debe ser Indómito.");
        org.junit.jupiter.api.Assertions.assertTrue(ProtagonistSpiritDoctrine.kiara()==CharacterClass.HERALDO,"Kiara debe ser Heraldo.");
        String truth=ProtagonistSpiritDoctrine.truth();
        org.junit.jupiter.api.Assertions.assertTrue(truth.contains("Portador de Sueños") && truth.contains("no porque sea un salvador"),
                "Kenan debe encarnar esperanza, no mesianismo.");
        org.junit.jupiter.api.Assertions.assertTrue(truth.contains("Espíritu Heráldico") && truth.contains("dar continuidad"),
                "Kiara debe expresar compatibilidad heráldica.");
    }

    private static void previousContractsRemain(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.EBONY_WARRIOR_V881.contemporaryHolder().orElseThrow().equals("Kenan"),"Kenan único intacto.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.INDIGENT.monthlyReferenceValeritas()==0," intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.PROSPECTOR.monthlyReferenceValeritas()==1250," intacta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000," referencia Noble intacta.");
    }

    private static void check(Subprofession s,Profession p,int value){
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==p,s.label()+" parent incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==value,s.label()+" renta/paga incorrecta.");
    }

    
}

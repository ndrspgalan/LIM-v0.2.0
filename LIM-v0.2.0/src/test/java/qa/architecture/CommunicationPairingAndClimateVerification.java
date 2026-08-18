package qa.architecture;

import domain.communication.*;
import domain.environment.time.Weather;
import domain.inventory.item.armor.ArmorCatalog;
import domain.social.RelationshipType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CommunicationPairingAndClimateVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var pano=ArmorCatalog.enlightenedPanopticonCanonical();
        org.junit.jupiter.api.Assertions.assertTrue(close(pano.protection().piercing(),40),"Panóptico debe usar perforante estándar de vidrio laminado 40.");
        org.junit.jupiter.api.Assertions.assertTrue(close(pano.protection().slashing(),100)&&close(pano.protection().blunt(),100),
                "Panóptico conserva 100 cortante/contundente por arquitectura celular.");

        org.junit.jupiter.api.Assertions.assertTrue(close(CommunicationRangePolicy.rangeMeters(
                CommunicationDeviceType.PANOPTICON, Weather.SUMMER_CLEAR.profile()),213),
                "Panóptico alcanza 213 m con visibilidad 1.");
        org.junit.jupiter.api.Assertions.assertTrue(close(CommunicationRangePolicy.rangeMeters(
                CommunicationDeviceType.PANOPTICON, Weather.WINTER_SNOWSTORM.profile()),79),
                "Panóptico cae a 79 m en la visibilidad mínima canónica.");

        org.junit.jupiter.api.Assertions.assertTrue(close(CommunicationRangePolicy.rangeMeters(
                CommunicationDeviceType.AERONAUT_INTERCOM, Weather.SPRING_PERSISTENT_RAIN.profile()),0),
                "La lluvia veta la TOMA A TIERRA del intercom y reduce el alcance a 0 m.");
        org.junit.jupiter.api.Assertions.assertTrue(close(CommunicationRangePolicy.rangeMeters(
                CommunicationDeviceType.AERONAUT_INTERCOM, Weather.WINTER_SNOW.profile()),100),
                "Sin lluvia y sobre terreno no asfaltado, el intercom conserva 100 m.");

        PairingEligibilityPolicy eligibility=new PairingEligibilityPolicy();
        PairingCandidate friendWithoutPanopticon=new PairingCandidate(
                "kiara","Kiara",100,RelationshipType.FRIENDLY,true,false);
        org.junit.jupiter.api.Assertions.assertTrue(eligibility.eligible(CommunicationDeviceType.PANOPTICON,
                        Weather.SUMMER_CLEAR.profile(),friendWithoutPanopticon),
                "El Panóptico no puede exigir otro Panóptico ni intercom en el receptor.");
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.eligible(CommunicationDeviceType.AERONAUT_INTERCOM,
                        Weather.SPRING_PERSISTENT_RAIN.profile(),friendWithoutPanopticon),
                "El Aeronauta sí exige un receptor de intercom terrestre compatible.");

        PairingCandidate reliableAeronaut=new PairingCandidate(
                "kenan","Kenan",80,RelationshipType.RELIABLE,true,true);
        org.junit.jupiter.api.Assertions.assertTrue(eligibility.eligible(CommunicationDeviceType.AERONAUT_INTERCOM,
                        Weather.SUMMER_CLEAR.profile(),reliableAeronaut),
                "RELIABLE + receptor compatible + distancia válida debe enlazar.");
        PairingCandidate indifferent=new PairingCandidate(
                "x","X",5,RelationshipType.INDIFFERENT,true,true);
        org.junit.jupiter.api.Assertions.assertTrue(!eligibility.eligible(CommunicationDeviceType.AERONAUT_INTERCOM,
                        Weather.SPRING_PERSISTENT_RAIN.profile(),indifferent),
                "Pairing sólo acepta FIABLE/AMISTOSA/ROMÁNTICA.");

        CommunicationPairingState state=new CommunicationPairingState();
        CommunicationPairingService service=new CommunicationPairingService();
        service.pair(state,CommunicationDeviceType.AERONAUT_INTERCOM,
                Weather.SUMMER_CLEAR.profile(),reliableAeronaut);
        org.junit.jupiter.api.Assertions.assertTrue(state.memory(CommunicationDeviceType.AERONAUT_INTERCOM).linked(),"Debe enlazar.");
        PairingCandidate tooFar=new PairingCandidate(
                "kenan","Kenan",101,RelationshipType.RELIABLE,true,true);
        service.refresh(state,CommunicationDeviceType.AERONAUT_INTERCOM,
                Weather.SUMMER_CLEAR.profile(),List.of(tooFar),true);
        org.junit.jupiter.api.Assertions.assertTrue(!state.memory(CommunicationDeviceType.AERONAUT_INTERCOM).linked()
                && "kenan".equals(state.memory(CommunicationDeviceType.AERONAUT_INTERCOM).lastUserId()),
                "Fuera de alcance rompe enlace pero conserva memoria.");
        service.refresh(state,CommunicationDeviceType.AERONAUT_INTERCOM,
                Weather.SUMMER_CLEAR.profile(),List.of(reliableAeronaut),true);
        org.junit.jupiter.api.Assertions.assertTrue(state.memory(CommunicationDeviceType.AERONAUT_INTERCOM).linked(),
                "Al volver al alcance debe re-enlazar automáticamente con el último usuario.");
        service.refresh(state,CommunicationDeviceType.AERONAUT_INTERCOM,
                Weather.SUMMER_CLEAR.profile(),List.of(reliableAeronaut),false);
        org.junit.jupiter.api.Assertions.assertTrue(!state.memory(CommunicationDeviceType.AERONAUT_INTERCOM).linked(),
                "Desequipar el dispositivo suspende el enlace sin borrar memoria.");

        String inventory=Files.readString(Path.of("src/main/java/presentation/menu/InventoryScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(inventory.contains("3. Enlazar usuario"),"La inspección debe exponer Enlazar usuario.");
        org.junit.jupiter.api.Assertions.assertTrue(inventory.contains("ALCANCE ACTUAL SEGÚN CLIMA"),"La inspección debe mostrar alcance climático actual.");
    }

    private static boolean close(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}

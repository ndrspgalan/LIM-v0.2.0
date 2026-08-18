package qa.domain;

import domain.communication.CommunicationDeviceType;
import domain.communication.CommunicationRangePolicy;
import domain.environment.time.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.equipment.GroundingPolicy;
import domain.inventory.item.accessory.V881ArtifactUsePolicy;
import domain.worldmemory.spatial.TerrainSurface;

import java.time.Duration;
import java.util.Map;

/** condiciones ambientales canónicas compartidas por artefactos, grounding e intercom. */
public final class EnvironmentalArtifactAndTerrainVerification {
    private EnvironmentalArtifactAndTerrainVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        WeatherProfile dry = Weather.SPRING_CLEAR.profile();
        WeatherProfile rain = Weather.SPRING_PERSISTENT_RAIN.profile();

        org.junit.jupiter.api.Assertions.assertTrue(TerrainWeatherPolicy.resolve(TerrainSurface.EARTH, dry)==TerrainWeatherState.NATURAL_DRY,"Natural/seco.");
        org.junit.jupiter.api.Assertions.assertTrue(TerrainWeatherPolicy.resolve(TerrainSurface.ASPHALT, dry)==TerrainWeatherState.ASPHALT_DRY,"Asfalto/seco.");
        org.junit.jupiter.api.Assertions.assertTrue(TerrainWeatherPolicy.resolve(TerrainSurface.EARTH, rain)==TerrainWeatherState.NATURAL_RAIN,"Natural/lluvia.");
        org.junit.jupiter.api.Assertions.assertTrue(TerrainWeatherPolicy.resolve(TerrainSurface.ASPHALT, rain)==TerrainWeatherState.ASPHALT_RAIN,"Asfalto/lluvia.");

        EquipmentState barefoot = new EquipmentState(Map.of());
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.groundedByFeet(barefoot),"Descalzo conserva compatibilidad nominal de FEET.");
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.fullBodyGroundingPath(barefoot,TerrainSurface.EARTH,dry),"Natural seco permite TOMA A TIERRA.");
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.fullBodyGroundingPath(barefoot,TerrainSurface.ASPHALT,dry),"Asfalto veta TOMA A TIERRA.");
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.fullBodyGroundingPath(barefoot,TerrainSurface.EARTH,rain),"Lluvia veta TOMA A TIERRA.");
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.fullBodyGroundingPath(barefoot,TerrainSurface.ASPHALT,rain),"Asfalto+lluvia veta TOMA A TIERRA.");

        close(CommunicationRangePolicy.rangeMeters(CommunicationDeviceType.AERONAUT_INTERCOM,dry,TerrainSurface.EARTH),100,"Intercom natural seco");
        close(CommunicationRangePolicy.rangeMeters(CommunicationDeviceType.AERONAUT_INTERCOM,dry,TerrainSurface.ASPHALT),0,"Intercom asfalto seco");
        close(CommunicationRangePolicy.rangeMeters(CommunicationDeviceType.AERONAUT_INTERCOM,rain,TerrainSurface.EARTH),0,"Intercom natural lluvia");
        close(CommunicationRangePolicy.rangeMeters(CommunicationDeviceType.AERONAUT_INTERCOM,rain,TerrainSurface.ASPHALT),0,"Intercom asfalto lluvia");

        close(V881ArtifactUsePolicy.seismoscope(true,true,true,TerrainSurface.EARTH,dry).value(),20,"Sismoscopio natural seco");
        close(V881ArtifactUsePolicy.seismoscope(true,true,true,TerrainSurface.ASPHALT,dry).value(),16,"Sismoscopio asfalto seco");
        close(V881ArtifactUsePolicy.seismoscope(true,true,true,TerrainSurface.EARTH,rain).value(),14,"Sismoscopio natural lluvia");
        close(V881ArtifactUsePolicy.seismoscope(true,true,true,TerrainSurface.ASPHALT,rain).value(),10,"Sismoscopio asfalto lluvia");

        EnvironmentalCycle clearDay = new EnvironmentalCycle(DayPhase.DAY,Duration.ZERO,Weather.SPRING_CLEAR);
        EnvironmentalCycle rainyDay = new EnvironmentalCycle(DayPhase.DAY,Duration.ZERO,Weather.SPRING_PERSISTENT_RAIN);
        EnvironmentalCycle clearNight = new EnvironmentalCycle(DayPhase.NIGHT,Duration.ZERO,Weather.SPRING_CLEAR);

        var tok = V881ArtifactUsePolicy.tokkosho(true,true,clearDay,false);
        org.junit.jupiter.api.Assertions.assertTrue(tok.activated() && tok.dischargeEmitted() && tok.chargeConsumed(),"Tokkosho válido descarga y consume.");
        var immune = V881ArtifactUsePolicy.tokkosho(true,true,clearDay,true);
        org.junit.jupiter.api.Assertions.assertTrue(!immune.activated() && !immune.dischargeEmitted() && !immune.chargeConsumed(),"Inmunidad eléctrica conserva la carga.");
        org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.tokkosho(true,true,rainyDay,false).activated(),"Lluvia invalida condición electroatmosférica.");

        org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.heliograph(true,true,false,clearDay).activated(),"Heliógrafo usa ciclo/clima real.");
        org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.heliograph(true,true,false,rainyDay).activated(),"Heliógrafo falla con cielo lluvioso.");
        org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.nocturlabe(true,true,clearNight).activated(),"Nocturlabio deriva NIGHT del ciclo.");
        org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.nocturlabe(true,true,clearDay).activated(),"Nocturlabio no funciona de día.");
    }

    private static void close(double actual,double expected,String message){
        if(Math.abs(actual-expected)>1e-9) throw new AssertionError(message+": "+actual+" != "+expected);
    }
    
}

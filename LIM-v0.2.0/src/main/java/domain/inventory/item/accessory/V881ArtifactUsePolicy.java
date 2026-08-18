package domain.inventory.item.accessory;

import domain.environment.time.DayPhase;
import domain.environment.time.EnvironmentalCycle;
import domain.environment.time.PrecipitationKind;
import domain.environment.time.TerrainWeatherState;
import domain.environment.time.TerrainWeatherPolicy;
import domain.environment.time.WeatherProfile;
import domain.worldmemory.spatial.TerrainSurface;

import java.util.Objects;

/** / — contratos de gameplay de los artefactos especializados. */
public final class V881ArtifactUsePolicy {
    private V881ArtifactUsePolicy(){}
    public static final double TOKKOSHO_ELECTRIC_DAMAGE=100.0;
    public static final double TOKKOSHO_LATENCY_SECONDS=0.35;
    /** Recuperación térmica/carga de campo propia; no hereda el cooldown de ninguna arma. */
    public static final double TOKKOSHO_COOLDOWN_SECONDS=14.0;
    public static final double NOCTURLABE_REPLAY_SECONDS=3.0;
    public static final double SEISMOSCOPE_NOMINAL_RADIUS_METERS=20.0;
    public static final double SEISMOSCOPE_ASPHALT_DRY_RADIUS_METERS=16.0;
    public static final double SEISMOSCOPE_NATURAL_RAIN_RADIUS_METERS=14.0;
    public static final double SEISMOSCOPE_ASPHALT_RAIN_RADIUS_METERS=10.0;
    private static final double HELIOGRAPH_MIN_VISIBILITY=0.70;

    /** Contrato  conservado sólo para compatibilidad de verificaciones/snapshots antiguos. */
    public record Context(boolean clarity22,boolean targetLocked,boolean electroAtmosphereCompatible,
                          boolean clearSky,boolean sunOrMoonAvailable,boolean night,boolean faceShielded,
                          boolean temporalEvidencePresent){}
    public record Result(boolean activated,String effect,double value){}

    /** distingue intento, emisión y consumo de carga del Tokkosho. */
    public record TokkoshoResult(boolean activated, boolean dischargeEmitted, boolean chargeConsumed,
                                 String effect, double value){}

    public static Result tokkosho(Context c){
        if(!c.clarity22()||!c.targetLocked()||!c.electroAtmosphereCompatible()) return new Result(false,"",0);
        return new Result(true,"ELECTRICITY",TOKKOSHO_ELECTRIC_DAMAGE);
    }

    /**
     * : usa el clima efectivo real. Un objetivo inmune no acepta la descarga y por tanto
     * no consume la carga almacenada del Tokkosho.
     */
    public static TokkoshoResult tokkosho(boolean clarity22, boolean targetLocked,
                                          EnvironmentalCycle cycle, boolean targetImmuneToElectricity){
        Objects.requireNonNull(cycle);
        if(!clarity22 || !targetLocked || !electroAtmosphereCompatible(cycle.weatherProfile()))
            return new TokkoshoResult(false,false,false,"",0);
        if(targetImmuneToElectricity)
            return new TokkoshoResult(false,false,false,"TARGET_IMMUNE_TO_ELECTRICITY",0);
        return new TokkoshoResult(true,true,true,"ELECTRICITY",TOKKOSHO_ELECTRIC_DAMAGE);
    }

    public static Result heliograph(Context c){
        if(!c.clarity22()||!c.clearSky()||!c.sunOrMoonAvailable()) return new Result(false,"",0);
        if(!c.targetLocked()) return new Result(true,"SIGNAL",0);
        if(c.faceShielded()) return new Result(false,"",0);
        return new Result(true,"INTERRUPT_CURRENT_ACTION",0);
    }

    /** cielo/luz proceden del EnvironmentalCycle, no de booleanos suministrados por el caller. */
    public static Result heliograph(boolean clarity22, boolean targetLocked, boolean faceShielded,
                                    EnvironmentalCycle cycle){
        Objects.requireNonNull(cycle);
        if(!clarity22 || !heliographSkyCompatible(cycle.weatherProfile()) || !sunOrMoonAvailable(cycle))
            return new Result(false,"",0);
        if(!targetLocked) return new Result(true,"SIGNAL",0);
        if(faceShielded) return new Result(false,"",0);
        return new Result(true,"INTERRUPT_CURRENT_ACTION",0);
    }

    public static Result nocturlabe(Context c){
        if(!c.clarity22()||!c.night()||!c.temporalEvidencePresent()) return new Result(false,"",0);
        return new Result(true,"REPLAY_LOCAL_TEMPORAL_EVIDENCE",NOCTURLABE_REPLAY_SECONDS);
    }

    /** la noche se deriva de DayPhase.NIGHT real. */
    public static Result nocturlabe(boolean clarity22, boolean temporalEvidencePresent, EnvironmentalCycle cycle){
        Objects.requireNonNull(cycle);
        if(!clarity22 || cycle.phase()!= DayPhase.NIGHT || !temporalEvidencePresent)
            return new Result(false,"",0);
        return new Result(true,"REPLAY_LOCAL_TEMPORAL_EVIDENCE",NOCTURLABE_REPLAY_SECONDS);
    }

    public static Result tuningForkInvisible(boolean clarity22,boolean invisible){return clarity22&&invisible?new Result(true,"ALLOW_TARGET_LOCK_FOR_CURRENT_INVISIBILITY_INSTANCE",0):new Result(false,"",0);}
    public static Result tuningForkShapeshifter(boolean clarity22,boolean shapeshifter){return clarity22&&shapeshifter?new Result(true,"PRIVATE_SHAPESHIFTER_IDENTIFICATION_AND_TARGET_LOCK",0):new Result(false,"",0);}

    /** Contrato  conservado para compatibilidad;  debe usar la sobrecarga ambiental. */
    public static Result seismoscope(boolean clarity22,boolean mechanicallyCoupled,boolean moving,double mediumFactor){
        if(!clarity22||!mechanicallyCoupled||!moving||mediumFactor<=0) return new Result(false,"",0);
        return new Result(true,"REVEAL_VIBRATION_SOURCE",SEISMOSCOPE_NOMINAL_RADIUS_METERS*Math.min(1.0,mediumFactor));
    }

    /**
     * : cuatro estados deliberadamente simples. La lluvia reduce SNR sísmico; el asfalto
     * amortigua/acopla de forma menos favorable que el terreno natural para este sensor portátil.
     */
    public static Result seismoscope(boolean clarity22, boolean mechanicallyCoupled, boolean moving,
                                     TerrainSurface surface, WeatherProfile weather){
        Objects.requireNonNull(surface); Objects.requireNonNull(weather);
        if(!clarity22 || !mechanicallyCoupled || !moving) return new Result(false,"",0);
        double radius = switch(TerrainWeatherPolicy.resolve(surface, weather)){
            case NATURAL_DRY -> SEISMOSCOPE_NOMINAL_RADIUS_METERS;
            case ASPHALT_DRY -> SEISMOSCOPE_ASPHALT_DRY_RADIUS_METERS;
            case NATURAL_RAIN -> SEISMOSCOPE_NATURAL_RAIN_RADIUS_METERS;
            case ASPHALT_RAIN -> SEISMOSCOPE_ASPHALT_RAIN_RADIUS_METERS;
        };
        return new Result(true,"REVEAL_VIBRATION_SOURCE",radius);
    }

    public static boolean electroAtmosphereCompatible(WeatherProfile profile){
        Objects.requireNonNull(profile);
        // Precipitación intensa/lluvia hace la descarga de precisión atmosféricamente inestable.
        return !TerrainWeatherPolicy.isRainy(profile);
    }

    public static boolean heliographSkyCompatible(WeatherProfile profile){
        Objects.requireNonNull(profile);
        return profile.precipitation()== PrecipitationKind.NONE
                && profile.visibilityFactor()>=HELIOGRAPH_MIN_VISIBILITY;
    }

    public static boolean sunOrMoonAvailable(EnvironmentalCycle cycle){
        Objects.requireNonNull(cycle);
        // LIM siempre dispone del astro correspondiente; la atmósfera decide si es utilizable.
        return cycle.phase()==DayPhase.DAY || cycle.phase()==DayPhase.AFTERNOON || cycle.phase()==DayPhase.NIGHT;
    }
}

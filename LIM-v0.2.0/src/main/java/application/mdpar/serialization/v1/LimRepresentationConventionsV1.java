package application.mdpar.serialization.v1;

import application.mdpar.representation.v1.LimCombatRepresentationV1;
import java.util.Map;

/** Convenciones físicas propias de las representaciones LIM; no forman parte del protocolo cognitivo MDPAR. */
public final class LimRepresentationConventionsV1 {
    private LimRepresentationConventionsV1() {}
    public static final String COMBAT_REPRESENTATION = LimCombatRepresentationV1.VERSION;
    public static final String DISTANCE = "meter";
    public static final String MASS = "kilogram";
    public static final String TIME = "second_or_explicit_tick";
    public static final String ANGLE = "degree";
    public static final String PA = "lim_stamina_point";
    public static final String PV = "lim_vitality_point";
    public static final Map<String,String> CANONICAL_UNITS = Map.of(
            "distance", DISTANCE, "mass", MASS, "time", TIME, "angle", ANGLE, "pa", PA, "pv", PV);
}

package application.mdpar.boundary.v1;

import java.util.Map;
import java.util.Objects;

/**
 * Superficie OPERATIONAL publicada por MDPAR, preservando literalmente sus bloques
 * ACTION -> WHY -> HOW -> CONCLUSION. SUPPORT viaja sólo como superficie de auditoría.
 */
public record MdparOperationalPublicationV1(
        String ACTION,
        String WHY,
        String HOW,
        String CONCLUSION,
        JsonObjectPayloadV1 SUPPORT
) {
    public MdparOperationalPublicationV1 {
        ACTION = required(ACTION, "ACTION");
        WHY = required(WHY, "WHY");
        HOW = required(HOW, "HOW");
        CONCLUSION = required(CONCLUSION, "CONCLUSION");
        Objects.requireNonNull(SUPPORT, "SUPPORT");
    }

    public static MdparOperationalPublicationV1 withoutSupport(String action, String why, String how, String conclusion) {
        return new MdparOperationalPublicationV1(action, why, how, conclusion, new JsonObjectPayloadV1(Map.of()));
    }

    private static String required(String s,String field){Objects.requireNonNull(s,field);if(s.isBlank())throw new IllegalArgumentException(field+" obligatorio.");return s;}
}

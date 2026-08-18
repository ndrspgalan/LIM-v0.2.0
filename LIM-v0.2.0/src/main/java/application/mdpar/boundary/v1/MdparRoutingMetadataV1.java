package application.mdpar.boundary.v1;

import java.util.Map;
import java.util.Objects;

/**
 * Trazabilidad global producida por MDPAR.
 *
 * <p>La frontera fija únicamente un núcleo de observabilidad común. Los detalles
 * propios del mecanismo de routing quedan encapsulados en {@code details} para no
 * imponer a todas las representaciones conceptos de una modalidad concreta de entrada.</p>
 */
public record MdparRoutingMetadataV1(
        String outputMode,
        String routingStatus,
        String closure,
        long runCompletedMs,
        JsonObjectPayloadV1 details
) {
    public MdparRoutingMetadataV1 {
        outputMode = required(outputMode, "outputMode");
        routingStatus = required(routingStatus, "routingStatus");
        closure = required(closure, "closure");
        if (runCompletedMs < 0) throw new IllegalArgumentException("runCompletedMs");
        details = Objects.requireNonNullElseGet(details, () -> new JsonObjectPayloadV1(Map.of()));
    }

    private static String required(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) throw new IllegalArgumentException(field + " obligatorio.");
        return value;
    }
}

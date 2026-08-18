package application.mdpar.boundary.v1;

import java.util.Objects;

/** Respuesta global MDPAR -> LIM correlacionada exclusivamente mediante requestId. */
public record MdparResponseEnvelopeV1(
        String boundaryVersion,
        String requestId,
        MdparRoutingMetadataV1 routing,
        MdparOperationalPublicationV1 operational
) {
    public static final String VERSION = MdparRequestEnvelopeV1.VERSION;
    public MdparResponseEnvelopeV1 {
        if (!VERSION.equals(boundaryVersion)) throw new IllegalArgumentException("Versión boundary no soportada: " + boundaryVersion);
        Objects.requireNonNull(requestId, "requestId"); if (requestId.isBlank()) throw new IllegalArgumentException("requestId obligatorio.");
        Objects.requireNonNull(routing, "routing");
        Objects.requireNonNull(operational, "operational");
    }
}

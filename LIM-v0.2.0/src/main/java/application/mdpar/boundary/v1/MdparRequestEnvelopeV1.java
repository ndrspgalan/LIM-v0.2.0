package application.mdpar.boundary.v1;

import java.util.Objects;

/** Frontera global LIM -> MDPAR. El payload es representación, no interpretación SCC. */
public record MdparRequestEnvelopeV1(
        String boundaryVersion,
        String requestId,
        String producer,
        String representationVersion,
        long stateRevision,
        JsonObjectPayloadV1 payload
) {
    public static final String VERSION = "mdpar-boundary/v1";
    public static final String LIM_PRODUCER = "LIM";

    public MdparRequestEnvelopeV1 {
        if (!VERSION.equals(boundaryVersion)) throw new IllegalArgumentException("Versión boundary no soportada: " + boundaryVersion);
        requestId = required(requestId, "requestId");
        producer = required(producer, "producer");
        representationVersion = required(representationVersion, "representationVersion");
        if (stateRevision < 0) throw new IllegalArgumentException("stateRevision");
        Objects.requireNonNull(payload, "payload");
    }

    public static MdparRequestEnvelopeV1 lim(String requestId, String representationVersion, long stateRevision, JsonObjectPayloadV1 payload) {
        return new MdparRequestEnvelopeV1(VERSION, requestId, LIM_PRODUCER, representationVersion, stateRevision, payload);
    }

    private static String required(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) throw new IllegalArgumentException(field + " obligatorio.");
        return value;
    }
}

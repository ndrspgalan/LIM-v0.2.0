package application.mdpar.transport.v1;

import application.mdpar.boundary.v1.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/** Transporte determinista de QA que ejercita la frontera sin red ni runtime MDPAR real. */
public final class FixtureMdparTransportV1 implements MdparTransportV1 {
    private final Function<MdparRequestEnvelopeV1,MdparResponseEnvelopeV1> responder;

    public FixtureMdparTransportV1(Function<MdparRequestEnvelopeV1,MdparResponseEnvelopeV1> responder) {
        this.responder = Objects.requireNonNull(responder);
    }

    @Override public CompletionStage<MdparResponseEnvelopeV1> request(MdparRequestEnvelopeV1 request) {
        try {
            MdparResponseEnvelopeV1 response = Objects.requireNonNull(responder.apply(Objects.requireNonNull(request)));
            if (!request.requestId().equals(response.requestId()))
                return CompletableFuture.failedFuture(new IllegalStateException("Respuesta MDPAR no correlacionada: " + response.requestId()));
            return CompletableFuture.completedFuture(response);
        } catch (RuntimeException ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }
}

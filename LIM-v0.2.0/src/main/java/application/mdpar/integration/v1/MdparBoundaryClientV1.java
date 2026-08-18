package application.mdpar.integration.v1;

import application.mdpar.boundary.v1.*;
import application.mdpar.serialization.v1.MdparBoundaryJsonCodecV1;
import application.mdpar.transport.v1.MdparTransportV1;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Orquesta envío/recepción y tracking sin introducir ninguna decisión de gameplay.
 * El preflight canónico valida el wire antes de entregar la request al transporte.
 */
public final class MdparBoundaryClientV1 {
    private final MdparTransportV1 transport;
    private final MdparBoundaryJsonCodecV1 codec;
    private final MdparRequestTrackerV1 tracker;

    public MdparBoundaryClientV1(MdparTransportV1 transport, MdparBoundaryJsonCodecV1 codec, MdparRequestTrackerV1 tracker) {
        this.transport = Objects.requireNonNull(transport);
        this.codec = Objects.requireNonNull(codec);
        this.tracker = Objects.requireNonNull(tracker);
    }

    public CompletionStage<MdparResponseEnvelopeV1> request(MdparRequestEnvelopeV1 request) {
        return request(request, null);
    }

    public CompletionStage<MdparResponseEnvelopeV1> request(MdparRequestEnvelopeV1 request, Duration timeout) {
        Objects.requireNonNull(request);
        if (timeout != null && (timeout.isZero() || timeout.isNegative())) throw new IllegalArgumentException("timeout debe ser positivo.");

        tracker.created(request);
        codec.writeRequest(request); // preflight: serializable bajo mdpar-boundary/v1
        tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.CREATED, MdparRequestLifecycleStateV1.SERIALIZED);
        tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.SERIALIZED, MdparRequestLifecycleStateV1.SENT);

        CompletableFuture<MdparResponseEnvelopeV1> future;
        try {
            future = transport.request(request).toCompletableFuture();
        } catch (RuntimeException ex) {
            tracker.terminal(request.requestId(), MdparRequestLifecycleStateV1.TRANSPORT_ERROR);
            return CompletableFuture.failedFuture(ex);
        }
        if (timeout != null) future = future.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);

        return future.handle((response,error) -> {
            if (error != null) {
                Throwable cause = unwrap(error);
                tracker.terminal(request.requestId(), cause instanceof TimeoutException
                        ? MdparRequestLifecycleStateV1.TIMED_OUT
                        : MdparRequestLifecycleStateV1.TRANSPORT_ERROR);
                throw new CompletionException(cause);
            }
            tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.SENT, MdparRequestLifecycleStateV1.RECEIVED);
            if (!request.requestId().equals(response.requestId())) {
                tracker.terminal(request.requestId(), MdparRequestLifecycleStateV1.INVALID);
                throw new CompletionException(new IllegalStateException("requestId no correlacionado: " + response.requestId()));
            }
            try {
                MdparResponseEnvelopeV1 normalized = codec.readResponse(codec.writeResponse(response));
                tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.RECEIVED, MdparRequestLifecycleStateV1.ACCEPTED);
                return normalized;
            } catch (RuntimeException ex) {
                tracker.terminal(request.requestId(), MdparRequestLifecycleStateV1.INVALID);
                throw new CompletionException(ex);
            }
        });
    }

    public void markActive(String requestId) {
        tracker.transition(requestId, MdparRequestLifecycleStateV1.ACCEPTED, MdparRequestLifecycleStateV1.ACTIVE);
    }

    public void markCompleted(String requestId) {
        tracker.terminal(requestId, MdparRequestLifecycleStateV1.COMPLETED);
    }

    private static Throwable unwrap(Throwable error) {
        if ((error instanceof CompletionException || error instanceof ExecutionException) && error.getCause() != null) return error.getCause();
        return error;
    }
}

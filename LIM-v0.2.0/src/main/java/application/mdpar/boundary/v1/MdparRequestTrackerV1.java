package application.mdpar.boundary.v1;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Tracking request -> estado. Sólo observa y correlaciona; no decide conducta de gameplay. */
public final class MdparRequestTrackerV1 {
    private static final Set<MdparRequestLifecycleStateV1> TERMINAL = EnumSet.of(
            MdparRequestLifecycleStateV1.COMPLETED,
            MdparRequestLifecycleStateV1.TIMED_OUT,
            MdparRequestLifecycleStateV1.SUPERSEDED,
            MdparRequestLifecycleStateV1.INVALID,
            MdparRequestLifecycleStateV1.TRANSPORT_ERROR);

    private static final Map<MdparRequestLifecycleStateV1, Set<MdparRequestLifecycleStateV1>> ALLOWED = allowedTransitions();
    private final Map<String, MdparRequestLifecycleStateV1> states = new ConcurrentHashMap<>();

    public void created(MdparRequestEnvelopeV1 request) {
        Objects.requireNonNull(request);
        if (states.putIfAbsent(request.requestId(), MdparRequestLifecycleStateV1.CREATED) != null) {
            throw new IllegalStateException("requestId duplicado: " + request.requestId());
        }
    }

    public void transition(String requestId, MdparRequestLifecycleStateV1 expected, MdparRequestLifecycleStateV1 next) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(expected);
        Objects.requireNonNull(next);
        requireAllowed(expected, next);
        if (!states.replace(requestId, expected, next)) {
            throw new IllegalStateException("Transición inválida para " + requestId + ": " + states.get(requestId)
                    + " -> " + next + " (esperado " + expected + ")");
        }
    }

    public void terminal(String requestId, MdparRequestLifecycleStateV1 terminal) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(terminal);
        if (!TERMINAL.contains(terminal)) throw new IllegalArgumentException("Estado no terminal: " + terminal);
        states.compute(requestId, (id, current) -> {
            if (current == null) throw new IllegalStateException("requestId desconocido: " + requestId);
            requireAllowed(current, terminal);
            return terminal;
        });
    }

    public Optional<MdparRequestLifecycleStateV1> state(String requestId) {
        return Optional.ofNullable(states.get(requestId));
    }

    public Map<String, MdparRequestLifecycleStateV1> snapshot() {
        return Map.copyOf(states);
    }

    private static void requireAllowed(MdparRequestLifecycleStateV1 from, MdparRequestLifecycleStateV1 to) {
        if (!ALLOWED.getOrDefault(from, Set.of()).contains(to)) {
            throw new IllegalStateException("Transición de lifecycle no autorizada: " + from + " -> " + to);
        }
    }

    private static Map<MdparRequestLifecycleStateV1, Set<MdparRequestLifecycleStateV1>> allowedTransitions() {
        EnumMap<MdparRequestLifecycleStateV1, Set<MdparRequestLifecycleStateV1>> map = new EnumMap<>(MdparRequestLifecycleStateV1.class);
        map.put(MdparRequestLifecycleStateV1.CREATED, EnumSet.of(
                MdparRequestLifecycleStateV1.SERIALIZED,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID));
        map.put(MdparRequestLifecycleStateV1.SERIALIZED, EnumSet.of(
                MdparRequestLifecycleStateV1.SENT,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID,
                MdparRequestLifecycleStateV1.TRANSPORT_ERROR));
        map.put(MdparRequestLifecycleStateV1.SENT, EnumSet.of(
                MdparRequestLifecycleStateV1.RECEIVED,
                MdparRequestLifecycleStateV1.TIMED_OUT,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID,
                MdparRequestLifecycleStateV1.TRANSPORT_ERROR));
        map.put(MdparRequestLifecycleStateV1.RECEIVED, EnumSet.of(
                MdparRequestLifecycleStateV1.ACCEPTED,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID));
        map.put(MdparRequestLifecycleStateV1.ACCEPTED, EnumSet.of(
                MdparRequestLifecycleStateV1.ACTIVE,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID));
        map.put(MdparRequestLifecycleStateV1.ACTIVE, EnumSet.of(
                MdparRequestLifecycleStateV1.COMPLETED,
                MdparRequestLifecycleStateV1.SUPERSEDED,
                MdparRequestLifecycleStateV1.INVALID));
        return Map.copyOf(map);
    }
}

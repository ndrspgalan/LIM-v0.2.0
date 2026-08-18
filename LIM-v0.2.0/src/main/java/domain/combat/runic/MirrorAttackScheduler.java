package domain.combat.runic;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

/** Cola determinista no bloqueante para Reflejo. */
public final class MirrorAttackScheduler {
    public static final double DELAY_SECONDS = 0.5;
    private final PriorityQueue<Scheduled> pending = new PriorityQueue<>(Comparator.comparingDouble(Scheduled::dueAt));

    public void schedule(double nowSeconds, MirroredAttackCommand command, boolean mirrorActive, ImpactOrigin origin) {
        if (!Double.isFinite(nowSeconds) || nowSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        Objects.requireNonNull(command); Objects.requireNonNull(origin);
        if (mirrorActive && origin == ImpactOrigin.PRIMARY_ATTACK) pending.add(new Scheduled(nowSeconds + DELAY_SECONDS, command));
    }
    public int executeDue(double nowSeconds) {
        if (!Double.isFinite(nowSeconds) || nowSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        int executed = 0;
        while (!pending.isEmpty() && pending.peek().dueAt() <= nowSeconds) {
            pending.remove().command().execute(); executed++;
        }
        return executed;
    }
    public int pendingCount() { return pending.size(); }
    private record Scheduled(double dueAt, MirroredAttackCommand command) {}
}

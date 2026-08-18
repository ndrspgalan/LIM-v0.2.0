package domain.combat.runic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Memoria por atacante y objetivo; persiste hasta muerte o fin del encuentro hostil. */
public final class ResonanceCombatMemory {
    private final Map<CombatPair, PreviousHit> previousHits = new HashMap<>();

    public ResonanceResult register(Object attackerId, Object targetId, AttackSignature signature,
                                    double currentPhysicalNetDamage, boolean resonanceActive,
                                    ImpactOrigin origin) {
        Objects.requireNonNull(attackerId); Objects.requireNonNull(targetId);
        Objects.requireNonNull(signature); Objects.requireNonNull(origin);
        if (!Double.isFinite(currentPhysicalNetDamage) || currentPhysicalNetDamage < 0) {
            throw new IllegalArgumentException("El daño físico neto debe ser finito y no negativo.");
        }
        CombatPair pair = new CombatPair(attackerId, targetId);
        PreviousHit previous = previousHits.get(pair);
        ResonanceResult result = resonanceActive && origin == ImpactOrigin.PRIMARY_ATTACK
                && previous != null && previous.signature().equals(signature)
                ? new ResonanceResult(true, previous.physicalNetDamage())
                : ResonanceResult.inactive();
        if (origin == ImpactOrigin.PRIMARY_ATTACK) {
            previousHits.put(pair, new PreviousHit(signature, currentPhysicalNetDamage));
        }
        return result;
    }

    public void onTargetDeath(Object targetId) {
        previousHits.keySet().removeIf(pair -> pair.targetId().equals(targetId));
    }
    public void onHostileEncounterEnded() { previousHits.clear(); }
    public int rememberedPairs() { return previousHits.size(); }

    private record CombatPair(Object attackerId, Object targetId) {}
    private record PreviousHit(AttackSignature signature, double physicalNetDamage) {}
}

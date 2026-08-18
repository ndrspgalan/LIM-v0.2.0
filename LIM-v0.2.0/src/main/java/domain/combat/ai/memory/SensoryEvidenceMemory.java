package domain.combat.ai.memory;

import domain.combat.ai.perception.PerceivedTargetEvidence;

/** Últimos instantes de evidencia por canal. -1 significa que nunca fue observado. */
public record SensoryEvidenceMemory(
        double lastVisualSeconds,
        double lastHeardSeconds,
        double lastScentSeconds,
        double lastFootprintSeconds,
        double lastImpactOriginSeconds,
        boolean targetLockAllowedAtLastVisual
) {
    public static SensoryEvidenceMemory empty() { return new SensoryEvidenceMemory(-1,-1,-1,-1,-1,false); }

    public SensoryEvidenceMemory observe(PerceivedTargetEvidence e, double now) {
        return new SensoryEvidenceMemory(
                e.visualContact() ? now : lastVisualSeconds,
                e.heard() ? now : lastHeardSeconds,
                e.scented() ? now : lastScentSeconds,
                e.footprintsObserved() ? now : lastFootprintSeconds,
                e.impactOriginObserved() ? now : lastImpactOriginSeconds,
                e.visualContact() ? e.targetLockAllowed() : targetLockAllowedAtLastVisual
        );
    }

    public double ageOfLatestEvidence(double now) {
        double latest=Math.max(lastVisualSeconds,Math.max(lastHeardSeconds,Math.max(lastScentSeconds,Math.max(lastFootprintSeconds,lastImpactOriginSeconds))));
        return latest < 0 ? Double.POSITIVE_INFINITY : Math.max(0, now-latest);
    }
}

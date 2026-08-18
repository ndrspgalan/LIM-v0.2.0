package domain.combat.ai.declarative;

import domain.combat.ai.remote.RemoteOffenseFamily;
import domain.combat.ai.remote.RemoteReadiness;
import domain.combat.ai.remote.RangedDistanceState;
import domain.inventory.item.LethalityProfile;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Acción remota factual. No contiene score, prioridad ni recomendación. */
public record RemoteActionCandidate(
        String sourceName,
        RemoteOffenseFamily family,
        RemoteActionType action,
        RemoteReadiness readiness,
        boolean immediatelyExecutable,
        double currentDistanceMeters,
        double minimumAdequateDistanceMeters,
        double maximumEffectiveDistanceMeters,
        RangedDistanceState distanceState,
        double preparationSeconds,
        double reloadDurationSeconds,
        double shotIntervalSeconds,
        double recoverySeconds,
        double chargeDurationSeconds,
        int availableUses,
        boolean supportsAiming,
        boolean currentlyAiming,
        boolean improvised,
        boolean recoverable,
        LethalityProfile physicalLethality,
        Optional<AmmunitionFact> ammunition,
        List<RemoteRelationFact> relations
) {
    public RemoteActionCandidate {
        if (sourceName==null||sourceName.isBlank()) throw new IllegalArgumentException("Fuente remota obligatoria.");
        Objects.requireNonNull(family); Objects.requireNonNull(action); Objects.requireNonNull(readiness);
        Objects.requireNonNull(distanceState); Objects.requireNonNull(physicalLethality);
        ammunition=Objects.requireNonNull(ammunition); relations=List.copyOf(Objects.requireNonNull(relations));
        if (!Double.isFinite(currentDistanceMeters)||currentDistanceMeters<0) throw new IllegalArgumentException("Distancia actual inválida.");
        if (!Double.isFinite(minimumAdequateDistanceMeters)||minimumAdequateDistanceMeters<0) throw new IllegalArgumentException("Distancia mínima inválida.");
        if (!Double.isFinite(maximumEffectiveDistanceMeters)||maximumEffectiveDistanceMeters<=minimumAdequateDistanceMeters) throw new IllegalArgumentException("Alcance remoto inválido.");
        for(double v:new double[]{preparationSeconds,reloadDurationSeconds,shotIntervalSeconds,recoverySeconds,chargeDurationSeconds}) if(!Double.isFinite(v)||v<0) throw new IllegalArgumentException("Temporalidad remota inválida."); if(availableUses<0) throw new IllegalArgumentException("Usos inválidos.");
    }
}

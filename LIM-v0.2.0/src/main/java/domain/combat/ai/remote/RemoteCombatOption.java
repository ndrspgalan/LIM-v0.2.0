package domain.combat.ai.remote;

import domain.inventory.InventoryEntry;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import java.util.Objects;
import java.util.Optional;

/** Vista factual y no destructiva de una posibilidad ofensiva remota.  separa los tiempos físicos. */
public record RemoteCombatOption(
        InventoryEntry source,
        RemoteOffenseFamily family,
        LethalityProfile lethality,
        double minimumAdequateDistanceMeters,
        double maximumEffectiveDistanceMeters,
        RemoteReadiness readiness,
        double preparationSeconds,
        double reloadDurationSeconds,
        double shotIntervalSeconds,
        double recoverySeconds,
        double chargeDurationSeconds,
        int availableUses,
        boolean supportsAiming,
        boolean improvised,
        boolean recoverable,
        Optional<AmmunitionDescriptor> ammunitionDescriptor
) {
    public RemoteCombatOption {
        source = Objects.requireNonNull(source); family=Objects.requireNonNull(family); lethality=Objects.requireNonNull(lethality);
        readiness=Objects.requireNonNull(readiness); ammunitionDescriptor=Objects.requireNonNull(ammunitionDescriptor);
        if(!Double.isFinite(minimumAdequateDistanceMeters)||minimumAdequateDistanceMeters<0)throw new IllegalArgumentException("Distancia mínima inválida.");
        if(!Double.isFinite(maximumEffectiveDistanceMeters)||maximumEffectiveDistanceMeters<=minimumAdequateDistanceMeters)throw new IllegalArgumentException("Alcance remoto inválido.");
        for(double v: new double[]{preparationSeconds,reloadDurationSeconds,shotIntervalSeconds,recoverySeconds,chargeDurationSeconds}) if(!Double.isFinite(v)||v<0)throw new IllegalArgumentException("Temporalidad remota inválida.");
        if(availableUses<0)throw new IllegalArgumentException("Usos inválidos.");
    }
    public String name(){return source.name();} public boolean ready(){return readiness==RemoteReadiness.READY;}
    public RangedDistanceState distanceState(double currentDistanceMeters){return RangedDistancePolicy.classify(currentDistanceMeters,minimumAdequateDistanceMeters,maximumEffectiveDistanceMeters);}
}

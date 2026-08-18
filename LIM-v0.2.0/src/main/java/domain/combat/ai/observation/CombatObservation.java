package domain.combat.ai.observation;

import domain.combat.ai.loadout.VisibleLoadout;
import domain.combat.ai.remote.RemoteArsenalSnapshot;
import domain.combat.ai.threat.CombatantPresence;
import java.util.Objects;

/**
 * Observación factual interna de LIM. Desde  no contiene banderas tácticas, perfiles de utilidad
 * ni estados resumidos para decidir; la percepción actor-específica se filtra posteriormente.
 */
public record CombatObservation(
        CombatantPresence self,
        CombatantPresence target,
        VisibleLoadout selfLoadout,
        VisibleLoadout targetLoadout,
        double currentDistanceMeters,
        RemoteArsenalSnapshot selfRemoteArsenal,
        RemoteArsenalSnapshot targetVisibleRemoteArsenal
) {
    public CombatObservation {
        Objects.requireNonNull(self);
        Objects.requireNonNull(target);
        Objects.requireNonNull(selfLoadout);
        Objects.requireNonNull(targetLoadout);
        Objects.requireNonNull(selfRemoteArsenal);
        Objects.requireNonNull(targetVisibleRemoteArsenal);
        if (!Double.isFinite(currentDistanceMeters) || currentDistanceMeters < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa ni no finita.");
        }
    }

    public static CombatObservation meleeOnly(CombatantPresence self, CombatantPresence target,
                                               VisibleLoadout selfLoadout, VisibleLoadout targetLoadout,
                                               double currentDistanceMeters) {
        return new CombatObservation(self,target,selfLoadout,targetLoadout,currentDistanceMeters,
                RemoteArsenalSnapshot.empty(),RemoteArsenalSnapshot.empty());
    }
}

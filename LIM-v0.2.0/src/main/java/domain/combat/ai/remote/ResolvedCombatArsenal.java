package domain.combat.ai.remote;

import domain.combat.ai.loadout.ResolvedCombatLoadout;

import java.util.Objects;

public record ResolvedCombatArsenal(
        ResolvedCombatLoadout melee,
        RemoteArsenalSnapshot remote
) {
    public ResolvedCombatArsenal {
        melee = Objects.requireNonNull(melee, "El loadout melee no puede ser nulo.");
        remote = Objects.requireNonNull(remote, "El arsenal remoto no puede ser nulo.");
    }

    public boolean hasReadyRemoteOffense() { return !remote.readyOptions().isEmpty(); }
}

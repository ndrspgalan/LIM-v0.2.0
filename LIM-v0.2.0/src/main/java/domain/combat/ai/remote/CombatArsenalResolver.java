package domain.combat.ai.remote;

import domain.combat.ai.loadout.CombatLoadoutResolver;
import domain.combat.ai.loadout.ResolvedCombatLoadout;
import domain.combat.ai.loadout.VisibleLoadout;

import java.util.Objects;

/**
 * Une el manejo melee existente con el nuevo snapshot remoto.  no decide
 * todavía qué opción es tácticamente superior; sólo construye el arsenal común.
 */
public final class CombatArsenalResolver {
    private final CombatLoadoutResolver meleeResolver;

    public CombatArsenalResolver() { this(new CombatLoadoutResolver()); }
    public CombatArsenalResolver(CombatLoadoutResolver meleeResolver) {
        this.meleeResolver = Objects.requireNonNull(meleeResolver);
    }

    public ResolvedCombatArsenal resolve(
            VisibleLoadout visibleMeleeLoadout,
            RemoteArsenalSnapshot remoteArsenal
    ) {
        return new ResolvedCombatArsenal(
                meleeResolver.resolve(visibleMeleeLoadout),
                Objects.requireNonNull(remoteArsenal));
    }
    }

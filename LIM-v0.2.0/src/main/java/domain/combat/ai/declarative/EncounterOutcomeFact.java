package domain.combat.ai.declarative;

import domain.combat.ai.memory.*;
import java.util.Objects;

/** Resultado episódico bruto observado; nunca contiene ajustes de utilidad. */
public record EncounterOutcomeFact(String kind, String action, String source, boolean connectedOrAvoided,
                                   double observedDamage, double observedStaggerSeconds, double resourceCost,
                                   double observedAtSeconds) {
    public EncounterOutcomeFact {
        kind=Objects.requireNonNull(kind); action=Objects.requireNonNull(action); source=Objects.requireNonNull(source);
        if(observedDamage<0||observedStaggerSeconds<0||resourceCost<0||observedAtSeconds<0) throw new IllegalArgumentException("Magnitud inválida.");
    }
    public static EncounterOutcomeFact from(OffensiveOutcome o){return new EncounterOutcomeFact("OFFENSIVE",o.key().action().name(),o.key().sourceName(),o.connected(),o.observedDamage(),o.observedStaggerSeconds(),o.resourceCost(),o.combatTimeSeconds());}
    public static EncounterOutcomeFact from(DefensiveOutcome o){return new EncounterOutcomeFact("DEFENSIVE",o.response().name(),o.sourceType()+":"+o.incomingAction(),o.avoided(),o.residualDamage(),o.residualStaggerSeconds(),o.resourceCost(),o.combatTimeSeconds());}
}

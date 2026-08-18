package domain.combat.ai.memory;

import domain.combat.ai.execution.CombatAction;
import java.util.Objects;

/** Identidad estable de una acción observada sin inferir categorías del objetivo. */
public record CombatActionKey(CombatAction action, String sourceName) {
    public CombatActionKey {
        Objects.requireNonNull(action, "La acción no puede ser nula.");
        sourceName = sourceName == null ? "" : sourceName;
    }
    public static CombatActionKey of(CombatAction action) { return new CombatActionKey(action, ""); }
}

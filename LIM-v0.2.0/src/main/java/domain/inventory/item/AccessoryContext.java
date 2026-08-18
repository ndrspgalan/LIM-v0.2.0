package domain.inventory.item;

import java.util.Objects;

/** Contexto mínimo necesario para resolver efectos variables de un abalorio. */
public record AccessoryContext(DayPhase dayPhase, boolean inInterstice) {
    public AccessoryContext {
        Objects.requireNonNull(dayPhase, "El tramo temporal no puede ser nulo.");
    }

    public static AccessoryContext day() {
        return new AccessoryContext(DayPhase.DAY, false);
    }

    public static AccessoryContext afternoon() {
        return new AccessoryContext(DayPhase.AFTERNOON, false);
    }

    public static AccessoryContext night() {
        return new AccessoryContext(DayPhase.NIGHT, false);
    }

    public static AccessoryContext interstice() {
        return new AccessoryContext(DayPhase.DAY, true);
    }
}

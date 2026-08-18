package domain.rest;

import domain.combat.HostileEncounterState;
import domain.environment.time.DayPhase;
import domain.environment.time.EnvironmentalCycle;

import java.util.Objects;

public final class SleepPolicy {
    public boolean canSleepVoluntarily(boolean enabled, HostileEncounterState encounter,
                                       EnvironmentalCycle cycle, SleepState state) {
        Objects.requireNonNull(encounter); Objects.requireNonNull(cycle); Objects.requireNonNull(state);
        state.synchronize(cycle);
        return enabled
                && cycle.phase() == DayPhase.NIGHT
                && !state.sleptDuringCurrentDay(cycle)
                && !encounter.isActive()
                && !state.forcedSleepDue();
    }

    /** El sueño forzoso ignora la ventana NIGHT; sólo la hostilidad puede diferirlo. */
    public boolean mustSleepNow(HostileEncounterState encounter, EnvironmentalCycle cycle, SleepState state) {
        Objects.requireNonNull(encounter); Objects.requireNonNull(cycle); Objects.requireNonNull(state);
        state.synchronize(cycle);
        return state.forcedSleepDue() && !encounter.isActive();
    }

    public String voluntaryBlockReason(boolean enabled, HostileEncounterState encounter,
                                       EnvironmentalCycle cycle, SleepState state) {
        state.synchronize(cycle);
        if (!enabled) return "No existe una zona habilitada para dormir.";
        if (encounter.isActive()) return "No se puede dormir durante un encuentro hostil.";
        if (state.sleptDuringCurrentDay(cycle)) return "Ya has dormido una vez durante este día.";
        if (cycle.phase() != DayPhase.NIGHT) return "Sólo se puede dormir voluntariamente durante el ciclo de noche.";
        if (state.forcedSleepDue()) return "La deuda máxima de sueño exige dormir en cuanto cese cualquier hostilidad.";
        return "El sueño no está disponible.";
    }
}

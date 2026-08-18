package domain.bestiarium.interstice.transcended;

import java.util.Map;
import java.util.Objects;

/** Aplica una causalidad una sola vez. No contiene reglas de quests, diálogos ni level design. */
public final class TranscendedCausalityPolicy {
    public boolean apply(CausalEvent event,TranscendedState state,TranscendedCausalMemory memory){
        Objects.requireNonNull(event);Objects.requireNonNull(state);Objects.requireNonNull(memory);
        if(memory.hasConsumed(event.uniquenessKey()))return false;
        for(Map.Entry<TranscendedLaw,TranscendedShift> effect:event.effects().entrySet())state.apply(effect.getKey(),effect.getValue());
        memory.record(event.uniquenessKey());
        return true;
    }
}

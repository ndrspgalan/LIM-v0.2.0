package domain.bestiarium.interstice.transcended;

import java.util.Objects;

/**
 * Contrato genérico para futuros nodos de oportunidad. Sólo conoce la tendencia de una ley.
 * Neutralidad devuelve siempre el estado ordinario y, por tanto, la ley no ejerce influencia.
 */
public final class TranscendedOpportunityPolicy {
    public <T> T resolve(TranscendedState state,TranscendedLaw law,T ordinary,T poleZero,T poleOne){
        Objects.requireNonNull(state);Objects.requireNonNull(law);Objects.requireNonNull(ordinary);
        return switch(state.tendencyOf(law)){
            case POLE_ZERO -> poleZero;
            case NEUTRAL -> ordinary;
            case POLE_ONE -> poleOne;
        };
    }
}

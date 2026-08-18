package domain.bestiarium.interstice.transcended;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Hecho causal único, no acción física. Una acción repetible puede materializar hechos distintos
 * según contexto; cada hecho sólo altera una vez el estado TRANSCENDED asociado a su clave.
 */
public record CausalEvent(String id,String uniquenessKey,Map<TranscendedLaw,TranscendedShift> effects,String narrative) {
    public CausalEvent {
        if(id==null||id.isBlank())throw new IllegalArgumentException("El hecho causal necesita id.");
        if(uniquenessKey==null||uniquenessKey.isBlank())throw new IllegalArgumentException("El hecho causal necesita clave de unicidad.");
        if(effects==null)throw new IllegalArgumentException("Los efectos causales son obligatorios, aunque estén vacíos.");
        EnumMap<TranscendedLaw,TranscendedShift> copy=new EnumMap<>(TranscendedLaw.class);
        copy.putAll(effects);
        if(copy.containsKey(null)||copy.containsValue(null))throw new IllegalArgumentException("Ley y desplazamiento causal no pueden ser null.");
        effects=Collections.unmodifiableMap(copy);
        narrative=narrative==null?"":narrative;
    }

    public static CausalEvent of(String id,String uniquenessKey,TranscendedLaw law,TranscendedShift shift,String narrative){
        return new CausalEvent(id,uniquenessKey,Map.of(law,shift),narrative);
    }

    /** Permite parametrizar hechos relevantes que no inclinan ninguna ley en ese momento. */
    public static CausalEvent measuredNeutral(String id,String uniquenessKey,String narrative){
        return new CausalEvent(id,uniquenessKey,Map.of(),narrative);
    }
}

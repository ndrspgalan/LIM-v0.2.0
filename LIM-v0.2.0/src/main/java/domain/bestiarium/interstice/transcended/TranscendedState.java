package domain.bestiarium.interstice.transcended;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Estado persistible de las leyes TRANSCENDED de Kenan. */
public final class TranscendedState {
    private final EnumMap<TranscendedLaw,TranscendedValue> values=new EnumMap<>(TranscendedLaw.class);

    public TranscendedState(){for(TranscendedLaw law:TranscendedLaw.values())values.put(law,TranscendedValue.neutral());}

    public TranscendedValue valueOf(TranscendedLaw law){return values.get(Objects.requireNonNull(law));}
    public TranscendedTendency tendencyOf(TranscendedLaw law){return valueOf(law).tendency();}
    public void apply(TranscendedLaw law,TranscendedShift shift){values.put(Objects.requireNonNull(law),valueOf(law).shifted(Objects.requireNonNull(shift)));}
    public Map<TranscendedLaw,TranscendedValue> snapshot(){return Collections.unmodifiableMap(new EnumMap<>(values));}
}

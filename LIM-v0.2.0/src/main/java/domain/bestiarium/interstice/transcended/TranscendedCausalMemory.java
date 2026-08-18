package domain.bestiarium.interstice.transcended;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Memoria persistible de causalidades únicas ya consumadas. */
public final class TranscendedCausalMemory {
    private final Set<String> consumed=new LinkedHashSet<>();
    public boolean hasConsumed(String uniquenessKey){return consumed.contains(Objects.requireNonNull(uniquenessKey));}
    boolean record(String uniquenessKey){return consumed.add(Objects.requireNonNull(uniquenessKey));}
    public Set<String> consumedKeys(){return Collections.unmodifiableSet(new LinkedHashSet<>(consumed));}
}

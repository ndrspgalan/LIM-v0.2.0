package domain.combat.ai.perception;

import domain.audio.ImpactSoundCue;
import domain.signal.CharacterSignal;
import domain.signal.CharacterSignalCategory;
import domain.signal.CharacterSignalModality;
import java.util.*;

/** Comparte exactamente las intensidades del HUD dinámico: la IA oye/observa las mismas señales. */
public final class DiegeticCombatPerceptionAdapter {
    public double staminaDepletionFrom(CharacterSignal signal){
        Objects.requireNonNull(signal);
        if(signal.category()!=CharacterSignalCategory.EXHAUSTION||signal.modality()!=CharacterSignalModality.BREATHING) throw new IllegalArgumentException("La señal no es respiración de PA.");
        return signal.intensity();
    }
    public List<CombatPerceptionSnapshot.ImpactObservation> impactsFrom(ImpactSoundCue cue){
        Objects.requireNonNull(cue);
        return cue.layers().stream().map(l->new CombatPerceptionSnapshot.ImpactObservation(l.sound(),l.intensity().normalizedValue())).toList();
    }
}

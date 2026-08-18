package domain.combat.ai.perception;

import domain.audio.ImpactSound;
import java.util.*;

/** Señales continuas reutilizadas del sistema diegético/HUD; no discretiza la percepción. */
public record CombatPerceptionSnapshot(double observedStaminaDepletionIntensity,List<ImpactObservation> impacts,PerceivedTargetEvidence targetEvidence){
    public CombatPerceptionSnapshot{if(observedStaminaDepletionIntensity<0||observedStaminaDepletionIntensity>1)throw new IllegalArgumentException("Intensidad inválida.");impacts=List.copyOf(impacts);Objects.requireNonNull(targetEvidence);}
    public record ImpactObservation(ImpactSound sound,double normalizedIntensity){public ImpactObservation{Objects.requireNonNull(sound);if(normalizedIntensity<0||normalizedIntensity>1)throw new IllegalArgumentException("Intensidad inválida.");}}
    public static CombatPerceptionSnapshot neutral(){return new CombatPerceptionSnapshot(0,List.of(),PerceivedTargetEvidence.visible());}
}

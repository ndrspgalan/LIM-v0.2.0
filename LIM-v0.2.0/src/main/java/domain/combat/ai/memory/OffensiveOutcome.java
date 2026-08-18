package domain.combat.ai.memory;

import domain.audio.ImpactSound;
import java.util.Optional;

/** Resultado perceptible de una acción ofensiva concreta. */
public record OffensiveOutcome(
        CombatActionKey key,
        boolean connected,
        double observedDamage,
        double observedStaggerSeconds,
        double resourceCost,
        Optional<ImpactSound> impactSound,
        double impactSoundIntensity,
        double combatTimeSeconds
) {
    public OffensiveOutcome {
        if (key == null || impactSound == null) throw new NullPointerException();
        if (!Double.isFinite(observedDamage) || observedDamage < 0) throw new IllegalArgumentException("Daño observado inválido.");
        if (!Double.isFinite(observedStaggerSeconds) || observedStaggerSeconds < 0) throw new IllegalArgumentException("Stagger observado inválido.");
        if (!Double.isFinite(resourceCost) || resourceCost < 0) throw new IllegalArgumentException("Coste inválido.");
        if (!Double.isFinite(impactSoundIntensity) || impactSoundIntensity < 0 || impactSoundIntensity > 1) throw new IllegalArgumentException("Intensidad inválida.");
        if (!Double.isFinite(combatTimeSeconds) || combatTimeSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
    }
}

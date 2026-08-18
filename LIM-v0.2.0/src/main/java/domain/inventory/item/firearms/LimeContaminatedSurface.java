package domain.inventory.item.firearms;

import domain.environment.EnvironmentalAdversity;

import java.util.Set;

/** Zona superficial contaminada por el Rociador de Cal Viva V881. */
public record LimeContaminatedSurface(double remainingSeconds, Set<EnvironmentalAdversity> adversities) {
    public LimeContaminatedSurface {
        if (!Double.isFinite(remainingSeconds) || remainingSeconds < 0) {
            throw new IllegalArgumentException("La duración de contaminación debe ser finita y no negativa.");
        }
        adversities = Set.copyOf(adversities);
    }

    public LimeContaminatedSurface advance(double elapsedSeconds) {
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) {
            throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        }
        return new LimeContaminatedSurface(Math.max(0.0, remainingSeconds - elapsedSeconds), adversities);
    }

    public boolean active() { return remainingSeconds > 0; }
}

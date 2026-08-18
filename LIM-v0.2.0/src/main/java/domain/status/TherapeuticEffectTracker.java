package domain.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/** Conserva efectos temporales sin duplicar su lógica en las políticas consumidoras. */
public final class TherapeuticEffectTracker {
    private final List<ActiveEntry> active = new ArrayList<>();

    public void add(TherapeuticEffectProfile effect) {
        Objects.requireNonNull(effect, "El efecto no puede ser nulo.");
        if (!effect.hasTimedEffects()) return;
        // Un mismo estimulante se renueva; no se acumula consigo mismo.
        active.removeIf(entry -> entry.effect().duration().name().equals(effect.duration().name()));
        active.add(new ActiveEntry(effect, effect.duration().duration()));
    }

    public ActiveTherapeuticEffects aggregate() {
        ActiveTherapeuticEffects result = ActiveTherapeuticEffects.none();
        for (ActiveEntry entry : active) result = result.apply(entry.effect());
        return result;
    }

    public void advanceRealSeconds(double seconds) {
        advance(seconds, TimeScale.REAL_SECONDS);
    }

    public void advanceGameHours(double hours) {
        if (hours < 0) throw new IllegalArgumentException("El tiempo no puede retroceder.");
        advance(hours, TimeScale.GAME_HOURS);
        advance(hours * 60.0, TimeScale.GAME_MINUTES);
    }

    private void advance(double amount, TimeScale scale) {
        if (amount < 0) throw new IllegalArgumentException("El tiempo no puede retroceder.");
        List<ActiveEntry> replacements = new ArrayList<>();
        Iterator<ActiveEntry> iterator = active.iterator();
        while (iterator.hasNext()) {
            ActiveEntry entry = iterator.next();
            if (entry.effect().duration().timeScale() != scale) continue;
            double remaining = entry.remaining() - amount;
            iterator.remove();
            if (remaining > 0) replacements.add(new ActiveEntry(entry.effect(), remaining));
        }
        active.addAll(replacements);
    }

    public boolean isFrenzyImmune() { return aggregate().frenzyImmune(); }
    public int activeCount() { return active.size(); }
    public boolean hasActive(String effectName) {
        Objects.requireNonNull(effectName, "El nombre del efecto no puede ser nulo.");
        return active.stream().anyMatch(entry -> entry.effect().duration().name().equalsIgnoreCase(effectName));
    }
    public double remaining(String effectName) {
        Objects.requireNonNull(effectName, "El nombre del efecto no puede ser nulo.");
        return active.stream().filter(entry -> entry.effect().duration().name().equalsIgnoreCase(effectName))
                .mapToDouble(ActiveEntry::remaining).max().orElse(0.0);
    }
    public java.util.Set<String> activeEffectNames() {
        return active.stream().map(entry -> entry.effect().duration().name())
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    /** vista factual inmutable para serialización/razonamiento externo; no expone la colección mutable interna. */
    public java.util.List<ActiveEffectSnapshot> snapshots() {
        return active.stream().map(entry -> new ActiveEffectSnapshot(
                entry.effect().duration().name(), entry.remaining(), entry.effect().duration().timeScale()))
                .toList();
    }

    public record ActiveEffectSnapshot(String name, double remaining, TimeScale timeScale) {
        public ActiveEffectSnapshot {
            java.util.Objects.requireNonNull(name); java.util.Objects.requireNonNull(timeScale);
            if (name.isBlank() || remaining < 0) throw new IllegalArgumentException("Estado temporal no válido.");
        }
    }

    private record ActiveEntry(TherapeuticEffectProfile effect, double remaining) {}
}

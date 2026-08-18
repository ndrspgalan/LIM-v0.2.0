package domain.ability;

import java.util.*;

/** Fuente única de efectos activos. Sustituir una manifestación retira su efecto anterior. */
public final class MasteryEffectRegistry {
    private final Map<String, MasteryEffect> byId = new LinkedHashMap<>();

    public synchronized void apply(MasteryEffect effect) { byId.put(Objects.requireNonNull(effect).id(), effect); }
    public synchronized void applyAll(Collection<MasteryEffect> effects) { effects.forEach(this::apply); }
    public synchronized boolean remove(String effectId) { return byId.remove(effectId) != null; }
    public synchronized int removeBySource(String manifestationId) {
        int before = byId.size();
        byId.values().removeIf(e -> e.sourceManifestationId().equals(manifestationId));
        return before - byId.size();
    }
    public synchronized Optional<MasteryEffect> find(String effectId) { return Optional.ofNullable(byId.get(effectId)); }
    public synchronized boolean contains(String effectId) { return byId.containsKey(effectId); }
    public synchronized List<MasteryEffect> active() { return List.copyOf(byId.values()); }
    public synchronized void tick(double realSeconds) {
        if (!Double.isFinite(realSeconds) || realSeconds < 0) throw new IllegalArgumentException("Tick inválido.");
        List<MasteryEffect> updated = new ArrayList<>();
        for (MasteryEffect effect : byId.values()) updated.add(effect.tick(realSeconds));
        byId.clear();
        for (MasteryEffect effect : updated) if (!effect.expired()) byId.put(effect.id(), effect);
    }
    public synchronized Set<String> activeSourceManifestations() {
        return byId.values().stream().map(MasteryEffect::sourceManifestationId)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }
    public synchronized void restore(Collection<MasteryEffect> effects) { byId.clear(); applyAll(effects); }
}

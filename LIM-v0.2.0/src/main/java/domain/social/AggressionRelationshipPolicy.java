package domain.social;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Política social común: dos agresiones sufridas restauran una relación HOSTIL. */
public final class AggressionRelationshipPolicy {
    private final Map<String, Integer> aggressions = new HashMap<>();

    public RelationshipType registerAggression(String targetId, RelationshipType current) {
        if (targetId == null || targetId.isBlank()) throw new IllegalArgumentException("El objetivo necesita identificador.");
        Objects.requireNonNull(current);
        int count = aggressions.merge(targetId.trim(), 1, Integer::sum);
        return count >= 2 ? RelationshipType.HOSTILE : current;
    }

    public int aggressionsAgainst(String targetId) { return aggressions.getOrDefault(targetId, 0); }
    public void clear(String targetId) { aggressions.remove(targetId); }
}

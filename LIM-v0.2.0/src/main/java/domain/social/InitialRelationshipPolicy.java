package domain.social;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/** Afinidad profesional inicial, estrictamente bidireccional. */
public final class InitialRelationshipPolicy {
    private static final Map<ProfessionPair, RelationshipType> RELATIONSHIPS = build();

    public RelationshipType between(Profession first, Profession second) {
        return RELATIONSHIPS.getOrDefault(ProfessionPair.of(first, second), RelationshipType.DISTRUSTFUL);
    }

    /** una segunda profesión sólo puede mejorar la relación inicial, nunca empeorarla. */
    public RelationshipType betweenPositiveOnly(java.util.Collection<Profession> first, java.util.Collection<Profession> second) {
        if (first == null || first.isEmpty() || second == null || second.isEmpty()) throw new IllegalArgumentException("Se requiere al menos una profesión por persona.");
        RelationshipType best = RelationshipType.HOSTILE;
        for (Profession a : first) for (Profession b : second) {
            RelationshipType candidate = between(a,b);
            if (candidate.ordinal() > best.ordinal()) best = candidate;
        }
        return best;
    }

    private static Map<ProfessionPair, RelationshipType> build() {
        Map<ProfessionPair, RelationshipType> map = new HashMap<>();
        for (Profession profession : Profession.values()) put(map, profession, profession, RelationshipType.FRIENDLY);
        for (Profession profession : Profession.values()) put(map, Profession.BEGGAR, profession, RelationshipType.INDIFFERENT);

        // Contratos ya fijados del dominio.
        reliable(map, Profession.EBONY_WARRIOR, Profession.MERCENARY, Profession.NOBLE, Profession.MERCHANT);
        put(map, Profession.EBONY_WARRIOR, Profession.SOLDIER, RelationshipType.INDIFFERENT);
        put(map, Profession.EBONY_WARRIOR, Profession.COURTESAN, RelationshipType.FRIENDLY);
        reliable(map, Profession.MERCENARY, Profession.MERCHANT);
        put(map, Profession.MERCENARY, Profession.NOBLE, RelationshipType.DISTRUSTFUL);
        indifferent(map, Profession.MERCENARY, Profession.COURTESAN, Profession.SOLDIER, Profession.BLACKSMITH,
                Profession.CARPENTER, Profession.FAIRGROUND_WORKER, Profession.TEACHER, Profession.JURIST,
                Profession.HUNTER, Profession.SAILOR, Profession.TANNER, Profession.DRESSMAKER,
                Profession.HAIRDRESSER, Profession.STONEMASON, Profession.DAY_LABORER);

        // Cadenas productivas, institucionales y de servicio.
        reliable(map, Profession.MERCHANT, Profession.COURTESAN, Profession.NOBLE, Profession.BLACKSMITH,
                Profession.CARPENTER, Profession.FAIRGROUND_WORKER, Profession.TEACHER, Profession.JURIST,
                Profession.SAILOR, Profession.TANNER, Profession.DRESSMAKER, Profession.HAIRDRESSER,
                Profession.STONEMASON, Profession.DAY_LABORER);
        reliable(map, Profession.COURTESAN, Profession.NOBLE, Profession.FAIRGROUND_WORKER,
                Profession.DRESSMAKER, Profession.HAIRDRESSER);
        reliable(map, Profession.NOBLE, Profession.SOLDIER, Profession.TEACHER,
                Profession.JURIST, Profession.DRESSMAKER, Profession.HAIRDRESSER);
        reliable(map, Profession.SOLDIER, Profession.BLACKSMITH, Profession.JURIST,
                Profession.STONEMASON, Profession.DAY_LABORER);
        reliable(map, Profession.BLACKSMITH, Profession.CARPENTER, Profession.TANNER, Profession.STONEMASON);
        reliable(map, Profession.CARPENTER, Profession.STONEMASON, Profession.DAY_LABORER);
        reliable(map, Profession.FAIRGROUND_WORKER, Profession.SAILOR, Profession.HAIRDRESSER);
        reliable(map, Profession.TEACHER, Profession.JURIST, Profession.HAIRDRESSER);
        reliable(map, Profession.HUNTER, Profession.TANNER, Profession.DAY_LABORER);
        reliable(map, Profession.TANNER, Profession.DRESSMAKER);
        reliable(map, Profession.DRESSMAKER, Profession.HAIRDRESSER);
        return Map.copyOf(map);
    }

    private static void reliable(Map<ProfessionPair, RelationshipType> map, Profession base, Profession... others) {
        for (Profession other : others) put(map, base, other, RelationshipType.RELIABLE);
    }
    private static void indifferent(Map<ProfessionPair, RelationshipType> map, Profession base, Profession... others) {
        for (Profession other : others) put(map, base, other, RelationshipType.INDIFFERENT);
    }
    private static void put(Map<ProfessionPair, RelationshipType> map, Profession a, Profession b, RelationshipType type) {
        map.put(ProfessionPair.of(a, b), type);
    }
}

package domain.bestiarium.physical_plane.aspirant;

import domain.social.Profession;
import domain.social.Subprofession;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Garantiza que cualquier subprofesión canónica pueda alojar de forma defendible al menos
 * una deriva ASPIRANT, sin convertir referente->oficio en una tabla determinista.
 */
public final class AspirantSubprofessionAffinityPolicy {
    private static final Map<Profession, Set<AspirantReferenceId>> PROFESSION_FALLBACKS = build();

    private AspirantSubprofessionAffinityPolicy() {}

    public static boolean compatible(AspirantReferenceId referenceId, Subprofession subprofession) {
        Objects.requireNonNull(referenceId);
        Objects.requireNonNull(subprofession);
        var profile = AspirantReferenceCatalog.profile(referenceId);
        return profile.ecology().explicitlyPreferredSubprofessions().contains(subprofession)
                || PROFESSION_FALLBACKS.getOrDefault(subprofession.profession(), Set.of()).contains(referenceId);
    }

    public static Set<AspirantReferenceId> compatibleReferences(Subprofession subprofession) {
        Objects.requireNonNull(subprofession);
        EnumSet<AspirantReferenceId> result = EnumSet.noneOf(AspirantReferenceId.class);
        for (var id : AspirantReferenceId.values()) if (compatible(id, subprofession)) result.add(id);
        return Set.copyOf(result);
    }

    private static Map<Profession, Set<AspirantReferenceId>> build() {
        EnumMap<Profession, Set<AspirantReferenceId>> m = new EnumMap<>(Profession.class);
        m.put(Profession.EBONY_WARRIOR, ids(AspirantReferenceId.CANID, AspirantReferenceId.FELID, AspirantReferenceId.BOVID, AspirantReferenceId.RAPTOR_BIRD));
        m.put(Profession.MERCHANT, ids(AspirantReferenceId.PORCINE, AspirantReferenceId.RODENT, AspirantReferenceId.CORVID, AspirantReferenceId.ARACHNID));
        m.put(Profession.COURTESAN, ids(AspirantReferenceId.PRIMATE, AspirantReferenceId.FELID, AspirantReferenceId.CORVID, AspirantReferenceId.CEPHALOPOD));
        m.put(Profession.MERCENARY, ids(AspirantReferenceId.CANID, AspirantReferenceId.FELID, AspirantReferenceId.SHARK, AspirantReferenceId.RAPTOR_BIRD));
        m.put(Profession.BEGGAR, ids(AspirantReferenceId.RODENT, AspirantReferenceId.GASTROPOD, AspirantReferenceId.CANID));
        m.put(Profession.NOBLE, ids(AspirantReferenceId.PRIMATE, AspirantReferenceId.CERVID, AspirantReferenceId.RAPTOR_BIRD, AspirantReferenceId.ARACHNID, AspirantReferenceId.ELEPHANTID));
        m.put(Profession.SOLDIER, ids(AspirantReferenceId.CANID, AspirantReferenceId.BOVID, AspirantReferenceId.ANT, AspirantReferenceId.RAPTOR_BIRD));
        m.put(Profession.BLACKSMITH, ids(AspirantReferenceId.COLEOPTERAN, AspirantReferenceId.ANT, AspirantReferenceId.PRIMATE));
        m.put(Profession.CARPENTER, ids(AspirantReferenceId.ANT, AspirantReferenceId.RODENT, AspirantReferenceId.PRIMATE));
        m.put(Profession.FAIRGROUND_WORKER, ids(AspirantReferenceId.CERVID, AspirantReferenceId.PRIMATE, AspirantReferenceId.CORVID, AspirantReferenceId.EQUID));
        m.put(Profession.TEACHER, ids(AspirantReferenceId.PRIMATE, AspirantReferenceId.CORVID, AspirantReferenceId.CEPHALOPOD, AspirantReferenceId.ELEPHANTID));
        m.put(Profession.JURIST, ids(AspirantReferenceId.PRIMATE, AspirantReferenceId.ELEPHANTID, AspirantReferenceId.ARACHNID, AspirantReferenceId.CORVID));
        m.put(Profession.HUNTER, ids(AspirantReferenceId.CANID, AspirantReferenceId.FELID, AspirantReferenceId.RAPTOR_BIRD, AspirantReferenceId.SERPENT));
        m.put(Profession.SAILOR, ids(AspirantReferenceId.PINNIPED, AspirantReferenceId.CETACEAN, AspirantReferenceId.TELEOST, AspirantReferenceId.SHARK, AspirantReferenceId.CEPHALOPOD));
        m.put(Profession.TANNER, ids(AspirantReferenceId.DIPTERAN, AspirantReferenceId.PORCINE, AspirantReferenceId.RODENT));
        m.put(Profession.DRESSMAKER, ids(AspirantReferenceId.CORVID, AspirantReferenceId.ARACHNID, AspirantReferenceId.PRIMATE));
        m.put(Profession.HAIRDRESSER, ids(AspirantReferenceId.PRIMATE, AspirantReferenceId.CERVID, AspirantReferenceId.CORVID));
        m.put(Profession.STONEMASON, ids(AspirantReferenceId.COLEOPTERAN, AspirantReferenceId.BOVID, AspirantReferenceId.ANT));
        m.put(Profession.DAY_LABORER, ids(AspirantReferenceId.BOVID, AspirantReferenceId.EQUID, AspirantReferenceId.ANT, AspirantReferenceId.PORCINE));
        return Map.copyOf(m);
    }

    private static Set<AspirantReferenceId> ids(AspirantReferenceId first, AspirantReferenceId... rest) {
        return Set.copyOf(EnumSet.of(first, rest));
    }
}

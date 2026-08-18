package domain.bestiarium.physical_plane.aspirant;

import domain.environment.time.DayPhase;
import domain.social.Subprofession;
import java.util.Objects;
import java.util.Set;

/** Ecología estructurada que explica dónde y por qué un ASPIRANT tendería a refugiar su vida humana. */
public record AspirantEcologicalProfile(
        Set<AspirantMobilityDomain> mobilityDomains,
        AspirantTemperatureBand temperatureBand,
        AspirantMoistureBand moistureBand,
        Set<DayPhase> preferredDayPhases,
        Set<AspirantHumanDrive> humanDrives,
        Set<AspirantShelter> preferredShelters,
        Set<Subprofession> explicitlyPreferredSubprofessions,
        String behavioralRationale) {

    public AspirantEcologicalProfile {
        mobilityDomains = immutableNonEmpty(mobilityDomains, "dominios de movilidad");
        Objects.requireNonNull(temperatureBand);
        Objects.requireNonNull(moistureBand);
        preferredDayPhases = immutableNonEmpty(preferredDayPhases, "fases del día");
        humanDrives = immutableNonEmpty(humanDrives, "pulsiones humanas");
        preferredShelters = immutableNonEmpty(preferredShelters, "refugios");
        explicitlyPreferredSubprofessions = Set.copyOf(Objects.requireNonNull(explicitlyPreferredSubprofessions));
        if (behavioralRationale == null || behavioralRationale.isBlank())
            throw new IllegalArgumentException("La ecología ASPIRANT necesita una justificación conductual.");
    }

    private static <T> Set<T> immutableNonEmpty(Set<T> values, String label) {
        Objects.requireNonNull(values);
        if (values.isEmpty()) throw new IllegalArgumentException("ASPIRANT necesita " + label + ".");
        return Set.copyOf(values);
    }
}

package domain.bestiarium.physical_plane.ferae;

import domain.ability.AnimalEmpathyContext;
import domain.ability.AnimalEmpathyPolicy;
import domain.milestone.PersonaMilestone;
import domain.persona.PersonaProfile;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Mantiene como máximo un compañero por rama; el vínculo no es un RelationshipType. */
public final class TravelCompanionState {
    private final EnumMap<FeraeBranch, FeraeSpecies> activeByBranch = new EnumMap<>(FeraeBranch.class);

    public Optional<FeraeSpecies> active(FeraeBranch branch) {
        return Optional.ofNullable(activeByBranch.get(Objects.requireNonNull(branch)));
    }

    public Optional<FeraeSpecies> charismaCompanion() { return active(FeraeBranch.CARISMA); }
    public Optional<FeraeSpecies> intelligenceCompanion() { return active(FeraeBranch.INTELIGENCIA); }
    public Map<FeraeBranch, FeraeSpecies> activeCompanions() { return Map.copyOf(activeByBranch); }

    /** Alias histórico: prioriza Carisma y después Inteligencia. */
    public Optional<FeraeSpecies> active() {
        return charismaCompanion().or(this::intelligenceCompanion);
    }

    public boolean bond(FeraeSpecies species, PersonaProfile persona, AnimalEmpathyContext context) {
        Objects.requireNonNull(species);
        Objects.requireNonNull(persona);
        AnimalEmpathyPolicy.CompanionEligibility eligibility = AnimalEmpathyPolicy.companionEligibility(species, context);
        if (!eligibility.eligible()) throw new IllegalStateException(eligibility.reason());
        boolean hadNoCompanions = activeByBranch.isEmpty();
        activeByBranch.put(species.branch(), species);
        if (hadNoCompanions) {
            persona.addMilestone(new PersonaMilestone(
                    "travel-companion",
                    "[COMPAÑERO DE VIAJE]",
                    "Establece por primera vez un vínculo de compañero de viaje con cualquier Ferae elegible de la rama de Carisma o de Inteligencia.",
                    true));
        }
        return hadNoCompanions;
    }
}

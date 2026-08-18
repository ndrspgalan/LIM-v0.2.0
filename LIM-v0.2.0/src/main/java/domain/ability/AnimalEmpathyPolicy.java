package domain.ability;

import domain.bestiarium.physical_plane.ferae.FeraeBranch;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.social.RelationshipType;

import java.util.Objects;

/** relación Ferae, progresión por especie y elegibilidad de compañero. */
public final class AnimalEmpathyPolicy {
    public record CompanionEligibility(boolean eligible, String reason) {
        public static CompanionEligibility allowed() { return new CompanionEligibility(true, "Vínculo disponible."); }
        public static CompanionEligibility denied(String reason) { return new CompanionEligibility(false, reason); }
    }

    private AnimalEmpathyPolicy() {}

    public static RelationshipType relationship(FeraeSpecies species, AnimalEmpathyContext context) {
        Objects.requireNonNull(species);
        Objects.requireNonNull(context);

        if (species.branch() == FeraeBranch.CARISMA) {
            if (context.masteryAffine()) return RelationshipType.FRIENDLY;
            return context.masteryKnowledge().isVisible()
                    ? RelationshipType.FRIENDLY
                    : RelationshipType.INDIFFERENT;
        }

        if (!context.empathyAvailable()) return species.naturalRelationship();
        boolean hasTrophy = species.trophy().map(context.ownedTrophies()::contains).orElse(false);
        if (hasTrophy && context.intelligence() >= species.empathyAttributeRequirement()) {
            return RelationshipType.RELIABLE;
        }
        return species.naturalRelationship();
    }

    public static CompanionEligibility companionEligibility(FeraeSpecies species, AnimalEmpathyContext context) {
        Objects.requireNonNull(species);
        Objects.requireNonNull(context);
        RelationshipType relationship = relationship(species, context);

        if (species.branch() == FeraeBranch.CARISMA) {
            if (relationship != RelationshipType.FRIENDLY) {
                return CompanionEligibility.denied("EMPATÍA ANIMAL todavía no ha convertido la relación en AMISTOSA.");
            }
            if (context.charisma() < species.empathyAttributeRequirement()) {
                return CompanionEligibility.denied("CARISMA insuficiente: requiere " + species.empathyAttributeRequirement() + ".");
            }
            return CompanionEligibility.allowed();
        }

        if (relationship != RelationshipType.RELIABLE) {
            return CompanionEligibility.denied("La Ferae debe haber alcanzado una relación FIABLE.");
        }
        if (species.isInitiallyHostile() && !context.hunterOfHunters()) {
            return CompanionEligibility.denied("Las Ferae inicialmente HOSTILES requieren [CAZADOR DE CAZADORES].");
        }
        return CompanionEligibility.allowed();
    }

    public static int threshold(FeraeSpecies species) {
        return Objects.requireNonNull(species).empathyAttributeRequirement();
    }

}

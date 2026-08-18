package domain.ability;

import domain.bestiarium.physical_plane.ferae.HuntingTrophy;

import java.util.Objects;
import java.util.Set;

/** Contexto persistente necesario para resolver EMPATÍA ANIMAL sin mezclar relación y vínculo. */
public record AnimalEmpathyContext(
        boolean masteryAffine,
        MasteryKnowledgeState masteryKnowledge,
        int charisma,
        int intelligence,
        Set<HuntingTrophy> ownedTrophies,
        boolean hunterOfHunters
) {
    public AnimalEmpathyContext {
        Objects.requireNonNull(masteryKnowledge);
        ownedTrophies = Set.copyOf(Objects.requireNonNull(ownedTrophies));
        if (charisma < 0 || intelligence < 0) throw new IllegalArgumentException("Los atributos no pueden ser negativos.");
    }

    public boolean empathyAvailable() {
        return masteryAffine || masteryKnowledge.isVisible();
    }
}

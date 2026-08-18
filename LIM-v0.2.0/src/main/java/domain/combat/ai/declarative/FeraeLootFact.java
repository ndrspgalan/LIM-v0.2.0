package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeSex;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import java.util.Objects;
import java.util.Optional;

/** Pillaje Ferae . Deliberadamente no existe ningún campo/drop de mucus. */
public record FeraeLootFact(
        String sourceId,
        FeraeSpecies species,
        FeraeSex sex,
        Optional<String> equippedTrophy,
        boolean trophyRemovableNow
) {
    public FeraeLootFact {
        Objects.requireNonNull(sourceId); Objects.requireNonNull(species); Objects.requireNonNull(sex); Objects.requireNonNull(equippedTrophy);
    }
}

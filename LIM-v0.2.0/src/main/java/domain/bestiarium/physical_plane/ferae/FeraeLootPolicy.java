package domain.bestiarium.physical_plane.ferae;

import domain.combat.ai.inventory.external.ExternalInventoryAccessPolicy;
import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;
import java.util.Optional;

/**  — pillaje Ferae sin semántica de drop de mucus. */
public final class FeraeLootPolicy {
    private final ExternalInventoryAccessPolicy access = new ExternalInventoryAccessPolicy();

    public boolean canLoot(ExternalInventoryOwnerState state){ return access.canOpen(state); }

    public Optional<HuntingTrophy> equippedTrophy(FeraeProfile fera){
        return fera.equippedTrophy();
    }
}

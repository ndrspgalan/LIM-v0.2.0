package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeLootPolicy;
import domain.combat.ai.inventory.external.ExternalInventoryAccessPolicy;
import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;
import domain.inventory.InventoryEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** declara acceso/adquisición de recursos externos sin priorizar loot. */
public final class ExternalResourceActionCandidateResolver {
    private final ExternalInventoryAccessPolicy access=new ExternalInventoryAccessPolicy();
    private final FeraeLootPolicy feraeLoot=new FeraeLootPolicy();

    public List<ExternalResourceFact> facts(ExternalResourceDecisionState state){
        ArrayList<ExternalResourceFact> out=new ArrayList<>();
        for(var s:state.sources()){
            if(!s.perceived()) continue;
            boolean inspect=access.canOpen(s.ownerState(),s.invisibilityActive(),s.hostileEncounter());
            out.add(new ExternalResourceFact(s.sourceId(),s.ownerActorId(),s.ownerState(),s.distanceMeters(),s.reachable(),inspect,s.sessionOpen(),s.ownershipKnown(),s.knownItems().size()));
        }
        return List.copyOf(out);
    }

    public List<ExternalInventoryActionCandidate> actions(ExternalResourceDecisionState state){
        ArrayList<ExternalInventoryActionCandidate> out=new ArrayList<>();
        for(var s:state.sources()){
            if(!s.perceived()) continue;
            boolean inspect=access.canOpen(s.ownerState(),s.invisibilityActive(),s.hostileEncounter());
            if(inspect && s.reachable() && !s.sessionOpen()){
                out.add(new ExternalInventoryActionCandidate(ExternalInventoryActionType.INSPECT_EXTERNAL_INVENTORY,s.sourceId(),Optional.empty(),Optional.empty(),
                        List.of("La fuente es perceptible, alcanzable y la política de acceso permite inspeccionarla en su estado actual."),
                        List.of("Abre una sesión de inventario externo; no guarda partida y el combate hostil no es un veto.")));
            }
            if(inspect && s.reachable() && s.sessionOpen()){
                for(InventoryEntry item:s.knownItems()){
                    out.add(new ExternalInventoryActionCandidate(ExternalInventoryActionType.TAKE_ITEM,s.sourceId(),Optional.of(InventoryActionCandidateResolver.fact(item,InventoryLocationFact.ground())),Optional.empty(),
                            List.of("La sesión externa está abierta y la instancia es conocida."),
                            List.of("La instancia abandona la fuente externa y entra por la política universal de admisión del inventario propio.","El siguiente snapshot puede habilitar EQUIP, USE, QUICK_ACCESS o RELOAD sin precalcular una recomendación.")));
                }
            }
            s.ferae().ifPresent(f->{
                var trophy=feraeLoot.equippedTrophy(f);
                boolean removable=s.ownerState()==ExternalInventoryOwnerState.DEAD && s.reachable() && trophy.isPresent();
                if(removable) out.add(new ExternalInventoryActionCandidate(ExternalInventoryActionType.LOOT_FERAE_TROPHY,s.sourceId(),Optional.empty(),Optional.of(trophy.orElseThrow().label()),
                        List.of("La Ferae está muerta y porta un trofeo equipado físicamente extraíble."),
                        List.of("El trofeo pasa a ser un recurso externo adquirido. Ningún drop de mucus se resuelve en esta operación.")));
            });
        }
        for(InventoryEntry item:state.reachableWorldItems()){
            out.add(new ExternalInventoryActionCandidate(ExternalInventoryActionType.PICK_UP_WORLD_ITEM,"WORLD",Optional.of(InventoryActionCandidateResolver.fact(item,InventoryLocationFact.ground())),Optional.empty(),
                    List.of("La instancia del mundo es perceptible y está al alcance físico inmediato."),
                    List.of("La instancia entra por la política universal de admisión/colocación del inventario propio.")));
        }
        return List.copyOf(out);
    }

    public List<FeraeLootFact> feraeFacts(ExternalResourceDecisionState state){
        ArrayList<FeraeLootFact> out=new ArrayList<>();
        for(var s:state.sources()) if(s.perceived()) s.ferae().ifPresent(f->{
            var trophy=feraeLoot.equippedTrophy(f);
            out.add(new FeraeLootFact(s.sourceId(),f.species(),f.sex(),trophy.map(t->t.label()),s.ownerState()==ExternalInventoryOwnerState.DEAD && s.reachable() && trophy.isPresent()));
        });
        return List.copyOf(out);
    }
}

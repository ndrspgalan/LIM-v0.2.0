package domain.combat.ai.encounter;
import java.util.*;
/** Selección puramente física para arcos/áreas: jamás filtra por aliado/enemigo. */
public final class PhysicalEffectTargetPolicy{
 public List<CombatSpatialActor> within(double rangeMeters,Collection<CombatSpatialActor> actors){
  if(rangeMeters<0||!Double.isFinite(rangeMeters))throw new IllegalArgumentException("Alcance inválido.");
  return actors.stream().filter(CombatSpatialActor::physicallyAffectable).filter(a->a.distanceMeters()<=rangeMeters+1e-9).toList();
 }
}

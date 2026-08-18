package domain.combat.ai.declarative;
import domain.ability.*; import domain.character.sheet.CharacterSheet; import java.util.*;
/** Proyecta catálogo/progresión/efectos a hechos declarativos. No puntúa ni elige. */
public final class AbilityActionCandidateResolver {
 public List<AbilityActionCandidate> resolve(CombatActorDecisionState actor, AbilityDecisionState state){
  Objects.requireNonNull(actor);Objects.requireNonNull(state); CharacterSheet sheet=actor.sheet(); List<AbilityActionCandidate> out=new ArrayList<>();
  for(MasteryManifestation m: state.masteries().selectableManifestations(MasteryType.ACTIVE,sheet)) addManifestation(out,m,state,mechanics(m));
  for(MasteryManifestation m: state.masteries().selectableManifestations(MasteryType.SUSTAINED,sheet)) addManifestation(out,m,state,mechanics(m));
  for(MasteryId id: state.masteries().unlockedIds()){
   Mastery mastery=MasteryCatalog.require(id);
   if(mastery instanceof PairMastery p){for(MasteryVariant v: state.masteries().visibleVariants(p,sheet))if(v.type()==MasteryType.PASSIVE)out.add(passive(id,p.name(),v.name(),v.mechanicalDescription()));}
   else if(mastery instanceof StructuredMastery s){for(MasteryStage st: state.masteries().visibleStages(s,sheet))if(st.natures().contains(MasteryType.PASSIVE))out.add(passive(id,s.name(),st.name(),st.mechanicalDescription()));}
   else if(mastery instanceof EvolutiveMastery e && e.isActiveFor(sheet)) out.add(passive(id,e.name(),e.name(),e.mechanicalDescription()));
  }
  // Transmutación: los nodos desbloqueados se conservan individualmente, incluidos pasivos y sostenidos.
  for(TransmutationNode n: state.masteries().visibleTransmutationNodes(sheet)){
   boolean active=isActive(state.effects(),n.name()); AbilityActionType type=n.type()==MasteryType.PASSIVE?AbilityActionType.PASSIVE_RELATION:(n.id()==TransmutationNodeId.METAMORPHOSIS?AbilityActionType.TRANSFORM:(active?AbilityActionType.MAINTAIN:AbilityActionType.ACTIVATE));
   out.add(new AbilityActionCandidate(MasteryId.TRANSMUTACION,"TRANSMUTACIÓN",n.name(),n.type(),type,active,n.mechanicalDescription(),magnitudes(state.effects(),n.name())));
   if(active&&n.type()==MasteryType.SUSTAINED) out.add(new AbilityActionCandidate(MasteryId.TRANSMUTACION,"TRANSMUTACIÓN",n.name(),n.type(),AbilityActionType.DEACTIVATE,true,"Interrumpir el estado sostenido; LIM conserva las consecuencias posteriores.",magnitudes(state.effects(),n.name())));
  }
  return List.copyOf(out);
 }
 public List<AbilityEffectFact> activeEffects(AbilityDecisionState state){return state.effects().active().stream().map(e->new AbilityEffectFact(e.id(),e.sourceManifestationId(),e.targetId(),e.remainingRealSeconds(),e.sustained(),e.magnitudes())).toList();}
 private void addManifestation(List<AbilityActionCandidate> out,MasteryManifestation m,AbilityDecisionState state,String mechanics){boolean active=isActive(state.effects(),m.name());out.add(new AbilityActionCandidate(m.familyId(),m.familyName(),m.name(),m.type(),active?AbilityActionType.MAINTAIN:AbilityActionType.ACTIVATE,active,mechanics,magnitudes(state.effects(),m.name())));if(active&&m.type()==MasteryType.SUSTAINED)out.add(new AbilityActionCandidate(m.familyId(),m.familyName(),m.name(),m.type(),AbilityActionType.DEACTIVATE,true,"Interrumpir la manifestación sostenida.",magnitudes(state.effects(),m.name())));}
 private AbilityActionCandidate passive(MasteryId id,String family,String name,String mechanics){return new AbilityActionCandidate(id,family,name,MasteryType.PASSIVE,AbilityActionType.PASSIVE_RELATION,true,mechanics,Map.of());}
 private String mechanics(MasteryManifestation m){Mastery x=MasteryCatalog.require(m.familyId());if(x instanceof PairMastery p){for(MasteryVariant v:List.of(p.original(),p.refined()))if(v.name().equals(m.name()))return v.mechanicalDescription();}if(x instanceof StructuredMastery s){for(MasteryStage st:s.stages())if(st.name().equals(m.name()))return st.mechanicalDescription();}if(x instanceof TransmutationMastery t){for(TransmutationNode n:t.orderedNodes())if(n.name().equals(m.name()))return n.mechanicalDescription();}return x.narrativeDescription();}
 private static boolean isActive(MasteryEffectRegistry r,String token){String n=norm(token);return r.active().stream().anyMatch(e->norm(e.sourceManifestationId()).contains(n));}
 private static Map<String,Double> magnitudes(MasteryEffectRegistry r,String token){String n=norm(token);Map<String,Double> m=new LinkedHashMap<>();for(MasteryEffect e:r.active())if(norm(e.sourceManifestationId()).contains(n))m.putAll(e.magnitudes());return Map.copyOf(m);}
 private static String norm(String s){return java.text.Normalizer.normalize(s,java.text.Normalizer.Form.NFD).replaceAll("\\p{M}","").toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]","");}
}

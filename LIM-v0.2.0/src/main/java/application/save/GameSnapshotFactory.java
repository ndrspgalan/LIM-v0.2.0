package application.save;
import domain.character.sheet.Attribute; import domain.persona.PersonaProfile; import domain.save.*; import domain.save.snapshot.*; import java.util.*;
/** el snapshot deja de fabricar subsistemas vacíos; captura el estado factual disponible de la sesión. */
public final class GameSnapshotFactory { private GameSnapshotFactory(){}
 public static GameSaveSnapshot from(GameSessionState game, PersonaProfile persona){
  Map<String,Integer> attrs=new LinkedHashMap<>();for(Attribute a:Attribute.values())attrs.put(a.name(),game.characterSheet().valueOf(a));
  Map<String,Double> mucus=new LinkedHashMap<>();game.progression().mucusWallet().quantities().forEach((k,v)->mucus.put(k.name(),v));
  var m=game.masteries();
  java.util.function.Function<domain.worldmemory.evidence.WorldKnowledgeSource,WorldMemorySourceSnapshot> sourceSnap=src->new WorldMemorySourceSnapshot(src.type().name(),src.sourceReference(),src.acquiredAt().toString(),src.reliability().name());
  List<WorldMemoryEntrySnapshot> worldEntries=game.worldMemory().knowledge().entries().values().stream().map(e->{var pos=e.spatialMemory();return new WorldMemoryEntrySnapshot(e.id().value(),e.category().name(),e.title(),e.description(),e.sources().stream().map(sourceSnap).toList(),pos.map(p->p.coordinate().x()).orElse(null),pos.map(p->p.coordinate().y()).orElse(null),pos.map(p->p.coordinate().elevation()).orElse(null),pos.map(domain.worldmemory.spatial.RememberedPosition::uncertaintyRadiusMeters).orElse(0.0),pos.map(p->p.precision().name()).orElse(""));}).toList();
  List<WorldMemoryRelationSnapshot> worldRelations=game.worldMemory().knowledge().relations().stream().map(r->new WorldMemoryRelationSnapshot(r.source().value(),r.type().name(),r.target().value(),r.note())).toList();
  List<TerrainObservationSnapshot> terrain=game.worldMemory().knowledge().terrain().observations().stream().map(t->new TerrainObservationSnapshot(t.coordinate().x(),t.coordinate().y(),t.coordinate().elevation(),t.surface().name(),t.observationRadiusMeters(),t.observedAt().toString(),sourceSnap.apply(t.source()))).toList();
  Map<String,String> droppedObjects=new LinkedHashMap<>();game.worldMemory().knowledge().persistentDroppedObjects().forEach((id,c)->droppedObjects.put(id.value(),c.x()+","+c.y()+","+c.elevation()));
  Map<String,String> droppedInstances=new LinkedHashMap<>();game.worldMemory().knowledge().persistentDroppedInstances().forEach((id,c)->droppedInstances.put(id.value(),c.x()+","+c.y()+","+c.elevation()));
  String observationMark=game.worldMemory().knowledge().observationMark().map(mark->mark.coordinate().x()+","+mark.coordinate().y()+","+mark.coordinate().elevation()).orElse("");
  var cycle=game.environmentalCycle();var occurrence=cycle.phenomenonOccurrence();
  var transportState=game.currentInventory().logistics().personalTransport();List<TransportUnitSnapshot> units=new ArrayList<>();transportState.units().forEach((type,u)->{var c=u.coordinate();units.add(new TransportUnitSnapshot(type.name(),u.owned(),u.operationState().name(),c==null?null:c.x(),c==null?null:c.y(),c==null?null:c.elevation(),u.summonAllowedByLevel(),u.assignedNpcId()));});
  return new GameSaveSnapshot(GameSaveSnapshot.CURRENT_SCHEMA_VERSION,
   new CharacterSnapshot(persona.id(),persona.name(),persona.gender().name(),persona.characterClass().name(),game.currentTitle().name(),game.level(),game.vitalResources().currentHealth(),game.vitalResources().currentStamina(),attrs,mucus,game.unarmedGuardDrawn(),game.unarmedRightLead()),
   new CombatSnapshot(game.hostileEncounterState().isActive(),m.activeSustainedManifestationIds(),m.activePassiveManifestations(game.characterSheet()),m.runtimeActiveEffects().stream().collect(java.util.stream.Collectors.toMap(x->x,x->1.0)),game.sleepProgressionActive()),
   InventorySnapshotCodec.snapshot(game.currentInventory()),
   new MasterySnapshot(m.revealedMasteryIds(),m.unlockedMasteryIds(),m.activeSustainedManifestationIds(),m.activePassiveManifestations(game.characterSheet()),m.unlockedStructuredStageIds(),m.unlockedTransmutationNodes().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()),m.runtimeActiveEffects(),persona.equippedRunicMark().map(Enum::name).orElse(""),"",""),
   new WorldSnapshot(cycle.phase().name(),cycle.elapsedInPhase().toSeconds(),cycle.weather().name(),cycle.activeSeason().name(),cycle.phenomenon().name(),occurrence.remainingDuration().toSeconds(),cycle.completedDays(),worldEntries,worldRelations,terrain,droppedObjects,droppedInstances,observationMark),
   new SurvivalSnapshot(game.vitalResources().currentHealth(),game.vitalResources().currentStamina(),game.hungerState().level().name(),game.hungerState().hoursUntilNextStage(),game.hungerState().lastConsumedFood().map(Enum::name).orElse(""),game.thirstState().level(),game.thirstState().hoursUntilNextLevel(),game.thirstState().hydratedHoursRemaining(),game.sleepState().wakeCount(),game.sleepState().lastSleptCompletedDay(),game.sleepState().wakeBaselineCompletedDay(),game.sleepState().hasSlept()),
   new TransportSnapshot(transportState.selectedType().map(Enum::name).orElse(""),units,0),
   new NarrativeSnapshot("KENAN","",persona.milestones().stream().map(x->x.id()).collect(java.util.stream.Collectors.toSet()),persona.milestones().stream().collect(java.util.stream.Collectors.toMap(x->x.id(),x->x.title())),persona.milestones().stream().collect(java.util.stream.Collectors.toMap(x->x.id(),x->x.description())),Map.of(),persona.gameCompleted(),persona.allRunicMarksUnlocked(),persona.equippedRunicMark().map(Enum::name).orElse("")),
   new ProfileReference("default-profile",0));
 }
}

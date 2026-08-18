package application.save;

import domain.ability.*;
import domain.animation.CharacterAnimationState;
import domain.character.*;
import domain.character.progression.*;
import domain.character.sheet.*;
import domain.combat.HostileEncounterState;
import domain.environment.time.*;
import domain.environment.time.randomizer.ClimateRandomizerSet;
import domain.inventory.InventoryState;
import domain.milestone.PersonaMilestone;
import domain.persona.*;
import domain.rest.SleepState;
import domain.runic.RunicMarkId;
import domain.save.*;
import domain.save.snapshot.*;
import domain.social.Profession;
import domain.status.VitalResourceState;
import domain.survival.*;
import domain.worldmemory.*;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.*;
import domain.worldmemory.evidence.*;
import domain.worldmemory.relation.*;
import domain.worldmemory.spatial.*;
import java.time.*;
import java.util.*;

/** hidratación inversa del snapshot. Un save deja de ser sólo serializable y vuelve a ser sesión jugable. */
public final class GameSnapshotHydrator {
    private GameSnapshotHydrator() {}

    public record LoadedGame(GameSessionState game, PersonaProfile persona) {}

    public static LoadedGame restore(GameSaveSnapshot raw, SaveSlot selectedSave) {
        GameSaveSnapshot snapshot=SaveMigrationRegistry.migrate(Objects.requireNonNull(raw));
        CharacterSnapshot c=snapshot.character();
        Gender gender=Gender.valueOf(c.gender()); CharacterClass clazz=CharacterClass.valueOf(c.characterClass());
        EnumMap<Attribute,Integer> attrs=new EnumMap<>(Attribute.class); for(Attribute a:Attribute.values()) attrs.put(a,c.attributes().get(a.name()));
        CharacterSheet sheet=new CharacterSheet(attrs);
        EnumMap<MucusType,Double> mucus=new EnumMap<>(MucusType.class); for(MucusType t:MucusType.values()) mucus.put(t,c.mucusMl().getOrDefault(t.name(),0.0));
        CharacterProgressionState progression=new CharacterProgressionState(c.level(),sheet,new MucusWallet(mucus));
        InventoryState inventory=InventorySnapshotHydrator.restore(snapshot.inventory(),snapshot.transport());
        EnvironmentalCycle cycle=restoreCycle(snapshot.world());
        CurrentCharacterStats stats=new DerivedStatisticsCalculator().calculate(sheet,gender,inventory,cycle.phase());
        VitalResourceState resources=new VitalResourceState(c.currentHealth(),stats.totalHealth().orElseThrow(),c.currentStamina(),stats.totalStamina().orElseThrow());
        CharacterMasteryCollection masteries=CharacterMasteryCollection.forClass(clazz,gender); restoreMasteries(masteries,snapshot.mastery());
        HostileEncounterState encounter=new HostileEncounterState(); if(snapshot.combat().hostileEncounter())encounter.begin();
        WorldMemory memory=restoreWorldMemory(snapshot.world());
        SurvivalSnapshot survival=snapshot.survival();
        SleepState sleep=new SleepState(cycle,survival.lastSleptCompletedDay(),survival.wakeBaselineCompletedDay(),survival.wakeCount(),survival.hasSlept());
        HungerState hunger=new HungerState(HungerLevel.valueOf(survival.hungerLevel()),survival.hungerHoursUntilNextStage(),survival.lastConsumedFood().isBlank()?Optional.empty():Optional.of(FoodType.valueOf(survival.lastConsumedFood())));
        ThirstState thirst=new ThirstState(survival.thirstLevel(),survival.thirstHoursUntilNextLevel(),survival.hydratedHoursRemaining());
        CharacterIdentity identity=new CharacterIdentity(c.name(),gender,clazz,domain.character.KenanCanonicalProfile.PROFESSION,domain.character.KenanCanonicalProfile.HEIGHT_METERS);
        GameSessionState game=new GameSessionState(new CharacterDefinition(identity),new CharacterTitle(c.title()),progression,stats,inventory,encounter,new CharacterAnimationState(),masteries,memory,cycle,resources,sleep,hunger,thirst);
        game.restoreUnarmedGuard(c.unarmedGuardDrawn(),c.unarmedRightLead()); if(snapshot.combat().sleepProgressionActive())game.beginSleepProgression();
        PersonaProfile persona=restorePersona(snapshot,selectedSave,gender,clazz);
        return new LoadedGame(game,persona);
    }

    private static PersonaProfile restorePersona(GameSaveSnapshot s,SaveSlot selected,Gender gender,CharacterClass clazz){
        CharacterSnapshot c=s.character(); NarrativeSnapshot n=s.narrative();
        List<PersonaMilestone> milestones=n.milestones().stream().map(id->new PersonaMilestone(id,n.milestoneTitles().getOrDefault(id,"["+id.toUpperCase(Locale.ROOT).replace('-',' ')+"]"),n.milestoneDescriptions().getOrDefault(id,""),true)).toList();
        PersonaProfile p=new PersonaProfile(c.personaId(),domain.character.KenanCanonicalProfile.NAME,c.level(),selected==null?List.of():List.of(selected),milestones);
        if(n.gameCompleted())p.markGameCompleted(); if(n.allRunicMarksUnlocked())p.unlockAllRunicMarks(); if(n.allRunicMarksUnlocked()&&!n.selectedRunicMark().isBlank())p.equipRunicMark(RunicMarkId.valueOf(n.selectedRunicMark())); p.replaceMasteryCollection(restoreMasteries(CharacterMasteryCollection.forClass(clazz,gender),s.mastery())); return p;
    }
    private static CharacterMasteryCollection restoreMasteries(CharacterMasteryCollection m,MasterySnapshot s){m.restoreKnowledge(s.revealed(),s.unlocked());m.restoreUnlockedStructuredStageIds(s.unlockedStructuredStages());m.restoreUnlockedTransmutationNodeIds(s.unlockedTransmutationNodes());m.restoreRuntimeActiveEffects(s.runtimeActiveEffects());m.restoreActiveSustainedManifestationIds(s.activeSustained());return m;}
    private static EnvironmentalCycle restoreCycle(WorldSnapshot w){AtmosphericPhenomenon p=AtmosphericPhenomenon.valueOf(w.phenomenon());AtmosphericPhenomenonOccurrence o=p==AtmosphericPhenomenon.NONE?AtmosphericPhenomenonOccurrence.none():new AtmosphericPhenomenonOccurrence(p,Duration.ofSeconds(Math.max(1,w.phenomenonRemainingSeconds())));return new EnvironmentalCycle(DayPhase.valueOf(w.dayPhase()),Duration.ofSeconds(w.elapsedInPhaseSeconds()),Weather.valueOf(w.weather()),WeatherSeason.valueOf(w.activeSeason()),o,w.completedDays(),ClimateRandomizerSet.defaults());}
    private static WorldMemory restoreWorldMemory(WorldSnapshot w){WorldMemory m=new WorldMemory();var k=m.knowledge();for(WorldMemoryEntrySnapshot e:w.entries()){List<WorldKnowledgeSource> sources=e.sources().stream().map(GameSnapshotHydrator::source).toList();Optional<RememberedPosition> pos=e.x()==null?Optional.empty():Optional.of(new RememberedPosition(new WorldCoordinate(e.x(),e.y(),e.z()),e.uncertaintyRadius(),SpatialPrecision.valueOf(e.precision())));k.rememberEntry(new WorldMemoryEntry(new WorldMemoryEntryId(e.id()),WorldMemoryCategory.valueOf(e.category()),e.title(),e.description(),sources,pos));}for(WorldMemoryRelationSnapshot r:w.relations())k.rememberRelation(new WorldMemoryRelation(new WorldMemoryEntryId(r.source()),WorldMemoryRelationType.valueOf(r.type()),new WorldMemoryEntryId(r.target()),r.note()));for(TerrainObservationSnapshot t:w.terrain())k.recordTerrain(new TerrainObservation(new WorldCoordinate(t.x(),t.y(),t.z()),TerrainSurface.valueOf(t.surface()),t.radiusMeters(),Instant.parse(t.observedAt()),source(t.source())));w.droppedObjects().forEach((id,c)->k.rememberPersistentDroppedObject(new domain.inventory.catalog.CanonicalObjectTypeId(id),coord(c)));w.droppedInstances().forEach((id,c)->k.rememberPersistentDroppedInstance(new WorldObjectInstanceId(id),coord(c)));if(!w.observationMark().isBlank())k.placeOrReplaceObservationMark(coord(w.observationMark()));return m;}
    private static WorldKnowledgeSource source(WorldMemorySourceSnapshot s){return new WorldKnowledgeSource(KnowledgeSourceType.valueOf(s.type()),s.reference(),Instant.parse(s.acquiredAt()),KnowledgeReliability.valueOf(s.reliability()));}
    private static WorldCoordinate coord(String csv){String[] p=csv.split(",");return new WorldCoordinate(Double.parseDouble(p[0]),Double.parseDouble(p[1]),Double.parseDouble(p[2]));}
}

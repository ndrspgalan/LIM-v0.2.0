package application.rest;

import application.save.GameSaveService;
import application.save.GameSnapshotFactory;
import domain.bestiarium.interstice.faerie.Doppelganger;
import domain.combat.HostileEncounterState;
import domain.environment.time.EnvironmentalCycle;
import domain.persona.PersonaProfile;
import domain.save.GameSessionState;
import domain.save.SaveSlot;
import domain.survival.HungerState;
import domain.survival.SurvivalTimePolicy;
import domain.survival.ThirstState;
import java.util.Objects;

/** ciclo canónico de sueño: encuentro -> progresión -> regeneración -> despertar -> save. */
public final class SleepCycleCoordinator {
    private final SleepProgressionUseCase progression = new SleepProgressionUseCase();
    private final GameSaveService saves;
    public SleepCycleCoordinator(GameSaveService saves){this.saves=Objects.requireNonNull(saves);}

    public SleepCycleStart begin(GameSessionState game, boolean sleepEnabled) {
        Objects.requireNonNull(game);
        var policy=new domain.rest.SleepPolicy();
        game.sleepState().synchronize(game.environmentalCycle());
        boolean forced=policy.mustSleepNow(game.hostileEncounterState(),game.environmentalCycle(),game.sleepState());
        if(!forced && !policy.canSleepVoluntarily(sleepEnabled,game.hostileEncounterState(),game.environmentalCycle(),game.sleepState()))
            return SleepCycleStart.blocked(policy.voluntaryBlockReason(sleepEnabled,game.hostileEncounterState(),game.environmentalCycle(),game.sleepState()));
        if(forced && game.hostileEncounterState().isActive()) return SleepCycleStart.blocked("La hostilidad activa difiere el sueño forzoso.");
        var prep=progression.begin(game);
        if(!prep.doppelgangerEncounter()) game.beginSleepProgression();
        return SleepCycleStart.started(forced,prep.doppelgangerEncounter(),prep.doppelganger());
    }

    public void resolveDoppelganger(GameSessionState game, PersonaProfile persona, boolean doppelgangerDefeated) {
        Objects.requireNonNull(game); Objects.requireNonNull(persona);
        if(game.sleepProgressionActive()) throw new IllegalStateException("La progresión ya está abierta; no hay encuentro pendiente.");
        progression.resolveDoppelganger(persona,doppelgangerDefeated);
        game.beginSleepProgression();
    }

    public SleepCycleCompletion complete(GameSessionState game, PersonaProfile persona,
                                         HungerState hunger, ThirstState thirst) {
        Objects.requireNonNull(game); Objects.requireNonNull(persona);
        if(!game.sleepProgressionActive()) throw new IllegalStateException("No existe una sesión de sueño activa.");
        EnvironmentalCycle cycle=game.environmentalCycle(); var elapsed=cycle.remainingInPhase(); var next=cycle.completeCurrentPhase();
        HungerState effectiveHunger=hunger==null?game.hungerState():hunger; ThirstState effectiveThirst=thirst==null?game.thirstState():thirst;
        HungerState nextHunger=new SurvivalTimePolicy().advance(effectiveHunger,effectiveThirst,elapsed); game.replaceSurvivalStates(nextHunger,effectiveThirst);
        game.vitalResources().restoreStamina(); if(!effectiveThirst.isDehydrated())game.vitalResources().restoreHealth();
        progression.wake(game); game.endSleepProgression();
        SaveSlot wake=saves.saveWake(persona.id(),game.sleepState().wakeCount(),GameSnapshotFactory.from(game,persona)); persona.registerSave(wake);
        return new SleepCycleCompletion(next,nextHunger,wake,game.sleepState().wakeCount());
    }

    public record SleepCycleStart(boolean allowed,boolean forced,boolean doppelgangerEncounter,Doppelganger doppelganger,String reason){
        static SleepCycleStart blocked(String reason){return new SleepCycleStart(false,false,false,null,reason);}
        static SleepCycleStart started(boolean forced,boolean encounter,Doppelganger d){return new SleepCycleStart(true,forced,encounter,d,"");}
    }
    public record SleepCycleCompletion(domain.environment.time.DayPhase nextPhase,HungerState hungerState,SaveSlot wakeSave,long wakeCount){}
}

package application.rest;
import domain.bestiarium.interstice.faerie.*; import domain.character.progression.MucusType; import domain.save.GameSessionState; import domain.persona.PersonaProfile; import java.util.Objects;
/** orden canónico previo al despertar. No regenera PV/PA antes del Doppelgänger. */
public final class SleepProgressionUseCase {
 private final DoppelgangerEncounterPolicy doppelPolicy=new DoppelgangerEncounterPolicy();
 public SleepPreparation begin(GameSessionState game){Objects.requireNonNull(game);boolean trigger=doppelPolicy.shouldTriggerOnNextSleep(game.masteries());return new SleepPreparation(trigger,trigger?Doppelganger.mirrorOf(game):null);}
 public void resolveDoppelganger(PersonaProfile persona, boolean doppelgangerDefeated){if(doppelgangerDefeated)doppelPolicy.defeat(Objects.requireNonNull(persona));}
 public void wake(GameSessionState game){Objects.requireNonNull(game);game.addMucus(MucusType.BLANCO,1.0);game.sleepState().recordWake(game.environmentalCycle());}
 public record SleepPreparation(boolean doppelgangerEncounter,Doppelganger doppelganger){}
}

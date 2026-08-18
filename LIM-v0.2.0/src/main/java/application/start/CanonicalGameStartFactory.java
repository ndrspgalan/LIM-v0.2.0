package application.start;

import domain.ability.CharacterMasteryCollection;
import domain.animation.CharacterAnimationState;
import domain.character.*;
import domain.character.canonical.CanonicalChildLoadout;
import domain.character.canonical.CanonicalChildLoadoutCatalog;
import domain.character.progression.CharacterProgressionState;
import domain.character.progression.MucusWallet;
import domain.character.sheet.CurrentCharacterStats;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.combat.HostileEncounterState;
import domain.environment.time.EnvironmentalCycle;
import domain.persona.PersonaProfile;
import domain.save.GameSessionState;
import domain.status.VitalResourceState;
import domain.worldmemory.WorldMemory;

import java.util.List;

/** Autoridad única del comienzo canónico de LIM. Bootstrap sólo cablea adaptadores. */
public final class CanonicalGameStartFactory {
    private CanonicalGameStartFactory() {}

    public record Start(GameSessionState game, PersonaProfile persona) {}

    public static Start kenanChild() {
        CharacterDefinition kenan=new CharacterDefinition(CharacterIdentity.kenanCanonical());
        var sheet=KenanCanonicalProfile.initialSheet();
        CanonicalChildLoadout loadout=CanonicalChildLoadoutCatalog.forCharacter(KenanCanonicalProfile.NAME,KenanCanonicalProfile.GENDER);
        var progression=new CharacterProgressionState(KenanCanonicalProfile.INITIAL_LEVEL,sheet,MucusWallet.empty());
        var cycle=new EnvironmentalCycle();
        CurrentCharacterStats stats=new DerivedStatisticsCalculator().calculate(sheet,Gender.HOMBRE,loadout.inventory(),cycle.phase());
        var masteries=CharacterMasteryCollection.forCanonicalChild(CharacterClass.INDOMITO,Gender.HOMBRE);
        var resources=new VitalResourceState(stats.totalHealth().orElse(1.0),stats.totalStamina().orElse(1.0));
        GameSessionState game=new GameSessionState(kenan,new CharacterTitle("Niño"),progression,stats,loadout.inventory(),
                new HostileEncounterState(),new CharacterAnimationState(),masteries,new WorldMemory(),cycle,resources);
        PersonaProfile persona=new PersonaProfile("kenan-indomito",KenanCanonicalProfile.NAME,KenanCanonicalProfile.INITIAL_LEVEL,List.of(),List.of());
        persona.replaceMasteryCollection(masteries);
        return new Start(game,persona);
    }
}

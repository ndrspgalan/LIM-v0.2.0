package domain.ability;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;

/** Contrato común para jugador e IA. */
public interface MasteryActor {
    CharacterSheet sheet();
    Gender gender();
    CharacterMasteryCollection masteries();
    MasteryRuntimeContext runtimeContext();
    default MasteryExecutionContext executionContext(){return MasteryExecutionContext.fromRuntimeContext(runtimeContext());}
}

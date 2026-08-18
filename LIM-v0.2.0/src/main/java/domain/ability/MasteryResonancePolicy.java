package domain.ability;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.CharacterClassDefinition;
import java.util.Map;
import java.util.Objects;

/** Resuelve la afinidad de clase y respeta la afinidad de género ya definida por las clases. */
public final class MasteryResonancePolicy {
    private static final Map<CharacterClass, CharacterClassDefinition> DEFINITIONS = CharacterClassDefinition.canonicalDefinitions();
    private MasteryResonancePolicy() {}
    public static boolean resonates(MasteryId masteryId, CharacterClass characterClass, Gender gender) {
        Objects.requireNonNull(masteryId); Objects.requireNonNull(characterClass); Objects.requireNonNull(gender);
        CharacterClassDefinition definition = DEFINITIONS.get(characterClass);
        Mastery mastery = MasteryCatalog.require(masteryId);
        if (mastery.category() == MasteryCategory.EVOLUTIVE) return false; // las evolutivas no pertenecen a ninguna clase.
        if (masteryId == MasteryId.INCITAR) {
            return (gender == Gender.HOMBRE && characterClass == CharacterClass.LUCHADOR)
                    || (gender == Gender.MUJER && characterClass == CharacterClass.HERALDO);
        }
        return definition.isAvailableFor(gender) && mastery.resonanceClass() == characterClass;
    }
}

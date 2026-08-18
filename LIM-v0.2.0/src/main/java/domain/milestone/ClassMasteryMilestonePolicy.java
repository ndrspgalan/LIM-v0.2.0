package domain.milestone;

import domain.ability.CharacterMasteryCollection;
import domain.ability.MasteryCatalog;
import domain.ability.MasteryId;
import domain.ability.MasteryCategory;
import domain.ability.MasteryResonancePolicy;
import domain.character.Gender;
import domain.character.CharacterClass;

import java.util.Arrays;
import java.util.Locale;

/** Concede un hito irreversible al dominar todas las maestrías de una clase, sea o no afín. */
public final class ClassMasteryMilestonePolicy {
    public PersonaMilestone evaluate(CharacterClass clazz, CharacterMasteryCollection collection) {
        Gender gender = switch(clazz) {
            case LUCHADOR, INTELECTUAL, INDOMITO -> Gender.HOMBRE;
            case ESPECIALISTA, APODERADO, HERALDO -> Gender.MUJER;
            case MAESTRO -> Gender.HOMBRE;
        };
        boolean complete = Arrays.stream(MasteryId.values())
                .filter(id -> MasteryResonancePolicy.resonates(id, clazz, gender))
                .allMatch(id -> collection.knowledgeState(id).isUsable());
        if (!complete) return null;
        String title = titleFor(clazz);
        return new PersonaMilestone("class-mastery-" + clazz.name().toLowerCase(Locale.ROOT), title,
                "Todas las maestrías pertenecientes a " + clazz.label() + " han sido desbloqueadas.", true);
    }

    public String titleFor(CharacterClass clazz) {
        String article = switch (clazz) {
            case APODERADO, ESPECIALISTA, HERALDO -> "LA ";
            case MAESTRO -> "";
            default -> "EL ";
        };
        return "[" + article + clazz.label().toUpperCase(Locale.ROOT) + "]";
    }
}

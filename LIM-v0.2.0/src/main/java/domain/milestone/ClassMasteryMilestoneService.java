package domain.milestone;
import domain.ability.CharacterMasteryCollection;
import domain.character.CharacterClass;
import domain.persona.PersonaProfile;
import java.util.Objects;
/** Integra la concesión de hitos de clase con el perfil persistente de PERSONA. */
public final class ClassMasteryMilestoneService {
    private final ClassMasteryMilestonePolicy policy = new ClassMasteryMilestonePolicy();
    public int awardCompletedClassMilestones(PersonaProfile persona, CharacterMasteryCollection collection) {
        Objects.requireNonNull(persona); Objects.requireNonNull(collection);
        int awarded = 0;
        for (CharacterClass clazz : CharacterClass.values()) {
            PersonaMilestone milestone = policy.evaluate(clazz, collection);
            if (milestone != null && persona.addMilestone(milestone)) awarded++;
        }
        return awarded;
    }
}

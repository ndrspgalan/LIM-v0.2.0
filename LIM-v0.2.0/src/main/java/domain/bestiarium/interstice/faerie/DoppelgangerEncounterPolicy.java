package domain.bestiarium.interstice.faerie;
import domain.ability.*; import domain.milestone.PersonaMilestone; import domain.persona.PersonaProfile; import java.util.*;
public final class DoppelgangerEncounterPolicy {
 public static final String GREATER_WILL_ENIGMA="Las voluntades pueden coexistir, cruzarse, oponerse o aliarse. Sus trayectorias divergen y se entrelazan a lo largo de incontables caminos. No obstante, cuando todas las trayectorias hayan alcanzado su término natural y el mundo haya depurado sus incoherencias, solo quedará una Voluntad Mayor.";
 public static final String GREATER_WILL_RESOLUTION="La Voluntad Mayor no es la voluntad que destruye a las demás. Es aquella bajo la cual las demás pueden existir sin disputarse ya la identidad del individuo. El Doppelgänger poseía todas las posibilidades y ninguna jerarquía entre ellas. Tú has conservado una continuidad capaz de incorporarlas sin convertirte en su suma. Desde este momento, ninguna Marca expresa ya aquello a lo que estabas condenado por afinidad. Todas expresan posibilidades que has demostrado poder gobernar.";
 public boolean shouldTriggerOnNextSleep(CharacterMasteryCollection c){return Arrays.stream(MasteryId.values()).allMatch(id->c.knowledgeState(id).isUsable());}
 public boolean defeat(PersonaProfile persona){Objects.requireNonNull(persona);persona.unlockAllRunicMarks();return persona.addMilestone(domain.milestone.CanonicalMilestones.voluntadMayor(GREATER_WILL_ENIGMA+"\n\n"+GREATER_WILL_RESOLUTION));}
}

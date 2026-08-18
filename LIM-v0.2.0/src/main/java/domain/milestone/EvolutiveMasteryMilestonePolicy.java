package domain.milestone;
import domain.ability.*; import domain.persona.PersonaProfile; import java.util.*;
public final class EvolutiveMasteryMilestonePolicy { public boolean evaluate(PersonaProfile p, CharacterMasteryCollection c){if(c.knowledgeState(MasteryId.ELECTROGENESIS).isUsable()&&c.knowledgeState(MasteryId.TRIBOGENESIS).isUsable()) return p.addMilestone(CanonicalMilestones.configuratioOriginalis()); return false;}}

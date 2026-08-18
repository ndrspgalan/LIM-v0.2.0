package domain.bestiarium.physical_plane.ferae;
import domain.milestone.PersonaMilestone; import domain.persona.PersonaProfile; import java.util.*;
public final class HuntingTrophyMilestonePolicy { public boolean evaluate(PersonaProfile persona, Set<HuntingTrophy> owned){Objects.requireNonNull(persona);Objects.requireNonNull(owned); if(!owned.containsAll(FeraeCatalog.canonicalTrophies())) return false; return persona.addMilestone(new PersonaMilestone("hunter-of-hunters","[CAZADOR DE CAZADORES]","Reúne simultáneamente todos los trofeos canónicos de caza Ferae.",true));}}

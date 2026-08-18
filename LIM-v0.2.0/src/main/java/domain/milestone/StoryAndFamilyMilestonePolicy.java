package domain.milestone;
import domain.metaprogression.ProfileProgression; import domain.persona.*; import java.util.Objects;
/** hitos canónicos de Kenan y sus recompensas visibles en MEMORAR. */
public final class StoryAndFamilyMilestonePolicy {
 private final ProfileProgression progression;
 public StoryAndFamilyMilestonePolicy(){this(new ProfileProgression());} public StoryAndFamilyMilestonePolicy(ProfileProgression p){progression=Objects.requireNonNull(p);}
 public boolean awardArc(PersonaProfile persona,String arcTitle){Objects.requireNonNull(persona);Objects.requireNonNull(arcTitle);return switch(arcTitle){
  case "La Edad de Hierro"->progression.awardMilestone(persona,CanonicalMilestones.flammarion());
  case "La Segunda Marcha Exaltada"->progression.awardMilestone(persona,CanonicalMilestones.advenimiento());
  default->false;};}
 public boolean awardSelfAwareness(PersonaProfile persona){return progression.awardMilestone(persona,CanonicalMilestones.ancoraEncarnada());}
 public boolean awardSpiralReached(PersonaProfile persona){return progression.awardMilestone(persona,CanonicalMilestones.laJugadaFinal());}
 public boolean awardConfiguratioOriginalis(PersonaProfile persona){return progression.awardMilestone(persona,CanonicalMilestones.configuratioOriginalis());}
 public boolean awardFamily(PersonaProfile persona,CanonicalFamilyState family,boolean gameFinished){return gameFinished&&Objects.requireNonNull(family).completeAfterGame()&&progression.awardMilestone(persona,CanonicalMilestones.familia());}
 public boolean awardFamily(PersonaProfile persona,PersonaRegistry ignored){return false;}
 public boolean awardRomanticPairing(PersonaProfile persona,boolean pairedWithCanonicalPartner){return pairedWithCanonicalPartner&&progression.awardMilestone(persona,CanonicalMilestones.envejecemosJuntos());}
 public boolean awardKiaraPairing(PersonaProfile persona,boolean pairedWithKiara){return awardRomanticPairing(persona,pairedWithKiara);}
 public ProfileProgression progression(){return progression;}
}

package domain.metaprogression;
import domain.milestone.PersonaMilestone;
import domain.persona.*;
import java.util.*;
/** Perfil de MEMORAR. : sólo contiene recompensas realmente obtenidas; no predeclara huecos bloqueados. */
public final class ProfileProgression {
 private final EnumSet<MemorarDesignWorks> designWorks=EnumSet.noneOf(MemorarDesignWorks.class);
 private final EnumSet<MemorarPoster> posters=EnumSet.noneOf(MemorarPoster.class);
 private final EnumSet<MemorarSoundtrack> soundtracks=EnumSet.noneOf(MemorarSoundtrack.class);
 private final LinkedHashMap<String,String> milestoneTexts=new LinkedHashMap<>();
 private MemorarPoster selectedMainMenuPoster;
 public ProfileProgression(){}
 public ProfileProgression(PersonaRegistry r){this();synchronizeFrom(r);}
 public void synchronizeFrom(PersonaRegistry r){for(var p:r.personas())p.milestones().forEach(this::registerMilestoneReward);}
 public void completeGame(PersonaProfile p){p.markGameCompleted();}
 public boolean registerMilestoneReward(PersonaMilestone m){
  if(m==null)return false; boolean changed=milestoneTexts.putIfAbsent(m.title(),m.description())==null;
  changed|=MemorarPoster.forMilestone(m).map(posters::add).orElse(false);
  changed|=MemorarSoundtrack.forMilestone(m).map(soundtracks::add).orElse(false);
  return changed;
 }
 public boolean registerMilestoneReward(PersonaProfile p,PersonaMilestone m){return registerMilestoneReward(m);}
 public boolean awardMilestone(PersonaProfile p,PersonaMilestone m){boolean n=p.addMilestone(m);if(n)registerMilestoneReward(m);return n;}
 public boolean awardProductOfMemory(PersonaProfile p){return awardMilestone(p,domain.milestone.CanonicalMilestones.productoDeUnRecuerdo());}
 public boolean unlockDesignWorks(MemorarDesignWorks work){return designWorks.add(Objects.requireNonNull(work));}

 public Optional<MemorarPoster> selectedMainMenuPoster(){return Optional.ofNullable(selectedMainMenuPoster);}
 public MemorarPoster effectiveMainMenuPoster(){return selectedMainMenuPoster==null?MemorarPoster.PORTADOR_DE_SUENOS:selectedMainMenuPoster;}
 public MemorarSoundtrack effectiveMainMenuSoundtrack(){
  String milestoneId=effectiveMainMenuPoster().milestoneId();
  return Arrays.stream(MemorarSoundtrack.values()).filter(s->s.milestoneId().equals(milestoneId)).findFirst().orElse(MemorarSoundtrack.PORTADOR_DE_SUENOS);
 }
 public void selectMainMenuPoster(MemorarPoster poster){
  Objects.requireNonNull(poster);
  if(!posters.contains(poster))throw new IllegalStateException("Sólo puede seleccionarse un hito ya desbloqueado en MEMORAR.");
  selectedMainMenuPoster=poster;
 }
 public Set<MemorarDesignWorks> designWorks(){return Set.copyOf(designWorks);}
 /** Alias transitorio de fuente: desde  la recompensa documental es DESIGN WORKS, no el manuscrito físico. */
 public Set<MemorarDocument> documents(){return Set.of();}
 public Set<MemorarPoster> posters(){return Set.copyOf(posters);} public Set<MemorarSoundtrack> soundtracks(){return Set.copyOf(soundtracks);}
 public Set<String> milestones(){return Set.copyOf(milestoneTexts.keySet());}
 public Map<String,String> milestoneTexts(){return Map.copyOf(milestoneTexts);}
}

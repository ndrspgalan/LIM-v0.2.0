package presentation.menu;
import domain.metaprogression.*; import presentation.console.ConsoleInput; import java.io.PrintStream; import java.util.*;
/** MEMORAR : sólo muestra lo obtenido y permite elegir un hito desbloqueado como presentación del menú principal. */
public final class MemorizeScreen {
 private static final String DIVIDER="============================================================"; private final ConsoleInput input; private final PrintStream output; private final ProfileProgression progression;
 public MemorizeScreen(ConsoleInput i,PrintStream o){this(i,o,new ProfileProgression());} public MemorizeScreen(ConsoleInput i,PrintStream o,ProfileProgression p){input=Objects.requireNonNull(i);output=Objects.requireNonNull(o);progression=Objects.requireNonNull(p);}
 public void open(){output.println("\n"+DIVIDER+"\n            MEMORAR\n"+DIVIDER+"\n");renderDesignWorks();renderPosters();renderSoundtracks();renderMilestones();selectMainMenuPresentationIfRequested();output.println();input.waitForEnter("Pulse Intro para volver...");}
 private void renderDesignWorks(){if(progression.designWorks().isEmpty())return;output.println("DESIGN WORKS");progression.designWorks().stream().sorted().forEach(d->output.println("  "+d.label()));output.println();}
 private void renderPosters(){if(progression.posters().isEmpty())return;output.println("LIVE WALLPAPERS");progression.posters().stream().sorted().forEach(p->output.println("  "+p.label()));output.println();}
 private void renderSoundtracks(){if(progression.soundtracks().isEmpty())return;output.println("OST");progression.soundtracks().stream().sorted().forEach(s->output.println("  "+s.label()));output.println();}
 private void renderMilestones(){if(progression.milestoneTexts().isEmpty())return;output.println("HITOS");progression.milestoneTexts().forEach((title,text)->{output.println("  "+title);if(text!=null&&!text.isBlank())output.println("    "+text.replace("\n","\n    "));});}
 private void selectMainMenuPresentationIfRequested(){
  if(progression.posters().isEmpty())return;
  var unlocked=progression.posters().stream().sorted().toList();
  output.println("FONDO DEL MENÚ PRINCIPAL"); output.println("  0. Mantener selección actual");
  for(int i=0;i<unlocked.size();i++)output.println("  "+(i+1)+". "+unlocked.get(i).label());
  int choice=input.readIntegerBetween("Seleccione un fondo: ",0,unlocked.size());
  if(choice>0)progression.selectMainMenuPoster(unlocked.get(choice-1));
 }
}

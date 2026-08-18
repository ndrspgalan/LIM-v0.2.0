package presentation.loading;

import domain.metaprogression.ProfileProgression;
import domain.persona.PersonaProfile;
import domain.save.SaveSlot;
import presentation.console.ConsoleInput;

import java.io.PrintStream;
import java.util.Objects;

/** GOLD : la carga reutiliza el live wallpaper del menú principal y silencia el OST. */
public final class LoadingScreen {
    private static final String DIVIDER="=================================";
    private final ConsoleInput input;
    private final PrintStream output;
    private final ProfileProgression progression;

    public LoadingScreen(ConsoleInput input,PrintStream output,ProfileProgression progression){
        this.input=Objects.requireNonNull(input);this.output=Objects.requireNonNull(output);this.progression=Objects.requireNonNull(progression);
    }
    public void openFor(PersonaProfile persona,SaveSlot savePoint){
        Objects.requireNonNull(persona);Objects.requireNonNull(savePoint);
        output.println();output.println(DIVIDER);output.println("       GENERANDO EL MUNDO");output.println(DIVIDER);output.println();
        output.printf("PERSONA: %s%n",persona.name());
        output.printf("PUNTO DE GUARDADO: %s%n",savePoint.title());
        if(!savePoint.description().isBlank())output.println(savePoint.description());
        output.println();output.printf("LIVE WALLPAPER: %s%n",progression.effectiveMainMenuPoster().label());
        output.println("OST: SILENCIADO DURANTE LA CARGA");output.println();
        input.waitForEnter("Pulse Intro para continuar...");
    }
}

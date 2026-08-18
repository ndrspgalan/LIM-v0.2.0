package presentation.menu;

import domain.persona.PersonaRegistry;
import domain.metaprogression.ProfileProgression;

import java.io.PrintStream;
import java.util.Objects;

public final class MainMenu {
    private static final String TITLE = "LA IDEA DEL MUNDO";
    private static final String SUBTITLE = "Vertical Slice";
    private static final String DIVIDER = "=================================";

    private final PrintStream output;
    private final PersonaRegistry registry;
    private ProfileProgression profileProgression;

    public MainMenu(PrintStream output, PersonaRegistry registry) {
        this.output = Objects.requireNonNull(output, "La salida no puede ser nula.");
        this.registry = Objects.requireNonNull(registry, "El registro no puede ser nulo.");
    }

    public void bindProfileProgression(ProfileProgression progression) { this.profileProgression = Objects.requireNonNull(progression); }

    public void display() {
        var presentation = profileProgression == null
                ? domain.metaprogression.MainMenuPresentation.portadorDeSuenosDefault()
                : domain.metaprogression.MainMenuPresentation.forMemorar(profileProgression);
        output.println();
        output.println(DIVIDER);
        output.println(center(TITLE, DIVIDER.length()));
        output.println(center(SUBTITLE, DIVIDER.length()));
        output.println(DIVIDER);
        output.println("FONDO: " + presentation.background());
        output.println("SOUNDTRACK: " + presentation.soundtrack());
        output.println();
        for (MainMenuOption option : MainMenuOption.values()) output.printf("%d. %s%n", option.code(), option.label());
        output.println();
    }

    private String center(String text, int width) {
        if (text.length() >= width) return text;
        int leftPadding = (width - text.length()) / 2;
        return " ".repeat(leftPadding) + text;
    }
}

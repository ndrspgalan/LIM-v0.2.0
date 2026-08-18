package bootstrap;

import application.GameApplication;
import application.start.CanonicalGameStartFactory;
import domain.character.progression.AttributeCapPolicy;
import domain.character.progression.CharacterClassDefinition;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.progression.MucusRequirementPolicy;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.persona.PersonaRegistry;
import presentation.console.ConsoleInput;
import presentation.menu.CharacterSheetScreen;
import presentation.menu.IncarnationMenu;
import presentation.menu.InventoryScreen;
import presentation.menu.MainMenu;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

/** Bootstrap de infraestructura. El estado canónico inicial vive en CanonicalGameStartFactory. */
public final class DemoBootstrap {
    private DemoBootstrap() {}

    public static GameApplication createApplication(InputStream inputStream, PrintStream output) {
        Objects.requireNonNull(inputStream, "La entrada no puede ser nula.");
        Objects.requireNonNull(output, "La salida no puede ser nula.");

        var start=CanonicalGameStartFactory.kenanChild();
        var closedDemoSave=start.game();
        var kenanProfile=start.persona();

        var saveRepository = new infrastructure.save.LocalJsonSaveRepository(java.nio.file.Path.of("saves"));
        PersonaRegistry personaRegistry = new PersonaRegistry(List.of(kenanProfile), saveRepository);
        GenderSoftcapProfile softcaps = GenderSoftcapProfile.canonical();
        AttributeCapPolicy capPolicy = new AttributeCapPolicy(softcaps, CharacterClassDefinition.canonicalDefinitions());
        MucusRequirementPolicy mucusPolicy = new MucusRequirementPolicy(softcaps);
        ConsoleInput input = new ConsoleInput(inputStream, output);
        DerivedStatisticsCalculator statisticsCalculator=new DerivedStatisticsCalculator();
        CharacterSheetScreen characterSheetScreen = new CharacterSheetScreen(
                closedDemoSave, capPolicy, mucusPolicy, statisticsCalculator, kenanProfile, input, output);

        return new GameApplication(
                closedDemoSave,
                new MainMenu(output, personaRegistry),
                new IncarnationMenu(personaRegistry, output),
                new InventoryScreen(closedDemoSave, input, output),
                characterSheetScreen,
                personaRegistry,
                input,
                output
        );
    }
}

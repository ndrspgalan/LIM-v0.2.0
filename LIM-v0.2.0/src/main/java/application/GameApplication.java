package application;

import domain.persona.PersonaProfile;
import domain.metaprogression.ProfileProgression;
import domain.persona.PersonaRegistry;
import domain.save.GameSessionState;
import domain.settings.ConfigurationContext;
import domain.settings.GameSettings;
import domain.settings.GameSettingsStore;
import presentation.console.ConsoleInput;
import presentation.console.GameplayConsole;
import presentation.menu.CharacterSheetScreen;
import presentation.menu.CharacterCreationScreen;
import presentation.menu.IncarnationMenu;
import presentation.menu.InventoryMenuOption;
import presentation.menu.InventoryScreen;
import presentation.menu.PersonaMenuScreen;
import presentation.loading.LoadingScreen;
import presentation.menu.MainMenu;
import presentation.menu.MainMenuOption;
import presentation.menu.MemorizeScreen;
import presentation.settings.ConfigurationScreen;

import java.io.PrintStream;
import java.util.Objects;

public final class GameApplication {
    private final GameSessionState savedGame;
    private final MainMenu mainMenu;
    private final IncarnationMenu incarnationMenu;
    private final InventoryScreen inventoryScreen;
    private final CharacterSheetScreen characterSheetScreen;
    private final ConsoleInput input;
    private final PrintStream output;
    private final InventoryAccessService inventoryAccessService;
    private final GameplayConsole gameplayConsole;
    private final ConfigurationScreen configurationScreen;
    private final CharacterCreationScreen characterCreationScreen;
    private final MemorizeScreen memorizeScreen;
    private final ProfileProgression profileProgression;
    private final PersonaRegistry personaRegistry;
    private final PersonaMenuScreen personaMenuScreen;
    private final LoadingScreen loadingScreen;

    public GameApplication(
            GameSessionState savedGame,
            MainMenu mainMenu,
            IncarnationMenu incarnationMenu,
            InventoryScreen inventoryScreen,
            CharacterSheetScreen characterSheetScreen,
            PersonaRegistry personaRegistry,
            ConsoleInput input,
            PrintStream output
    ) {
        this.savedGame = Objects.requireNonNull(savedGame, "La partida guardada no puede ser nula.");
        this.mainMenu = Objects.requireNonNull(mainMenu, "El menú principal no puede ser nulo.");
        this.incarnationMenu = Objects.requireNonNull(incarnationMenu, "El menú de encarnación no puede ser nulo.");
        this.inventoryScreen = Objects.requireNonNull(inventoryScreen, "La pantalla de inventario no puede ser nula.");
        this.characterSheetScreen = Objects.requireNonNull(characterSheetScreen, "La hoja no puede ser nula.");
        this.personaRegistry = Objects.requireNonNull(personaRegistry, "El registro de PERSONAS no puede ser nulo.");
        this.input = Objects.requireNonNull(input, "La entrada no puede ser nula.");
        this.output = Objects.requireNonNull(output, "La salida no puede ser nula.");
        this.inventoryAccessService = new InventoryAccessService(savedGame.hostileEncounterState());
        GameSettingsStore settingsStore = new GameSettingsStore();
        GameSettings settings = settingsStore.load();
        this.configurationScreen = new ConfigurationScreen(settings, settingsStore, input, output);
        this.characterCreationScreen = new CharacterCreationScreen(input, output);
        this.profileProgression = new ProfileProgression(personaRegistry);
        this.mainMenu.bindProfileProgression(this.profileProgression);
        this.memorizeScreen = new MemorizeScreen(input, output, profileProgression);
        this.loadingScreen = new LoadingScreen(input, output, profileProgression);
        this.personaMenuScreen = new PersonaMenuScreen(input, output, loadingScreen, personaRegistry);
        this.gameplayConsole = new GameplayConsole(savedGame, inventoryScreen, characterSheetScreen, input, output, settings, configurationScreen, personaRegistry.personas().get(0), new application.save.GameSaveService(personaRegistry.saveRepository()));
    }

    public void run() {
        boolean running = true;
        while (running) {
            mainMenu.display();
            MainMenuOption selectedOption = MainMenuOption.fromCode(
                    input.readIntegerBetween("Seleccione una opción: ", 1, 4)
            );
            switch (selectedOption) {
                case ENCARNAR -> openIncarnationMenu();
                case CONFIGURAR -> configurationScreen.open(ConfigurationContext.MAIN_MENU);
                case MEMORAR -> memorizeScreen.open();
                case ABANDONAR -> running = false;
            }
        }
        output.println();
        output.println("Gracias por haber formado parte de La Idea del Mundo. Hasta la próxima.");
    }

    private void openIncarnationMenu() {
        if (personaRegistry.personas().isEmpty()) {
            PersonaProfile created = characterCreationScreen.create(personaRegistry); personaRegistry.register(created); personaRegistry.markLastSaved(created);
            gameplayConsole.play(); return;
        }
        PersonaProfile kenan=personaRegistry.personas().get(0);
        if (!kenan.gameCompleted()) { gameplayConsole.play(); return; }
        personaMenuScreen.open(kenan);
    }

    private void openInventoryScreen() {
        InventoryAccessResult access = inventoryAccessService.requestAccess();
        if (!access.allowed()) {
            output.println(); output.println(access.message()); output.println();
            input.waitForEnter("Pulse Intro para volver..."); return;
        }
        InventoryMenuOption selectedOption = inventoryScreen.open();
        if (selectedOption == InventoryMenuOption.ACCEPT) {
            resumeGameSessionState();
            gameplayConsole.play();
            if (gameplayConsole.hasPendingDisplayChanges()) gameplayConsole.applyPendingDisplayChangesAtMainMenu();
        }
    }

    private void resumeGameSessionState() {
        savedGame.animationState().resumeRestLoop();
        savedGame.animationState().exitRest();
    }

    private void showUnavailableSection(String sectionName) {
        output.println();
        output.printf("%s aún no está disponible.%n", sectionName);
        output.println();
        input.waitForEnter("Pulse Intro para volver...");
    }
}

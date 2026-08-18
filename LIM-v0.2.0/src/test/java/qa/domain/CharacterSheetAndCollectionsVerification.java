package qa.domain;

import domain.character.CharacterDefinition;
import domain.character.CharacterIdentity;
import domain.character.CharacterTitle;
import domain.character.Gender;
import domain.character.progression.AttributeCapPolicy;
import domain.character.progression.CharacterClassDefinition;
import domain.character.progression.CharacterProgressionState;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.progression.MucusRequirementPolicy;
import domain.character.progression.MucusWallet;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.CurrentCharacterStats;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.persona.PersonaProfile;
import domain.save.GameSessionState;
import presentation.console.ConsoleInput;
import presentation.menu.CharacterSheetScreen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class CharacterSheetAndCollectionsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        PersonaProfile persona = new PersonaProfile("kenan", "Kenan", Gender.HOMBRE,
                domain.character.CharacterClass.INDOMITO, 198, List.of(), List.of());
        CharacterSheet sheet = CharacterSheet.of(27, 40, 12, 30, 20, 30, 3, 25, 11);
        CharacterProgressionState progression = new CharacterProgressionState(198, sheet, MucusWallet.of(0, 0, 0, 0, 0, 0));
        var inventory = domain.inventory.InventoryState.emptyWithoutPersonalTransport();
        DerivedStatisticsCalculator calculator = new DerivedStatisticsCalculator();
        CurrentCharacterStats stats = calculator.calculate(sheet, Gender.HOMBRE, inventory,
                domain.environment.time.DayPhase.DAY);
        GameSessionState save = new GameSessionState(new CharacterDefinition(CharacterIdentity.kenanCanonical()),
                new CharacterTitle("Guerrero de Ébano"), progression, stats, inventory);
        GenderSoftcapProfile softcaps = GenderSoftcapProfile.canonical();

        String first = renderSheet(save, persona, calculator, softcaps, "0\n");
        org.junit.jupiter.api.Assertions.assertTrue(first.contains("Explorar colección de maestrías"),
                "La colección de maestrías debe estar siempre disponible.");
        org.junit.jupiter.api.Assertions.assertTrue(!first.contains("Explorar Marcas Rúnicas"),
                "Las Marcas Rúnicas no deben aparecer antes de [VOLUNTAD MAYOR].");
        org.junit.jupiter.api.Assertions.assertTrue(!first.contains("TIEMPO REAL"), "La Hoja no debe mantener dos flujos visibles.");

        persona.unlockAllRunicMarks();
        String awakened = renderSheet(save, persona, calculator, softcaps, "0\n");
        org.junit.jupiter.api.Assertions.assertTrue(awakened.contains("Explorar Marcas Rúnicas"),
                "Las Marcas Rúnicas deben aparecer tras [VOLUNTAD MAYOR].");

        org.junit.jupiter.api.Assertions.assertTrue(!hasMethod("selectedMastery"), "Debe desaparecer la selección global antigua de familias.");
        org.junit.jupiter.api.Assertions.assertTrue(!hasMethod("next"), "Debe desaparecer el recorrido global siguiente.");
        org.junit.jupiter.api.Assertions.assertTrue(!hasMethod("previous"), "Debe desaparecer el recorrido global anterior.");
        org.junit.jupiter.api.Assertions.assertTrue(!hasMethod("activateSelected"), "Debe desaparecer la activación global antigua.");
        org.junit.jupiter.api.Assertions.assertTrue(!hasMethod("togglePairVersion"), "Debe desaparecer la conmutación global antigua por pares.");
    }

    private static String renderSheet(GameSessionState save, PersonaProfile persona,
                                      DerivedStatisticsCalculator calculator,
                                      GenderSoftcapProfile softcaps, String commands) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        CharacterSheetScreen screen = new CharacterSheetScreen(save,
                new AttributeCapPolicy(softcaps, CharacterClassDefinition.canonicalDefinitions()),
                new MucusRequirementPolicy(softcaps), calculator, persona,
                new ConsoleInput(new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)), output), output);
        screen.open();
        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static boolean hasMethod(String name) {
        return java.util.Arrays.stream(domain.ability.CharacterMasteryCollection.class.getDeclaredMethods())
                .anyMatch(method -> method.getName().equals(name));
    }

    
}

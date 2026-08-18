package qa.architecture;

import presentation.menu.CharacterSheetInspectionEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class DerivedStatisticsAndResistancesVerification {
    private static final List<String> DERIVED = List.of(
            "PV total", "PV REGEN", "Estabilidad física", "CORDURA",
            "PA total", "PA REGEN", "Carga"
    );
    private static final List<String> RESISTANCES = List.of(
            "Resistencia perforante", "Resistencia cortante", "Resistencia contundente",
            "Resistencia al veneno", "Resistencia a quemadura", "Resistencia a congelación",
            "Resistencia a electricidad", "Resistencia a maldición", "Resistencia a frenesí"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var entries = CharacterSheetInspectionEntry.canonicalEntries();
        for (String label : DERIVED) {
            requireNarrative(entries, label);
        }
        for (String label : RESISTANCES) {
            requireNarrative(entries, label);
        }
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().noneMatch(entry -> entry.label().equals("Arma principal")),
                "La Hoja todavía permite inspeccionar el arma de la mano derecha.");
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().noneMatch(entry -> entry.label().equals("Arma secundaria")),
                "La Hoja todavía permite inspeccionar el arma de la mano izquierda.");

        String screen = Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(!screen.contains("PERFILES DE LETALIDAD"), "La Hoja todavía presenta perfiles de letalidad.");
        org.junit.jupiter.api.Assertions.assertTrue(!screen.contains("EQUIPAMIENTO"), "La Hoja todavía presenta equipamiento activo.");
        org.junit.jupiter.api.Assertions.assertTrue(!screen.contains("displayEquipment"), "La Hoja todavía conserva el render de equipamiento.");
        org.junit.jupiter.api.Assertions.assertTrue(!screen.contains("displayWeaponLethality"), "La Hoja todavía conserva el render de armas.");
    }

    private static void requireNarrative(List<CharacterSheetInspectionEntry> entries, String label) {
        var entry = entries.stream().filter(candidate -> candidate.label().equals(label)).findFirst()
                .orElseThrow(() -> new AssertionError("Falta la entrada: " + label));
        String description = entry.description();
        org.junit.jupiter.api.Assertions.assertTrue(description != null && !description.isBlank(), "La entrada carece de descripción: " + label);
    }

    
}

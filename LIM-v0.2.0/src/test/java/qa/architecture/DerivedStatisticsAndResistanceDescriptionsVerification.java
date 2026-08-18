package qa.architecture;

import domain.character.Gender;
import domain.character.sheet.DerivedStatisticsCalculator;
import presentation.menu.CharacterSheetInspectionEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class DerivedStatisticsAndResistanceDescriptionsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var entries = CharacterSheetInspectionEntry.canonicalEntries();
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().anyMatch(e -> e.label().equals("PV REGEN") && e.description().contains("6 segundos en hombre") && e.description().contains("5 segundos en mujer")), "PV REGEN debe explicar cadencia sexual.");
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().anyMatch(e -> e.label().equals("CORDURA") && e.description().contains("StaggerPolicy") && e.description().contains("reduce el daño recibido")), "CORDURA debe explicar el pipeline mental.");
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().anyMatch(e -> e.label().equals("PA REGEN") && e.description().contains("1,20 segundos") && e.description().contains("5 segundos")), "PA REGEN debe explicar latencia y carga.");
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().noneMatch(e -> e.label().equals("Estabilidad mental") || e.label().equals("PV regenerados") || e.label().equals("PA regenerados")), "No deben sobrevivir etiquetas derivadas obsoleto.");

        String[] resistanceLabels = {
                "Resistencia perforante", "Resistencia cortante", "Resistencia contundente",
                "Resistencia al veneno", "Resistencia a quemadura", "Resistencia a congelación",
                "Resistencia a electricidad", "Resistencia a maldición", "Resistencia a frenesí"
        };
        for (String label : resistanceLabels) {
            var entry = entries.stream().filter(e -> e.label().equals(label)).findFirst().orElseThrow();
            org.junit.jupiter.api.Assertions.assertTrue(entry.description().contains("100 %") || entry.description().contains("techo del 100 %"), label + " debe explicitar el techo o compartirlo en su texto.");
            org.junit.jupiter.api.Assertions.assertTrue(!entry.description().contains("sus protecciones") && !entry.description().contains("su equipamiento"), label + " no debe atribuir la armadura a la resistencia intrínseca.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().filter(e -> e.label().equals("Resistencia a maldición")).findFirst().orElseThrow().description().contains("+0,25"), "Maldición femenina debe ser +0,25 pp/nivel.");
        org.junit.jupiter.api.Assertions.assertTrue(entries.stream().filter(e -> e.label().equals("Resistencia a electricidad")).findFirst().orElseThrow().description().contains("1 y 75 no desarrolla"), "Electricidad debe explicitar +0 ordinario.");

        var calc = new DerivedStatisticsCalculator();
        close(calc.resistanceProfileFromAdaptability(75, Gender.MUJER).curse().orElseThrow(), 18.75, "Maldición mujer 75");
        close(calc.resistanceProfileFromAdaptability(120, Gender.MUJER).curse().orElseThrow(), 50.25, "Maldición mujer 120");
        close(calc.resistanceProfileFromAdaptability(120, Gender.MUJER).electricity().orElseThrow(), 31.5, "Electricidad sólo CONFIGURATIO ORIGINALIS");

        String calculator = Files.readString(Path.of("src/main/java/domain/character/sheet/DerivedStatisticsCalculator.java"));
        org.junit.jupiter.api.Assertions.assertTrue(!calculator.contains("resistanceFromAdaptability(int adaptability)"), "Debe eliminarse el cálculo uniforme obsoleto sin consumidores.");
    }

    private static void close(double a, double b, String message) {
        if (Math.abs(a - b) > 1e-9) throw new IllegalStateException(message + ": " + a + " != " + b);
    }
    
}

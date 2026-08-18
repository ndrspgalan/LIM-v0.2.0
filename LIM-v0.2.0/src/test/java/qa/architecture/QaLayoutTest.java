package qa.architecture;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("architecture")
@Tag("gold-smoke")
final class QaLayoutTest {
    @Test
    void productionAndQaUseSeparateMavenSourceSets() {
        assertTrue(Files.isDirectory(Path.of("src/main/java")));
        assertTrue(Files.isDirectory(Path.of("src/test/java")));
        assertFalse(Files.exists(Path.of("src/verification")));
    }

    @Test
    void junitTestsDoNotExposeObsoletoMainEntrypoints() throws IOException {
        try (var sources = Files.walk(Path.of("src/test/java"))) {
            boolean obsoletoMain = sources
                    .filter(p -> p.toString().endsWith(".java"))
                    .anyMatch(QaLayoutTest::containsObsoletoMain);
            assertFalse(obsoletoMain, "El QA GOLD no debe ejecutarse mediante main() manuales.");
        }
    }

    private static boolean containsObsoletoMain(Path source) {
        try {
            return Files.readString(source).contains("static void " + "main");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

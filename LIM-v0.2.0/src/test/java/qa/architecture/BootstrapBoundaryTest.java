package qa.architecture;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Tag("architecture")
@Tag("gold-smoke")
final class BootstrapBoundaryTest {
    @Test
    void bootstrapOnlyWiresInfrastructureAndDoesNotOwnCanonicalDomainData() throws Exception {
        Path bootstrap = Path.of("src/main/java/bootstrap");
        try (var files = Files.list(bootstrap)) {
            var javaFiles = files.filter(p -> p.toString().endsWith(".java")).map(p -> p.getFileName().toString()).sorted().toList();
            assertEquals(java.util.List.of("DemoBootstrap.java"), javaFiles,
                    "bootstrap no debe contener catálogos/factorías de dominio paralelos.");
        }
        String source = Files.readString(bootstrap.resolve("DemoBootstrap.java"));
        assertTrue(source.contains("CanonicalGameStartFactory.kenanChild()"));
        assertFalse(source.contains("MucusWallet"));
        assertFalse(source.contains("CharacterSheet.of"));
        assertFalse(source.contains("InventoryState.empty"));
        assertFalse(source.contains("MasteryId."));
    }
}

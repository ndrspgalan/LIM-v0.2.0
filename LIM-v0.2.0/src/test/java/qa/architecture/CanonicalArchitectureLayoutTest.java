package qa.architecture;
import java.nio.file.*;
import org.junit.jupiter.api.Tag; import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
@Tag("architecture") @Tag("gold-smoke")
final class CanonicalArchitectureLayoutTest {
 @Test void removedObsoletoPackagesAndDualPersonaTypesDoNotExist(){
   for(String path: java.util.List.of(
     "src/main/java/domain/persona/NarrativeOrigin.java","src/main/java/domain/persona/OriginPreset.java","src/main/java/domain/persona/OriginStoryArc.java",
     "src/main/java/domain/knowledge/HiddenKnowledgePolicy.java","src/main/java/domain/knowledge/ClairvoyanceKnowledgePolicy.java",
     "src/main/java/domain/loading","src/main/java/application/loading","src/main/java/domain/bestiarium/feare","src/main/java/domain/bestiarium/humanus_dormiens",
     "src/main/java/domain/bestiarium/interstice/npc","src/main/java/domain/bestiarium/mythologicae","src/main/java/domain/bestiarium/physical_plane/mythologicae",
     "src/main/java/domain/bestiarium/interstice/faerie/package-info.java","src/main/java/domain/bestiarium/physical_plane/npc/package-info.java"))
       assertFalse(Files.exists(Path.of(path)),path);
 }
}

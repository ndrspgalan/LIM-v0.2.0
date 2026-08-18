package qa.integration;
import domain.character.KenanCanonicalProfile; import domain.metaprogression.ProfileProgression; import domain.persona.*; import domain.save.*; import presentation.console.ConsoleInput; import presentation.loading.LoadingScreen;
import java.io.*; import java.nio.charset.StandardCharsets; import java.util.List;
import org.junit.jupiter.api.Tag; import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
@Tag("integration") @Tag("gold-smoke")
final class LoadingPresentationPolicyTest {
 @Test void loadingUsesMainMenuWallpaperAndNeverPlaysOstOrTruthCatalog(){
  var persona=new PersonaProfile("kenan",KenanCanonicalProfile.NAME,KenanCanonicalProfile.INITIAL_LEVEL,List.of(),List.of());
  var progression=new ProfileProgression(new PersonaRegistry(List.of(persona)));
  var slot=new SaveSlot("kenan-q","Punto","",SaveKind.QUICKSAVE);
  var bytes=new ByteArrayOutputStream(); var out=new PrintStream(bytes,true,StandardCharsets.UTF_8);
  new LoadingScreen(new ConsoleInput(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8)),out),out,progression).openFor(persona,slot);
  String text=bytes.toString(StandardCharsets.UTF_8);
  assertTrue(text.contains(progression.effectiveMainMenuPoster().label())); assertTrue(text.contains("OST: SILENCIADO"));
  assertFalse(text.contains("VERDADES DE LA IDEA DEL MUNDO")); assertFalse(text.contains("PÓSTER:"));
 }
}

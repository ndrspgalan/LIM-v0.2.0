package qa.domain;

import domain.character.KenanCanonicalProfile;
import domain.metaprogression.ProfileProgression;
import domain.persona.PersonaProfile;
import domain.persona.PersonaRegistry;
import domain.save.*;
import presentation.console.ConsoleInput;
import presentation.loading.LoadingScreen;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Contrato histórico de loading absorbido por la política GOLD . */
public final class MenuLoadingContractVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        PersonaProfile persona=new PersonaProfile("kenan",KenanCanonicalProfile.NAME,KenanCanonicalProfile.INITIAL_LEVEL,List.of(),List.of());
        PersonaRegistry registry=new PersonaRegistry(List.of(persona));
        ProfileProgression progression=new ProfileProgression(registry);
        SaveSlot slot=new SaveSlot("kenan-test","Último punto","",SaveKind.QUICKSAVE);
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        PrintStream out=new PrintStream(bytes,true,StandardCharsets.UTF_8);
        LoadingScreen screen=new LoadingScreen(new ConsoleInput(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8)),out),out,progression);
        screen.openFor(persona,slot);
        String rendered=bytes.toString(StandardCharsets.UTF_8);
        org.junit.jupiter.api.Assertions.assertTrue(rendered.contains(progression.effectiveMainMenuPoster().label()),"Loading reutiliza el mismo live wallpaper que el menú.");
        org.junit.jupiter.api.Assertions.assertTrue(rendered.contains("OST: SILENCIADO"),"El OST no debe sonar durante la carga.");
        org.junit.jupiter.api.Assertions.assertTrue(!rendered.contains("VERDADES DE LA IDEA DEL MUNDO"),"Las verdades antiguas no pertenecen ya a loading.");
    }
}

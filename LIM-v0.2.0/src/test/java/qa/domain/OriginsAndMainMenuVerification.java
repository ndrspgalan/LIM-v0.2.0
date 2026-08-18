package qa.domain;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.KenanCanonicalProfile;
import domain.metaprogression.ProfileProgression;
import domain.metaprogression.MainMenuPresentation;
import domain.persona.PersonaProfile;
import domain.persona.PersonaRegistry;
import java.util.List;

/** Contrato histórico absorbido: GOLD conserva una única PERSONA jugable, Kenan. */
public final class OriginsAndMainMenuVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        PersonaProfile kenan=new PersonaProfile("k",KenanCanonicalProfile.NAME,Gender.HOMBRE,CharacterClass.INDOMITO,KenanCanonicalProfile.INITIAL_LEVEL,List.of(),List.of());
        PersonaRegistry registry=new PersonaRegistry(List.of()); registry.register(kenan);
        org.junit.jupiter.api.Assertions.assertTrue(registry.personas().size()==1,"Sólo Kenan puede ser PERSONA jugable.");
        org.junit.jupiter.api.Assertions.assertTrue(registry.personas().get(0).characterClass()==CharacterClass.INDOMITO,"Kenan conserva Indómito.");
        MainMenuPresentation presentation=MainMenuPresentation.forMemorar(new ProfileProgression(registry));
        org.junit.jupiter.api.Assertions.assertTrue(presentation.background().contains("Portador de Sueños"),"El Portador de Sueños es el fallback de live wallpaper.");
        org.junit.jupiter.api.Assertions.assertTrue(presentation.soundtrack().contains("Portador de Sueños"),"El OST fallback debe acompañar al menú principal.");
    }
}

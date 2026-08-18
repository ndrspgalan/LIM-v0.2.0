package domain.metaprogression;

/** Proyección audiovisual seleccionada en MEMORAR para el menú principal. */
public record MainMenuPresentation(String background,String soundtrack) {
    public static MainMenuPresentation forMemorar(ProfileProgression progression){
        var poster=progression.effectiveMainMenuPoster();
        var soundtrack=progression.effectiveMainMenuSoundtrack();
        return new MainMenuPresentation(poster.label(),soundtrack.label());
    }
    public static MainMenuPresentation portadorDeSuenosDefault(){return new MainMenuPresentation("Live wallpaper — El Portador de Sueños","OST — El Portador de Sueños");}
    public static MainMenuPresentation neutral(){return portadorDeSuenosDefault();}
}

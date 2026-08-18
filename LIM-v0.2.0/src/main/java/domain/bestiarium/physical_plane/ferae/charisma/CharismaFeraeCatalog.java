package domain.bestiarium.physical_plane.ferae.charisma;
import domain.bestiarium.*;
import domain.bestiarium.physical_plane.ferae.*;
import java.util.List;
public final class CharismaFeraeCatalog {
    private CharismaFeraeCatalog() {}
    public static List<BestiaryDescriptor> canonical() {
        return FeraeCatalog.branch(FeraeBranch.CARISMA).stream()
                .map(s -> new BestiaryDescriptor(s.label(), ExistencePlane.PHYSICAL_PLANE)).toList();
    }
    public static List<FeraeProfile> profiles(){ return CharismaFeraeProfiles.all(); }
}

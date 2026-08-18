package domain.bestiarium.physical_plane.ferae.intelligence;
import domain.bestiarium.*;
import java.util.List;
public final class IntelligenceFeraeCatalog {
    private IntelligenceFeraeCatalog() {}
    public static List<BestiaryDescriptor> canonical() {
        return List.of("Rata","Cuervo","Lobo","Lince","Águila","Jabalí","Oso","Ciervo","Toro","Caballo de Paseo","Caballo de Carreras","Caballo de Tiro","Cerdo","Armadillo","León","Rinoceronte","Serpiente")
                .stream().map(s -> new BestiaryDescriptor(s, ExistencePlane.PHYSICAL_PLANE)).toList();
    }
}

package domain.bestiarium.physical_plane.ferae.equine;

import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.inventory.logistics.PersonalTransportType;
import java.util.Objects;

/**  — vínculo 1:1 entre los tres transportes ecuestres y sus Ferae macho/hembra. */
public enum EquineMountVariant {
    PASEO(PersonalTransportType.HORSE_LEISURE,FeraeSpecies.CABALLO_PASEO,FeraeSpecies.YEGUA_PASEO),
    CARRERAS(PersonalTransportType.HORSE_RACING,FeraeSpecies.CABALLO_CARRERAS,FeraeSpecies.YEGUA_CARRERAS),
    TIRO(PersonalTransportType.HORSE_DRAFT,FeraeSpecies.CABALLO_TIRO,FeraeSpecies.YEGUA_TIRO);
    private final PersonalTransportType transport; private final FeraeSpecies stallion; private final FeraeSpecies mare;
    EquineMountVariant(PersonalTransportType transport,FeraeSpecies stallion,FeraeSpecies mare){this.transport=transport;this.stallion=stallion;this.mare=mare;}
    public PersonalTransportType transport(){return transport;} public FeraeSpecies stallion(){return stallion;} public FeraeSpecies mare(){return mare;}
    public static EquineMountVariant of(PersonalTransportType t){Objects.requireNonNull(t);for(var v:values())if(v.transport==t)return v;throw new IllegalArgumentException("No es transporte ecuestre: "+t);}
}

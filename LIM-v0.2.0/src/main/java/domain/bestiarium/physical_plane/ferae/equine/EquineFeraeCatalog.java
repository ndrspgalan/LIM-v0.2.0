package domain.bestiarium.physical_plane.ferae.equine;

import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.charisma.CharismaFeraeProfiles;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import java.util.*;

/** Autoridad canónica  de las tres variedades ecuestres y sus yeguas. */
public final class EquineFeraeCatalog {
 private EquineFeraeCatalog(){}
 public static FeraeProfile stallion(EquineMountVariant v){return IntelligenceFeraeProfiles.of(v.stallion(),FeraeSex.MACHO);}
 public static FeraeProfile mare(EquineMountVariant v){return CharismaFeraeProfiles.of(v.mare(),FeraeSex.HEMBRA);}
 public static List<FeraeProfile> canonical(){var out=new ArrayList<FeraeProfile>();for(var v:EquineMountVariant.values()){out.add(stallion(v));out.add(mare(v));}return List.copyOf(out);}
 public static FeraeProfile profile(FeraeSpecies species){return canonical().stream().filter(p->p.species()==species).findFirst().orElseThrow(()->new IllegalArgumentException("No es una variedad ecuestre : "+species));}
}

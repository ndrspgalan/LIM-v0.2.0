package domain.bestiarium.physical_plane.ferae;
import java.util.*;
public final class FeraeCatalog {
 private FeraeCatalog(){} public static List<FeraeSpecies> all(){return List.of(FeraeSpecies.values());}
 public static List<FeraeSpecies> branch(FeraeBranch branch){return all().stream().filter(s->s.branch()==branch).toList();}
 public static Set<HuntingTrophy> canonicalTrophies(){return EnumSet.allOf(HuntingTrophy.class);}
}

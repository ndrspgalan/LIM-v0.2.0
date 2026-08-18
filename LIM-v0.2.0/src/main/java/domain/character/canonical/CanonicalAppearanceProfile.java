package domain.character.canonical;
import java.util.Objects;
public record CanonicalAppearanceProfile(String personality,String face,String hair,String eyes,String facialHair,String physique,String presence){
 public CanonicalAppearanceProfile{Objects.requireNonNull(personality);Objects.requireNonNull(face);Objects.requireNonNull(hair);Objects.requireNonNull(eyes);Objects.requireNonNull(facialHair);Objects.requireNonNull(physique);Objects.requireNonNull(presence);}
}

package domain.character.canonical;
public record CanonicalBodyProfile(double heightMeters,double weightKilograms,double wristCentimeters,String build){
 public CanonicalBodyProfile{if(heightMeters<=0||weightKilograms<=0||wristCentimeters<=0)throw new IllegalArgumentException("Antropometría inválida");}
}

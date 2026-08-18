package domain.character;

import domain.character.sheet.CharacterSheet;
import domain.social.Profession;

/** autoridad del estado canónico de Kenan al comenzar LIM, a los seis años. */
public final class KenanCanonicalProfile {
    public static final String NAME="Kenan";
    public static final int AGE_YEARS=6;
    public static final double HEIGHT_METERS=1.16;
    public static final double WEIGHT_KILOGRAMS=20.5;
    public static final double WRIST_CENTIMETERS=12.0;
    public static final int INITIAL_LEVEL=9;
    public static final Gender GENDER=Gender.HOMBRE;
    public static final CharacterClass CHARACTER_CLASS=CharacterClass.INDOMITO;
    /** Compatibilidad de CharacterIdentity: CHILD no posee oficio; BEGGAR es sólo el sentinel legado de ausencia profesional. */
    public static final Profession PROFESSION=Profession.BEGGAR;
    private KenanCanonicalProfile(){}
    public static CharacterSheet initialSheet(){CharacterSheet s=CharacterSheet.of(1,1,1,1,1,1,1,1,1);if(s.totalAttributeLevel()!=INITIAL_LEVEL)throw new IllegalStateException("Kenan CHILD debe comenzar en nivel 9.");return s;}
    public static CharacterIdentity identity(){return new CharacterIdentity(NAME,GENDER,CHARACTER_CLASS,PROFESSION,HEIGHT_METERS);}
    public static java.util.List<domain.ability.MasteryId> childForcedMasteries(){return java.util.List.of(domain.ability.MasteryId.REGENERACION_THETA,domain.ability.MasteryId.ESPIRITU_INFATIGABLE);}
}

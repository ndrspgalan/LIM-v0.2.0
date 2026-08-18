package domain.bestiarium.physical_plane.ancient;

import domain.character.CharacterClass;
import domain.character.sheet.CharacterSheet;

/**
 * Construcción canónica ANCIENT. VITALIDAD 120 es invariante; ADAPTABILIDAD es 93 salvo Anteo (INDÓMITO), que alcanza 119.
 * Los restantes atributos usan el máximo ordinario de su sexo y 75 en la afinidad de clase.
 * Tiresias, hermafrodita, toma el mayor límite humano de ambos sexos en cada atributo.
 */
public final class AncientAttributePolicy {
    public static final int VITALITY = 120;
    public static final int ADAPTABILITY = 93;
    public static final int INDOMITO_ADAPTABILITY = 119;
    private AncientAttributePolicy() {}

    public static CharacterSheet sheet(AncientArchetype a) {
        int endurance = switch(a.sex()) { case HOMBRE -> 40; case MUJER -> 30; case HERMAFRODITA -> 40; };
        int strength = switch(a.sex()) { case HOMBRE -> 50; case MUJER -> 30; case HERMAFRODITA -> 50; };
        int dexterity = 70;
        int intelligence = 70;
        int faith = 60;
        int charisma = switch(a.sex()) { case HOMBRE -> 50; case MUJER -> 40; case HERMAFRODITA -> 50; };
        int clairvoyance = 75;
        CharacterClass c=a.characterClass();
        int adaptability = c==CharacterClass.INDOMITO ? INDOMITO_ADAPTABILITY : ADAPTABILITY;
        if(c==CharacterClass.INDOMITO) endurance=75;
        if(c==CharacterClass.LUCHADOR) strength=75;
        if(c==CharacterClass.ESPECIALISTA) dexterity=75;
        if(c==CharacterClass.INTELECTUAL) intelligence=75;
        if(c==CharacterClass.APODERADO) faith=75;
        if(c==CharacterClass.HERALDO) charisma=75;
        if(c==CharacterClass.MAESTRO) clairvoyance=75;
        return CharacterSheet.of(VITALITY,endurance,adaptability,strength,dexterity,intelligence,faith,charisma,clairvoyance);
    }
}

package domain.bestiarium.physical_plane.ancient;

import domain.character.CharacterClass;
import domain.runic.RunicMarkId;

import java.util.Objects;

/** Los siete ANCIENT canónicos. El nombre es antropónimo; el concepto no prescribe personalidad. */
public enum AncientArchetype {
    ALCIDES("Alcides", CharacterClass.LUCHADOR, AncientSex.HOMBRE, "Imposición",
            "Su afinidad con FUERZA culmina en la idea de imposición: cuando dos configuraciones materiales compiten por el mismo espacio, una obliga a la otra a ceder. No implica tiranía ni temperamento dominante; describe el principio que su existencia corporal ha llevado al extremo.", RunicMarkId.RESONANCIA),
    METIS("Metis", CharacterClass.INTELECTUAL, AncientSex.HOMBRE, "Recursión",
            "Su afinidad con INTELIGENCIA culmina en la recursión. La rumiación neuronal extrema permite que una representación se convierta en objeto de otra representación y vuelva a examinarse desde capas sucesivas sin que la cadena necesite cerrarse. Su personalidad puede ser serena, obsesiva o pragmática: la recursión describe su arquitectura mental, no su carácter.", RunicMarkId.ESPEJO),
    ANTEO("Anteo", CharacterClass.INDOMITO, AncientSex.HOMBRE, "Persistencia por contracción",
            "Su afinidad con AGUANTE culmina en una persistencia biomecánica majestuosa. Su CAMBIAFORMAS mantiene una anatomía antropomórfica perfecta y reconocible, revestida por una piel de silicio pulido cuya trama evoca un árbol mineral. Endurece regiones del cuerpo de forma instantánea y combina esa rigidez con una elasticidad tendinosa extrema para almacenar y liberar inercia al correr, fintar y golpear sin abandonar nunca una presencia regia, proporcionada y no grotesca.", RunicMarkId.PARHELIO),
    DEXIA("Dexia", CharacterClass.ESPECIALISTA, AncientSex.MUJER, "Continuidad cinemática",
            "Su afinidad con DESTREZA culmina en la continuidad cinemática: la relación entre postura, trayectoria y apoyo deja de organizarse en segmentos corporales intuitivos. La idea no es velocidad pura, sino que la transición entre dos configuraciones puede resultar ilegible para un observador que espera articulaciones y secuencias humanas.", RunicMarkId.SILENCIO),
    ELPIS("Elpis", CharacterClass.APODERADO, AncientSex.MUJER, "Convicción",
            "Su afinidad con FE culmina en la convicción entendida como estabilidad de una representación interior. No encarna religión ni bondad: encarna la capacidad de conservar una definición interna incluso cuando el entorno insiste en proponer otra.", RunicMarkId.VOTO_VINCULANTE),
    AGLAIA("Aglaia", CharacterClass.HERALDO, AncientSex.MUJER, "Centralidad",
            "Su afinidad con CARISMA culmina en la centralidad. La atención de quienes comparten un espacio tiende a reorganizarse alrededor de su presencia sin que esa centralidad obligue a Aglaia a ser extrovertida, amable ni seductora. Es una propiedad relacional, no una personalidad.", RunicMarkId.TRANSPOSICION),
    TIRESIAS("Tiresias", CharacterClass.MAESTRO, AncientSex.HERMAFRODITA, "Perspectiva",
            "Su afinidad con CLARIVIDENCIA culmina en la perspectiva: sostener simultáneamente marcos de observación que para una mente ordinaria serían incompatibles. Su hermafroditismo forma parte de su biología canónica, pero no reduce el concepto a sexo o dualidad; la cuestión central es que ninguna orientación única agota aquello que observa.", RunicMarkId.ROSA_DE_LOS_VIENTOS);

    private final String displayName;
    private final CharacterClass characterClass;
    private final AncientSex sex;
    private final String concept;
    private final String narrative;
    private final RunicMarkId originalRunicMark;

    AncientArchetype(String displayName, CharacterClass characterClass, AncientSex sex, String concept,
                     String narrative, RunicMarkId originalRunicMark) {
        this.displayName = requireText(displayName);
        this.characterClass = Objects.requireNonNull(characterClass);
        this.sex = Objects.requireNonNull(sex);
        this.concept = requireText(concept);
        this.narrative = requireText(narrative);
        this.originalRunicMark = Objects.requireNonNull(originalRunicMark);
    }
    public String displayName(){ return displayName; }
    public CharacterClass characterClass(){ return characterClass; }
    public AncientSex sex(){ return sex; }
    public String concept(){ return concept; }
    public String narrative(){ return narrative; }
    public RunicMarkId originalRunicMark(){ return originalRunicMark; }
    private static String requireText(String s){ Objects.requireNonNull(s); String n=s.trim(); if(n.isEmpty())throw new IllegalArgumentException("Texto vacío."); return n; }
}

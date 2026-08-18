package domain.character.progression;

/** Tipo de actor usado para aplicar el techo canónico 76..120 de VITALIDAD/ADAPTABILIDAD. */
public enum AttributeActorScope {
    KENAN, CANONICAL_NPC, ASPIRANT, ANCIENT, PROCEDURAL_SUBPROFESSION_NPC, FERAE, OTHER;
    public boolean allowsExtendedVitalityAdaptability() {
        return this == KENAN || this == CANONICAL_NPC || this == ASPIRANT || this == ANCIENT;
    }
}

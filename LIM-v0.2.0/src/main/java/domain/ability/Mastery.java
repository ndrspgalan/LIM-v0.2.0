package domain.ability;

import domain.character.CharacterClass;

public sealed interface Mastery permits PairMastery, TransmutationMastery, StructuredMastery, EvolutiveMastery {
    MasteryId id();
    String name();
    MasteryStructure structure();
    String narrativeDescription();
    CharacterClass resonanceClass();
    default MasteryCategory category() { return MasteryCategory.CLASS_SPECIALIZED; }
}

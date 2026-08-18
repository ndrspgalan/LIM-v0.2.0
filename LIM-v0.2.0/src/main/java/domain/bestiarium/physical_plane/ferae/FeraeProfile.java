package domain.bestiarium.physical_plane.ferae;

import domain.bestiarium.BestiaryTaxon;
import domain.character.sheet.CharacterSheet;
import domain.character.progression.AttributeActorCapPolicy;
import domain.character.progression.AttributeActorScope;
import java.util.Objects;
import java.util.Optional;

/** Individuo canónico Ferae: sexo, atributos, nivel derivado e inventario saqueable. */
public record FeraeProfile(
        FeraeSpecies species,
        FeraeSex sex,
        CharacterSheet attributes,
        String genericNarrative,
        String sexNarrative
) {
    public FeraeProfile {
        Objects.requireNonNull(species);
        Objects.requireNonNull(sex);
        Objects.requireNonNull(attributes);
        AttributeActorCapPolicy.requireValid(AttributeActorScope.FERAE, attributes);
        if(genericNarrative==null || genericNarrative.isBlank() || sexNarrative==null || sexNarrative.isBlank())
            throw new IllegalArgumentException("Toda Ferae necesita descripción genérica y sexual.");
    }

    public BestiaryTaxon taxon(){ return BestiaryTaxon.FERAE; }
    /** En LIM el nivel es exactamente la suma de los nueve atributos. */
    public int canonicalLevel(){ return attributes.totalAttributeLevel(); }
    public double canonicalHeightMeters(){ return FeraeMorphologyCatalog.canonicalHeightMeters(species,sex); }

    /** Sólo el macho puede portar el trofeo canónico definido para su especie. */
    public Optional<HuntingTrophy> equippedTrophy(){
        return sex==FeraeSex.MACHO ? species.trophy() : Optional.empty();
    }
}

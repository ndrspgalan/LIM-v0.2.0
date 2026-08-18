package domain.ability;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;
import java.util.Optional;

public record TransmutationNode(
        TransmutationNodeId id, String name, MasteryType type, Attribute requirementAttribute, int requirementMinimum,
        TransmutationNodeId prerequisite, boolean narrativeUnlock, String narrativeDescription, String mechanicalDescription
) {
    public TransmutationNode {
        Objects.requireNonNull(id); name=requireText(name); Objects.requireNonNull(type);
        narrativeDescription=requireText(narrativeDescription); mechanicalDescription=requireText(mechanicalDescription);
        if(requirementAttribute==null&&requirementMinimum!=0) throw new IllegalArgumentException("Un nodo sin atributo debe usar umbral cero.");
        if(requirementAttribute!=null&&(requirementMinimum<1||requirementMinimum>120)) throw new IllegalArgumentException("El requisito debe estar entre 1 y 120.");
    }
    public boolean meetsAttributeRequirement(CharacterSheet sheet){Objects.requireNonNull(sheet);return requirementAttribute==null||sheet.valueOf(requirementAttribute)>=requirementMinimum;}
    public Optional<Attribute> requirementAttributeOptional(){return Optional.ofNullable(requirementAttribute);}
    public Optional<TransmutationNodeId> prerequisiteOptional(){return Optional.ofNullable(prerequisite);}
    private static String requireText(String v){Objects.requireNonNull(v);String n=v.trim();if(n.isEmpty())throw new IllegalArgumentException("El texto no puede estar vacío.");return n;}
}

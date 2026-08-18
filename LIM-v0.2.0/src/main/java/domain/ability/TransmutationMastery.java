package domain.ability;

import domain.character.CharacterClass;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class TransmutationMastery implements Mastery {
    private final MasteryId id;
    private final String name;
    private final String narrativeDescription;
    private final CharacterClass resonanceClass;
    private final List<TransmutationNode> orderedNodes;
    private final Map<TransmutationNodeId, TransmutationNode> nodesById;

    public TransmutationMastery(MasteryId id, String name, String narrativeDescription, CharacterClass resonanceClass, List<TransmutationNode> orderedNodes) {
        this.id = Objects.requireNonNull(id, "El identificador no puede ser nulo.");
        this.name = requireText(name);
        this.narrativeDescription = requireText(narrativeDescription);
        this.resonanceClass = Objects.requireNonNull(resonanceClass, "La clase de resonancia no puede ser nula.");
        Objects.requireNonNull(orderedNodes, "Los nodos no pueden ser nulos.");
        if (orderedNodes.isEmpty() || orderedNodes.get(0).id() != TransmutationNodeId.OVERCLOCK) {
            throw new IllegalArgumentException("El árbol de Transmutación debe comenzar en OVERCLOCK.");
        }
        EnumMap<TransmutationNodeId, TransmutationNode> index = new EnumMap<>(TransmutationNodeId.class);
        for (TransmutationNode node : orderedNodes) {
            Objects.requireNonNull(node, "Un nodo no puede ser nulo.");
            if (index.put(node.id(), node) != null) throw new IllegalArgumentException("Nodo duplicado: " + node.id());
        }
        this.orderedNodes = List.copyOf(orderedNodes);
        this.nodesById = Map.copyOf(index);
    }

    @Override public MasteryId id() { return id; }
    @Override public String name() { return name; }
    @Override public String narrativeDescription() { return narrativeDescription; }
    @Override public MasteryStructure structure() { return MasteryStructure.BRANCHED; }
    @Override public CharacterClass resonanceClass() { return resonanceClass; }
    public List<TransmutationNode> orderedNodes() { return orderedNodes; }
    public TransmutationNode node(TransmutationNodeId id) {
        TransmutationNode node = nodesById.get(Objects.requireNonNull(id));
        if (node == null) throw new IllegalArgumentException("Nodo inexistente: " + id);
        return node;
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "El nombre no puede ser nulo.");
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        return normalized;
    }
}

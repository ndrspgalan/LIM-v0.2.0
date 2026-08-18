package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.relation.WorldMemoryRelation;
import domain.worldmemory.relation.WorldMemoryRelationDirection;
import domain.worldmemory.relation.WorldMemoryRelationType;
import domain.worldmemory.ui.WorldMemoryEntryViewAssembler;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryRelationsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        var source = new WorldKnowledgeSource(KnowledgeSourceType.CONVERSATION, "Jacob",
                Instant.parse("2026-01-01T00:00:00Z"), KnowledgeReliability.PLAUSIBLE);
        var jacob = entry("jacob", WorldMemoryCategory.PEOPLE, "Jacob", source);
        var sofia = entry("sofia", WorldMemoryCategory.PEOPLE, "Sofía", source);
        var sanctuary = entry("sanctuary", WorldMemoryCategory.PLACES, "Santuario Blanco", source);
        memory.knowledge().rememberEntry(jacob);
        memory.knowledge().rememberEntry(sofia);
        memory.knowledge().rememberEntry(sanctuary);

        memory.knowledge().rememberRelation(new WorldMemoryRelation(jacob.id(),
                WorldMemoryRelationType.TRADES_WITH, sofia.id(), "Intercambian suministros."));
        memory.knowledge().rememberRelation(new WorldMemoryRelation(jacob.id(),
                WorldMemoryRelationType.MENTIONED, sanctuary.id()));

        var assembler = new WorldMemoryEntryViewAssembler(memory.knowledge());
        var jacobView = assembler.assemble(jacob);
        org.junit.jupiter.api.Assertions.assertTrue(jacobView.relations().size() == 2, "Jacob debe exponer sus dos relaciones adquiridas.");
        org.junit.jupiter.api.Assertions.assertTrue(jacobView.relations().stream().allMatch(r -> r.direction() == WorldMemoryRelationDirection.OUTGOING),
                "Las aristas de Jacob deben proyectarse como salientes.");

        var sofiaView = assembler.assemble(sofia);
        org.junit.jupiter.api.Assertions.assertTrue(sofiaView.relations().size() == 1, "Sofía debe consultar la misma arista sin duplicarla.");
        org.junit.jupiter.api.Assertions.assertTrue(sofiaView.relations().get(0).direction() == WorldMemoryRelationDirection.INCOMING,
                "La relación vista desde Sofía debe ser entrante.");
        org.junit.jupiter.api.Assertions.assertTrue(sofiaView.relations().get(0).label().equals("Comercia con"),
                "Una relación simétrica debe conservar una etiqueta natural en ambos extremos.");

        boolean danglingRejected = false;
        try {
            memory.knowledge().rememberRelation(new WorldMemoryRelation(jacob.id(),
                    WorldMemoryRelationType.KNOWS, new WorldMemoryEntryId("desconocido")));
        } catch (IllegalArgumentException expected) {
            danglingRejected = true;
        }
        org.junit.jupiter.api.Assertions.assertTrue(danglingRejected, "No deben persistirse relaciones hacia conocimiento no adquirido.");

        memory.knowledge().rememberRelation(new WorldMemoryRelation(jacob.id(),
                WorldMemoryRelationType.MENTIONED, sanctuary.id()));
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().relations().size() == 2, "Las relaciones idénticas deben ser idempotentes.");
    }

    private static WorldMemoryEntry entry(String id, WorldMemoryCategory category, String title,
                                          WorldKnowledgeSource source) {
        return new WorldMemoryEntry(new WorldMemoryEntryId(id), category, title, "", List.of(source), Optional.empty());
    }

    
}

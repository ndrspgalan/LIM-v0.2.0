package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.history.WorldMemoryRevisionType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryHistoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        WorldMemoryEntryId id = new WorldMemoryEntryId("torre-velada");
        WorldKnowledgeSource rumor = source(KnowledgeSourceType.CONVERSATION, "Mercader", "2026-07-20T10:00:00Z",
                KnowledgeReliability.RUMOR);
        WorldKnowledgeSource observation = source(KnowledgeSourceType.OBSERVATION, "Kenan", "2026-07-21T12:00:00Z",
                KnowledgeReliability.VERIFIED);

        WorldMemoryEntry initial = new WorldMemoryEntry(id, WorldMemoryCategory.PLACES, "Torre Velada", "",
                List.of(rumor), Optional.empty());
        memory.knowledge().rememberEntry(initial);
        memory.knowledge().rememberEntry(initial);

        WorldMemoryEntry revised = new WorldMemoryEntry(id, WorldMemoryCategory.PLACES, "Torre Velada",
                "Una torre de piedra visible desde el paso septentrional.", List.of(rumor, observation), Optional.empty());
        memory.knowledge().rememberEntry(revised);

        var history = memory.knowledge().historyOf(id);
        org.junit.jupiter.api.Assertions.assertTrue(history.size() == 2, "Una repetición idéntica no debe crear una revisión adicional.");
        org.junit.jupiter.api.Assertions.assertTrue(history.get(0).type() == WorldMemoryRevisionType.ACQUISITION,
                "La primera versión debe registrarse como adquisición.");
        org.junit.jupiter.api.Assertions.assertTrue(history.get(1).type() == WorldMemoryRevisionType.RELIABILITY_CHANGE,
                "La evidencia verificada debe registrar el cambio de fiabilidad.");
        org.junit.jupiter.api.Assertions.assertTrue(history.get(0).snapshot().description().isBlank(),
                "La instantánea inicial debe conservar el estado histórico original.");
        org.junit.jupiter.api.Assertions.assertTrue(history.get(1).snapshot().description().contains("paso septentrional"),
                "La revisión debe conservar la nueva descripción.");
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().entry(id).orElseThrow().equals(revised),
                "El acceso vigente debe resolverse sin recorrer ni sustituir el historial.");

        boolean rejected = false;
        try {
            memory.knowledge().historyOf(new WorldMemoryEntryId("desconocida"));
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "No debe poder consultarse el historial de conocimiento no adquirido.");
    }

    private static WorldKnowledgeSource source(KnowledgeSourceType type, String reference, String acquiredAt,
                                                KnowledgeReliability reliability) {
        return new WorldKnowledgeSource(type, reference, Instant.parse(acquiredAt), reliability);
    }

    
}

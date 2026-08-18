package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.query.WorldMemoryQuery;
import domain.worldmemory.search.WorldMemorySearch;
import domain.worldmemory.search.WorldMemorySearchResult;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemorySearchVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        WorldKnowledgeSource source = new WorldKnowledgeSource(
                KnowledgeSourceType.OBSERVATION, "Prueba", Instant.parse("2026-07-28T00:00:00Z"),
                KnowledgeReliability.VERIFIED);

        remember(memory, "kenan", WorldMemoryCategory.PEOPLE, "Kenan", source);
        remember(memory, "santuario", WorldMemoryCategory.PLACES, "Santuario Blanco", source);
        remember(memory, "sofia", WorldMemoryCategory.PEOPLE, "Sofía", source);
        remember(memory, "orden", WorldMemoryCategory.INSTITUTIONS_AND_FACTIONS, "Orden de Sofía", source);

        WorldMemorySearch search = new WorldMemorySearch(memory.knowledge());
        var exact = search.search(new WorldMemoryQuery("KENAN"));
        org.junit.jupiter.api.Assertions.assertTrue(exact.size() == 1 && exact.get(0).matchQuality() == WorldMemorySearchResult.MatchQuality.EXACT,
                "La coincidencia exacta debe ignorar mayúsculas.");

        var accentInsensitive = search.search(new WorldMemoryQuery("sofia"));
        org.junit.jupiter.api.Assertions.assertTrue(accentInsensitive.size() == 2, "La búsqueda debe ignorar diacríticos.");
        org.junit.jupiter.api.Assertions.assertTrue(accentInsensitive.get(0).title().equals("Sofía"),
                "La coincidencia exacta debe preceder a la coincidencia contenida.");

        var prefix = search.search(new WorldMemoryQuery("santu"));
        org.junit.jupiter.api.Assertions.assertTrue(prefix.size() == 1 && prefix.get(0).matchQuality() == WorldMemorySearchResult.MatchQuality.PREFIX,
                "Debe admitirse búsqueda por prefijo.");

        org.junit.jupiter.api.Assertions.assertTrue(search.search(new WorldMemoryQuery("desconocido")).isEmpty(),
                "La búsqueda no puede inventar entradas no adquiridas.");
        org.junit.jupiter.api.Assertions.assertTrue(search.search(new WorldMemoryQuery("   ")).isEmpty(),
                "Una consulta vacía no debe devolver toda la memoria.");
    }

    private static void remember(WorldMemory memory, String id, WorldMemoryCategory category,
                                 String title, WorldKnowledgeSource source) {
        memory.knowledge().rememberEntry(new WorldMemoryEntry(new WorldMemoryEntryId(id), category,
                title, "", List.of(source), Optional.empty()));
    }

    
}

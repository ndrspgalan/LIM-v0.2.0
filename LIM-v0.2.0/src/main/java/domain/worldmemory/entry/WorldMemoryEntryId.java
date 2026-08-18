package domain.worldmemory.entry;

/** Identidad neutral de una futura entrada de conocimiento, distinta de un indicador espacial. */
public record WorldMemoryEntryId(String value) {
    public WorldMemoryEntryId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("El identificador no puede estar vacío.");
        value = value.trim();
    }
}

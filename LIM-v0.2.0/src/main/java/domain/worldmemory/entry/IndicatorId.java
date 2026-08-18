package domain.worldmemory.entry;

public record IndicatorId(String value) {
    public IndicatorId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El identificador del indicador no puede estar vacío.");
        }
        value = value.trim();
    }
}

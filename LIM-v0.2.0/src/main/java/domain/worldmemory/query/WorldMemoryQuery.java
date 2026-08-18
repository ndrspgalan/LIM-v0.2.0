package domain.worldmemory.query;

import java.util.Objects;

/** Texto de consulta introducido por el jugador para localizar conocimiento adquirido. */
public record WorldMemoryQuery(String text) {
    public WorldMemoryQuery {
        text = Objects.requireNonNullElse(text, "").trim();
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }
}

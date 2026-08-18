package domain.milestone;

import java.util.Objects;

public record PersonaMilestone(String id, String title, String description, boolean achieved) {
    public PersonaMilestone {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El identificador del hito es obligatorio.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("El título del hito es obligatorio.");
        description = Objects.requireNonNullElse(description, "").trim();
    }
}

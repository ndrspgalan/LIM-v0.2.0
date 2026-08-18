package domain.bestiarium;

import java.util.Objects;

/** Descriptor mínimo vigente: identidad visible y plano de existencia. La taxonomía específica vive en sus catálogos. */
public record BestiaryDescriptor(String species, ExistencePlane plane) {
    public BestiaryDescriptor {
        if(species==null||species.isBlank()) throw new IllegalArgumentException("Especie obligatoria.");
        Objects.requireNonNull(plane);
    }
}

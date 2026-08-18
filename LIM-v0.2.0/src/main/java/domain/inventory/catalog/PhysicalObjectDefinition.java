package domain.inventory.catalog;

import java.util.Objects;

public record PhysicalObjectDefinition(
        CanonicalObjectTypeId typeId,
        String displayName,
        String family,
        PhysicalStorageSemantics storageSemantics,
        boolean dynamicallyCreated
) {
    public PhysicalObjectDefinition {
        Objects.requireNonNull(typeId);
        displayName = require(displayName, "nombre");
        family = require(family, "familia");
        Objects.requireNonNull(storageSemantics);
    }

    private static String require(String s,String field){
        Objects.requireNonNull(s, field+" nulo");
        String v=s.trim();
        if(v.isEmpty()) throw new IllegalArgumentException(field+" vacío");
        return v;
    }
}

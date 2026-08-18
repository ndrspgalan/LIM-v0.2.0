package domain.inventory.logistics;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** cada transporte compatible utiliza un sistema de alforjas físicamente específico. */
public final class PersonalTransportSaddlebagPolicy {
    private static final Map<PersonalTransportType, InventoryCompartmentType> TYPES = build();
    private PersonalTransportSaddlebagPolicy() {}

    private static Map<PersonalTransportType, InventoryCompartmentType> build() {
        EnumMap<PersonalTransportType, InventoryCompartmentType> m = new EnumMap<>(PersonalTransportType.class);
        m.put(PersonalTransportType.HORSE_LEISURE, InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE);
        m.put(PersonalTransportType.HORSE_RACING, InventoryCompartmentType.SADDLEBAGS_HORSE_RACING);
        m.put(PersonalTransportType.HORSE_DRAFT, InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT);
        m.put(PersonalTransportType.BICYCLE_MILITARY_V881, InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY);
        m.put(PersonalTransportType.MOTORCYCLE_CARDAN_V881, InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN);
        return Map.copyOf(m);
    }

    public static Optional<InventoryCompartmentType> compartmentType(PersonalTransportType transport) {
        Objects.requireNonNull(transport, "El transporte no puede ser nulo.");
        return Optional.ofNullable(TYPES.get(transport));
    }

    public static Optional<PersonalTransportType> transportFor(InventoryCompartmentType type) {
        Objects.requireNonNull(type, "El compartimento no puede ser nulo.");
        return TYPES.entrySet().stream().filter(e -> e.getValue() == type).map(Map.Entry::getKey).findFirst();
    }

    public static boolean isSaddlebagType(InventoryCompartmentType type) {
        return transportFor(type).isPresent();
    }
}

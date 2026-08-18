package domain.worldmemory.category;

import domain.worldmemory.entry.IndicatorType;

/** Traduce los antiguos indicadores espaciales a las nuevas familias consultables. */
public final class WorldMemoryCategoryProjection {
    private WorldMemoryCategoryProjection() {}

    public static WorldMemoryCategory from(IndicatorType type) {
        return switch (type) {
            case LOCATION, ROUTE -> WorldMemoryCategory.PLACES;
            case PERSON -> WorldMemoryCategory.PEOPLE;
            case RESOURCE, OBJECTIVE_REFERENCE -> WorldMemoryCategory.OBJECTS_AND_RESOURCES;
            case EVENT -> WorldMemoryCategory.EVENTS;
            case WARNING, HYPOTHESIS -> WorldMemoryCategory.WORLD_KNOWLEDGE;
        };
    }
}

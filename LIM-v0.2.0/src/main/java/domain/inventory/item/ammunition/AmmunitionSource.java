package domain.inventory.item.ammunition;
public interface AmmunitionSource {
    AmmunitionDescriptor ammunitionDescriptor();
    int remainingUnits();
    int maxUnits();
    int shotsLoadedPerConsumedUnit();
    boolean consumeOneUnit();
    default int remainingShots() { return remainingUnits() * shotsLoadedPerConsumedUnit(); }
    default boolean consumeShots(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva.");
        if (quantity != shotsLoadedPerConsumedUnit()) return false;
        return consumeOneUnit();
    }
    default boolean depleted(){ return remainingShots()<=0; }
}

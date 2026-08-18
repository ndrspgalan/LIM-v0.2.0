package domain.inventory.item.armor;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import java.util.List;
import java.util.Set;

/** Pieza tecnológica de cabeza con estados operativos adicionales. */
public final class SpecializedHeadEquipment {
    public enum State { OPEN, CLOSED, ACTIVE, INACTIVE }
    private final ArmorPiece armor;
    private State state;

    public SpecializedHeadEquipment(ArmorPiece armor, State initialState) {
        if (armor.headCoverageRatio() <= 0) throw new IllegalArgumentException("El equipo especializado debe proteger la cabeza.");
        this.armor = armor;
        this.state = initialState;
    }
    public ArmorPiece armor() { return armor; }
    public State state() { return state; }
    public void activate() { state = State.ACTIVE; }
    public void deactivate() { state = State.INACTIVE; }
    public void open() { state = State.OPEN; }
    public void close() { state = State.CLOSED; }
}

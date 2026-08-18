package domain.runic;

import domain.character.CharacterClass;
import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Marca equipable desde su obtención, pero latente hasta que el personaje puede percibirla. */
public final class RunicMarkItem extends InventoryEntry {
    private final RunicMarkId id;
    private final CharacterClass affinity;
    private final RunicMarkGeometry geometry;

    public RunicMarkItem(
            RunicMarkId id,
            String name,
            CharacterClass affinity,
            RunicMarkGeometry geometry,
            String narrativeDescription,
            List<String> statistics
    ) {
        super(name, narrativeDescription, 0.0, InventoryFootprint.equipmentOnly(), withGeometry(geometry, statistics), List.of());
        this.id = Objects.requireNonNull(id, "El identificador rúnico no puede ser nulo.");
        this.affinity = Objects.requireNonNull(affinity, "La afinidad de clase no puede ser nula.");
        this.geometry = Objects.requireNonNull(geometry, "La geometría rúnica no puede ser nula.");
    }

    private static List<String> withGeometry(RunicMarkGeometry geometry,List<String> statistics){
        Objects.requireNonNull(geometry); Objects.requireNonNull(statistics);
        java.util.ArrayList<String> out=new java.util.ArrayList<>();
        out.add("FORMA | "+geometry.label()); out.addAll(statistics); return List.copyOf(out);
    }

    public RunicMarkId id() { return id; }
    public CharacterClass affinity() { return affinity; }
    public RunicMarkGeometry geometry(){ return geometry; }

    public boolean isAwakenedFor(CharacterSheet sheet) { Objects.requireNonNull(sheet); return true; }
    public Optional<String> visibleNarrative(CharacterSheet sheet) { return isAwakenedFor(sheet) ? Optional.of(narrativeDescription()) : Optional.empty(); }
    public List<String> visibleStatistics(CharacterSheet sheet) { return isAwakenedFor(sheet) ? statistics() : List.of(); }
    public List<ItemProperty> visibleProperties(CharacterSheet sheet) { return List.of(); }
    public Optional<String> visibleNarrative(RunicMarkProgressState state) { return state == RunicMarkProgressState.AWAKENED ? Optional.of(narrativeDescription()) : Optional.empty(); }
    public List<String> visibleStatistics(RunicMarkProgressState state) { return state == RunicMarkProgressState.AWAKENED ? statistics() : List.of(); }
    public boolean isCosmeticallyPresent(RunicMarkProgressState state) { return state != RunicMarkProgressState.ABSENT; }
}

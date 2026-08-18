package domain.worldmemory.selection;

import domain.worldmemory.entry.IndicatorId;
import domain.worldmemory.entry.WorldMemoryEntryId;

import java.util.Objects;
import java.util.Optional;

/** Estado de selección espacial separado del repositorio general de conocimiento.
 *
 * Desde  la identidad primaria de una referencia es WorldMemoryEntryId.
 * IndicatorId se conserva como adaptador de compatibilidad con el modelo espacial antiguo.
 */
public final class WorldMemorySelection {
    private WorldMemoryEntryId selectedEntryId;

    public void select(WorldMemoryEntryId id) {
        selectedEntryId = Objects.requireNonNull(id, "La entrada seleccionada no puede ser nula.");
    }

    public void select(IndicatorId id) {
        Objects.requireNonNull(id, "El indicador seleccionado no puede ser nulo.");
        select(new WorldMemoryEntryId(id.value()));
    }

    public void clear() { selectedEntryId = null; }

    public Optional<WorldMemoryEntryId> selectedEntryId() {
        return Optional.ofNullable(selectedEntryId);
    }

    /** Adaptador obsoleto para verificaciones y consumidores todavía basados en indicadores. */
    public Optional<IndicatorId> selectedIndicatorId() {
        return selectedEntryId().map(id -> new IndicatorId(id.value()));
    }
}

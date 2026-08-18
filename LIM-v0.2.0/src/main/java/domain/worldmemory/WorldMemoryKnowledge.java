package domain.worldmemory;

import domain.worldmemory.entry.IndicatorId;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.entry.WorldMemoryEntryProjection;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.relation.WorldMemoryRelation;
import domain.worldmemory.revision.MemoryRevisionResult;
import domain.worldmemory.history.WorldMemoryEntryRevision;
import domain.worldmemory.history.WorldMemoryHistory;
import domain.worldmemory.revision.WorldMemoryRevisionPolicy;
import domain.worldmemory.selection.WorldMemorySelection;
import domain.worldmemory.spatial.RememberedTerrain;
import domain.worldmemory.spatial.TerrainObservation;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.spatial.PersonalObservationMark;
import domain.inventory.catalog.CanonicalObjectTypeId;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class WorldMemoryKnowledge {
    private final RememberedTerrain terrain;
    private final Map<IndicatorId, RememberedIndicator> indicators;
    private final WorldMemoryRevisionPolicy revisionPolicy;
    private final Map<WorldMemoryEntryId, WorldMemoryEntry> entries = new LinkedHashMap<>();
    private final List<WorldMemoryRelation> relations = new ArrayList<>();
    private final WorldMemorySelection selection = new WorldMemorySelection();
    private final WorldMemoryHistory history = new WorldMemoryHistory();
    private PersonalObservationMark observationMark;
    private boolean observationMarkSelected;
    /** última ubicación conocida de objetos únicos abandonados físicamente. */
    private final Map<CanonicalObjectTypeId, WorldCoordinate> persistentDroppedObjects = new LinkedHashMap<>();
    /** tracking por instancia para permitir múltiples contenedores del mismo tipo en el mundo. */
    private final Map<domain.worldmemory.WorldObjectInstanceId, WorldCoordinate> persistentDroppedInstances = new LinkedHashMap<>();

    public WorldMemoryKnowledge() {
        this(new RememberedTerrain(), new LinkedHashMap<>(), new WorldMemoryRevisionPolicy());
    }

    WorldMemoryKnowledge(
            RememberedTerrain terrain,
            Map<IndicatorId, RememberedIndicator> indicators,
            WorldMemoryRevisionPolicy revisionPolicy
    ) {
        this.terrain = Objects.requireNonNull(terrain);
        this.indicators = Objects.requireNonNull(indicators);
        this.revisionPolicy = Objects.requireNonNull(revisionPolicy);
    }

    public RememberedTerrain terrain() {
        return terrain;
    }

    public Map<IndicatorId, RememberedIndicator> indicators() {
        return Collections.unmodifiableMap(indicators);
    }

    public Optional<RememberedIndicator> indicator(IndicatorId id) {
        return Optional.ofNullable(indicators.get(Objects.requireNonNull(id)));
    }

    public MemoryRevisionResult remember(RememberedIndicator incoming) {
        RememberedIndicator current = indicators.get(incoming.id());
        MemoryRevisionResult result = revisionPolicy.revise(current, incoming);
        indicators.put(incoming.id(), result.indicator());
        rememberEntry(WorldMemoryEntryProjection.from(result.indicator()));
        return result;
    }

    public void rememberEntry(WorldMemoryEntry entry) {
        Objects.requireNonNull(entry);
        WorldMemoryEntry previous = entries.put(entry.id(), entry);
        history.record(previous, entry);
    }

    public List<WorldMemoryEntryRevision> historyOf(WorldMemoryEntryId id) {
        Objects.requireNonNull(id, "La entrada no puede ser nula.");
        if (!entries.containsKey(id)) {
            throw new IllegalArgumentException("No puede consultarse el historial de conocimiento no adquirido.");
        }
        return history.revisionsOf(id);
    }

    public Map<WorldMemoryEntryId, WorldMemoryEntry> entries() {
        return Collections.unmodifiableMap(entries);
    }

    public Optional<WorldMemoryEntry> entry(WorldMemoryEntryId id) {
        return Optional.ofNullable(entries.get(Objects.requireNonNull(id)));
    }

    public List<WorldMemoryEntry> entries(WorldMemoryCategory category) {
        Objects.requireNonNull(category);
        if (category == WorldMemoryCategory.EXPLORED_TERRITORY) return List.of();
        return entries.values().stream().filter(entry -> entry.category() == category).toList();
    }

    public int knownCount(WorldMemoryCategory category) {
        if (category == WorldMemoryCategory.EXPLORED_TERRITORY) return terrain.observationCount();
        return entries(category).size();
    }

    public void rememberRelation(WorldMemoryRelation relation) {
        Objects.requireNonNull(relation, "La relación no puede ser nula.");
        if (!entries.containsKey(relation.source()) || !entries.containsKey(relation.target())) {
            throw new IllegalArgumentException("Solo puede recordarse una relación entre entradas ya adquiridas.");
        }
        if (!relations.contains(relation)) relations.add(relation);
    }

    public List<WorldMemoryRelation> relations() {
        return List.copyOf(relations);
    }

    public List<WorldMemoryRelation> relationsOf(WorldMemoryEntryId entryId) {
        Objects.requireNonNull(entryId, "La entrada no puede ser nula.");
        if (!entries.containsKey(entryId)) {
            throw new IllegalArgumentException("No pueden consultarse relaciones de conocimiento no adquirido.");
        }
        return relations.stream()
                .filter(relation -> relation.source().equals(entryId) || relation.target().equals(entryId))
                .toList();
    }

    public void rememberPersistentDroppedObject(CanonicalObjectTypeId typeId, WorldCoordinate coordinate) {
        persistentDroppedObjects.put(Objects.requireNonNull(typeId), Objects.requireNonNull(coordinate));
    }

    public Optional<WorldCoordinate> persistentDroppedObjectLocation(CanonicalObjectTypeId typeId) {
        return Optional.ofNullable(persistentDroppedObjects.get(Objects.requireNonNull(typeId)));
    }

    public Map<CanonicalObjectTypeId, WorldCoordinate> persistentDroppedObjects() {
        return Collections.unmodifiableMap(persistentDroppedObjects);
    }

    public void rememberPersistentDroppedInstance(domain.worldmemory.WorldObjectInstanceId id, WorldCoordinate coordinate) {
        persistentDroppedInstances.put(Objects.requireNonNull(id),Objects.requireNonNull(coordinate));
    }
    public Optional<WorldCoordinate> persistentDroppedInstanceLocation(domain.worldmemory.WorldObjectInstanceId id) {
        return Optional.ofNullable(persistentDroppedInstances.get(Objects.requireNonNull(id)));
    }
    public Map<domain.worldmemory.WorldObjectInstanceId,WorldCoordinate> persistentDroppedInstances() {
        return Collections.unmodifiableMap(persistentDroppedInstances);
    }

    public void recordTerrain(TerrainObservation observation) {
        terrain.record(observation);
    }

    public void select(WorldMemoryEntryId entryId) {
        Objects.requireNonNull(entryId, "La entrada seleccionada no puede ser nula.");
        WorldMemoryEntry entry = entries.get(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("No puede seleccionarse conocimiento que el protagonista no recuerda.");
        }
        if (entry.spatialMemory().isEmpty()) {
            throw new IllegalArgumentException("La entrada no conserva una ubicación recordada.");
        }
        observationMarkSelected = false;
        selection.select(entryId);
    }

    public void select(IndicatorId id) {
        Objects.requireNonNull(id, "El indicador seleccionado no puede ser nulo.");
        if (!indicators.containsKey(id)) {
            throw new IllegalArgumentException("No puede seleccionarse un indicador que el protagonista no recuerda.");
        }
        observationMarkSelected = false;
        selection.select(new WorldMemoryEntryId(id.value()));
    }

    public void clearSelection() {
        selection.clear();
        observationMarkSelected = false;
    }

    public Optional<PersonalObservationMark> observationMark() {
        return Optional.ofNullable(observationMark);
    }

    /** Colocar una nueva marca sustituye siempre a la anterior y la convierte en referencia espacial vigente. */
    public void placeOrReplaceObservationMark(WorldCoordinate coordinate) {
        observationMark = new PersonalObservationMark(Objects.requireNonNull(coordinate));
        selection.clear();
        observationMarkSelected = true;
    }

    /** E con el monocular activo: el mismo punto retira la marca; cualquier punto distinto la sustituye. */
    public void toggleObservationMark(WorldCoordinate coordinate) {
        Objects.requireNonNull(coordinate, "La coordenada de observación no puede ser nula.");
        if (observationMark != null && observationMark.coordinate().equals(coordinate)) {
            clearObservationMark();
        } else {
            placeOrReplaceObservationMark(coordinate);
        }
    }

    public void clearObservationMark() {
        observationMark = null;
        observationMarkSelected = false;
    }

    public boolean observationMarkSelected() { return observationMark != null && observationMarkSelected; }

    /** En la Memoria del Mundo, seleccionar otra vez la marca propia equivale a retirarla. */
    public void toggleObservationMarkSelection() {
        if (observationMark == null) return;
        if (observationMarkSelected) {
            clearObservationMark();
        } else {
            selection.clear();
            observationMarkSelected = true;
        }
    }

    /** Contrato global: al aproximarse a menos de 5 m, la marca temporal se elimina automáticamente. */
    public boolean clearObservationMarkIfWithin(WorldCoordinate viewer, double thresholdMeters) {
        Objects.requireNonNull(viewer, "La posición del protagonista no puede ser nula.");
        if (!Double.isFinite(thresholdMeters) || thresholdMeters <= 0) throw new IllegalArgumentException("El umbral debe ser positivo.");
        if (observationMark == null) return false;
        WorldCoordinate target = observationMark.coordinate();
        double dx = target.x() - viewer.x();
        double dy = target.y() - viewer.y();
        double dz = target.elevation() - viewer.elevation();
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) < thresholdMeters) {
            clearObservationMark();
            return true;
        }
        return false;
    }

    public boolean clearObservationMarkIfReached(WorldCoordinate viewer) {
        return clearObservationMarkIfWithin(viewer, 5.0);
    }

    /** Entrada vigente seleccionada como referencia espacial. Se resuelve dinámicamente para que
     * cualquier revisión posterior de la memoria sea visible para el Astrolabio sin reselección. */
    public Optional<WorldMemoryEntry> selectedEntry() {
        return selection.selectedEntryId().flatMap(this::entry);
    }

    /** Alias para consumidores del contrato compacto. */
    public Optional<RememberedIndicator> selectedIndicator() {
        return selection.selectedIndicatorId().flatMap(this::indicator);
    }
}

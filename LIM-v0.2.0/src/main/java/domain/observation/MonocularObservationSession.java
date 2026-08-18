package domain.observation;

import domain.inventory.InventoryState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.item.misc.ReconnaissanceMonocularItem;
import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.Objects;

/** Estado runtime del monocular. Activarlo desde acceso rápido envaina semánticamente el arma activa. */
public final class MonocularObservationSession {
    private boolean active;
    private MonocularMagnification magnification = MonocularMagnification.X3;
    private boolean weaponSheathedByActivation;

    public boolean active() { return active; }
    public MonocularMagnification magnification() { return magnification; }
    public double effectiveObservationRangeMeters() { return magnification.effectiveRangeMeters(); }
    public boolean weaponSheathedByActivation() { return weaponSheathedByActivation; }

    public void activate(ReconnaissanceMonocularItem monocular, InventoryState inventory) {
        Objects.requireNonNull(monocular, "El monocular no puede ser nulo.");
        Objects.requireNonNull(inventory, "El inventario no puede ser nulo.");
        if (!QuickAccessUsePolicy.authorize(monocular, inventory).allowed()) {
            throw new IllegalStateException("El Monocular de Reconocimiento V881 debe estar equipado en acceso rápido.");
        }
        active = true;
        magnification = MonocularMagnification.X3;
        weaponSheathedByActivation = true;
    }

    public void deactivate() {
        active = false;
        weaponSheathedByActivation = false;
    }

    /** Reutilizar el mismo acceso rápido pliega el monocular. */
    public void toggle(ReconnaissanceMonocularItem monocular, InventoryState inventory) {
        if (active) deactivate(); else activate(monocular, inventory);
    }

    public void wheelUp() { requireActive(); magnification = magnification.increase(); }
    public void wheelDown() { requireActive(); magnification = magnification.decrease(); }

    /** Telemetría continua: solo publica una medida si el punto está dentro del alcance instrumental actual. */
    public MonocularTelemetry telemetry(double distanceMeters) {
        requireActive();
        if (!Double.isFinite(distanceMeters) || distanceMeters < 0 || distanceMeters > effectiveObservationRangeMeters()) {
            return MonocularTelemetry.unavailable();
        }
        return MonocularTelemetry.measured(distanceMeters);
    }

    public void toggleObservationMark(WorldMemoryKnowledge memory, WorldCoordinate coordinate) {
        requireActive();
        Objects.requireNonNull(memory, "La Memoria del Mundo no puede ser nula.");
        memory.toggleObservationMark(Objects.requireNonNull(coordinate));
    }

    private void requireActive() {
        if (!active) throw new IllegalStateException("El monocular no está activo.");
    }
}

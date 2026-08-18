package domain.inventory.item.aeronautics;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import java.util.List;

/** Equipamiento activo exterior del torso; no sustituye ni aporta protección de armadura. */
public final class DisposableGliderItem extends InventoryEntry {
    private DisposableGliderState state = DisposableGliderState.FOLDED;
    private boolean activelyEquippedOverTorso;

    public DisposableGliderItem() {
        super("Planeador Desechable V881",
                "Dispositivo textil-estructural de despliegue corporal que transforma una caída libre desde gran altura en un descenso controlado y una toma sobrevivible. Prioriza resistencia aerodinámica, estabilidad postural, fallo progresivo y disipación sacrificial de energía; no posee perfil de protección ni puede recuperarse después de utilizarse.",
                9.000, new InventoryFootprint(7, 4),
                List.of("EQUIPAMIENTO ACTIVO | Torso exterior", "PROTECCIÓN | Ninguna", "APERTURA | SPACE durante caída", "DESECHABLE | No reparable ni reutilizable"),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.ACTIVE_GLIDER, "PLANEADOR ACTIVO", "Puede equiparse por encima de la coraza sin sustituirla.", "SPACE | Abrir durante caída"),
                        ItemProperty.alwaysActive(ItemPropertyId.CONTROLLED_FAILURE, "FALLO CONTROLADO", "Las capas fallan progresivamente para disipar energía antes de alcanzar el cuerpo.", "IMPACTO | Disipación sacrificial"),
                        ItemProperty.alwaysActive(ItemPropertyId.DISPOSABLE, "DESECHABLE", "Su integridad depende de la destrucción controlada durante un único uso.", "REUTILIZACIÓN | No")
                ));
    }

    /** el planeador plegado ocupa excepcionalmente el volumen OUTER CHEST. */
    public boolean canEquipOverTorso(domain.inventory.equipment.ArmorEquipmentLayout layout) {
        return layout.layers().stream().noneMatch(l -> l.slot() == domain.inventory.equipment.EquipmentSlot.CHEST
                && l.position() == domain.inventory.item.armor.ArmorLayerPosition.OUTER);
    }
    public DisposableGliderState state() { return state; }
    public boolean activelyEquippedOverTorso() { return activelyEquippedOverTorso; }
    public void equipOverTorso() { if (state != DisposableGliderState.FOLDED) throw new IllegalStateException("Solo puede equiparse plegado."); activelyEquippedOverTorso = true; }
    public boolean pressSpaceWhileFalling(boolean falling) {
        if (!falling || !activelyEquippedOverTorso || state != DisposableGliderState.FOLDED) return false;
        state = DisposableGliderState.DEPLOYED; return true;
    }
    public void resolveLanding() { if (state == DisposableGliderState.DEPLOYED) { state = DisposableGliderState.CONSUMED; activelyEquippedOverTorso = false; } }
    public void destroy() { state = DisposableGliderState.DESTROYED; activelyEquippedOverTorso = false; }
}

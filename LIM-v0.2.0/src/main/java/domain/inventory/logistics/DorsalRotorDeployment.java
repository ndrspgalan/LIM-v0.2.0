package domain.inventory.logistics;

import domain.inventory.item.WeaponItem;
import java.util.Objects;

/** Resultado atómico de extraer el Espadón de Rotor del sistema dorsal para manejo inmediato. */
public record DorsalRotorDeployment(LogisticsState logistics, WeaponItem rotor) {
    public DorsalRotorDeployment {
        Objects.requireNonNull(logistics);
        Objects.requireNonNull(rotor);
    }
}

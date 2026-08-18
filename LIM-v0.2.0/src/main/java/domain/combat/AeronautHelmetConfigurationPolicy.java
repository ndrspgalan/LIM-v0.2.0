package domain.combat;

import java.util.Objects;

/**
 * : el casco no usa una acción manual de plegado en gameplay.
 * Fuera de encuentro hostil permanece replegado; al iniciar uno se despliega
 * automáticamente y vuelve a replegarse cuando concluye.
 *
 * El estado no altera su volumen de inventario: equipado ocupa HEAD; desequipado
 * conserva la envolvente física canónica de almacenamiento.
 */
public final class AeronautHelmetConfigurationPolicy {
    private AeronautHelmetConfigurationPolicy() {}

    public static AeronautHelmetConfiguration resolve(HostileEncounterState encounter) {
        Objects.requireNonNull(encounter, "El encuentro no puede ser nulo.");
        return encounter.isActive() ? AeronautHelmetConfiguration.DEPLOYED : AeronautHelmetConfiguration.RETRACTED;
    }

    public static boolean headProtectionOperational(HostileEncounterState encounter) {
        return resolve(encounter) == AeronautHelmetConfiguration.DEPLOYED;
    }
}

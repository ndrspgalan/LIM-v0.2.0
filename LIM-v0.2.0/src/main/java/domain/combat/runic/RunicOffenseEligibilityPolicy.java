package domain.combat.runic;

import java.util.Objects;

/**
 * RESONANCIA, VOTO VINCULANTE y ESPEJO sólo se alimentan de ataques primarios
 * ejecutados en DESARMADO o con armas cuerpo a cuerpo.
 */
public final class RunicOffenseEligibilityPolicy {
    public boolean supportsResonance(RunicOffenseSource source) { return supportsPrimaryRunicOffense(source); }
    public boolean supportsBindingVow(RunicOffenseSource source) { return supportsPrimaryRunicOffense(source); }
    public boolean supportsMirror(RunicOffenseSource source) { return supportsPrimaryRunicOffense(source); }

    public boolean supportsPrimaryRunicOffense(RunicOffenseSource source) {
        Objects.requireNonNull(source, "La fuente ofensiva no puede ser nula.");
        return source == RunicOffenseSource.UNARMED || source == RunicOffenseSource.MELEE;
    }
}

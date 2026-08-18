package domain.bestiarium;
import domain.combat.DamageType;
import java.util.Objects;
/** El Intersticio no produce ni recibe canales físicos P/C/Ct. */
public final class PlaneDamagePolicy {
    public boolean canReceive(BestiaryDescriptor target, DamageType type) {
        Objects.requireNonNull(target); Objects.requireNonNull(type);
        return target.plane() != ExistencePlane.INTERSTICE || !isPhysical(type);
    }
    public boolean canInflict(BestiaryDescriptor source, DamageType type) {
        Objects.requireNonNull(source); Objects.requireNonNull(type);
        return source.plane() != ExistencePlane.INTERSTICE || !isPhysical(type);
    }
    private boolean isPhysical(DamageType type) {
        String n = type.name();
        return n.contains("PIERC") || n.contains("PERFOR") || n.contains("SLASH") || n.contains("CORT") || n.contains("BLUNT") || n.contains("CONTUND");
    }
}

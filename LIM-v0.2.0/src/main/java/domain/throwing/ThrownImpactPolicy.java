package domain.throwing;

import domain.combat.PhysicalDamage;
import domain.inventory.item.LethalityProfile;

/**
 * Daño de lanzamiento .
 * Cada punto de FUERZA aporta B1 bruto y cada kilogramo arrojado aporta B1 adicional.
 * P/C propios del objeto se conservan sin escalar.
 */
public final class ThrownImpactPolicy {
    public PhysicalDamage resolve(int strength, ThrowProfile profile) {
        if (strength < 1 || strength > 75) {
            throw new IllegalArgumentException("FUERZA debe estar entre 1 y 75.");
        }
        if (profile == null) throw new IllegalArgumentException("El perfil de lanzamiento no puede ser nulo.");

        double basalBlunt = strength + profile.massKg();
        LethalityProfile lethality = profile.lethalityProfile().orElse(new LethalityProfile(0, 0, 0));
        return new PhysicalDamage(
                lethality.piercing(),
                lethality.slashing(),
                basalBlunt + lethality.blunt()
        );
    }
}

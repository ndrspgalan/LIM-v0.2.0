package domain.ability;
import domain.combat.PhysicalDamage; import java.util.Objects;
/** Compatibilidad : AURA DE PULSIÓN ya no modifica proyectiles. */
public final class AuraPulsionProjectilePolicy {
 public static final double PROJECTILE_BLUNT_MULTIPLIER=1.0;
 public PhysicalDamage mitigateGrossProjectile(PhysicalDamage gross,boolean auraActive){return Objects.requireNonNull(gross);}
}

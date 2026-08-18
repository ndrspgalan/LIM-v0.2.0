package domain.combat;

import domain.inventory.item.WeaponItem;
import java.util.Objects;

/** DESVIAR es la única fuente de verdad; Mirror Parry reutiliza esta resolución automáticamente. */
public final class ParryResolutionPolicy {
    private final ParryTargetEligibilityPolicy eligibility = new ParryTargetEligibilityPolicy();
    private final CombatTechniqueUnlockPolicy techniques = new CombatTechniqueUnlockPolicy();

    public ParryResolution resolve(WeaponItem opposingWeapon, boolean succeeded) {
        return resolve(opposingWeapon, succeeded, CombatTechniqueUnlockPolicy.DEFLECTION_DEXTERITY_REQUIREMENT, 0.0);
    }

    public ParryResolution resolve(WeaponItem opposingWeapon, boolean succeeded, int dexterity, double recoilUnits) {
        Objects.requireNonNull(opposingWeapon, "El arma rival no puede ser nula.");
        if (!succeeded) return ParryResolution.rejected("No se produjo una ventana o colisión válida.");
        if (!techniques.canDeflect(dexterity)) return ParryResolution.rejected("DESVIAR requiere DESTREZA 20.");
        if (!eligibility.isEligible(opposingWeapon)) return ParryResolution.rejected("El arma rival no admite PARRY.");
        double stun = techniques.deflectionStunDurationSeconds(dexterity);
        double recoil = StaggerPolicy.knockbackDistanceMeters(Math.max(0.0, recoilUnits));
        return ParryResolution.success(stun, recoil);
    }
}

package domain.combat;

import domain.inventory.item.LethalityProfile;

import java.util.Objects;

/**
 * Aplica la pérdida de transferencia contundente propia de blandir la pieza de la mano izquierda
 * con la mano no dominante durante dual wielding. No convierte la pieza en un "modo
 * ofensivo": únicamente calcula su perfil efectivo mientras se usa conjuntamente con
 * el arma de la mano derecha.
 */
public final class DualWieldLethalityPolicy {
    public static final double LEFT_HAND_BLUNT_MULTIPLIER = 0.75;

    private DualWieldLethalityPolicy() {}

    public static LethalityProfile leftHandEffectiveProfile(LethalityProfile baseProfile) {
        Objects.requireNonNull(baseProfile, "El perfil de letalidad de la mano izquierda no puede ser nulo.");
        return new LethalityProfile(
                baseProfile.piercing(),
                baseProfile.slashing(),
                baseProfile.blunt() * LEFT_HAND_BLUNT_MULTIPLIER
        );
    }
}

package domain.inventory.item;

/**
 * Repertorio ordinal de ataques ligeros disponible para un modo de agarre.
 * El perfil standard de 3 golpes es un PLACEHOLDER PROVISIONAL del vertical slice: cada arma,
 * incluido DESARMADO, podrá definir posteriormente su longitud canónica propia.
 */
public record LightAttackComboProfile(int attackCount) {
    public LightAttackComboProfile {
        if (attackCount < 1) throw new IllegalArgumentException("Un combo ligero debe contener al menos un ataque.");
    }

    public boolean supports(int ordinal) {
        return ordinal >= 1 && ordinal <= attackCount;
    }

    /** El bonus ordinario de remate sólo existe en combos ligeros de tres ataques o más. */
    public boolean hasFinisherBonus() {
        return attackCount >= 3;
    }

    public static final int PROVISIONAL_STANDARD_ATTACK_COUNT = 3;

    public static LightAttackComboProfile standard() {
        return new LightAttackComboProfile(PROVISIONAL_STANDARD_ATTACK_COUNT);
    }
}

package domain.ability;

/** Bonificación colectiva temporal canónica de GRITO DE GUERRA. */
public record WarCryEffect(
        double staminaRegenerationMultiplier,
        int physicalStabilityBonus,
        int sanityBonus,
        int durationRealSeconds
) {
    public static WarCryEffect canonical() { return new WarCryEffect(1.3, 1, 1, 60); }
    public WarCryEffect {
        if (staminaRegenerationMultiplier < 1.0 || physicalStabilityBonus < 0 || sanityBonus < 0 || durationRealSeconds <= 0)
            throw new IllegalArgumentException("Bonificación de GRITO DE GUERRA inválida.");
    }
}

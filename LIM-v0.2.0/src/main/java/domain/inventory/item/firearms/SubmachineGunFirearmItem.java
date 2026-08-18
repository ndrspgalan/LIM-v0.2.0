package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import java.util.List;
import java.util.Set;

/** Subfusil V881: AA exclusivo, sin AIMING y retroceso acumulable solo en el primer disparo de cada pulsación. */
public class SubmachineGunFirearmItem extends FirearmItem {
    public static final int RECOIL_ACCUMULATION_SHOTS = 1;
    private final int cyclicRateRpm;

    public SubmachineGunFirearmItem(String name, String narrativeDescription, double loadedWeightKg,
            double lengthMeters, double widthMeters, double effectiveRangeMeters, String caliber,
            FirearmCartridge magazineDefinition, LethalityProfile lethalityProfile,
            double recoilVelocityPerShotMps, int cyclicRateRpm, Set<WeaponTrait> traits) {
        super(name, narrativeDescription, loadedWeightKg, lengthMeters, widthMeters, effectiveRangeMeters,
                caliber, magazineDefinition, lethalityProfile, recoilVelocityPerShotMps,
                List.of(FireMode.AUTO_A), false, true, traits);
        if (cyclicRateRpm <= 0) throw new IllegalArgumentException("La cadencia cíclica debe ser positiva.");
        this.cyclicRateRpm = cyclicRateRpm;
    }

    @Override public boolean coupDeGracePropertyPresent() { return true; }

    public int cyclicRateRpm() { return cyclicRateRpm; }
    @Override public boolean supportsAiming() { return false; }

    @Override protected void consumeShot() {
        double recoilForThisShot = triggerState().shotsOnCurrentPress() < RECOIL_ACCUMULATION_SHOTS
                ? recoilVelocityPerShotMps() : 0.0;
        consumeAmmunitionAndRegisterShot(recoilForThisShot);
    }

    @Override public String destabilizingTechniqueDescription() {
        return "Golpe desestabilizador con la culata del Subfusil Automático V881.";
    }
}

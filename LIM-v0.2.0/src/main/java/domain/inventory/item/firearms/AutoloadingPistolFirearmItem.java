package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;

import java.util.List;
import java.util.Set;

/** Pistola autocargadora V881: estrictamente monomanual y exclusivamente 1A. */
public class AutoloadingPistolFirearmItem extends FirearmItem {
    public AutoloadingPistolFirearmItem(
            String name, String narrativeDescription, double loadedWeightKg,
            double lengthMeters, double widthMeters, double effectiveRangeMeters,
            String caliber, FirearmCartridge magazineDefinition, LethalityProfile lethalityProfile,
            double recoilVelocityPerShotMps, Set<WeaponTrait> traits) {
        super(name, narrativeDescription, loadedWeightKg, lengthMeters, widthMeters, effectiveRangeMeters,
                caliber, magazineDefinition, lethalityProfile, recoilVelocityPerShotMps,
                List.of(FireMode.ONE_A), true, false, traits);
        if (loadedWeightKg > 1.0) throw new IllegalArgumentException("Una pistola monomanual V881 no puede superar 1 kg cargada.");
        if (lengthMeters > 0.50) throw new IllegalArgumentException("Una pistola monomanual V881 no puede superar 0,50 m.");
    }

    @Override public boolean coupDeGracePropertyPresent() { return true; }

    @Override public String destabilizingTechniqueDescription() {
        return "Golpe desestabilizador con la culata/empuñadura de la pistola.";
    }
}

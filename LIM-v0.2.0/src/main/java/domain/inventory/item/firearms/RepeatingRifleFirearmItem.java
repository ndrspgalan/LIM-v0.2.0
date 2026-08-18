package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import java.util.List;
import java.util.Set;

/** Fusil de Repetición V881 inspirado doctrinalmente en el fusil de cerrojo de servicio. */
public class RepeatingRifleFirearmItem extends FirearmItem {
    public RepeatingRifleFirearmItem(String name, String narrativeDescription, double loadedWeightKg,
            double lengthMeters, double widthMeters, double effectiveRangeMeters, String caliber,
            FirearmCartridge cartridgeDefinition, LethalityProfile lethalityProfile,
            double recoilVelocityPerShotMps, Set<WeaponTrait> traits) {
        super(name, narrativeDescription, loadedWeightKg, lengthMeters, widthMeters, effectiveRangeMeters,
                caliber, cartridgeDefinition, lethalityProfile, recoilVelocityPerShotMps,
                List.of(FireMode.ONE_A), false, true, traits);
    }

    @Override public boolean coupDeGracePropertyPresent() { return true; }

    @Override public String destabilizingTechniqueDescription() {
        return "Golpe desestabilizador mediante bayonetazo corto del Fusil de Repetición V881.";
    }

    public LethalityProfile destabilizingBayonetProfile(int strength) {
        return new domain.combat.DestabilizingStrikePolicy().profile(strength);
    }

    public LethalityProfile bayonetChargeProfile(int strength) {
        return new LethalityProfile(65, 65, domain.combat.StrengthMassBluntPolicy.blunt(strength, weightKg()));
    }
}

package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import java.util.List;
import java.util.Set;

/** Cañón Antimaterial V881: plataforma pesada 20 mm, 1A y recuperación mínima de un segundo. */
public final class AntiMaterielCannonFirearmItem extends FirearmItem {
    public static final double SHOT_RECOVERY_SECONDS = 1.0;
    public static final double EXPLOSION_RADIUS_METERS = 0.5;
    private double recoveryRemainingSeconds;

    public AntiMaterielCannonFirearmItem(String narrative, FirearmCartridge cartridge) {
        super("Cañón Antimaterial V881", narrative, 20.0, 1.35, 0.30, 150.0, "20 mm", cartridge,
                new LethalityProfile(100, 0, 100), 1.20, List.of(FireMode.ONE_A), false, true, Set.<WeaponTrait>of());
    }

    @Override public boolean fulminatingPropertyPresent() { return true; }
    public double explosionRadiusMeters() { return EXPLOSION_RADIUS_METERS; }
    public domain.inventory.item.LethalityProfile radialLethalityAt(double distanceMeters) {
        return domain.combat.RadialLethalityPolicy.physical(new domain.inventory.item.LethalityProfile(100,0,100), distanceMeters, EXPLOSION_RADIUS_METERS);
    }
    public domain.combat.StaggerResult radialStaggerAt(double distanceMeters) {
        double blunt=radialLethalityAt(distanceMeters).blunt();
        return domain.combat.StaggerPolicy.resolve(blunt);
    }
    public double shotRecoverySeconds() { return SHOT_RECOVERY_SECONDS; }
    public double recoveryRemainingSeconds() { return recoveryRemainingSeconds; }
    public boolean recoveringFromShot() { return recoveryRemainingSeconds > 0; }
    public void advanceTime(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        recoveryRemainingSeconds = Math.max(0, recoveryRemainingSeconds - seconds);
    }
    @Override protected boolean canConsumeShot() { return !recoveringFromShot() && super.canConsumeShot(); }
    @Override protected void consumeShot() {
        super.consumeShot();
        recoveryRemainingSeconds = SHOT_RECOVERY_SECONDS;
    }
    @Override public String destabilizingTechniqueDescription() { return "Golpe desestabilizador con la culata del Cañón Antimaterial V881."; }
}

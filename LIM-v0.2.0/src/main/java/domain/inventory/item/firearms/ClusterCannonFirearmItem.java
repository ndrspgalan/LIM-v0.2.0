package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import java.util.List;
import java.util.Set;

/** Cañón de Racimo V881: cohete unitario, 1A y temporizador mecánico 3/4/5 s. */
public final class ClusterCannonFirearmItem extends FirearmItem {
    public static final double IMPACT_RADIUS_METERS = 25.0;
    public static final double BURN_DAMAGE = 100.0;
    private int timerSeconds = 3;

    public ClusterCannonFirearmItem(String narrative, FirearmLoadDefinition loadDefinition) {
        super("Cañón de Racimo V881", narrative, 5.0, 1.10, 0.25, 150.0, "85 mm", loadDefinition,
                new LethalityProfile(0, 100, 0), 0.90, List.of(FireMode.ONE_A), false, true, Set.<WeaponTrait>of());
    }

    public int timerSeconds() { return timerSeconds; }
    public boolean beginTimerConfiguration() { enterClusterTimerConfigurationState(); return true; }
    public int cycleTimer() { timerSeconds = timerSeconds == 3 ? 4 : timerSeconds == 4 ? 5 : 3; return timerSeconds; }
    public void cancelTimerConfiguration() { leaveClusterTimerConfigurationState(); }
    public ClusterCannonImpactProfile impactProfile() { return new ClusterCannonImpactProfile(IMPACT_RADIUS_METERS, 100, BURN_DAMAGE, true); }
    public ClusterCannonImpactProfile impactProfileAt(double distanceMeters) { return impactProfile().atDistance(distanceMeters); }
    public String timerRule() { return "Actúa al impacto o, si no impacta antes, al agotarse el temporizador seleccionado de 3/4/5 s."; }
    public String confidentialMechanism() { return "CONFIDENCIAL — mecanismo interno de espoleta, carga y dispersión."; }
    @Override public String destabilizingTechniqueDescription() { return "Golpe desestabilizador con la culata del Cañón de Racimo V881."; }
}

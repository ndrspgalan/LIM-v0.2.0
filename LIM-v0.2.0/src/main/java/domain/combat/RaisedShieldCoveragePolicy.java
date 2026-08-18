package domain.combat;

import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.armor.ArmorProtectionProfile;

/** un escudo alzado se comporta como capa exterior adicional, no como barra de vida. */
public final class RaisedShieldCoveragePolicy {
    private RaisedShieldCoveragePolicy(){}
    public static Layer dedicated(ShieldGuardPosition position){
        return new Layer(position.location(), ShieldCombatPolicy.PAVESINA_V881.raisedCoverageRatio(),
                ShieldCombatPolicy.PAVESINA_V881.protection(), ShieldCombatPolicy.PAVESINA_V881.wearMultiplier());
    }
    public static Layer improvisedHead(ArmorProtectionProfile bracerProtection,double bracerWearMultiplier){
        return new Layer(ArmorHitLocation.HEAD,0.025,bracerProtection,bracerWearMultiplier);
    }
    public record Layer(ArmorHitLocation location,double coverageRatio,ArmorProtectionProfile protection,double wearMultiplier){}
}

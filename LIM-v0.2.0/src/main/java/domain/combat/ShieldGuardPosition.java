package domain.combat;

import domain.inventory.item.armor.ArmorHitLocation;

/** la guardia de escudo dedicado sólo protege una región cada vez. */
public enum ShieldGuardPosition {
    HEAD(ArmorHitLocation.HEAD),
    BODY(ArmorHitLocation.BODY);

    private final ArmorHitLocation location;
    ShieldGuardPosition(ArmorHitLocation location){this.location=location;}
    public ArmorHitLocation location(){return location;}
    public ShieldGuardPosition toggle(){return this==HEAD?BODY:HEAD;}
}

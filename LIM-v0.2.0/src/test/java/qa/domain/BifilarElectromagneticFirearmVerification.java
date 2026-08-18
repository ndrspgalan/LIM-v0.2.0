package qa.domain;

import domain.inventory.item.firearms.*;

/** Verificación histórica  actualizada al canon  sin perder los contratos electromagnéticos originales. */
public final class BifilarElectromagneticFirearmVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var pneumatic = FirearmCatalog.repeatingPneumaticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(pneumatic.lethalityProfile().piercing()==55, " eleva el neumático a P55.");
        var rifle = FirearmCatalog.bifilarElectromagneticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.softThresholdIPiercing()==70 && rifle.softThresholdIIPiercing()==80 && rifle.hardThresholdPiercing()==90, "Bifilar P70/P80/P90.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.fulminatingPropertyPresent(), "FULMINANTE debe conservarse.");
        org.junit.jupiter.api.Assertions.assertTrue(FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 99, 84, 85), "FULMINANTE exige AND y sirve en BODY/HEAD.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 100, 0, 85), "Cobertura HEAD 100 debe bloquear FULMINANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 50, 85, 85), "Protección igual bloquea FULMINANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.projectile().material().toLowerCase().contains("tungsten"), "El proyectil sigue siendo de tungsteno.");
        org.junit.jupiter.api.Assertions.assertTrue(!rifle.projectile().material().toLowerCase().contains("cobre"), "El Bifilar no usa camisa de cobre.");
    }
    
}

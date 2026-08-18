package qa.domain;

import domain.inventory.item.firearms.*;
import domain.inventory.item.firearmAccessories.*;

public final class BallisticRebalanceVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyCatalog();
        verifySlingErgonomics();
        verifyOptics();
        verifySubmachineGun();
        verifyTerminalPolicies();
        verifyBifilar();
    }

    private static void verifyCatalog() {
        var pneumatic = FirearmCatalog.repeatingPneumaticRifleV881();
        close(pneumatic.lethalityProfile().piercing(), 55, "Rifle neumático P55");
        org.junit.jupiter.api.Assertions.assertTrue(pneumatic.coupDeGracePropertyPresent(), "Rifle neumático: GOLPE DE GRACIA");
        org.junit.jupiter.api.Assertions.assertTrue(pneumatic.cartridgeDefinition().material().equals("Plomo"), "Rifle neumático sin cobre");

        var pistol = FirearmCatalog.autoloadingPistolV881();
        org.junit.jupiter.api.Assertions.assertTrue(pistol.name().equals("Pistola Autocargadora V881"), "Nombre pistola ");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.fireModes().size()==1 && pistol.activeFireMode()==FireMode.ONE_A, "Pistola 1A exclusiva");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.cartridgeDefinition().capacity()==8, "Pistola 8 cartuchos");
        org.junit.jupiter.api.Assertions.assertTrue(pistol.cartridgeDefinition().material().contains("camisa de cobre"), "Pistola encamisada");

        var smg = FirearmCatalog.submachineGunV881();
        org.junit.jupiter.api.Assertions.assertTrue(smg.cartridgeDefinition().capacity()==25 && smg.activeFireMode()==FireMode.AUTO_A, "Subfusil 25 / AA");
        close(smg.effectiveRangeMeters(),100,"Subfusil 100 m");
        close(smg.lethalityProfile().piercing(),85,"Subfusil P85");
        close(smg.lethalityProfile().blunt(),35,"Subfusil Ct35");
        org.junit.jupiter.api.Assertions.assertTrue(!smg.supportsAiming(), "Subfusil sin AIMING");
        org.junit.jupiter.api.Assertions.assertTrue(smg.supportedFirearmAccessoryMounts().equals(java.util.Set.of(FirearmAccessoryMount.SLING)), "Subfusil solo correa");

        var rifle = FirearmCatalog.repeatingRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.caliber().equals("7,92×57 mm"), "Fusil repetición calibre");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.cartridgeDefinition().capacity()==5, "Fusil repetición 5");
        close(rifle.effectiveRangeMeters(),1500,"Fusil repetición 1500m");
        close(rifle.lethalityProfile().piercing(),95,"Fusil repetición P95");
        close(rifle.lethalityProfile().blunt(),35,"Fusil repetición Ct35");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.admitsAttachment(FirearmAccessoryMount.OPTIC) && !rifle.admitsAttachment(FirearmAccessoryMount.BIPOD), "Fusil repetición óptica sí / bípode no");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.all().size()==9, ": nueve firearms canónicas");
    }

    private static void verifySlingErgonomics() {
        var rifle = FirearmCatalog.repeatingRifleV881();
        double physicalBefore = rifle.weightKg();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.mountAttachment(FirearmAccessoryCatalog.slingV881()), "Debe montar correa");
        close(rifle.weightKg(), physicalBefore + 0.18, "La masa física incluye correa");
        close(rifle.effectiveHandlingWeightKg(), (physicalBefore + 0.18)*0.75, "MEJOR ERGONOMÍA x0,75");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.betterErgonomicsActive(), "Correa activa MEJOR ERGONOMÍA");
    }

    private static void verifyOptics() {
        var rifle = FirearmCatalog.repeatingRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.mountAttachment(FirearmAccessoryCatalog.fiedlerSightV881()), "Fiedler");
        close(rifle.effectiveRangeWithAttachmentsMeters(),1500,"Fiedler no altera alcance");
        rifle.unmountAttachment(FirearmAccessoryMount.OPTIC);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.mountAttachment(FirearmAccessoryCatalog.zeissSightV881()), "Zeiss");
        close(rifle.effectiveRangeWithAttachmentsMeters(),1500,"Zeiss no altera alcance");
        rifle.unmountAttachment(FirearmAccessoryMount.OPTIC);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.mountAttachment(FirearmAccessoryCatalog.winchesterA5SightV881()), "Winchester A5");
        close(rifle.effectiveRangeWithAttachmentsMeters(),1500,"A5 no altera alcance");
        close(rifle.lethalityProfile().piercing(),95,"Óptica no altera P");
    }

    private static void verifySubmachineGun() {
        var smg = FirearmCatalog.submachineGunV881();
        var input = new FirearmInputResolutionPolicy();
        input.resolve(FirearmInput.RIGHT_PRESS, smg);
        close(smg.recoilState().accumulatedVelocityMps(),0.38,"SMG recoil shot1");
        input.resolve(FirearmInput.RIGHT_HOLD, smg);
        close(smg.recoilState().accumulatedVelocityMps(),0.38,"SMG recoil capped from shot2");
        input.resolve(FirearmInput.RIGHT_HOLD, smg);
        close(smg.recoilState().accumulatedVelocityMps(),0.38,"SMG recoil capped from shot3");
    }

    private static void verifyTerminalPolicies() {
        org.junit.jupiter.api.Assertions.assertTrue(CoupDeGracePolicy.isCoupDeGrace(true,99,54,55), "GOLPE DE GRACIA con P superior");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true,100,0,95), "Cobertura HEAD 100 debe bloquear GOLPE DE GRACIA");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true,50,95,95), "Igual P no activa gracia");
        org.junit.jupiter.api.Assertions.assertTrue(FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET,99,84,85), "FULMINANTE BODY/HEAD con AND");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET,100,0,85), "Cobertura HEAD 100 debe bloquear FULMINANTE");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET,50,85,85), "FULMINANTE requiere P superior");
    }

    private static void verifyBifilar() {
        var bifilar = FirearmCatalog.bifilarElectromagneticRifleV881();
        close(bifilar.softThresholdIPiercing(),70,"Bifilar P70");
        close(bifilar.softThresholdIIPiercing(),80,"Bifilar P80");
        close(bifilar.hardThresholdPiercing(),90,"Bifilar P90");
        org.junit.jupiter.api.Assertions.assertTrue(!bifilar.cartridgeDefinition().material().toLowerCase().contains("cobre"), "Bifilar sin cobre");
    }

    private static void close(double actual,double expected,String message){ if(Math.abs(actual-expected)>1e-9) throw new AssertionError(message+" actual="+actual+" expected="+expected); }
    
}

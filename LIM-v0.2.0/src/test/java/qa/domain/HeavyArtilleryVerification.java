package qa.domain;

import domain.control.ControlAction;
import domain.control.InputGesture;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.*;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;

public final class HeavyArtilleryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var anti = FirearmCatalog.antiMaterielCannonV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.baseWeightKg(),20.0), "Antimaterial 20 kg");
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.effectiveDirectRangeMeters(),150.0), "Antimaterial 150 m");
        org.junit.jupiter.api.Assertions.assertTrue(anti.lethalityProfile().piercing()==100 && anti.lethalityProfile().blunt()==100, "Antimaterial 100/0/100");
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.explosionRadiusMeters(),0.5) && !anti.coupDeGracePropertyPresent(), "Explosión 0,5 + sin GOLPE DE GRACIA redundante frente a FULMINANTE");
        org.junit.jupiter.api.Assertions.assertTrue(anti.cartridgeDefinition().capacity()==4 && anti.cartridgeDefinition().footprint().verticalSlots()==2 && anti.cartridgeDefinition().footprint().horizontalSlots()==1, "Cartucho 4 y 2x1 por XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(anti.mountAttachment(FirearmAccessoryCatalog.slingV881()), "Correa antimaterial");
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.effectiveHandlingWeightKg(), (20.0+0.18)*0.75), "Mejor ergonomía");
        org.junit.jupiter.api.Assertions.assertTrue(anti.mountAttachment(FirearmAccessoryCatalog.bipodV881()), "Bípode antimaterial");
        anti.mountedAttachment(domain.inventory.item.firearmAccessories.FirearmAccessoryMount.BIPOD).orElseThrow().deploy();
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.effectiveRecoilVelocityPerShotMps(),0), "Bípode = recoil 0");
        org.junit.jupiter.api.Assertions.assertTrue(anti.mountAttachment(FirearmAccessoryCatalog.fiedlerSightV881()), "Óptica antimaterial");
        org.junit.jupiter.api.Assertions.assertTrue(close(anti.effectiveRangeWithAttachmentsMeters(),150.0), "Óptica no amplía alcance antimaterial");
        var input = new FirearmInputResolutionPolicy();
        var shot = input.resolve(FirearmInput.RIGHT_PRESS, anti);
        org.junit.jupiter.api.Assertions.assertTrue(shot.shotsFired()==1 && anti.recoveringFromShot(), "Disparo y ciclo 1s");
        org.junit.jupiter.api.Assertions.assertTrue(!input.resolve(FirearmInput.RIGHT_PRESS, anti).allowed(), "Bloqueado durante recuperación");
        anti.advanceTime(1.0); anti.triggerState().release();
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_PRESS, anti).shotsFired()==1, "Segundo disparo tras 1s");

        var cluster = FirearmCatalog.clusterCannonV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(cluster.baseWeightKg(),5.0) && close(cluster.effectiveDirectRangeMeters(),150.0), "Racimo 5kg/150m");
        org.junit.jupiter.api.Assertions.assertTrue(cluster.lethalityProfile().slashing()==100 && cluster.impactProfile().burn()==100 && cluster.impactProfile().suffocatingBurn(), "Paquete impacto conjunto");
        org.junit.jupiter.api.Assertions.assertTrue(close(cluster.impactProfile().radiusMeters(),25.0), "Radio 25m");
        org.junit.jupiter.api.Assertions.assertTrue(!cluster.usesPhysicalCartridgeContainer() && cluster.ammunitionCapacity()==1, "Cohete unitario sin cartucho");
        var rocket = AmmunitionCatalog.clusterRocket85mm();
        org.junit.jupiter.api.Assertions.assertTrue(close(rocket.weightKg(),4.0) && rocket.footprint().verticalSlots()==7 && rocket.footprint().horizontalSlots()==1, "Cohete 4kg 7x1 por 650x85x85 mm");
        org.junit.jupiter.api.Assertions.assertTrue(cluster.mountAttachment(FirearmAccessoryCatalog.slingV881()) && cluster.mountAttachment(FirearmAccessoryCatalog.bipodV881()) && cluster.mountAttachment(FirearmAccessoryCatalog.zeissSightV881()), "Accesorios racimo");
        org.junit.jupiter.api.Assertions.assertTrue(close(cluster.effectiveRangeWithAttachmentsMeters(),150.0), "Óptica no amplía racimo");
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RELOAD_HOLD, cluster).action()==FirearmAction.ENTER_CLUSTER_TIMER_CONFIGURATION, "HOLD R temporizador");
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RIGHT_PRESS, cluster).action()==FirearmAction.CYCLE_CLUSTER_TIMER && cluster.timerSeconds()==4, "3->4");
        input.resolve(FirearmInput.RIGHT_PRESS, cluster); org.junit.jupiter.api.Assertions.assertTrue(cluster.timerSeconds()==5, "4->5");
        input.resolve(FirearmInput.RIGHT_PRESS, cluster); org.junit.jupiter.api.Assertions.assertTrue(cluster.timerSeconds()==3, "5->3");
        org.junit.jupiter.api.Assertions.assertTrue(input.resolve(FirearmInput.RELOAD_PRESS, cluster).action()==FirearmAction.CANCEL_CLUSTER_TIMER_CONFIGURATION, "R confirma/sale");

        var pneumatic=FirearmCatalog.repeatingPneumaticRifleV881();
        pneumatic.mountAttachment(FirearmAccessoryCatalog.winchesterA5SightV881());
        org.junit.jupiter.api.Assertions.assertTrue(close(pneumatic.effectiveRangeWithAttachmentsMeters(),150.0), "Neumático queda 150");
        var bifilar=FirearmCatalog.bifilarElectromagneticRifleV881();
        bifilar.mountAttachment(FirearmAccessoryCatalog.winchesterA5SightV881());
        org.junit.jupiter.api.Assertions.assertTrue(close(bifilar.effectiveRangeWithAttachmentsMeters(),420.0), "Bifilar queda 420");
        var repeating=FirearmCatalog.repeatingRifleV881();
        repeating.mountAttachment(FirearmAccessoryCatalog.winchesterA5SightV881());
        org.junit.jupiter.api.Assertions.assertTrue(close(repeating.effectiveRangeWithAttachmentsMeters(),1500.0), "Repetición 1500");
        org.junit.jupiter.api.Assertions.assertTrue(repeating.trajectoryMayContinueBeyondEffectiveDirectRange(), "Tiro directo no destruye trayectoria física");

        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action()==ControlAction.ENTER_CLUSTER_TIMER_CONFIGURATION && b.gesture()==InputGesture.HOLD), "PC HOLD R racimo");
        org.junit.jupiter.api.Assertions.assertTrue(Ps4ControlScheme.canonicalBindings().stream().anyMatch(b -> b.action()==ControlAction.ENTER_CLUSTER_TIMER_CONFIGURATION && b.gesture()==InputGesture.HOLD), "PS4 temporizador sincronizado");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.ANTI_MATERIEL_CANNON_NARRATIVE.contains("cañones de riel estáticos") && FirearmCatalog.ANTI_MATERIEL_CANNON_NARRATIVE.contains("seis metros"), "Narrativa antimaterial estratégica");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.CLUSTER_CANNON_NARRATIVE.contains("bombardeo cinético orbital") && FirearmCatalog.CLUSTER_CANNON_NARRATIVE.contains("4,60 cm"), "Narrativa racimo estratégica");
    }
    
    private static boolean close(double a,double b){return Math.abs(a-b)<0.000001;}
}

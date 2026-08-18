package qa.domain;

import domain.hud.*;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorCatalog;
import domain.targeting.InvisibilityTargetLockPolicy;
import domain.targeting.TargetLockDefinition;

import java.util.Map;

public final class MinimalHudAndTargetingVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract() throws Exception{
        conventionalHudAndEnemyVitalityAreRemoved();
        pauseIsBinarySilentInfrastructure();
        engineerSpineIsTheOnlyPersistentProjection();
        targetingContractsRemainAvailable();
    }

    private static void conventionalHudAndEnemyVitalityAreRemoved() throws Exception {
        absent("domain.hud.HudSnapshot");
        absent("domain.hud.HudConfiguration");
        absent("domain.hud.MinimapHud");
        absent("domain.hud.ObjectiveGuideHud");
        absent("domain.hud.EnemyVitalityVisibilityPolicy");
        absent("domain.hud.EnemyVitalityPresentation");
    }

    private static void pauseIsBinarySilentInfrastructure() {
        org.junit.jupiter.api.Assertions.assertTrue(HudMode.values().length == 2, "HudMode solo debe distinguir tiempo real y pausa.");
        HudModeCyclePolicy policy = new HudModeCyclePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.next(HudMode.REALTIME) == HudMode.PAUSED, "La primera pulsación debe pausar.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.next(HudMode.PAUSED) == HudMode.REALTIME, "La segunda pulsación debe reanudar.");
        org.junit.jupiter.api.Assertions.assertTrue(!HudMode.REALTIME.hudVisible() && !HudMode.PAUSED.hudVisible(),
                "Ningún modo debe reintroducir un HUD convencional.");
    }

    private static void engineerSpineIsTheOnlyPersistentProjection() {
        EngineerSpineProjectionService service = new EngineerSpineProjectionService();
        org.junit.jupiter.api.Assertions.assertTrue(!service.project(EquipmentState.empty()).visible(),
                "Sin el Conjunto del Ingeniero no debe proyectarse nada.");

        var suit = ArmorCatalog.engineerSuit();
        var equipment = new EquipmentState(Map.of(EquipmentSlot.CHEST, suit));
        EngineerSpineIndicator full = service.project(equipment);
        org.junit.jupiter.api.Assertions.assertTrue(full.visible() && close(full.levelRatio(), 1.0), "El traje nuevo debe mostrar la columna completa.");
        org.junit.jupiter.api.Assertions.assertTrue(full.chromaticVariant().equals(EngineerSpineIndicator.CYAN_COOLANT),
                "La columna debe utilizar un único cian refrigerante.");

        suit.applyBluntWear(10);
        EngineerSpineIndicator worn = service.project(equipment);
        org.junit.jupiter.api.Assertions.assertTrue(worn.levelRatio() < 1.0 && worn.levelRatio() > 0,
                "La longitud visible debe disminuir con el desgaste, sin cambiar de color.");
    }

    private static void targetingContractsRemainAvailable() {
        org.junit.jupiter.api.Assertions.assertTrue(TargetLockDefinition.stomachHeight().heightRatio() == 0.55,
                "La fijación al estómago debe conservarse.");
        org.junit.jupiter.api.Assertions.assertTrue(!InvisibilityTargetLockPolicy.canLockTarget(true),
                "La política de invisibilidad debe conservar su comportamiento.");
    }

    private static void absent(String className) throws Exception {
        try {
            Class.forName(className);
            throw new IllegalStateException("La clase retirada sigue presente: " + className);
        } catch (ClassNotFoundException expected) {
            // Correcto.
        }
    }

    private static boolean close(double left, double right) { return Math.abs(left - right) < 0.000001; }
    
}

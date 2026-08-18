package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.ability.CharacterMasteryCollection;
import domain.character.sheet.CharacterSheet;
import domain.combat.CombatTechnique;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.inventory.item.misc.*;
import domain.movement.MobilityPolicy;
import domain.movement.MobilityProfile;

public final class ObjectAndTechniqueVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyDurableWeightIsConstant();
        verifyActionTimes();
        verifyCombatTechniqueContracts();
        verifyFeintProgression();
        verifyCurrencyName();
    }

    private static void verifyDurableWeightIsConstant() {
        UtilityObjectItem whetstone = MiscellaneousItemCatalog.whetstone();
        UtilityObjectItem flint = MiscellaneousItemCatalog.flint();
        double whetstoneWeight = whetstone.weightKg();
        double flintWeight = flint.weightKg();
        whetstone.consumeOne();
        flint.consumeOne();
        org.junit.jupiter.api.Assertions.assertTrue(close(whetstone.weightKg(), whetstoneWeight) && close(whetstoneWeight, 0.400),
                "La piedra debe conservar 0,400 kg durante su durabilidad.");
        org.junit.jupiter.api.Assertions.assertTrue(close(flint.weightKg(), flintWeight) && close(flintWeight, 0.250),
                "El pedernal debe conservar 0,250 kg durante su durabilidad.");
    }

    private static void verifyActionTimes() {
        UtilityObjectItem whetstone = MiscellaneousItemCatalog.whetstone();
        UtilityObjectItem pebble = MiscellaneousItemCatalog.pebble();
        org.junit.jupiter.api.Assertions.assertTrue(close(whetstone.useAnimation().durationRealSeconds(), 8.0), "Afilar debe durar 8 s.");
        org.junit.jupiter.api.Assertions.assertTrue(close(pebble.useAnimation().durationRealSeconds(), 0.8), "Preparar y lanzar el guijarro debe durar 0,8 s.");
        IgnitionResult spark = new IgnitionPolicy().executeDetailed(
                MiscellaneousItemCatalog.amadou(), MiscellaneousItemCatalog.flint(), UtilityAction.GENERATE_SPARK);
        org.junit.jupiter.api.Assertions.assertTrue(spark.successful() && close(spark.durationRealSeconds(), 4.0), "Generar chispa debe durar 4 s.");
        IgnitionResult lock = new IgnitionPolicy().executeDetailed(
                MiscellaneousItemCatalog.amadou(), MiscellaneousItemCatalog.flint(), UtilityAction.IGNITE_LOCK);
        org.junit.jupiter.api.Assertions.assertTrue(lock.successful() && close(lock.durationRealSeconds(), 3.0), "Incendiar cerrojo debe durar 3 s.");
    }

    private static void verifyCombatTechniqueContracts() {
        CombatTechniqueUnlockPolicy policy = new CombatTechniqueUnlockPolicy();
        CharacterSheet minimum = CharacterSheet.of(1,1,1,1,1,1,1,1,1);
        CharacterSheet strength29 = CharacterSheet.of(1,1,1,29,1,1,1,1,1);
        CharacterSheet strength30 = CharacterSheet.of(1,1,1,30,1,1,1,1,1);
        CharacterSheet dexterity19 = CharacterSheet.of(1,1,1,1,19,1,1,1,1);
        CharacterSheet dexterity20 = CharacterSheet.of(1,1,1,1,20,1,1,1,1);
        CharacterSheet dexterity34 = CharacterSheet.of(1,1,1,1,34,1,1,1,1);
        CharacterSheet dexterity35 = CharacterSheet.of(1,1,1,1,35,1,1,1,1);
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.FEINT,dexterity34) && policy.isUnlocked(CombatTechnique.FEINT,dexterity35), "Fintar requiere Destreza 35.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.STAGGERING_STRIKE, strength29), "Golpe desestabilizador no debe desbloquearse con Fuerza 29.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(CombatTechnique.STAGGERING_STRIKE, strength30), "Golpe desestabilizador debe desbloquearse con Fuerza 30.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.DEFLECTION, dexterity19), "Desviar no debe desbloquearse con Destreza 19.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(CombatTechnique.DEFLECTION, dexterity20), "Desviar debe desbloquearse con Destreza 20.");
    }

    private static void verifyFeintProgression() {
        MobilityPolicy policy=new MobilityPolicy(); CharacterMasteryCollection none=CharacterMasteryCollection.jacobCanonical(); double h=1.72;
        double d35=policy.resolve(h,CharacterSheet.of(1,1,1,1,35,1,1,1,1),none).feintDistanceMeters();
        double d40=policy.resolve(h,CharacterSheet.of(1,1,1,1,40,1,1,1,1),none).feintDistanceMeters();
        double d50=policy.resolve(h,CharacterSheet.of(1,1,1,1,50,1,1,1,1),none).feintDistanceMeters();
        double d70=policy.resolve(h,CharacterSheet.of(1,1,1,1,70,1,1,1,1),none).feintDistanceMeters();
        org.junit.jupiter.api.Assertions.assertTrue(close(d35,h*.35)&&d35<d40&&d40<d50&&close(d50,h*.50)&&close(d70,d50),"Finta 35%-50% de altura entre DEX35-50 con tope.");
    }

    private static void verifyCurrencyName() {
        org.junit.jupiter.api.Assertions.assertTrue(CurrencyType.BERYLARE.label().equals("Berylares"), "El término canónico debe ser BERYLARE.");
    }

    private static boolean close(double left, double right) { return Math.abs(left - right) < 0.000001; }
    
}

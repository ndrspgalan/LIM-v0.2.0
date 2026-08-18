package qa.domain;

import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.movement.*;
import domain.throwing.*;

public final class FormulaReworkVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyRecoilCurve();
        verifyThrowingHumanAndExtraordinaryDomains();
        verifyCombatTechniqueThresholds();
        verifyExplorationTechniqueThresholds();
        verifyClimbingPreservesUniversalDomain();
    }

    private static void verifyRecoilCurve() {
        double at1=StaggerPolicy.knockbackDistanceMeters(1), at25=StaggerPolicy.knockbackDistanceMeters(25), at50=StaggerPolicy.knockbackDistanceMeters(50);
        org.junit.jupiter.api.Assertions.assertTrue(close(at1,.5)&&at1<at25&&at25<at50,"Retroceso universal FUERZA 1-50 debe crecer.");
        org.junit.jupiter.api.Assertions.assertTrue(close(at50,2.0)&&close(StaggerPolicy.knockbackDistanceMeters(120),2.0),"Retroceso satura en 2 m desde FUERZA 50.");
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.resolve(50).staggerDurationSeconds(),2.0),"Aturdimiento máximo 2 s.");
    }

    private static void verifyThrowingHumanAndExtraordinaryDomains() {
        ThrowPolicy policy = new ThrowPolicy();
        ThrownPayload pebble = ThrownPayload.item("Guijarro", ThrowProfile.improvised(0.080, true));
        ThrownPayload heavy = ThrownPayload.item("Arma", ThrowProfile.improvised(0.700, true));
        ThrowResult human = policy.resolve(new ThrowRequest(75, 75, 1.72, 35), pebble);
                ThrowResult heavyHuman = policy.resolve(new ThrowRequest(75, 75, 1.72, 35), heavy);
        org.junit.jupiter.api.Assertions.assertTrue(close(human.horizontalDistanceMeters(), 75.0), "DEX75 debe equivaler a 75 m a 35 grados.");
        org.junit.jupiter.api.Assertions.assertTrue(close(heavyHuman.horizontalDistanceMeters(), human.horizontalDistanceMeters()), "La masa no modifica el alcance gobernado por DESTREZA.");
        org.junit.jupiter.api.Assertions.assertTrue(close(human.damage().blunt(), 75.08), "B debe ser FUERZA + masa en kg.");
        org.junit.jupiter.api.Assertions.assertTrue(close(heavyHuman.damage().blunt(), 75.7), "La masa añade B1 por kilogramo.");
    }

    private static void verifyCombatTechniqueThresholds() {
        CombatTechniqueUnlockPolicy policy = new CombatTechniqueUnlockPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.FEINT, sheet(1,34,1)) && policy.isUnlocked(CombatTechnique.FEINT,sheet(1,35,1)), "Fintar requiere DESTREZA 35.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.DEFLECTION, sheet(1, 19, 1)), "Desviar requiere DESTREZA 20.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(CombatTechnique.DEFLECTION, sheet(1, 20, 1)), "Desviar debe abrirse en DESTREZA 20.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(CombatTechnique.STAGGERING_STRIKE, sheet(29, 1, 1)), "Golpe desestabilizador requiere FUERZA 30.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(CombatTechnique.STAGGERING_STRIKE, sheet(30, 1, 1)), "Golpe desestabilizador debe abrirse en FUERZA 30.");
    }

    private static void verifyExplorationTechniqueThresholds() {
        ExplorationTechniqueUnlockPolicy policy = new ExplorationTechniqueUnlockPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(ExplorationTechnique.RIDE, sheet(1, 19, 25)), "Cabalgar requiere DESTREZA 20.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.isUnlocked(ExplorationTechnique.RIDE, sheet(1, 20, 24)), "Cabalgar requiere CARISMA 25.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(ExplorationTechnique.RIDE, sheet(1, 20, 25)), "Cabalgar debe abrirse en DESTREZA 20 y CARISMA 25.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(ExplorationTechnique.CLIMB, sheet(20, 20, 1)), "Escalar conserva FUERZA 20 y DESTREZA 20.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.isUnlocked(ExplorationTechnique.SWIM, sheet(15, 15, 1)), "Nadar conserva FUERZA 15 y DESTREZA 15.");
    }

    private static void verifyClimbingPreservesUniversalDomain() {
        ClimbingPolicy policy = new ClimbingPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(policy.maximumClimbAngleDegrees(sheet(75, 20, 1)), 120.0), "FUERZA 75 debe alcanzar el máximo canónico de escalada.");
    }

    private static CharacterSheet sheet(int strength, int dexterity, int charisma) {
        return CharacterSheet.of(1, 1, 1, strength, dexterity, 1, 1, charisma, 1);
    }

    private static boolean close(double left, double right) { return Math.abs(left - right) < 0.000001; }
    
}

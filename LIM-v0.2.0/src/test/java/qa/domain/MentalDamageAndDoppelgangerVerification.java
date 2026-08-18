package qa.domain;

import domain.bestiarium.interstice.faerie.DoppelgangerDamagePolicy;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.CurrentCharacterStats;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.combat.MentalPressurePolicy;
import domain.combat.PhysicalDamage;
import domain.inventory.InventoryState;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import domain.inventory.item.firearms.FulminatingPolicy;

public final class MentalDamageAndDoppelgangerVerification {
    private static final double EPS = 1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverageNoLongerGrantsTerminalImmunity();
        curseAndFrenzyUsePercentageResistanceAndSanity();
        doppelgangerUsesSymmetricMentalChannels();
        canonicalKenanAgainstDoppelganger();
    }

    private static void coverageNoLongerGrantsTerminalImmunity() {
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true, 100, 54, 55),
                "100 % de cobertura no debe neutralizar GOLPE DE GRACIA si P supera la protección.");
        org.junit.jupiter.api.Assertions.assertTrue(!FulminatingPolicy.isFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET, 100, 89, 90),
                "100 % de cobertura no debe neutralizar FULMINANTE si P supera la protección.");
        org.junit.jupiter.api.Assertions.assertTrue(!CoupDeGracePolicy.isCoupDeGrace(true, 0, 55, 55),
                "La igualdad de P debe seguir bloqueando GOLPE DE GRACIA.");
    }

    private static void curseAndFrenzyUsePercentageResistanceAndSanity() {
        var impact = MentalPressurePolicy.resolve(100, 25, 40);
        close(impact.netDamage(), 75, "25 % de resistencia debe dejar 75 de daño mental.");
        close(impact.mentalRecoilUnits(), 45, "CORDURA 40 debe reducir en 40 % la presión mental restante.");
        var capped = MentalPressurePolicy.resolve(100, 0, 140);
        close(capped.mentalRecoilUnits(), 0, "CORDURA superior a 100 debe saturar a 100 % para stagger.");
    }

    private static void doppelgangerUsesSymmetricMentalChannels() {
        var result = new DoppelgangerDamagePolicy().transmute(
                new PhysicalDamage(30, 20, 50), 80, 40,
                25, 50, 20);
        close(result.rawFrenzy(), 180, "Frenesí bruto del Doppelgänger.");
        close(result.netFrenzy(), 135, "Resistencia 25 % a Frenesí.");
        close(result.netCurse(), 20, "Resistencia 50 % a Maldición.");
        close(result.frenzyMentalRecoilUnits(), 108, "CORDURA 20 % sobre Frenesí.");
        close(result.curseMentalRecoilUnits(), 16, "CORDURA 20 % sobre Maldición.");
        close(result.totalMentalRecoilUnits(), 124, "Ambas presiones mentales deben acumularse antes de stagger.");
        org.junit.jupiter.api.Assertions.assertTrue(result.stagger().staggered(), "El impacto combinado debe producir stagger.");
    }

    private static void canonicalKenanAgainstDoppelganger() {
        CharacterSheet kenan = CharacterSheet.of(27, 40, 12, 30, 20, 30, 3, 25, 11);
        CurrentCharacterStats stats = new DerivedStatisticsCalculator().calculate(
                kenan, Gender.HOMBRE, InventoryState.emptyWithoutPersonalTransport(), domain.environment.time.DayPhase.DAY);
        double frenzyResistance = stats.resistances().frenzy().orElseThrow();
        double curseResistance = stats.resistances().curse().orElseThrow();
        double sanity = stats.sanity().orElseThrow();
        close(frenzyResistance, 1.2, "Kenan obtiene +0,1 pp/nivel de Frenesí por ADAPTABILIDAD masculina.");
        close(curseResistance, 1.2, "Kenan obtiene +0,1 pp/nivel de Maldición por ADAPTABILIDAD masculina.");
        close(sanity, 30, "CORDURA canónica de Kenan.");

        var moderate = new DoppelgangerDamagePolicy().transmute(
                new PhysicalDamage(20, 20, 20), 0, 0,
                frenzyResistance, curseResistance, sanity);
        close(moderate.netFrenzy(), 59.28, "El daño físico reflejado se transmuta a Frenesí y después aplica resistencia.");
        close(moderate.totalMentalRecoilUnits(), 41.496, "CORDURA 30 % debe actuar sobre el Frenesí ya reducido por resistencia.");
        org.junit.jupiter.api.Assertions.assertTrue(moderate.stagger().staggered(), "Un reflejo moderado de 60 debe tambalear a Kenan.");

        var curseMix = new DoppelgangerDamagePolicy().transmute(
                new PhysicalDamage(20, 20, 20), 0, 30,
                frenzyResistance, curseResistance, sanity);
        close(curseMix.totalNetDamage(), 88.92, "Frenesí y Maldición deben conservar canales separados tras sus resistencias.");
        close(curseMix.totalMentalRecoilUnits(), 62.244, "Ambos canales deben sufrir la misma CORDURA porcentual tras resistencias.");
    }

    private static void close(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPS) throw new AssertionError(message + " Esperado=" + expected + " actual=" + actual);
    }
    
}

package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.character.Gender;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.character.sheet.DamageResistanceProfile;
import domain.character.sheet.DerivedStatisticsCalculator;

import java.util.List;

public final class GenderedAttributesAndMeadVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyAdaptability();
        verifySoftcaps();
        verifyMead();
    }

    private static void verifyAdaptability() {
        DerivedStatisticsCalculator calculator = new DerivedStatisticsCalculator();
        DamageResistanceProfile man = calculator.resistanceProfileFromAdaptability(75, Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(close(man.piercing().orElseThrow(), 15.0), "Hombre: Perforante 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.slashing().orElseThrow(), 15.0), "Hombre: Cortante 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.blunt().orElseThrow(), 15.0), "Hombre: Contundente 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.burn().orElseThrow(), 7.5), "Hombre: Quemadura 7,5 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.electricity().orElseThrow(), 0.0), "Hombre: Electricidad 7,5 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.poison().orElseThrow(), 7.5), "Hombre: Veneno no progresa ordinariamente.");
        org.junit.jupiter.api.Assertions.assertTrue(close(man.frenzy().orElseThrow(), 7.5), "Hombre: Frenesí no progresa ordinariamente.");

        DamageResistanceProfile woman = calculator.resistanceProfileFromAdaptability(75, Gender.MUJER);
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.poison().orElseThrow(), 7.5), "Mujer: Veneno 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.frost().orElseThrow(), 7.5), "Mujer: Congelación 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.curse().orElseThrow(), 18.75), "Mujer: Maldición 18,75 % en 75 (+0,25 pp/nivel).");
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.frenzy().orElseThrow(), 3.75), "Mujer: Frenesí 15 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.burn().orElseThrow(), 7.5), "Mujer: Quemadura 7,5 % en 75.");
        org.junit.jupiter.api.Assertions.assertTrue(close(woman.piercing().orElseThrow(), 7.5), "Mujer: Perforante no progresa ordinariamente.");

        DamageResistanceProfile extraordinary = calculator.resistanceProfileFromAdaptability(76, Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(close(extraordinary.poison().orElseThrow(), 8.2), "El tramo extraordinario vuelve a afectar a todos los canales.");
        org.junit.jupiter.api.Assertions.assertTrue(close(extraordinary.piercing().orElseThrow(), 15.7), "El tramo extraordinario se suma al perfil masculino.");
    }

    private static void verifySoftcaps() {
        GenderSoftcapProfile profile = GenderSoftcapProfile.canonical();
        org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(Gender.MUJER, Attribute.FUERZA).equals(List.of(21, 30)),
                "FUERZA femenina debe usar 21 y 30.");
        org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(Gender.HOMBRE, Attribute.CARISMA).equals(List.of(25, 50)),
                "CARISMA masculina debe usar 25 y hardcap 50.");
        org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(Gender.MUJER, Attribute.CARISMA).equals(List.of(18, 21, 40)),
                "CARISMA femenina debe usar 18, 21 y hardcap ordinario 40.");
    }

    private static void verifyMead() {
        var mead = new domain.consumable.MeadPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(mead.attackStaminaMultiplier(1.3, true), 1.0), "Hidromiel debe normalizar ataques por encima de x1.");
        org.junit.jupiter.api.Assertions.assertTrue(close(mead.regenerationDelaySeconds(true, 0.7), 1.20), "Hidromiel debe imponer latencia 1,20 s.");
        org.junit.jupiter.api.Assertions.assertTrue(!mead.canTargetLock(true) && mead.constantSway(true), "Hidromiel impide lock-on y causa tambaleo.");
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < 0.000001; }
    
}

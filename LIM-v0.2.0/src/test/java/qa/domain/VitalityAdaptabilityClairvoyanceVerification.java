package qa.domain;

import domain.character.Gender;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.character.sheet.HealthRegenerationCadencePolicy;
import presentation.menu.AdaptabilityNarrative;
import presentation.menu.ClairvoyanceNarrative;
import presentation.menu.VitalityNarrative;

public final class VitalityAdaptabilityClairvoyanceVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var calc = new DerivedStatisticsCalculator();
        var man = calc.resistanceProfileFromAdaptability(75, Gender.HOMBRE);
        close(man.piercing().orElseThrow(), 15.0, "Perforante hombre");
        close(man.poison().orElseThrow(), 7.5, "Veneno hombre");
        close(man.electricity().orElseThrow(), 0.0, "Electricidad hombre");
        var woman = calc.resistanceProfileFromAdaptability(75, Gender.MUJER);
        close(woman.piercing().orElseThrow(), 7.5, "Perforante mujer");
        close(woman.curse().orElseThrow(), 18.75, "Maldición mujer +0,25/nivel");
        close(woman.frenzy().orElseThrow(), 3.75, "Frenesí mujer");
        close(woman.electricity().orElseThrow(), 0.0, "Electricidad mujer");

        var cadence = new HealthRegenerationCadencePolicy();
        close(cadence.baselineSeconds(Gender.HOMBRE), 6.0, "PV REGEN hombre");
        close(cadence.baselineSeconds(Gender.MUJER), 5.0, "PV REGEN mujer");
        close(cadence.resolveSeconds(Gender.MUJER, true), 1.0, "Inyección estimulante");

        org.junit.jupiter.api.Assertions.assertTrue(VitalityNarrative.descriptionFor(74).contains("no se me ocurría nada"), "VITALIDAD plana 1-74");
        org.junit.jupiter.api.Assertions.assertTrue(VitalityNarrative.descriptionFor(75).trim().equals("just SiO2 maxxing dude"), "VITALIDAD 75");
        org.junit.jupiter.api.Assertions.assertTrue(AdaptabilityNarrative.descriptionFor(75, Gender.MUJER).trim().equals("just SiO2 maxxing dude"), "ADAPTABILIDAD 75");
        org.junit.jupiter.api.Assertions.assertTrue(VitalityNarrative.descriptionFor(76).equals(AdaptabilityNarrative.descriptionFor(76, Gender.MUJER)), "CONFIGURATIO ORIGINALIS común");
        org.junit.jupiter.api.Assertions.assertTrue(!AdaptabilityNarrative.descriptionFor(12, Gender.HOMBRE).equals(AdaptabilityNarrative.descriptionFor(12, Gender.MUJER)), "ADAPTABILIDAD segregada");
        org.junit.jupiter.api.Assertions.assertTrue(ClairvoyanceNarrative.descriptionFor(1).equals(ClairvoyanceNarrative.descriptionFor(75)), "CLARIVIDENCIA única");
    }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>1e-9) throw new IllegalStateException(m+": "+a+" != "+b); }
    
}

package qa.domain;

import domain.character.Gender;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.combat.MentalPressurePolicy;
import domain.combat.StaggerPolicy;
import presentation.menu.AttributeLevelUpGuidance;

import java.util.List;

public final class AttributeTechnicalGuidanceVerification {
    private static final String[] FORBIDDEN_MASTERY_NAMES = {
            "ELECTROGÉNESIS", "TRIBOGÉNESIS", "PULSIÓN", "AURA DE PULSIÓN",
            "EXPLOSIÓN CINÉTICA", "ENDURECIMIENTO POTENCIAL", "LIBERACIÓN HELICOIDAL",
            "OPTIMIZACIÓN HELICOIDAL", "ANULACIÓN", "EMPATÍA ANIMAL", "HOMEOSTASIS TÉRMICA",
            "TRANSMUTACIÓN", "OVERCLOCK", "OVERDRIVE", "METAMORPHOSIS", "MIRAGE", "MIRROR'S EDGE",
            "ESPÍRITU INFATIGABLE", "REGENERACIÓN THETA", "DRENAR", "RESTAURAR", "CUSTODIA",
            "CAPITALIZAR", "RENTABILIZAR", "PROVOCAR", "GRITO DE GUERRA", "TRAYECTORIA CONVERGENTE",
            "INVISIBILIDAD"
    };

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var softcaps = GenderSoftcapProfile.canonical();
        org.junit.jupiter.api.Assertions.assertTrue(softcaps.softcaps(Gender.HOMBRE, Attribute.FE).equals(List.of(3,13,32,40,60)), "FE hombre debe incluir softcap 40.");
        org.junit.jupiter.api.Assertions.assertTrue(softcaps.softcaps(Gender.MUJER, Attribute.FE).equals(List.of(3,13,32,40,60)), "FE mujer debe incluir softcap 40.");

        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.resolve(1).staggerDurationSeconds(), .5), "Stagger mínimo 0,5 s.");
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.resolve(50).staggerDurationSeconds(), 2.0), "Stagger máximo 2 s.");
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.resolve(120).staggerDurationSeconds(), 2.0), "Stagger satura en 2 s.");

        var mental = MentalPressurePolicy.resolve(100, 25, 40);
        org.junit.jupiter.api.Assertions.assertTrue(close(mental.netDamage(), 75), "La resistencia debe preceder a CORDURA.");
        org.junit.jupiter.api.Assertions.assertTrue(close(mental.mentalRecoilUnits(), 45), "CORDURA debe reducir la presión que alimenta StaggerPolicy.");

        for (Attribute a : Attribute.values()) {
            String text = AttributeLevelUpGuidance.descriptionOf(a);
            org.junit.jupiter.api.Assertions.assertTrue(text != null && !text.isBlank(), "Falta guía técnica para " + a);
            for (String mastery : FORBIDDEN_MASTERY_NAMES) {
                org.junit.jupiter.api.Assertions.assertTrue(!text.contains(mastery), "La guía de " + a + " filtra una maestría: " + mastery);
            }
        }

        String vitality = AttributeLevelUpGuidance.descriptionOf(Attribute.VITALIDAD);
        String adaptability = AttributeLevelUpGuidance.descriptionOf(Attribute.ADAPTABILIDAD);
        String sharedHint = "Tu corazón todavía recuerda: hubo una configuración anterior a ésta. No era más resistente porque soportase mejor la agresión, sino porque la materia misma de la que dependía su continuidad admitía otra forma de organizar la vida.";
        org.junit.jupiter.api.Assertions.assertTrue(vitality.contains(sharedHint) && adaptability.contains(sharedHint), "CONFIGURATIO ORIGINALIS debe compartir exactamente la misma pista.");

        String charisma = AttributeLevelUpGuidance.descriptionOf(Attribute.CARISMA);
        org.junit.jupiter.api.Assertions.assertTrue(charisma.contains("quizá el mercado lleve tiempo valorándote a ti también"), "CARISMA debe conservar la pista comercial femenina.");
        String clairvoyance = AttributeLevelUpGuidance.descriptionOf(Attribute.CLARIVIDENCIA);
        org.junit.jupiter.api.Assertions.assertTrue(clairvoyance.endsWith("Continúa mirando dentro de ti.\n"), "CLARIVIDENCIA debe cerrar con la pista acordada.");
        org.junit.jupiter.api.Assertions.assertTrue(!AttributeLevelUpGuidance.descriptionOf(Attribute.FE).contains("Tu corazón todavía recuerda"), "FE no necesita pista externa.");
    }

    private static boolean close(double a, double b) { return Math.abs(a-b) < 1e-9; }
    
}

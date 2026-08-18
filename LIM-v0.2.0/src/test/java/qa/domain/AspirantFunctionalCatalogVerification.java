package qa.domain;

import domain.bestiarium.physical_plane.aspirant.*;
import domain.combat.DamageType;
import domain.social.Subprofession;
import java.util.EnumSet;

public final class AspirantFunctionalCatalogVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        catalog();
        evolutionaryAffinity();
        domains();
        subprofessionCoverage();
        individualProfile();
        materialDamage();
        doctrine();
    }

    private static void catalog() {
        org.junit.jupiter.api.Assertions.assertTrue(AspirantReferenceCatalog.all().size() == 30, " debe cerrar 30 arquitecturas zoológicas funcionales.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantReferenceCatalog.all().keySet().equals(EnumSet.allOf(AspirantReferenceId.class)), "Todo AspirantReferenceId debe tener perfil.");
        for (var p : AspirantReferenceCatalog.all().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(!p.ecology().humanDrives().isEmpty(), "Cada arquitectura necesita una pulsión humana legible.");
            org.junit.jupiter.api.Assertions.assertTrue(!p.ecology().explicitlyPreferredSubprofessions().isEmpty(), "Cada arquitectura necesita refugios profesionales explícitos.");
            org.junit.jupiter.api.Assertions.assertTrue(!p.anatomicalSynthesis().isBlank(), "Cada arquitectura necesita síntesis anatómica.");
        }
    }

    private static void evolutionaryAffinity() {
        long primate = count(AspirantEvolutionaryAffinity.PRIMATE_NEAR);
        long mammal = count(AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL);
        long vertebrate = count(AspirantEvolutionaryAffinity.OTHER_VERTEBRATE);
        long remote = count(AspirantEvolutionaryAffinity.DISTANT_METAZOAN);
        org.junit.jupiter.api.Assertions.assertTrue(primate == 1 && mammal == 13 && vertebrate == 10 && remote == 6, "Distribución evolutiva  inesperada.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantEvolutionaryAffinity.PRIMATE_NEAR.minimumCambiaformasHumanDeviation()
                < AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL.minimumCambiaformasHumanDeviation(), "Primate debe admitir menor desviación.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL.minimumCambiaformasHumanDeviation()
                < AspirantEvolutionaryAffinity.OTHER_VERTEBRATE.minimumCambiaformasHumanDeviation(), "Vertebrado distante debe exigir más refundición que mamífero.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantEvolutionaryAffinity.OTHER_VERTEBRATE.minimumCambiaformasHumanDeviation()
                < AspirantEvolutionaryAffinity.DISTANT_METAZOAN.minimumCambiaformasHumanDeviation(), "Metazoo distante debe exigir la mayor refundición.");
    }

    private static long count(AspirantEvolutionaryAffinity affinity) {
        return AspirantReferenceCatalog.all().values().stream().filter(p -> p.evolutionaryAffinity() == affinity).count();
    }

    private static void domains() {
        EnumSet<AspirantMobilityDomain> seen = EnumSet.noneOf(AspirantMobilityDomain.class);
        AspirantReferenceCatalog.all().values().forEach(p -> seen.addAll(p.ecology().mobilityDomains()));
        org.junit.jupiter.api.Assertions.assertTrue(seen.equals(EnumSet.allOf(AspirantMobilityDomain.class)), "El catálogo debe cubrir tierra, aire, agua y anfibio.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantReferenceCatalog.profile(AspirantReferenceId.BAT).ecology().mobilityDomains().contains(AspirantMobilityDomain.AERIAL), "Quiróptero debe habilitar arquitectura aérea.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantReferenceCatalog.profile(AspirantReferenceId.CEPHALOPOD).ecology().mobilityDomains().contains(AspirantMobilityDomain.AQUATIC), "Cefalópodo debe habilitar arquitectura acuática.");
    }

    private static void subprofessionCoverage() {
        for (var s : Subprofession.values()) {
            org.junit.jupiter.api.Assertions.assertTrue(!AspirantSubprofessionAffinityPolicy.compatibleReferences(s).isEmpty(), "Subprofesión sin referente ASPIRANT natural: " + s);
        }
    }

    private static void individualProfile() {
        var porcine = AspirantReferenceCatalog.profile(AspirantReferenceId.PORCINE);
        var human = new AspirantMorphology(porcine.animalReference(), new AnthropometricDeviation(1), new AnthropometricDeviation(9));
        var changed = new AspirantMorphology(porcine.animalReference(), new AnthropometricDeviation(7), new AnthropometricDeviation(3));
        var p = new AspirantProfile(AspirantReferenceId.PORCINE, human, changed, Subprofession.TAVERN_KEEPER,
                new AspirantSomaticHistory("Disponibilidad alimentaria constante, desperdicio y rutina de cocina reforzaron durante años la misma solución somática."));
        org.junit.jupiter.api.Assertions.assertTrue(p.morphology(AspirantForm.HUMANA).region() == AspirantMorphologyRegion.HUMAN_DOMINANT, "HUMANA debe conservar predominio humano.");
        org.junit.jupiter.api.Assertions.assertTrue(p.morphology(AspirantForm.CAMBIAFORMAS).region() == AspirantMorphologyRegion.ANIMALOID_SYNTHESIS, "CAMBIAFORMAS extrema debe ser síntesis animaloide.");
        org.junit.jupiter.api.Assertions.assertTrue(p.subprofession(AspirantForm.HUMANA) == Subprofession.TAVERN_KEEPER && p.subprofession(AspirantForm.CAMBIAFORMAS) == Subprofession.TAVERN_KEEPER,
                "La persona conserva profesión en ambas formas.");

        var fly = AspirantReferenceCatalog.profile(AspirantReferenceId.DIPTERAN);
        var flyHuman = new AspirantMorphology(fly.animalReference(), new AnthropometricDeviation(1), new AnthropometricDeviation(9));
        var tooSoft = new AspirantMorphology(fly.animalReference(), new AnthropometricDeviation(7), new AnthropometricDeviation(3));
        boolean rejected = false;
        try {
            new AspirantProfile(AspirantReferenceId.DIPTERAN, flyHuman, tooSoft, Subprofession.SANITATION_OPERATOR,
                    new AspirantSomaticHistory("Materia orgánica degradada y saneamiento."));
        } catch (IllegalArgumentException expected) { rejected = true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "Un metazoo distante no puede aparecer con CAMBIAFORMAS demasiado suave.");

        var flyChanged = new AspirantMorphology(fly.animalReference(), new AnthropometricDeviation(8), new AnthropometricDeviation(3));
        new AspirantProfile(AspirantReferenceId.DIPTERAN, flyHuman, flyChanged, Subprofession.SANITATION_OPERATOR,
                new AspirantSomaticHistory("Materia orgánica degradada y saneamiento durante años justifican una refundición extrema."));

        boolean animalRejected = false;
        try {
            var literalAnimal = new AspirantMorphology(porcine.animalReference(), new AnthropometricDeviation(10), new AnthropometricDeviation(0));
            new AspirantProfile(AspirantReferenceId.PORCINE, human, literalAnimal, Subprofession.TAVERN_KEEPER,
                    new AspirantSomaticHistory("Convergencia zoológica literal inválida."));
        } catch (IllegalArgumentException expected) { animalRejected = true; }
        org.junit.jupiter.api.Assertions.assertTrue(animalRejected, "ASPIRANT nunca debe converger literalmente al animal.");
    }

    private static void materialDamage() {
        var damage = new AspirantDamagePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(damage.outgoingType(AspirantForm.CAMBIAFORMAS, DamageType.BLUNT) == DamageType.BLUNT, "CAMBIAFORMAS debe conservar daño contundente físico.");
        org.junit.jupiter.api.Assertions.assertTrue(damage.outgoingType(AspirantForm.CAMBIAFORMAS, DamageType.SLASHING) == DamageType.SLASHING, "CAMBIAFORMAS debe conservar daño cortante físico.");
    }

    private static void doctrine() {
        org.junit.jupiter.api.Assertions.assertTrue(AspirantDoctrine.CANON.contains("porcentaje universal de ADN"), "La doctrina debe rechazar porcentajes universales de ADN.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantDoctrine.CANON.contains("treinta arquitecturas funcionales"), "La exhaustividad funcional debe quedar explícita.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantDoctrine.CANON.contains("vergonzoso reconocer"), "La intención de horror conductual debe quedar explícita.");
        org.junit.jupiter.api.Assertions.assertTrue(!AspirantDoctrine.CANON.contains("todos sus ataques se expresan como MALDICIÓN"), "No debe sobrevivir la ontología mística antigua.");
    }

    
}

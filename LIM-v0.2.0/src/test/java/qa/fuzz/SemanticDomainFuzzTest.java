package qa.fuzz;

import domain.bestiarium.physical_plane.ancient.AnteoChargedPropertyPolicy;
import domain.bestiarium.physical_plane.ferae.FeraeBranch;
import domain.bestiarium.physical_plane.ferae.FeraeCatalog;
import domain.bestiarium.physical_plane.ferae.FeraeSex;
import domain.bestiarium.physical_plane.ferae.charisma.CharismaFeraeProfiles;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.character.progression.AttributeActorCapPolicy;
import domain.character.progression.AttributeActorScope;
import domain.movement.LocomotionDistancePolicy;
import domain.movement.LocomotionMode;
import domain.social.Subprofession;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.property.support.SeededPropertySupport;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fuzzing semántico : genera estados válidos desde autoridades LIM, nunca bytes/números
 * arbitrarios sin significado. El primer fallo siempre es reproducible por seed + índice.
 */
@Tag("fuzz")
@Tag("stress-smoke")
final class SemanticDomainFuzzTest {
    private final AnteoChargedPropertyPolicy anteo = new AnteoChargedPropertyPolicy();
    private final LocomotionDistancePolicy locomotion = new LocomotionDistancePolicy();

    @Test
    void validGeneratedDomainStatesNeverBreakCoreInvariants() {
        long seed = SeededPropertySupport.fuzzSeed();
        var random = SeededPropertySupport.random(seed);
        int cases = SeededPropertySupport.fuzzCases();

        for (int i = 0; i < cases; i++) {
            int family = random.nextInt(5);
            int caseIndex = i;
            SeededPropertySupport.check("semantic-fuzz", seed, i, "family=" + family,
                    () -> runFamily(random, family, caseIndex));
        }
    }

    private void runFamily(java.util.SplittableRandom random, int family, int caseIndex) {
        switch (family) {
            case 0 -> randomSheet(random, caseIndex);
            case 1 -> randomAnteoImpact(random);
            case 2 -> randomLocomotion(random);
            case 3 -> randomSubprofession(random);
            case 4 -> randomFerae(random);
            default -> throw new AssertionError("Familia fuzz desconocida: " + family);
        }
    }

    private static void randomSheet(java.util.SplittableRandom random, int caseIndex) {
        AttributeActorScope[] scopes = AttributeActorScope.values();
        AttributeActorScope scope = scopes[random.nextInt(scopes.length)];
        EnumMap<Attribute,Integer> values = new EnumMap<>(Attribute.class);
        int total = 0;
        for (Attribute a : Attribute.values()) {
            int maximum = AttributeActorCapPolicy.absoluteMaximum(scope, a);
            int v = random.nextInt(CharacterSheet.MINIMUM_ATTRIBUTE_VALUE, maximum + 1);
            values.put(a, v);
            total += v;
        }
        var sheet = new CharacterSheet(values);
        AttributeActorCapPolicy.requireValid(scope, sheet);
        assertEquals(total, sheet.totalAttributeLevel());
        Attribute selected = Attribute.values()[caseIndex % Attribute.values().length];
        assertEquals(values.get(selected).intValue(), sheet.valueOf(selected));
    }

    private void randomAnteoImpact(java.util.SplittableRandom random) {
        double max = 1.0 + random.nextDouble() * 500.0;
        double current = random.nextDouble() * max;
        double net = random.nextDouble() * 2_000.0;
        var r = anteo.resolve(net, current, max);
        assertTrue(r.staminaAfter() >= current && r.staminaAfter() <= max);
        assertEquals(net, r.electricityConvertedToStamina() + r.healthDamage(), 1e-9);
    }

    private void randomLocomotion(java.util.SplittableRandom random) {
        var modes = LocomotionMode.values();
        var genders = Gender.values();
        var mode = modes[random.nextInt(modes.length)];
        var gender = genders[random.nextInt(genders.length)];
        double height = 0.05 + random.nextDouble() * 4.95;
        double speed = locomotion.metersPerSecond(mode, gender, height);
        assertTrue(Double.isFinite(speed) && speed > 0.0);
        assertEquals(height * locomotion.coefficient(mode, gender), speed, 1e-12);
    }

    private static void randomSubprofession(java.util.SplittableRandom random) {
        var values = Subprofession.values();
        var s = values[random.nextInt(values.length)];
        var g = Gender.values()[random.nextInt(Gender.values().length)];
        assertTrue(Double.isFinite(s.canonicalHeightMeters(g)) && s.canonicalHeightMeters(g) > 0.0);
        assertFalseBlank(s.label());
        assertFalseBlank(s.narrativeDescription());
    }

    private static void randomFerae(java.util.SplittableRandom random) {
        var branch = FeraeBranch.values()[random.nextInt(FeraeBranch.values().length)];
        var species = FeraeCatalog.branch(branch).get(random.nextInt(FeraeCatalog.branch(branch).size()));
        var sex = FeraeSex.values()[random.nextInt(FeraeSex.values().length)];
        var profile = branch == FeraeBranch.CARISMA
                ? CharismaFeraeProfiles.of(species, sex)
                : IntelligenceFeraeProfiles.of(species, sex);
        assertEquals(profile.attributes().totalAttributeLevel(), profile.canonicalLevel());
        assertTrue(profile.canonicalHeightMeters() > 0.0);
        assertFalseBlank(profile.genericNarrative());
        assertFalseBlank(profile.sexNarrative());
    }

    private static void assertFalseBlank(String value) {
        assertTrue(value != null && !value.isBlank());
    }
}

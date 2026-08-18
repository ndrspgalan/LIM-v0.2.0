package qa.property;

import domain.character.Gender;
import domain.movement.LocomotionDistancePolicy;
import domain.movement.LocomotionMode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.property.support.SeededPropertySupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("property")
@Tag("gold-smoke")
final class LocomotionPropertyTest {
    private final LocomotionDistancePolicy policy = new LocomotionDistancePolicy();

    @Test
    void distanceScalesLinearlyAndWaterModesKeepCanonicalEquivalences() {
        long seed = SeededPropertySupport.propertySeed() ^ 0x10C0L;
        var random = SeededPropertySupport.random(seed);
        int cases = SeededPropertySupport.propertyCases();
        var modes = LocomotionMode.values();
        var genders = Gender.values();

        for (int i = 0; i < cases; i++) {
            double h1 = 0.05 + random.nextDouble() * 4.95;
            double scale = 0.1 + random.nextDouble() * 5.0;
            double h2 = h1 * scale;
            LocomotionMode mode = modes[random.nextInt(modes.length)];
            Gender gender = genders[random.nextInt(genders.length)];
            String state = mode + "/" + gender + "/h=" + h1 + "/scale=" + scale;
            SeededPropertySupport.check("LocomotionDistance", seed, i, state, () -> {
                double v1 = policy.metersPerSecond(mode, gender, h1);
                double v2 = policy.metersPerSecond(mode, gender, h2);
                assertTrue(Double.isFinite(v1) && v1 > 0.0);
                assertTrue(Double.isFinite(v2) && v2 > 0.0);
                assertEquals(v1 * scale, v2, 1e-9);
                assertEquals(policy.metersPerSecond(LocomotionMode.WALKING, gender, h1),
                        policy.metersPerSecond(LocomotionMode.SWIMMING, gender, h1), 1e-12);
                assertEquals(policy.metersPerSecond(LocomotionMode.RUNNING, gender, h1),
                        policy.metersPerSecond(LocomotionMode.FAST_SWIMMING, gender, h1), 1e-12);
                assertEquals(policy.metersPerSecond(LocomotionMode.TROTTING, gender, h1),
                        policy.metersPerSecond(LocomotionMode.DIVING, gender, h1), 1e-12);
                assertEquals(policy.metersPerSecond(LocomotionMode.CROUCH_WALKING, gender, h1),
                        policy.metersPerSecond(LocomotionMode.CLIMBING, gender, h1), 1e-12);
            });
        }
    }
}

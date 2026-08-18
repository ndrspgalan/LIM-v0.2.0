package qa.unit;

import domain.character.Gender;
import domain.movement.LocomotionDistancePolicy;
import domain.movement.LocomotionMode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@Tag("gold-smoke")
final class LocomotionDistancePolicyTest {
    private final LocomotionDistancePolicy policy = new LocomotionDistancePolicy();

    @Test
    void swimmingAndRunningEquivalencesRemainCanonical() {
        double h = 1.72;
        assertEquals(policy.metersPerSecond(LocomotionMode.WALKING, Gender.HOMBRE, h),
                policy.metersPerSecond(LocomotionMode.SWIMMING, Gender.HOMBRE, h));
        assertEquals(policy.metersPerSecond(LocomotionMode.RUNNING, Gender.HOMBRE, h),
                policy.metersPerSecond(LocomotionMode.FAST_SWIMMING, Gender.HOMBRE, h));
        assertEquals(policy.metersPerSecond(LocomotionMode.TROTTING, Gender.HOMBRE, h),
                policy.metersPerSecond(LocomotionMode.DIVING, Gender.HOMBRE, h));
    }

    @Test
    void climbingUsesCrouchedDistance() {
        double h = 1.58;
        assertEquals(policy.metersPerSecond(LocomotionMode.CROUCH_WALKING, Gender.MUJER, h),
                policy.metersPerSecond(LocomotionMode.CLIMBING, Gender.MUJER, h));
    }
}

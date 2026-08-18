package qa.unit;

import domain.bestiarium.physical_plane.ancient.AnteoChargedPropertyPolicy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
@Tag("gold-smoke")
final class AnteoElectricityPolicyTest {
    private final AnteoChargedPropertyPolicy policy = new AnteoChargedPropertyPolicy();

    @Test
    void electricityFillsMissingStaminaBeforeDamagingHealth() {
        var result = policy.resolve(30.0, 50.0, 75.0);
        assertEquals(75.0, result.staminaAfter());
        assertEquals(25.0, result.electricityConvertedToStamina());
        assertEquals(5.0, result.healthDamage());
    }

    @Test
    void electricityDoesNotDamageHealthWhileStaminaCanAbsorbIt() {
        var result = policy.resolve(20.0, 50.0, 75.0);
        assertEquals(70.0, result.staminaAfter());
        assertEquals(20.0, result.electricityConvertedToStamina());
        assertEquals(0.0, result.healthDamage());
    }

    @Test
    void invalidResourcesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> policy.resolve(-1.0, 50.0, 75.0));
        assertThrows(IllegalArgumentException.class, () -> policy.resolve(10.0, 80.0, 75.0));
    }
}

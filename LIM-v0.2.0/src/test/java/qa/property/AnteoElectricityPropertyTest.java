package qa.property;

import domain.bestiarium.physical_plane.ancient.AnteoChargedPropertyPolicy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.property.support.SeededPropertySupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("property")
@Tag("gold-smoke")
final class AnteoElectricityPropertyTest {
    private final AnteoChargedPropertyPolicy policy = new AnteoChargedPropertyPolicy();

    @Test
    void netElectricityIsConservedBetweenStaminaAndHealth() {
        long seed = SeededPropertySupport.propertySeed() ^ 0xA17E0L;
        var random = SeededPropertySupport.random(seed);
        int cases = SeededPropertySupport.propertyCases();

        for (int i = 0; i < cases; i++) {
            double maximum = 1.0 + random.nextDouble() * 500.0;
            double current = random.nextDouble() * maximum;
            double electricity = random.nextDouble() * 1_000.0;
            String state = "electricity=" + electricity + ", current=" + current + ", maximum=" + maximum;
            SeededPropertySupport.check("Anteo.CARGADO", seed, i, state, () -> {
                var result = policy.resolve(electricity, current, maximum);
                assertTrue(Double.isFinite(result.staminaAfter()));
                assertTrue(Double.isFinite(result.healthDamage()));
                assertTrue(result.staminaAfter() >= current);
                assertTrue(result.staminaAfter() <= maximum);
                assertTrue(result.electricityConvertedToStamina() >= 0.0);
                assertTrue(result.healthDamage() >= 0.0);
                assertEquals(electricity,
                        result.electricityConvertedToStamina() + result.healthDamage(), 1e-9);
                assertEquals(current + result.electricityConvertedToStamina(), result.staminaAfter(), 1e-9);
                if (current < maximum && electricity <= maximum - current) {
                    assertEquals(0.0, result.healthDamage(), 1e-9);
                }
            });
        }
    }
}

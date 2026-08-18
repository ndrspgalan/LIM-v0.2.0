package domain.combat;

import domain.inventory.equipment.EquipmentState;

import java.util.List;
import java.util.Objects;

public record ArmorImpactResult(
        PhysicalDamage netDamage,
        StaggerResult stagger,
        List<String> damagedArmor,
        List<String> brokenArmor,
        EquipmentState resultingEquipment
) {
    public ArmorImpactResult {
        Objects.requireNonNull(netDamage, "El daño neto no puede ser nulo.");
        Objects.requireNonNull(stagger, "El resultado de stagger no puede ser nulo.");
        damagedArmor = List.copyOf(damagedArmor);
        brokenArmor = List.copyOf(brokenArmor);
        Objects.requireNonNull(resultingEquipment, "El equipamiento resultante no puede ser nulo.");
    }

    /** Compatibilidad semántica con consumidores anteriores: el knockback existe cuando hay stagger. */
    public boolean causesKnockback() {
        return stagger.staggered();
    }
}

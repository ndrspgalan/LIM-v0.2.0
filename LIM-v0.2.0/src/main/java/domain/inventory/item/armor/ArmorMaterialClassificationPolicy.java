package domain.inventory.item.armor;

import java.util.Objects;
import java.util.Set;

/** deriva LIGHT/MEDIUM/HEAVY exclusivamente de los materiales reales. */
public final class ArmorMaterialClassificationPolicy {
    private ArmorMaterialClassificationPolicy() {}

    public static ArmorMaterialClass classify(Set<ArmorMaterial> materials) {
        Objects.requireNonNull(materials, "Los materiales no pueden ser nulos.");
        if (materials.isEmpty()) throw new IllegalArgumentException("Una pieza de armadura debe declarar al menos un material.");
        if (materials.stream().anyMatch(m -> m.materialClass() == ArmorMaterialClass.HEAVY)) return ArmorMaterialClass.HEAVY;
        if (materials.stream().anyMatch(m -> m.materialClass() == ArmorMaterialClass.MEDIUM)) return ArmorMaterialClass.MEDIUM;
        return ArmorMaterialClass.LIGHT;
    }
}

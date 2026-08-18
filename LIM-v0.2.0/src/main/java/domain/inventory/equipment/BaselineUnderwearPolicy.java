package domain.inventory.equipment;

import domain.character.Gender;
import java.util.List;
import java.util.Objects;

/**
 * : representación basal cuando CHEST/LEGGINGS carecen de una prenda real.
 * No es inventario: no pesa, no protege, no se desgasta, no ocupa capas y no contribuye a ABRIGO.
 */
public final class BaselineUnderwearPolicy {
    private BaselineUnderwearPolicy() {}
    public record BaselineUnderwear(List<String> visibleGarments, boolean chestFallback, boolean leggingsFallback) {}

    public static BaselineUnderwear resolve(Gender gender, boolean hasChestGarment, boolean hasLeggingsGarment) {
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        java.util.ArrayList<String> visible = new java.util.ArrayList<>();
        if (!hasLeggingsGarment) visible.add("Taparrabos");
        if (!hasChestGarment && gender == Gender.MUJER) visible.add("Venda pectoral");
        return new BaselineUnderwear(List.copyOf(visible), !hasChestGarment, !hasLeggingsGarment);
    }
}

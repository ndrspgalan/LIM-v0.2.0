package domain.combat.ai.loadout;

import domain.inventory.item.WeaponItem;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Equipamiento físicamente visible en las manos de un combatiente. */
public record VisibleLoadout(
        Optional<WeaponItem> rightHand,
        Optional<WeaponItem> leftHand
) {
    public VisibleLoadout {
        rightHand = Objects.requireNonNull(rightHand, "La mano derecha no puede ser nula.");
        leftHand = Objects.requireNonNull(leftHand, "La mano izquierda no puede ser nula.");
    }

    public static VisibleLoadout of(WeaponItem rightHand, WeaponItem leftHand) {
        return new VisibleLoadout(Optional.ofNullable(rightHand), Optional.ofNullable(leftHand));
    }

    public List<WeaponItem> weapons() {
        return java.util.stream.Stream.of(rightHand, leftHand)
                .flatMap(Optional::stream)
                .toList();
    }

    public double maximumVisibleReachMeters() {
        return weapons().stream().mapToDouble(WeaponItem::reachMeters).max().orElse(0.0);
    }
}

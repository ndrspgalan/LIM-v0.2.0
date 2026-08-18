package domain.environment;

import domain.character.sheet.DamageResistanceProfile;
import domain.combat.NonConventionalDamageResolver;
import domain.combat.NonConventionalImpactResult;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorHitLocation;

import java.util.Objects;
import java.util.Optional;

/** Convierte el drenaje ambiental activo en daño físico no convencional real. */
public final class EnvironmentalDamageResolver {
    private final NonConventionalDamageResolver damageResolver = new NonConventionalDamageResolver();

    public Optional<NonConventionalImpactResult> resolve(
            EnvironmentalTickResult tick,
            DamageResistanceProfile resistances,
            EquipmentState equipment
    ) {
        Objects.requireNonNull(tick, "El pulso ambiental no puede ser nulo.");
        Objects.requireNonNull(resistances, "Las resistencias no pueden ser nulas.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        if (tick.rawHealthDamage() <= 0 || tick.adversity().continuousDamageType().isEmpty()) {
            return Optional.empty();
        }
        var type = tick.adversity().continuousDamageType().orElseThrow();
        ArmorHitLocation location = tick.adversity() == EnvironmentalAdversity.BITING_FROST
                ? ArmorHitLocation.BODY
                : ArmorHitLocation.HEAD;
        return Optional.of(damageResolver.resolve(
                type,
                tick.rawHealthDamage(),
                location,
                equipment,
                resistances.percentageFor(type),
                tick.naturalConductor()
        ));
    }
}

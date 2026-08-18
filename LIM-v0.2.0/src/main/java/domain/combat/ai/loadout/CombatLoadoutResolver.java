package domain.combat.ai.loadout;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.*;
import domain.combat.ai.threat.CombatantPresence;
import domain.combat.natural.NaturalCombatWeaponFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Resuelve el manejo de la IA sin recurrir a jerarquías de arma principal/secundaria. */
public final class CombatLoadoutResolver {
    private final WeaponItem unarmedFallback = UnarmedWeaponFactory.create();

    public ResolvedCombatLoadout resolve(VisibleLoadout visible) {
        return resolve(visible, null);
    }

    /** El cuerpo sólo sustituye DESARMADO cuando no existe arma física equipada. */
    public ResolvedCombatLoadout resolve(VisibleLoadout visible, CombatantPresence presence) {
        Objects.requireNonNull(visible, "El loadout visible no puede ser nulo.");

        if (visible.rightHand().isPresent() && visible.leftHand().isPresent()
                && supportsCanonicalDualWield(visible.rightHand().orElseThrow(), visible.leftHand().orElseThrow())) {
            WeaponItem right = visible.rightHand().orElseThrow();
            WeaponItem left = visible.leftHand().orElseThrow();
            WeaponConfiguration rightConfiguration = DualWieldConfigurationPolicy.rightHandConfiguration(right);
            WeaponConfiguration leftConfiguration = DualWieldConfigurationPolicy.leftHandConfiguration(left);
            ResolvedWeaponHandling handling = new ResolvedWeaponHandling(
                    ResolvedHand.active(EquipmentSlot.RIGHT_HAND, right, rightConfiguration),
                    ResolvedHand.active(EquipmentSlot.LEFT_HAND, left, leftConfiguration),
                    WieldingState.DUAL_WIELD
            );
            return resolved(handling, right, rightConfiguration);
        }

        if (visible.rightHand().isPresent()) {
            WeaponItem right = visible.rightHand().orElseThrow();
            WeaponConfiguration configuration = chooseSingleConfiguration(right);
            ResolvedWeaponHandling handling = new ResolvedWeaponHandling(
                    ResolvedHand.active(EquipmentSlot.RIGHT_HAND, right, configuration),
                    visible.leftHand().map(weapon -> ResolvedHand.stowed(EquipmentSlot.LEFT_HAND, weapon))
                            .orElseGet(() -> ResolvedHand.empty(EquipmentSlot.LEFT_HAND)),
                    WieldingState.SINGLE_WIELD
            );
            return resolved(handling, right, configuration);
        }

        if (visible.leftHand().isPresent()) {
            WeaponItem left = visible.leftHand().orElseThrow();
            WeaponConfiguration configuration = chooseSingleConfiguration(left);
            ResolvedWeaponHandling handling = new ResolvedWeaponHandling(
                    ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                    ResolvedHand.active(EquipmentSlot.LEFT_HAND, left, configuration),
                    WieldingState.SINGLE_WIELD
            );
            return resolved(handling, left, configuration);
        }

        WeaponItem effectiveUnarmed = presence!=null && presence.naturalCombatProfile().isPresent()
                ? NaturalCombatWeaponFactory.create(presence.naturalCombatProfile().orElseThrow(), presence.sheet(), presence.heightMeters())
                : unarmedFallback;
        WeaponConfiguration unarmedConfiguration = effectiveUnarmed.currentConfiguration();
        ResolvedWeaponHandling handling = new ResolvedWeaponHandling(
                ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                ResolvedHand.empty(EquipmentSlot.LEFT_HAND),
                WieldingState.UNARMED
        );
        return resolved(handling, effectiveUnarmed, unarmedConfiguration);
    }

    private boolean supportsCanonicalDualWield(WeaponItem right, WeaponItem left) {
        try {
            DualWieldConfigurationPolicy.rightHandConfiguration(right);
            DualWieldConfigurationPolicy.leftHandConfiguration(left);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private WeaponConfiguration chooseSingleConfiguration(WeaponItem weapon) {
        List<WeaponConfiguration> configurations = weapon.availableConfigurations();
        WeaponActionMode preferredMode = weapon.supportsActionMode(WeaponActionMode.PRIMARY)
                ? WeaponActionMode.PRIMARY
                : configurations.getFirst().actionMode();
        GripMode preferredGrip = weapon.hasTrait(WeaponTrait.SHIELD)
                ? GripMode.ONE_HANDED
                : GripMode.TWO_HANDED;
        return configurations.stream()
                .filter(configuration -> configuration.actionMode() == preferredMode)
                .sorted(Comparator.comparing(configuration -> configuration.gripMode() != preferredGrip))
                .findFirst()
                .orElseGet(() -> configurations.stream()
                        .sorted(Comparator.comparing(configuration -> configuration.gripMode() != preferredGrip))
                        .findFirst().orElseThrow());
    }

    private ResolvedCombatLoadout resolved(
            ResolvedWeaponHandling handling,
            WeaponItem attackingWeapon,
            WeaponConfiguration attackingConfiguration
    ) {
        return new ResolvedCombatLoadout(
                handling,
                attackingWeapon,
                attackingConfiguration,
                chooseLethalityMode(attackingWeapon.modes())
        );
    }

    private WeaponMode chooseLethalityMode(List<WeaponMode> modes) {
        return modes.stream().max(Comparator.comparingDouble((WeaponMode mode) ->
                mode.lethality().piercing()+mode.lethality().slashing()+mode.lethality().blunt())).orElseThrow();
    }

}

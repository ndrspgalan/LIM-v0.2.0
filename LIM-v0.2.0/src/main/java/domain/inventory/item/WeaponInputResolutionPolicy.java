package domain.inventory.item;

import java.util.Objects;
import java.util.Set;

/** Resuelve entradas contra manos, agarres y modos efectivos ya determinados. */
public final class WeaponInputResolutionPolicy {
    private final DualWieldLightComboPolicy comboPolicy = new DualWieldLightComboPolicy();
    private final WeaponItem unarmedFallback = UnarmedWeaponFactory.create();
    private WeaponActionMode unarmedMode = WeaponActionMode.PRIMARY;

    public WeaponInputResolution resolve(
            WeaponInput input,
            ResolvedWeaponHandling handling,
            boolean withinParryWindow
    ) {
        return resolve(input, handling, withinParryWindow, false, new DualWieldComboState());
    }

    public WeaponInputResolution resolve(
            WeaponInput input,
            ResolvedWeaponHandling handling,
            boolean withinParryWindow,
            DualWieldComboState comboState
    ) {
        return resolve(input, handling, withinParryWindow, false, comboState);
    }

    public WeaponInputResolution resolve(
            WeaponInput input,
            ResolvedWeaponHandling handling,
            boolean withinParryWindow,
            boolean improvisedBracerAvailable,
            DualWieldComboState comboState
    ) {
        Objects.requireNonNull(input, "La entrada no puede ser nula.");
        Objects.requireNonNull(handling, "El manejo resuelto no puede ser nulo.");
        Objects.requireNonNull(comboState, "El estado de combo no puede ser nulo.");

        return switch (handling.wieldingState()) {
            case UNARMED -> resolveUnarmed(input);
            case SINGLE_WIELD -> resolveSingle(input, activeHand(handling), withinParryWindow, improvisedBracerAvailable, comboState);
            case DUAL_WIELD -> resolveDualWield(input, handling, withinParryWindow, comboState);
        };
    }

    /**
     * Adaptador temporal para consumidores que se migrarán en /.
     * Los parámetros ya representan físicamente derecha e izquierda.
     */
    public WeaponInputResolution resolve(
            WeaponInput input,
            WeaponItem rightHand,
            WeaponItem leftHand,
            boolean dualWielding,
            boolean withinParryWindow
    ) {
        return resolve(input, rightHand, leftHand, dualWielding, withinParryWindow, new DualWieldComboState());
    }

    public WeaponInputResolution resolve(
            WeaponInput input,
            WeaponItem rightHand,
            WeaponItem leftHand,
            boolean dualWielding,
            boolean withinParryWindow,
            DualWieldComboState comboState
    ) {
        ResolvedWeaponHandling handling;
        if (dualWielding) {
            if (rightHand == null || leftHand == null) {
                return WeaponInputResolution.blocked("Dual wielding requiere ambas manos ocupadas.");
            }
            handling = new ResolvedWeaponHandling(
                    ResolvedHand.active(domain.inventory.equipment.EquipmentSlot.RIGHT_HAND, rightHand,
                            DualWieldConfigurationPolicy.rightHandConfiguration(rightHand)),
                    ResolvedHand.active(domain.inventory.equipment.EquipmentSlot.LEFT_HAND, leftHand,
                            DualWieldConfigurationPolicy.leftHandConfiguration(leftHand)),
                    WieldingState.DUAL_WIELD
            );
        } else if (rightHand != null && leftHand == null) {
            handling = new ResolvedWeaponHandling(
                    ResolvedHand.active(domain.inventory.equipment.EquipmentSlot.RIGHT_HAND, rightHand,
                            rightHand.currentConfiguration()),
                    ResolvedHand.empty(domain.inventory.equipment.EquipmentSlot.LEFT_HAND),
                    WieldingState.SINGLE_WIELD
            );
        } else if (rightHand == null && leftHand != null) {
            handling = new ResolvedWeaponHandling(
                    ResolvedHand.empty(domain.inventory.equipment.EquipmentSlot.RIGHT_HAND),
                    ResolvedHand.active(domain.inventory.equipment.EquipmentSlot.LEFT_HAND, leftHand,
                            leftHand.currentConfiguration()),
                    WieldingState.SINGLE_WIELD
            );
        } else if (rightHand == null) {
            handling = new ResolvedWeaponHandling(
                    ResolvedHand.empty(domain.inventory.equipment.EquipmentSlot.RIGHT_HAND),
                    ResolvedHand.empty(domain.inventory.equipment.EquipmentSlot.LEFT_HAND),
                    WieldingState.UNARMED
            );
        } else {
            return WeaponInputResolution.blocked("Dos manos ocupadas requieren dual wielding.");
        }
        return resolve(input, handling, withinParryWindow, comboState);
    }

    private WeaponInputResolution resolveDualWield(
            WeaponInput input,
            ResolvedWeaponHandling handling,
            boolean withinParryWindow,
            DualWieldComboState comboState
    ) {
        ResolvedHand right = handling.rightHand();
        ResolvedHand left = handling.leftHand();
        WeaponItem rightWeapon = right.weapon().orElseThrow();
        WeaponItem leftWeapon = left.weapon().orElseThrow();
        WeaponConfiguration rightConfiguration = right.effectiveConfiguration().orElseThrow();
        WeaponConfiguration leftConfiguration = left.effectiveConfiguration().orElseThrow();

        if (input == WeaponInput.RIGHT_PRESS) {
            return lightAttack(rightWeapon, rightConfiguration.actionMode(), comboState,
                    rightWeapon.hasTrait(WeaponTrait.SHIELD)
                            ? "Arrollamiento con el escudo de la mano derecha."
                            : "Ataque ligero con el arma de la mano derecha.");
        }

        if (input == WeaponInput.DESTABILIZE_PRESS) {
            return actionIfContextuallyAllowed(rightWeapon, rightConfiguration,
                    WeaponCombatAction.DESTABILIZE,
                    "Golpe desestabilizador en dual wielding con el arma de la mano derecha.");
        }

        if (input == WeaponInput.JUMP_PRESS) {
            return actionIfContextuallyAllowed(rightWeapon, rightConfiguration,
                    WeaponCombatAction.JUMP_ATTACK,
                    "Ataque con salto en dual wielding con el arma de la mano derecha.");
        }

        if (input == WeaponInput.HEAVY_PRESS || input == WeaponInput.CHARGED_HOLD) {
            return WeaponInputResolution.blocked(
                    "Un arma sostenida a una mano no puede ejecutar ataque fuerte ni ataque cargado.");
        }

        Set<WeaponCombatAction> leftActions = leftWeapon.combatActionsFor(leftConfiguration.actionMode());
        if (leftWeapon.hasTrait(WeaponTrait.SHIELD)) {
            if ((input == WeaponInput.LEFT_HOLD || input == WeaponInput.LEFT_PRESS)
                    && leftActions.contains(WeaponCombatAction.BLOCK)) {
                return WeaponInputResolution.allowed(WeaponCombatAction.BLOCK,
                        "Bloqueo con el escudo izquierdo; la postura HEAD/BODY se conserva de forma independiente.");
            }
            return WeaponInputResolution.blocked("El escudo izquierdo bloquea; no dispone de PARRY.");
        }

        if (input == WeaponInput.LEFT_PRESS) {
            if (leftActions.contains(WeaponCombatAction.PARRY)
                    && !leftActions.contains(WeaponCombatAction.LIGHT_ATTACK)) {
                return WeaponInputResolution.allowed(WeaponCombatAction.PARRY,
                        "El ataque ligero alternativo de la mano izquierda es PARRY.");
            }
            if (leftActions.contains(WeaponCombatAction.LIGHT_ATTACK)) {
                return lightAttack(leftWeapon, leftConfiguration.actionMode(), comboState,
                        "Ataque ligero con el modo alternativo de la mano izquierda.");
            }
        }
        return WeaponInputResolution.blocked(
                "En dual wielding la mano izquierda solo admite ataque ligero alternativo o PARRY alternativo.");
    }

    private WeaponInputResolution resolveSingle(
            WeaponInput input,
            ResolvedHand activeHand,
            boolean withinParryWindow,
            boolean improvisedBracerAvailable,
            DualWieldComboState comboState
    ) {
        WeaponItem weapon = activeHand.weapon().orElseThrow();
        WeaponConfiguration configuration = activeHand.effectiveConfiguration().orElseThrow();
        Set<WeaponCombatAction> actions = weapon.combatActionsFor(configuration.actionMode());
        String hand = activeHand.hand() == domain.inventory.equipment.EquipmentSlot.RIGHT_HAND
                ? "derecha" : "izquierda";

        return switch (input) {
            case RIGHT_PRESS -> actions.contains(WeaponCombatAction.LIGHT_ATTACK)
                    ? lightAttack(weapon, configuration.actionMode(), comboState,
                    weapon.hasTrait(WeaponTrait.SHIELD)
                            ? "Arrollamiento con el escudo de la mano " + hand + "."
                            : "Ataque ligero con la mano " + hand + ".")
                    : WeaponInputResolution.blocked("El modo activo no permite ataque ligero.");
            case LEFT_HOLD -> actions.contains(WeaponCombatAction.BLOCK)
                    ? WeaponInputResolution.allowed(WeaponCombatAction.BLOCK,
                    "Bloqueo sostenido con la mano " + hand + ".")
                    : improvisedBracerAvailable
                    ? WeaponInputResolution.allowed(WeaponCombatAction.BLOCK,
                    "ESCUDO IMPROVISADO: bloqueo sostenido con el brazal izquierdo en modo alternativo virtual.")
                    : WeaponInputResolution.blocked("El modo activo no permite bloquear.");
            case LEFT_PRESS -> {
                if (!weapon.hasTrait(WeaponTrait.SHIELD) && withinParryWindow && actions.contains(WeaponCombatAction.PARRY)) {
                    yield WeaponInputResolution.allowed(WeaponCombatAction.PARRY,
                            "Parry con la mano " + hand + " dentro de la ventana.");
                }
                if (withinParryWindow && improvisedBracerAvailable) {
                    yield WeaponInputResolution.allowed(WeaponCombatAction.PARRY,
                            "ESCUDO IMPROVISADO: parry con el brazal izquierdo.");
                }
                if (weapon.hasTrait(WeaponTrait.SHIELD) && actions.contains(WeaponCombatAction.BLOCK)) {
                    yield WeaponInputResolution.allowed(WeaponCombatAction.BLOCK,
                            "Inicio de bloqueo con el escudo de la mano " + hand + ".");
                }
                if (improvisedBracerAvailable) {
                    yield WeaponInputResolution.allowed(WeaponCombatAction.BLOCK,
                            "ESCUDO IMPROVISADO: inicio de bloqueo con el brazal izquierdo.");
                }
                yield WeaponInputResolution.blocked("El modo activo no permite PARRY mediante esta entrada.");
            }
            case DESTABILIZE_PRESS -> (weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE) || weapon.hasTrait(WeaponTrait.STAFF_FLOURISH_HANDLING))
                    ? actionIfContextuallyAllowed(weapon, configuration, WeaponCombatAction.CHARGED_ATTACK,
                    weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)
                            ? "Espadón de Rotor: DESTABILIZE redirige al CHARGED con todas sus políticas."
                            : "Bō: DESTABILIZE redirige al CHARGED con todas sus políticas.")
                    : actionIfContextuallyAllowed(weapon, configuration, WeaponCombatAction.DESTABILIZE,
                    weapon.name().equals("Horca") ? "Horca: patada frontal desestabilizadora."
                            : (weapon.name().equals("Guadaña") || weapon.name().equals("Boathook"))
                            ? weapon.name()+": patada frontal desestabilizadora."
                            : "Golpe desestabilizador con animación universal de hombro.");
            case HEAVY_PRESS -> configuration.gripMode() == GripMode.ONE_HANDED
                    && !weapon.hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY)
                    && !weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)
                    && !weapon.hasTrait(WeaponTrait.STAFF_FLOURISH_HANDLING)
                    ? WeaponInputResolution.blocked(
                    "Un arma sostenida a una mano no puede ejecutar ataque fuerte.")
                    : actionIfAllowed(actions, WeaponCombatAction.HEAVY_ATTACK,
                    weapon.hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY)
                            ? "Ataque fuerte electro-mecánico monomanual."
                            : "Ataque fuerte a dos manos.");
            case CHARGED_HOLD -> (weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE) || weapon.hasTrait(WeaponTrait.STAFF_FLOURISH_HANDLING))
                    ? WeaponInputResolution.blocked("Esta arma no usa una tecla CHARGED convencional: su DESTABILIZE ejecuta CHARGED.")
                    : configuration.gripMode() == GripMode.ONE_HANDED
                    ? WeaponInputResolution.blocked("Un arma sostenida a una mano no puede ejecutar ataque cargado.")
                    : actionIfAllowed(actions, WeaponCombatAction.CHARGED_ATTACK, "Ataque cargado a dos manos.");
            case JUMP_PRESS -> actionIfAllowed(actions, WeaponCombatAction.JUMP_ATTACK, "Ataque con salto.");
        };
    }

    /** MOUSE WHEEL espeja la guardia desarmada sin cambiar su resolución lógica RIGHT_HAND + 2H. */
    public WeaponActionMode toggleUnarmedMode() {
        unarmedMode = unarmedMode == WeaponActionMode.PRIMARY ? WeaponActionMode.ALTERNATIVE : WeaponActionMode.PRIMARY;
        return unarmedMode;
    }
    public WeaponActionMode unarmedMode() { return unarmedMode; }

    /** DESARMADO solo aparece cuando ambas manos están libres. */
    private WeaponInputResolution resolveUnarmed(WeaponInput input) {
        Set<WeaponCombatAction> actions = unarmedFallback.combatActionsFor(unarmedMode);
        return switch (input) {
            case RIGHT_PRESS -> actionIfAllowed(actions, WeaponCombatAction.LIGHT_ATTACK,
                    "DESARMADO " + unarmedMode.label() + ": directo → swing → gancho con extremidades espejadas.");
            case HEAVY_PRESS -> actionIfAllowed(actions, WeaponCombatAction.HEAVY_ATTACK,
                    "DESARMADO: tornado kick 360°.");
            case CHARGED_HOLD -> WeaponInputResolution.blocked("DESARMADO no posee ataque cargado.");
            case LEFT_PRESS, DESTABILIZE_PRESS -> actionIfAllowed(actions, WeaponCombatAction.DESTABILIZE,
                    "DESARMADO: back kick desestabilizador.");
            case LEFT_HOLD -> actionIfAllowed(actions, WeaponCombatAction.BLOCK,
                    "DESARMADO: guardia mantenida; HEAD +50% cobertura y protección contundente = AGUANTE.");
            case JUMP_PRESS -> actionIfAllowed(actions, WeaponCombatAction.JUMP_ATTACK,
                    "DESARMADO: ataque con salto.");
        };
    }

    private WeaponInputResolution lightAttack(
            WeaponItem weapon,
            WeaponActionMode mode,
            DualWieldComboState state,
            String baseReason
    ) {
        if (!weapon.combatActionsFor(mode).contains(WeaponCombatAction.LIGHT_ATTACK)) {
            return WeaponInputResolution.blocked("El modo efectivo no permite ataque ligero.");
        }
        DualWieldLightComboResolution combo = comboPolicy.resolve(weapon, mode, state);
        String suffix = combo.restartedForWeapon()
                ? " Reinicia su repertorio en el ataque ligero 1."
                : " Ejecuta el ataque ligero " + combo.executedOrdinal() + ".";
        if (combo.finisherBonusApplies()) suffix += " Conserva/aplica la bonificación de final de combo LIGHT.";
        return WeaponInputResolution.lightAttack(combo.executedOrdinal(), combo.finisherBonusApplies(), baseReason + suffix);
    }

    private static ResolvedHand activeHand(ResolvedWeaponHandling handling) {
        if (handling.rightHand().disposition() == HandDisposition.ACTIVE) return handling.rightHand();
        if (handling.leftHand().disposition() == HandDisposition.ACTIVE) return handling.leftHand();
        throw new IllegalStateException("SINGLE_WIELD requiere exactamente una mano activa.");
    }

    private static WeaponInputResolution actionIfContextuallyAllowed(
            WeaponItem weapon,
            WeaponConfiguration configuration,
            WeaponCombatAction action,
            String reason
    ) {
        Set<WeaponCombatAction> actions = weapon.combatActionsFor(configuration.actionMode());
        return actionIfAllowed(actions, action, reason);
    }

    private static WeaponInputResolution actionIfAllowed(
            Set<WeaponCombatAction> actions,
            WeaponCombatAction action,
            String reason
    ) {
        return actions.contains(action)
                ? WeaponInputResolution.allowed(action, reason)
                : WeaponInputResolution.blocked("La configuración efectiva no permite " + action + ".");
    }
}

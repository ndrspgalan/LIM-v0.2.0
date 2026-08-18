package domain.inventory.item.firearms;

import domain.inventory.InventoryState;
import java.util.Objects;

/**
 * Resuelve la gramática de entrada propia de las armas de fuego.
 * Las entradas físicas se contextualizan por estado: RIGHT CLICK dispara en manejo normal
 * y presuriza cuando el personaje está sujetando el mecanismo neumático.
 */
public final class FirearmInputResolutionPolicy {



    /**
     * Ruta física : cuando existe inventario, R consume una fuente global de munición compatible.
     * El resto de la gramática conserva la resolución contextual existente.
     */
    public FirearmActionResult resolve(FirearmInput input, FirearmItem firearm, InventoryState inventory) {
        Objects.requireNonNull(inventory, "El inventario no puede ser nulo.");
        if (input == FirearmInput.RELOAD_PRESS
                && firearm.handlingState() != FirearmHandlingState.PNEUMATIC_PRESSURIZATION
                && firearm.handlingState() != FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION
                && firearm.handlingState() != FirearmHandlingState.ARC_MANUAL_CHARGE
                && firearm.handlingState() != FirearmHandlingState.CLUSTER_TIMER_CONFIGURATION) {
            var loaded = firearm.reloadFromInventory(inventory);
            return loaded.loaded()
                    ? FirearmActionResult.allowed(FirearmAction.RELOAD_CARTRIDGE, firearm.reloadDurationSeconds(),
                        "R carga munición compatible desde el inventario: " + loaded.shotsLoaded() + " disparos.")
                    : FirearmActionResult.blocked(loaded.message());
        }
        return resolve(input, firearm);
    }

    public FirearmActionResult resolve(FirearmInput input, FirearmItem firearm) {
        Objects.requireNonNull(input, "La entrada no puede ser nula.");
        Objects.requireNonNull(firearm, "El arma no puede ser nula.");

        if (firearm instanceof PneumaticFirearmItem pneumatic
                && firearm.handlingState() == FirearmHandlingState.PNEUMATIC_PRESSURIZATION) {
            return resolvePneumaticManipulation(input, pneumatic);
        }
        if (firearm instanceof ElectromagneticFirearmItem electromagnetic
                && firearm.handlingState() == FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION) {
            return resolveElectromagneticSelection(input, electromagnetic);
        }
        if (firearm instanceof ArcInductionFirearmItem arc
                && firearm.handlingState() == FirearmHandlingState.ARC_MANUAL_CHARGE) {
            return resolveArcManipulation(input, arc);
        }
        if (firearm instanceof ClusterCannonFirearmItem cluster
                && firearm.handlingState() == FirearmHandlingState.CLUSTER_TIMER_CONFIGURATION) {
            return resolveClusterTimerConfiguration(input, cluster);
        }

        return switch (input) {
            case LEFT_PRESS -> {
                if (!firearm.supportsAiming()) {
                    yield FirearmActionResult.blocked("Esta plataforma no utiliza AIMING.");
                }
                firearm.toggleAim();
                yield FirearmActionResult.allowed(FirearmAction.TOGGLE_AIM,
                        firearm.handlingState() == FirearmHandlingState.AIMING
                                ? "Apuntado activado."
                                : "Apuntado desactivado.");
            }
            case RIGHT_PRESS -> fireOnPress(firearm);
            case RIGHT_HOLD -> fireOnHold(firearm);
            case RIGHT_RELEASE -> {
                firearm.triggerState().release();
                yield FirearmActionResult.allowed(FirearmAction.NONE, "Pulsación de disparo liberada.");
            }
            case RELOAD_PRESS -> {
                if (firearm instanceof ArcInductionFirearmItem) {
                    yield FirearmActionResult.blocked("El Lanza-Arcos no utiliza munición ni cargador.");
                }
                firearm.reloadFullCartridge();
                yield FirearmActionResult.allowed(FirearmAction.RELOAD_CARTRIDGE,
                        "R sustituye el cartucho completo.");
            }
            case RELOAD_HOLD -> {
                if (firearm instanceof PneumaticFirearmItem pneumatic) {
                    if (!pneumatic.beginPressurization()) {
                        yield FirearmActionResult.allowed(FirearmAction.NONE,
                                "La presión ya está al máximo; fallback al agarre normal.");
                    }
                    yield FirearmActionResult.allowed(FirearmAction.ENTER_PNEUMATIC_PRESSURIZATION,
                            "HOLD R: el personaje sujeta el mecanismo frontal de presurización.");
                }
                if (firearm instanceof ElectromagneticFirearmItem electromagnetic) {
                    electromagnetic.beginChargeSelection();
                    yield FirearmActionResult.allowed(FirearmAction.ENTER_ELECTROMAGNETIC_CHARGE_SELECTION,
                            "HOLD R: selector de penetración abierto en " + electromagnetic.selectedSetting() + "; LEFT CLICK recorre P50-P90 y R confirma/sale.");
                }
                if (firearm instanceof ArcInductionFirearmItem arc) {
                    if (!arc.operationalBatteryInstalled()) {
                        yield FirearmActionResult.blocked("El Lanza-Arcos requiere una Batería Portátil Electromagnética V881 acoplada.");
                    }
                    if (!arc.beginManualCharge()) {
                        yield FirearmActionResult.allowed(FirearmAction.NONE,
                                "Los tres módulos ya están cargados; fallback al agarre normal.");
                    }
                    yield FirearmActionResult.allowed(FirearmAction.ENTER_ARC_MANUAL_CHARGE,
                            "HOLD R: el personaje mantiene explícitamente la manivela; fuera de esta manipulación la carga manual preferente puede avanzar automáticamente si no hay otra acción prioritaria.");
                }
                if (firearm instanceof ClusterCannonFirearmItem cluster) {
                    cluster.beginTimerConfiguration();
                    yield FirearmActionResult.allowed(FirearmAction.ENTER_CLUSTER_TIMER_CONFIGURATION,
                            "HOLD R: ajuste del temporizador del Cañón de Racimo; RIGHT CLICK alterna 3/4/5 s y R confirma/sale.");
                }
                yield FirearmActionResult.blocked("El arma no dispone de una manipulación secundaria mediante HOLD R.");
            }
            case CYCLE_FIRE_MODE -> {
                FireMode before = firearm.activeFireMode();
                FireMode after = firearm.cycleFireMode();
                yield FirearmActionResult.allowed(FirearmAction.CYCLE_FIRE_MODE,
                        before == after
                                ? "El arma solo dispone de la cadencia " + after.code() + "."
                                : "Cadencia seleccionada: " + after.code() + ".");
            }
            case DESTABILIZE_PRESS -> FirearmActionResult.allowed(FirearmAction.DESTABILIZE,
                    firearm.destabilizingTechniqueDescription());
            case HEAVY_PRESS -> FirearmActionResult.blocked(
                    "Las armas de fuego no resuelven un ataque fuerte ordinario mediante esta entrada.");
            case CHARGED_HOLD -> firearm instanceof RepeatingRifleFirearmItem
                    ? FirearmActionResult.allowed(FirearmAction.BAYONET_CHARGE,
                    "HOLD ataque cargado: carga con bayoneta; consume PA con la misma tasa que correr hasta soltar, impactar, ser interrumpido o agotar PA.")
                    : FirearmActionResult.blocked("Esta arma de fuego no define una carga cuerpo a cuerpo.");
        };
    }

    private FirearmActionResult resolvePneumaticManipulation(FirearmInput input, PneumaticFirearmItem firearm) {
        return switch (input) {
            case RIGHT_PRESS -> {
                boolean changed = firearm.pressurizeOneStep();
                yield FirearmActionResult.allowed(changed ? FirearmAction.PRESSURIZE : FirearmAction.NONE, changed ? firearm.pressureStepDurationSeconds() : 0.0,
                        firearm.isPressureFull()
                                ? "Presión máxima alcanzada; fallback automático al agarre normal."
                                : "El mecanismo aumenta una unidad la presión neumática.");
            }
            case RELOAD_PRESS -> {
                firearm.cancelPressurization();
                yield FirearmActionResult.allowed(FirearmAction.CANCEL_PNEUMATIC_PRESSURIZATION,
                        "R cancela la presurización y devuelve el arma al agarre normal.");
            }
            case RELOAD_HOLD -> FirearmActionResult.allowed(FirearmAction.ENTER_PNEUMATIC_PRESSURIZATION,
                    "El personaje continúa sujetando el mecanismo de presurización.");
            case RIGHT_RELEASE -> FirearmActionResult.allowed(FirearmAction.NONE,
                    "Liberar RIGHT CLICK no abandona el mecanismo; R cancela la manipulación.");
            default -> FirearmActionResult.blocked(
                    "Durante la presurización solo se admite RIGHT CLICK para bombear o R para volver al agarre normal.");
        };
    }


    private FirearmActionResult resolveElectromagneticSelection(FirearmInput input, ElectromagneticFirearmItem firearm) {
        return switch (input) {
            case LEFT_PRESS -> {
                ElectromagneticChargeSetting before = firearm.selectedSetting();
                ElectromagneticChargeSetting after = firearm.cycleChargeSetting();
                yield FirearmActionResult.allowed(FirearmAction.CYCLE_ELECTROMAGNETIC_CHARGE_SETTING,
                        before == after
                                ? "No puede seleccionarse un umbral inferior a la carga ya almacenada."
                                : "Umbral seleccionado: " + after + "; " + format(after.piercing()) + " P; "
                                + format(after.rangeMeters()) + " m; bloqueo " + format(after.thermalLockSeconds())
                                + " s; " + format(after.equivalentTurns()) + " vueltas equivalentes.");
            }
            case RELOAD_PRESS -> {
                firearm.exitChargeSelection();
                yield FirearmActionResult.allowed(FirearmAction.EXIT_ELECTROMAGNETIC_CHARGE_SELECTION,
                        "R confirma " + firearm.selectedSetting() + " y devuelve el fusil al estado normal.");
            }
            case RELOAD_HOLD -> FirearmActionResult.allowed(FirearmAction.ENTER_ELECTROMAGNETIC_CHARGE_SELECTION,
                    "El selector electromagnético permanece abierto.");
            default -> FirearmActionResult.blocked(
                    "Durante la selección solo se admite LEFT CLICK para recorrer P50-P90 o R para confirmar y salir.");
        };
    }

    private FirearmActionResult resolveArcManipulation(FirearmInput input, ArcInductionFirearmItem firearm) {
        return switch (input) {
            case RIGHT_PRESS, RIGHT_HOLD -> {
                boolean changed = firearm.turnCrankOneRevolution();
                ArcDischargeProfile profile = firearm.currentDischargeProfile();
                yield FirearmActionResult.allowed(changed ? FirearmAction.TURN_ARC_CRANK : FirearmAction.NONE,
                        firearm.chargeFull()
                                ? "Carga máxima de tres módulos alcanzada; fallback automático al agarre normal."
                                : "Carga actual: " + format(profile.offensiveReserve()) + " de reserva eléctrica; "
                                + profile.fullyActiveModules() + " módulos completos; bloqueo previsto "
                                + format(profile.thermalLockSeconds()) + " s.");
            }
            case RELOAD_PRESS -> {
                firearm.cancelManualCharge();
                yield FirearmActionResult.allowed(FirearmAction.CANCEL_ARC_MANUAL_CHARGE,
                        "R cancela la carga manual y devuelve el arma al agarre normal sin perder la reserva acumulada.");
            }
            case RELOAD_HOLD -> FirearmActionResult.allowed(FirearmAction.ENTER_ARC_MANUAL_CHARGE,
                    "El personaje continúa sujetando la manivela del Lanza-Arcos.");
            case RIGHT_RELEASE -> FirearmActionResult.allowed(FirearmAction.NONE,
                    "Liberar RIGHT CLICK no abandona la manivela; R cancela la manipulación.");
            default -> FirearmActionResult.blocked(
                    "Durante la carga del Lanza-Arcos solo se admite RIGHT CLICK para girar la manivela o R para volver al agarre normal.");
        };
    }

    private FirearmActionResult resolveClusterTimerConfiguration(FirearmInput input, ClusterCannonFirearmItem cluster) {
        return switch (input) {
            case RIGHT_PRESS -> FirearmActionResult.allowed(FirearmAction.CYCLE_CLUSTER_TIMER,
                    "Temporizador seleccionado: " + cluster.cycleTimer() + " s.");
            case RELOAD_PRESS -> {
                cluster.cancelTimerConfiguration();
                yield FirearmActionResult.allowed(FirearmAction.CANCEL_CLUSTER_TIMER_CONFIGURATION,
                        "Temporizador confirmado en " + cluster.timerSeconds() + " s; vuelta al agarre normal.");
            }
            case RELOAD_HOLD -> FirearmActionResult.allowed(FirearmAction.ENTER_CLUSTER_TIMER_CONFIGURATION,
                    "Se mantiene abierto el ajuste temporal del Cañón de Racimo.");
            case RIGHT_RELEASE -> FirearmActionResult.allowed(FirearmAction.NONE, "Liberar RIGHT CLICK no abandona el ajuste.");
            default -> FirearmActionResult.blocked("Durante el ajuste temporal solo se admite RIGHT CLICK o R.");
        };
    }

    private FirearmActionResult fireOnPress(FirearmItem firearm) {
        if (!FirearmTriggerPolicy.canFireOnPress(firearm.activeFireMode(), firearm.triggerState())) {
            return FirearmActionResult.blocked("La cadencia actual ya agotó esta pulsación del gatillo.");
        }
        return fireOne(firearm);
    }

    private FirearmActionResult fireOnHold(FirearmItem firearm) {
        if (!FirearmTriggerPolicy.canFireOnHold(firearm.activeFireMode(), firearm.triggerState())) {
            return FirearmActionResult.blocked(
                    firearm.activeFireMode() == FireMode.ONE_A
                            ? "1A solo permite una bala por RIGHT CLICK."
                            : "3A ya ha disparado tres balas en esta pulsación; hay que volver a pulsar RIGHT CLICK.");
        }
        return fireOne(firearm);
    }

    private FirearmActionResult fireOne(FirearmItem firearm) {
        if (!firearm.canConsumeShot()) {
            firearm.triggerState().release();
            if (firearm instanceof AntiMaterielCannonFirearmItem anti && anti.recoveringFromShot()) {
                return FirearmActionResult.blocked("Ciclo de recuperación del Cañón Antimaterial: " + format(anti.recoveryRemainingSeconds()) + " s restantes.");
            }
            if (firearm instanceof PneumaticFirearmItem pneumatic && !pneumatic.hasPressure()) {
                return FirearmActionResult.blocked("Presión 0: el arma no dispara.");
            }
            if (firearm instanceof ElectromagneticFirearmItem electromagnetic) {
                if (electromagnetic.triggerThermallyLocked()) {
                    return FirearmActionResult.blocked("El gatillo permanece bloqueado por temperatura durante "
                            + format(electromagnetic.thermalLockRemainingSeconds()) + " s.");
                }
                if (!electromagnetic.hasElectricalCharge()) {
                    return FirearmActionResult.blocked("Carga eléctrica 0: el arma no dispara.");
                }
            }
            if (firearm instanceof ArcInductionFirearmItem arc) {
                if (arc.triggerThermallyLocked()) {
                    return FirearmActionResult.blocked("El Lanza-Arcos permanece bloqueado por temperatura durante "
                            + format(arc.thermalLockRemainingSeconds()) + " s.");
                }
                if (!arc.hasElectricalCharge()) {
                    return FirearmActionResult.blocked("Carga eléctrica 0: el Lanza-Arcos no descarga.");
                }
            }
            return FirearmActionResult.blocked("El arma no contiene munición disponible.");
        }
        firearm.consumeShot();
        if (!firearm.supportsAiming()) {
            return FirearmActionResult.fired(1, "Descarga ejecutada sin AIMING.");
        }
        return FirearmActionResult.fired(1,
                firearm.handlingState() == FirearmHandlingState.AIMING
                        ? "Disparo apuntado."
                        : "Disparo sin apuntar.");
    }

    private static String format(double value) {
        double rounded = Math.rint(value * 100.0) / 100.0;
        if (Math.abs(rounded - Math.rint(rounded)) < 0.000001) return Long.toString(Math.round(rounded));
        return Double.toString(rounded);
    }
}


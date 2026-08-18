package application.rest;

import domain.combat.HostileEncounterState;
import domain.environment.time.EnvironmentalCycle;
import domain.rest.SleepPolicy;
import domain.rest.SleepState;
import domain.status.VitalResourceState;
import domain.survival.HungerState;
import domain.survival.SurvivalTimePolicy;
import domain.survival.ThirstState;

import java.util.Objects;

/**
 * : un sueño voluntario por día y sólo de noche; dos ciclos completos sin dormir
 * generan una deuda dura. La hostilidad puede diferir esa deuda, nunca cancelarla.
 */
public final class SleepUseCase {
    private final SleepPolicy policy = new SleepPolicy();

    public SleepSurvivalResult execute(boolean enabled, HostileEncounterState hostileEncounter, EnvironmentalCycle cycle,
                                       VitalResourceState resources, HungerState hunger, ThirstState thirst) {
        return execute(enabled, hostileEncounter, cycle, resources, new SleepState(cycle), hunger, thirst);
    }

    public SleepSurvivalResult execute(boolean enabled, HostileEncounterState hostileEncounter, EnvironmentalCycle cycle,
                                       VitalResourceState resources, SleepState sleepState,
                                       HungerState hunger, ThirstState thirst) {
        Objects.requireNonNull(hostileEncounter); Objects.requireNonNull(cycle);
        Objects.requireNonNull(resources); Objects.requireNonNull(sleepState);

        sleepState.synchronize(cycle);

        if (policy.mustSleepNow(hostileEncounter, cycle, sleepState)) {
            return performSleep(cycle, resources, sleepState, hunger, thirst, true);
        }
        if (!policy.canSleepVoluntarily(enabled, hostileEncounter, cycle, sleepState)) {
            return new SleepSurvivalResult(
                    SleepResult.blocked(policy.voluntaryBlockReason(enabled, hostileEncounter, cycle, sleepState)), hunger);
        }
        return performSleep(cycle, resources, sleepState, hunger, thirst, false);
    }

    /**
     * Debe invocarse al concluir un encuentro hostil (o tras sincronizar el reloj).
     * Si vencieron dos días durante el encuentro, atraviesa exactamente el mismo pipeline
     * de sueño que una acción normal, pero ignora la restricción de fase NIGHT.
     */
    public SleepSurvivalResult afterHostileEncounterConcluded(EnvironmentalCycle cycle,
                                                               VitalResourceState resources,
                                                               SleepState sleepState,
                                                               HungerState hunger, ThirstState thirst) {
        Objects.requireNonNull(cycle); Objects.requireNonNull(resources); Objects.requireNonNull(sleepState);
        sleepState.synchronize(cycle);
        if (!sleepState.forcedSleepDue()) {
            return new SleepSurvivalResult(SleepResult.blocked("No existe deuda máxima de sueño pendiente."), hunger);
        }
        return performSleep(cycle, resources, sleepState, hunger, thirst, true);
    }

    private SleepSurvivalResult performSleep(EnvironmentalCycle cycle, VitalResourceState resources,
                                             SleepState sleepState, HungerState hunger, ThirstState thirst,
                                             boolean forced) {
        var elapsed = cycle.remainingInPhase();
        var next = cycle.completeCurrentPhase();
        HungerState nextHunger = hunger;
        if (hunger != null && thirst != null) nextHunger = new SurvivalTimePolicy().advance(hunger, thirst, elapsed);
        resources.restoreStamina();
        if (thirst == null || !thirst.isDehydrated()) resources.restoreHealth();
        sleepState.recordWake(cycle);
        return new SleepSurvivalResult(forced ? SleepResult.forced(next) : SleepResult.completed(next), nextHunger);
    }

    public record SleepSurvivalResult(SleepResult result, HungerState hungerState) {}
}

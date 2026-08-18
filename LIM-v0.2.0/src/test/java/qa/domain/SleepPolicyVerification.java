package qa.domain;

import application.rest.SleepUseCase;
import domain.combat.HostileEncounterState;
import domain.environment.time.*;
import domain.rest.SleepState;
import domain.social.RelationshipType;
import domain.status.VitalResourceState;

import java.time.Duration;

public final class SleepPolicyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        voluntaryOnlyAtNightAndOncePerDay();
        hardTwoDayLimitAndHostileDeferral();
        forcedSleepMayOccurAfterDawn();
    }

    private static void voluntaryOnlyAtNightAndOncePerDay() {
        var useCase=new SleepUseCase();
        var combat=new HostileEncounterState();
        var day=new EnvironmentalCycle(DayPhase.DAY,Duration.ZERO,Weather.CLEAR);
        var state=new SleepState(day);
        var resources=new VitalResourceState(10,100,5,40);

        var blocked=useCase.execute(true,combat,day,resources,state,null,null).result();
        org.junit.jupiter.api.Assertions.assertTrue(!blocked.slept(),"No se puede dormir voluntariamente de día.");

        day.advance(Duration.ofMinutes(60)); // DAY + AFTERNOON -> NIGHT
        org.junit.jupiter.api.Assertions.assertTrue(day.phase()==DayPhase.NIGHT,"Debe alcanzarse NIGHT.");
        var slept=useCase.execute(true,combat,day,resources,state,null,null).result();
        org.junit.jupiter.api.Assertions.assertTrue(slept.slept(),"Debe poder dormir de noche.");
        org.junit.jupiter.api.Assertions.assertTrue(state.sleptDuringCurrentDay(day),"El sueño debe quedar registrado.");

        // Volvemos a NIGHT del mismo completedDay mediante un ciclo aislado con el mismo contador no es posible;
        // comprobamos directamente que el estado bloquea una segunda consumición antes del siguiente amanecer.
        var sameNight=new EnvironmentalCycle(DayPhase.NIGHT,Duration.ZERO,Weather.CLEAR,
                WeatherSeason.SPRING, AtmosphericPhenomenonOccurrence.none(),
                state.lastSleptCompletedDay(), domain.environment.time.randomizer.ClimateRandomizerSet.defaults());
        org.junit.jupiter.api.Assertions.assertTrue(state.sleptDuringCurrentDay(sameNight),"Debe reconocer que ya se durmió ese día.");
        org.junit.jupiter.api.Assertions.assertTrue(!useCase.execute(true,combat,sameNight,resources,state,null,null).result().slept(),
                "Sólo se permite un sueño por día.");
    }

    private static void hardTwoDayLimitAndHostileDeferral() {
        var useCase=new SleepUseCase();
        var combat=new HostileEncounterState();
        var cycle=new EnvironmentalCycle(DayPhase.DAY,Duration.ZERO,Weather.CLEAR);
        var state=new SleepState(cycle);
        var resources=new VitalResourceState(10,100,5,40);

        combat.beginFor(RelationshipType.HOSTILE);
        cycle.advance(EnvironmentalCycle.DAY_DURATION.multipliedBy(2));
        state.synchronize(cycle);
        org.junit.jupiter.api.Assertions.assertTrue(state.forcedSleepDue(),"Dos ciclos completos deben generar deuda forzosa.");

        var blocked=useCase.execute(true,combat,cycle,resources,state,null,null).result();
        org.junit.jupiter.api.Assertions.assertTrue(!blocked.slept(),"La hostilidad activa difiere incluso el sueño forzoso.");

        combat.conclude();
        var forced=useCase.afterHostileEncounterConcluded(cycle,resources,state,null,null).result();
        org.junit.jupiter.api.Assertions.assertTrue(forced.slept(),"Al terminar el encuentro debe cobrarse inmediatamente la deuda.");
        org.junit.jupiter.api.Assertions.assertTrue(!state.forcedSleepDue(),"Dormir debe cancelar la deuda.");
    }

    private static void forcedSleepMayOccurAfterDawn() {
        var useCase=new SleepUseCase();
        var combat=new HostileEncounterState();
        var cycle=new EnvironmentalCycle(DayPhase.DAY,Duration.ZERO,Weather.CLEAR);
        var state=new SleepState(cycle);
        var resources=new VitalResourceState(10,100,5,40);

        combat.begin();
        cycle.advance(EnvironmentalCycle.DAY_DURATION.multipliedBy(2)); // vuelve a DAY
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phase()==DayPhase.DAY,"La deuda puede vencer al amanecer.");
        combat.conclude();

        var forced=useCase.afterHostileEncounterConcluded(cycle,resources,state,null,null).result();
        org.junit.jupiter.api.Assertions.assertTrue(forced.slept(),"El sueño forzoso debe ignorar la restricción NIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phase()==DayPhase.AFTERNOON,
                "Debe atravesar el pipeline normal y completar la fase DAY actual.");
    }

    
}

package qa.integration;

import application.rest.SleepUseCase;
import domain.combat.HostileEncounterState;
import domain.combat.ProjectileDefensePolicy;
import domain.character.sheet.CharacterSheet;
import domain.combat.ai.loadout.VisibleLoadout;
import domain.environment.time.DayPhase;
import domain.environment.time.EnvironmentalCycle;
import domain.environment.time.Weather;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import domain.status.VitalResourceState;

import java.time.Duration;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public final class EnvironmentalCombatOptimizationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        environmentalCycleAndSleep();
        dualWieldAndShields();
        projectileDefense();
        chargedAttackIsDeclaredNotSelected();
    }

    private static void environmentalCycleAndSleep() {
        EnvironmentalCycle cycle = new EnvironmentalCycle(DayPhase.NIGHT, Duration.ofMinutes(29), Weather.CLEAR);
        cycle.advance(Duration.ofMinutes(1));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phase() == DayPhase.DAY && cycle.elapsedInPhase().isZero(),
                "Cada tramo debe durar exactamente media hora.");

        VitalResourceState resources = new VitalResourceState(25, 100, 4, 40);
        HostileEncounterState combat = new HostileEncounterState();
        domain.rest.SleepState sleepState = new domain.rest.SleepState(cycle);
        cycle.advance(Duration.ofMinutes(12));
        cycle.advance(Duration.ofMinutes(48)); // sueño voluntario sólo durante NIGHT.
        var result = new SleepUseCase().execute(true, combat, cycle, resources, sleepState, null, null).result();
        org.junit.jupiter.api.Assertions.assertTrue(result.slept() && cycle.phase() == DayPhase.DAY && cycle.elapsedInPhase().isZero(),
                "Dormir de noche debe terminar el tramo actual y comenzar el siguiente.");
        org.junit.jupiter.api.Assertions.assertTrue(resources.currentHealth() == 100 && resources.currentStamina() == 40,
                "Dormir debe restaurar todos los PV y PA.");

        combat.begin();
        var blocked = new SleepUseCase().execute(true, combat, cycle, resources, sleepState, null, null).result();
        org.junit.jupiter.api.Assertions.assertTrue(!blocked.slept() && cycle.phase() == DayPhase.DAY,
                "No puede dormirse durante un combate hostil.");
    }

    private static void dualWieldAndShields() {
        WeaponItem primary = weapon("Espada", Set.of(WeaponCombatAction.LIGHT_ATTACK));
        WeaponItem secondary = weapon("Daga", Set.of(WeaponCombatAction.LIGHT_ATTACK));
        secondary.withCombatActionsFor(WeaponActionMode.ALTERNATIVE,
                Set.of(WeaponCombatAction.LIGHT_ATTACK));

        WeaponInputResolutionPolicy policy = new WeaponInputResolutionPolicy();
        var light = policy.resolve(WeaponInput.LEFT_PRESS, primary, secondary, true, false);
        org.junit.jupiter.api.Assertions.assertTrue(light.allowed() && light.action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "LEFT CLICK debe atacar con el agarre alternativo secundario en dual wielding.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.resolve(WeaponInput.LEFT_HOLD, primary, secondary, true, false).allowed(),
                "Las armas ordinarias no deben bloquear en dual wielding.");

        secondary.withCombatActionsFor(WeaponActionMode.ALTERNATIVE, Set.of(WeaponCombatAction.PARRY));
        var exclusive = policy.resolve(WeaponInput.LEFT_PRESS, primary, secondary, true, true);
        org.junit.jupiter.api.Assertions.assertTrue(exclusive.allowed() && exclusive.action().orElseThrow() == WeaponCombatAction.PARRY,
                "Una acción exclusiva del agarre alternativo debe conservarse.");

        WeaponItem shield = weapon("Escudo", Set.of(WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.BLOCK, WeaponCombatAction.PARRY), WeaponTrait.SHIELD);
        shield.withCombatActionsFor(WeaponActionMode.ALTERNATIVE,
                Set.of(WeaponCombatAction.BLOCK, WeaponCombatAction.PARRY));
        var shieldParry = policy.resolve(WeaponInput.LEFT_PRESS, primary, shield, true, true);
        org.junit.jupiter.api.Assertions.assertTrue(shieldParry.action().orElseThrow() == WeaponCombatAction.BLOCK,
                "Desde  el escudo dedicado bloquea incluso dentro de ventana; no hace PARRY.");
        var shieldBlock = policy.resolve(WeaponInput.LEFT_HOLD, primary, shield, true, false);
        org.junit.jupiter.api.Assertions.assertTrue(shieldBlock.action().orElseThrow() == WeaponCombatAction.BLOCK,
                "Todo escudo debe poder bloquear.");
        var shieldBash = policy.resolve(WeaponInput.RIGHT_PRESS, shield, secondary, true, false);
        org.junit.jupiter.api.Assertions.assertTrue(shieldBash.action().orElseThrow() == WeaponCombatAction.LIGHT_ATTACK,
                "El escudo principal debe arrollar mediante su ataque ligero.");
    }

    private static void projectileDefense() {
        ProjectileDefensePolicy policy = new ProjectileDefensePolicy();
        WeaponItem shield = weapon("Escudo", Set.of(WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.BLOCK), WeaponTrait.SHIELD);
        WeaponItem parryingWeapon = weapon("Hoja de desvío", Set.of(WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.PARRY));
        org.junit.jupiter.api.Assertions.assertTrue(policy.canBlock(shield), "Los escudos deben bloquear proyectiles.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canParry(parryingWeapon), "Las armas con desvío deben desviar proyectiles.");
    }


    private static void chargedAttackIsDeclaredNotSelected() {
        WeaponItem chargedWeapon = weapon("Mandoble", Set.of(
                WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.CHARGED_ATTACK));
        var actor = new domain.combat.ai.declarative.CombatActorDecisionState("npc", domain.character.Gender.HOMBRE,
                CharacterSheet.of(10,10,10,40,10,10,10,20,10),1.8,40,40);
        var state = domain.combat.ai.declarative.MeleeDecisionState.initial(WeaponActionMode.PRIMARY, GripMode.ONE_HANDED);
        var candidates = new domain.combat.ai.declarative.MeleeActionCandidateResolver().resolve(actor, chargedWeapon, state);
        org.junit.jupiter.api.Assertions.assertTrue(candidates.stream().anyMatch(c -> c.action()==WeaponCombatAction.CHARGED_ATTACK),
                "LIM debe declarar CHARGED cuando el arma lo permite, no seleccionarlo heurísticamente.");
    }

    private static WeaponItem weapon(String name, Set<WeaponCombatAction> actions, WeaponTrait... traits) {
        return new WeaponItem(name, "Arma de verificación.", 1, new InventoryFootprint(1, 1), 1,
                List.of(new WeaponMode("Base", new LethalityProfile(1, 1, 1))),
                List.of(), List.of(), List.of(), OptionalDouble.empty(), 0, false,
                new WeaponConfigurationPolicy(List.of(
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                        new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE))), Set.of(traits))
                .withCombatPolicy(new WeaponCombatPolicy(actions));
    }

    
}

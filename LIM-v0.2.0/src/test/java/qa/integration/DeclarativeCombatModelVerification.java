package qa.integration;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.ai.declarative.*;
import domain.combat.moveset.ModeAttackRef;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.movement.*;
import java.util.Optional;
import java.util.OptionalDouble;

/**  — contrato declarativo melee/locomoción. No ejecutar en el ciclo normal. */
public final class DeclarativeCombatModelVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyLocomotionCurves(); verifyVerticalJump(); verifyMeleeHasNoScoring(); verifyRotorTransitions();
    }

    private static void verifyLocomotionCurves(){
        var p=new LocomotionStaminaPolicy();
        close(p.exertionFractionPerSecond(Gender.HOMBRE,20),.20,"H20 20%");
        close(p.exertionFractionPerSecond(Gender.HOMBRE,30),.15,"H30 interpolación");
        close(p.exertionFractionPerSecond(Gender.HOMBRE,40),.10,"H40 10%");
        close(p.exertionFractionPerSecond(Gender.MUJER,15),.20," 20%");
        close(p.exertionFractionPerSecond(Gender.MUJER,30),.10," 10%");
        close(p.verticalJumpCost(),1.0,"salto vertical 1 PA");
    }

    private static void verifyVerticalJump(){
        var p=new VerticalJumpPolicy(); var h=CharacterSheet.of(20,40,20,30,30,20,20,20,20); var m=CharacterSheet.of(20,30,20,30,30,20,20,20,20);
        close(p.heightMeters(Gender.HOMBRE,h,1.80),1.80*.40,"hombre segundo softcap 40% altura");
        close(p.heightMeters(Gender.MUJER,m,1.65),1.65*.35,"mujer segundo softcap 35% altura");
    }

    private static void verifyMeleeHasNoScoring(){
        var actor=new CombatActorDecisionState("npc",Gender.HOMBRE,CharacterSheet.of(20,40,20,60,60,20,20,20,20),1.80,80,100);
        var katana=MeleeWeaponCatalog.katanaTermoMecanicaV881();
        var state=MeleeDecisionState.initial(WeaponActionMode.PRIMARY,GripMode.TWO_HANDED);
        var candidates=new MeleeActionCandidateResolver().resolve(actor,katana,state);
        org.junit.jupiter.api.Assertions.assertTrue(!candidates.isEmpty(),"Katana declara alternativas melee");
        org.junit.jupiter.api.Assertions.assertTrue(candidates.stream().noneMatch(c->c.trajectory().isBlank()),"Las alternativas describen hechos, no score");
    }

    private static void verifyRotorTransitions(){
        var actor=new CombatActorDecisionState("npc",Gender.HOMBRE,CharacterSheet.of(20,40,20,60,60,20,20,20,20),1.80,80,100);
        var rotor=MeleeWeaponCatalog.espadonDeRotor();
        var state=new MeleeDecisionState(WeaponActionMode.PRIMARY,GripMode.TWO_HANDED,
                Optional.of(new ModeAttackRef(WeaponActionMode.PRIMARY,"2J")),1,0,false,false);
        var candidates=new MeleeActionCandidateResolver().resolve(actor,rotor,state);
        var oneHandLight=candidates.stream().filter(c->c.mode()==WeaponActionMode.ALTERNATIVE&&c.action()==WeaponCombatAction.LIGHT_ATTACK).findFirst().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(oneHandLight.transitionFromPrevious().isPresent(),"Rotor expone transición cruzada");
        close(oneHandLight.transitionFromPrevious().orElseThrow().executionTimeMultiplier(),.80,"2J→1L excellent");
    }

     private static void close(double a,double b,String m){if(Math.abs(a-b)>1e-9)throw new AssertionError(m+": "+a);}
}

package qa.integration;

import domain.combat.*;
import domain.combat.moveset.*;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

/**  — Rotor multimodo, Horca lineal y DESARMADO espejado/bloqueador. */
public final class RotorPitchforkUnarmedMovesetsVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){ verifyRotor(); verifyPitchfork(); verifyUnarmed(); }
 private static void verifyRotor(){
  var w=MeleeWeaponCatalog.espadonDeRotor();
  org.junit.jupiter.api.Assertions.assertTrue(w.availableConfigurations().contains(new WeaponConfiguration(GripMode.TWO_HANDED,WeaponActionMode.PRIMARY)),"Rotor 2H PRIMARY");
  org.junit.jupiter.api.Assertions.assertTrue(w.availableConfigurations().contains(new WeaponConfiguration(GripMode.ONE_HANDED,WeaponActionMode.ALTERNATIVE)),"Rotor 1H ALT");
  org.junit.jupiter.api.Assertions.assertTrue(w.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==1 && w.lightAttackComboFor(WeaponActionMode.ALTERNATIVE).attackCount()==1,"Rotor sin combo LIGHT/finisher");
  org.junit.jupiter.api.Assertions.assertTrue(!w.combatActionsFor(WeaponActionMode.PRIMARY).contains(WeaponCombatAction.DESTABILIZE),"Rotor sin DESTABILIZE propio");
  org.junit.jupiter.api.Assertions.assertTrue(w.combatActionsFor(WeaponActionMode.PRIMARY).contains(WeaponCombatAction.CHARGED_ATTACK),"Rotor recupera CHARGED");
  org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.strengthRequirement(3.8,GripMode.ONE_HANDED,w.traits())==48,"Rotor 1H fuerza 48");
  org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.dexterityRequirementForGrip(1.3,GripMode.ONE_HANDED,w.traits(),null,false)==20,"Rotor 1H destreza 20");
  var tr=w.crossModeTransitionProfile().transition(new ModeAttackRef(WeaponActionMode.PRIMARY,"2J"),new ModeAttackRef(WeaponActionMode.ALTERNATIVE,"1L")).orElseThrow();
  org.junit.jupiter.api.Assertions.assertTrue(tr.continuity()==TransitionContinuity.EXCELLENT,"2J->1L debe ser excelente");
  var policy=new WeaponInputResolutionPolicy();
  var rr=policy.resolve(WeaponInput.DESTABILIZE_PRESS,w,null,false,false);
  org.junit.jupiter.api.Assertions.assertTrue(rr.allowed()&&rr.action().orElseThrow()==WeaponCombatAction.CHARGED_ATTACK,"Input desestabilizador Rotor redirige a CHARGED");
 }
 private static void verifyPitchfork(){
  var w=MeleeWeaponCatalog.horca();
  org.junit.jupiter.api.Assertions.assertTrue(w.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==2,"Horca combo 2");
  org.junit.jupiter.api.Assertions.assertTrue(!w.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK)&&!w.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),"Horca sin H/C");
  org.junit.jupiter.api.Assertions.assertTrue(w.allowsCombatAction(WeaponCombatAction.DESTABILIZE),"Horca patada frontal DESTABILIZE");
 }
 private static void verifyUnarmed(){
  var w=UnarmedWeaponFactory.create(1.80,20);
  org.junit.jupiter.api.Assertions.assertTrue(w.supportsActionMode(WeaponActionMode.PRIMARY)&&w.supportsActionMode(WeaponActionMode.ALTERNATIVE),"Desarmado dos guardias");
  org.junit.jupiter.api.Assertions.assertTrue(w.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==3,"Desarmado combo 3");
  org.junit.jupiter.api.Assertions.assertTrue(!w.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK)&&w.allowsCombatAction(WeaponCombatAction.BLOCK),"Desarmado BLOCK sin CHARGED");
  var input=new WeaponInputResolutionPolicy();
  org.junit.jupiter.api.Assertions.assertTrue(input.unarmedMode()==WeaponActionMode.PRIMARY&&input.toggleUnarmedMode()==WeaponActionMode.ALTERNATIVE,"Mouse wheel espeja guardia");
 }
 
}

package qa.domain;

import domain.audio.ImpactSound;
import domain.combat.ai.execution.*;
import domain.combat.ai.memory.*;
import domain.combat.ai.observation.*;
import domain.combat.ai.perception.*;
import domain.inventory.item.WeaponCombatAction;
import java.util.*;

/** Regresión : memoria episódica continua y adaptación sin etiquetas nominales. */
public final class CombatOutcomeMemoryVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){offense();defense();sensory();configuration();resources();}
 static void offense(){
  var m=new CombatOutcomeMemory();var key=new CombatActionKey(CombatAction.RANGED_ATTACK,"Prueba");
  org.junit.jupiter.api.Assertions.assertTrue(m.offensiveAdjustment(key)==0,"sin experiencia");
  m.record(new OffensiveOutcome(key,true,80,1.2,4,Optional.of(ImpactSound.CLANG),.8,0));
  double good=m.offensiveAdjustment(key);org.junit.jupiter.api.Assertions.assertTrue(good>0,"resultado eficaz aumenta valor");
  for(int i=0;i<4;i++)m.record(new OffensiveOutcome(key,false,0,0,4,Optional.empty(),0,0));
  org.junit.jupiter.api.Assertions.assertTrue(m.offensiveAdjustment(key)<good,"fallos posteriores reducen valor sin clasificar al objetivo");
 }
 static void defense(){
  var m=new CombatOutcomeMemory();
  for(int i=0;i<3;i++)m.record(new DefensiveOutcome(AttackSourceType.FIREARM_PROJECTILE,WeaponCombatAction.LIGHT_ATTACK,CombatAction.BLOCK,false,70,1,0,0));
  for(int i=0;i<3;i++)m.record(new DefensiveOutcome(AttackSourceType.FIREARM_PROJECTILE,WeaponCombatAction.LIGHT_ATTACK,CombatAction.FEINT,true,0,0,2,0));
  org.junit.jupiter.api.Assertions.assertTrue(m.defensiveAdjustment(AttackSourceType.FIREARM_PROJECTILE,WeaponCombatAction.LIGHT_ATTACK,CombatAction.FEINT)>m.defensiveAdjustment(AttackSourceType.FIREARM_PROJECTILE,WeaponCombatAction.LIGHT_ATTACK,CombatAction.BLOCK),"respuesta que funcionó mejor");
 }
 static void sensory(){
  var m=new CombatOutcomeMemory();
  var inv=new PerceivedTargetEvidence(false,false,true,false,false,true,0);
  m.observePerception(new CombatPerceptionSnapshot(.4,List.of(),inv));
  org.junit.jupiter.api.Assertions.assertTrue(m.sensoryEvidence().ageOfLatestEvidence(0)==0,"evidencia inmediata");
  m.advanceTime(3.5);org.junit.jupiter.api.Assertions.assertTrue(Math.abs(m.sensoryEvidence().ageOfLatestEvidence(m.combatTimeSeconds())-3.5)<1e-9,"evidencia envejece");
  org.junit.jupiter.api.Assertions.assertTrue(m.sensoryEvidence().lastVisualSeconds()<0,"invisible no crea contacto visual");
 }
 static void configuration(){
  var m=new CombatOutcomeMemory();var k=CombatActionKey.of(CombatAction.HEAVY_ATTACK);
  m.observeTargetConfiguration("A");m.record(new OffensiveOutcome(k,true,100,1,1,Optional.empty(),0,0));org.junit.jupiter.api.Assertions.assertTrue(m.offensiveObservations(k)==1,"aprendido A");
  m.reveal("target","BIFILAR");m.observeTargetConfiguration("B");org.junit.jupiter.api.Assertions.assertTrue(m.offensiveObservations(k)==0,"cambio de loadout invalida rendimiento");org.junit.jupiter.api.Assertions.assertTrue(m.hasObservedResource("target","BIFILAR"),"hecho observado persiste");
 }
 static void resources(){var m=new CombatOutcomeMemory();m.reveal("x","TOROIDAL");org.junit.jupiter.api.Assertions.assertTrue(m.hasObservedResource("x","TOROIDAL"),"recurso revelado");m.clear();org.junit.jupiter.api.Assertions.assertTrue(!m.hasObservedResource("x","TOROIDAL"),"fin de encuentro limpia memoria");}
 
}

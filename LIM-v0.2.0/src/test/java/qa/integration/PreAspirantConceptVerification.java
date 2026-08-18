package qa.integration;
import domain.bestiarium.physical_plane.ancient.*;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.DamageType;
import domain.combat.ai.declarative.*;
import domain.combat.moveset.UnarmedMovesetCatalog;
import domain.control.*;
import domain.inventory.item.*;
import domain.movement.*;
import java.util.*;

public final class PreAspirantConceptVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){ anteo(); locomotion(); aquatic(); unarmed(); controls(); }
 static void anteo(){
  var d=new AncientDamagePolicy(); org.junit.jupiter.api.Assertions.assertTrue(d.canReceive(DamageType.BLUNT)&&d.canReceive(DamageType.ELECTRICITY),"Anteo material debe recibir daño físico."); org.junit.jupiter.api.Assertions.assertTrue(!d.canReceive(DamageType.CURSE)&&!d.canReceive(DamageType.FRENZY),"Ancient no debe usar canales espirituales corporales."); org.junit.jupiter.api.Assertions.assertTrue(d.naturalMeleeType(AncientForm.HUMANA)==DamageType.BLUNT&&d.naturalMeleeType(AncientForm.CAMBIAFORMAS)==DamageType.BLUNT,"Golpe natural contundente.");
  var p=new AnteoChargedPropertyPolicy(); var a=p.resolve(20,50,75); close(a.staminaAfter(),70,"CARGADO 20 a PA"); close(a.healthDamage(),0,"sin excedente PV"); var b=p.resolve(30,50,75); close(b.staminaAfter(),75,"PA completos"); close(b.healthDamage(),5,"excedente eléctrico a PV"); close(b.electricityConvertedToStamina(),25,"absorción exacta");
  String n=AnteoDoctrine.characterDescription(); org.junit.jupiter.api.Assertions.assertTrue(n.contains("CARGADO")&&n.contains("rellena primero los PA")&&!n.contains("todos sus ataques producen daño MALDITO"),"Narrativa Anteo no consolidada.");
 }
 static void locomotion(){
  var p=new LocomotionDistancePolicy(); double h=1.72; close(p.metersPerSecond(LocomotionMode.WALKING,Gender.HOMBRE,h),1.376,"caminar"); close(p.metersPerSecond(LocomotionMode.TROTTING,Gender.HOMBRE,h),3.01,"trotar"); close(p.metersPerSecond(LocomotionMode.RUNNING,Gender.HOMBRE,h),5.504,"correr"); close(p.metersPerSecond(LocomotionMode.CLIMBING,Gender.HOMBRE,h),p.metersPerSecond(LocomotionMode.CROUCH_WALKING,Gender.HOMBRE,h),"escalar=agachado");
  var s=new LocomotionStaminaPolicy(); close(s.climbingCostPerSecond(Gender.HOMBRE,CharacterSheet.of(10,10,10,10,10,10,10,10,10),100),1,"escalar 1 PA/s"); close(s.divingCostPerSecond(),1,"bucear 1 PA/s");
 }
 static void aquatic(){
  var p=new LocomotionDistancePolicy(); double h=1.65; close(p.metersPerSecond(LocomotionMode.SWIMMING,Gender.HOMBRE,h),p.metersPerSecond(LocomotionMode.WALKING,Gender.HOMBRE,h),"nado normal=caminar"); close(p.metersPerSecond(LocomotionMode.FAST_SWIMMING,Gender.HOMBRE,h),p.metersPerSecond(LocomotionMode.RUNNING,Gender.HOMBRE,h),"brazadas=correr"); close(p.metersPerSecond(LocomotionMode.DIVING,Gender.HOMBRE,h),p.metersPerSecond(LocomotionMode.TROTTING,Gender.HOMBRE,h),"buceo=trote");
  var swim=new SwimmingPolicy(); var r=swim.tick(2,2,false); org.junit.jupiter.api.Assertions.assertTrue(r.dead()&&r.staminaAfter()==0,"PA=0 bajo agua debe matar.");
 }
 static void unarmed(){
  var low=UnarmedMovesetCatalog.rightLead(1); var d35=UnarmedMovesetCatalog.rightLead(35); var d50=UnarmedMovesetCatalog.rightLead(50);
  String lowH=motion(low,WeaponCombatAction.HEAVY_ATTACK), lowD=motion(low,WeaponCombatAction.DESTABILIZE); org.junit.jupiter.api.Assertions.assertTrue(lowH.contains("Patada baja")&&lowD.contains("Patada frontal"),"Fallback desarmado incorrecto."); org.junit.jupiter.api.Assertions.assertTrue(motion(d35,WeaponCombatAction.DESTABILIZE).contains("Back kick"),"Back kick DES35"); org.junit.jupiter.api.Assertions.assertTrue(motion(d50,WeaponCombatAction.HEAVY_ATTACK).contains("Tornado kick 360"),"Tornado DES50");
  var actor=new CombatActorDecisionState("x",Gender.HOMBRE,CharacterSheet.of(10,10,10,10,1,10,10,10,10),1.72,10,10); var cs=new MeleeActionCandidateResolver().resolve(actor,UnarmedWeaponFactory.create(),MeleeDecisionState.initial(WeaponActionMode.PRIMARY,GripMode.ONE_HANDED)); org.junit.jupiter.api.Assertions.assertTrue(cs.stream().anyMatch(x->x.action()==WeaponCombatAction.HEAVY_ATTACK&&x.trajectory().contains("Patada baja")),"MDPAR debe ver fallback heavy real.");
 }
 static void controls(){ org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b->b.input().equals("C")&&b.action()==ControlAction.DIVE),"C debe bucear en agua."); }
 static String motion(domain.combat.moveset.MeleeMovesetProfile p,WeaponCombatAction a){return p.motions().stream().filter(m->m.action()==a).findFirst().orElseThrow().trajectory();}
  static void close(double a,double b,String m){if(Math.abs(a-b)>1e-9)throw new AssertionError(m+": "+a+" != "+b);}
}

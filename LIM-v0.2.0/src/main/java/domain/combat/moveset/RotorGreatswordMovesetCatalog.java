package domain.combat.moveset;
import domain.inventory.item.*;
import java.util.*;

/**  — Espadón de Rotor: acciones unitarias por agarre y grafo cruzado completo. */
public final class RotorGreatswordMovesetCatalog {
 private RotorGreatswordMovesetCatalog(){}
 public static MeleeMovesetProfile twoHanded(){
  var l=m("2L",WeaponCombatAction.LIGHT_ATTACK,1,"Hombro derecho, control bimanual","Barrido diagonal amplio con avance y conservación parcial de momento","Filo","Hoja baja al lado izquierdo",BodyAdvance.SLIGHT);
  var h=m("2H",WeaponCombatAction.HEAVY_ATTACK,0,"Guardia alta bimanual","Descendente brutal con paso y caída de hombros/cadera","Filo","Arma baja y adelantada",BodyAdvance.COMMITTED);
  var c=m("2C",WeaponCombatAction.CHARGED_ATTACK,0,"Hombro y cadera retraídos","Punzada frontal de máxima extensión; al final el arma cae por masa e inercia","Punta","Punta adelantada y hoja venciendo hacia abajo",BodyAdvance.COMMITTED);
  var j=m("2J",WeaponCombatAction.JUMP_ATTACK,0,"Impulso bimanual","Salto con descarga descendente vertical/diagonal y gran ocupación frontal","Filo","Arma baja frontal",BodyAdvance.COMMITTED);
  return new MeleeMovesetProfile(List.of(l,h,c,j),internal(List.of(l,h,c,j)));
 }
 public static MeleeMovesetProfile oneHanded(){
  var l=m("1L",WeaponCombatAction.LIGHT_ATTACK,1,"Arma extendida a un lado","Semi-pirueta de barrido, dejando pasar la masa alrededor del cuerpo","Filo","Arma extendida al lado contrario",BodyAdvance.SLIGHT);
  var h=m("1H",WeaponCombatAction.HEAVY_ATTACK,0,"Arma lateral a una mano","Swing de cuerpo completo que gana espacio aunque sacrifica recuperación y momento","Filo","Brazo extendido; arma desacelerada fuera de línea",BodyAdvance.COMMITTED);
  var c=m("1C",WeaponCombatAction.CHARGED_ATTACK,0,"Arma colgada del hombro/cadera","Preparación rotatoria amplia y descarga de trayectoria envolvente","Filo","Arma baja y adelantada",BodyAdvance.COMMITTED);
  var j=m("1J",WeaponCombatAction.JUMP_ATTACK,0,"Arma baja/adelantada","Salto con rotación aérea: el arma pasa sobre el cuerpo como un rotor y cae en golpe descendente","Filo","Descarga vertical, cuerpo aterrizando tras la rotación",BodyAdvance.COMMITTED);
  return new MeleeMovesetProfile(List.of(l,h,c,j),internal(List.of(l,h,c,j)));
 }
 private static List<MeleeAttackTransition> internal(List<MeleeAttackMotion> ms){
  List<MeleeAttackTransition> r=new ArrayList<>();
  for(var a:ms)for(var b:ms)if(a!=b){
   TransitionContinuity q=quality(a.id(),b.id());
   r.add(new MeleeAttackTransition(a.id(),b.id(),q,"Compatibilidad biomecánica específica entre la postura final de "+a.id()+" y la entrada de "+b.id()+"."));
  } return r;
 }
 private static TransitionContinuity quality(String a,String b){
  if((a.equals("1L")&&b.equals("1H"))||(a.equals("2J")&&b.equals("2C"))) return TransitionContinuity.FORCED;
  if((a.equals("2H")&&b.equals("2C"))||(a.equals("1C")&&b.equals("1J"))||(a.equals("2L")&&b.equals("2H"))) return TransitionContinuity.NATURAL;
  if((a.equals("2C")&&b.equals("2L"))||(a.equals("1J")&&b.equals("1L"))) return TransitionContinuity.EXCELLENT;
  return TransitionContinuity.NEUTRAL;
 }
 public static CrossModeTransitionProfile crossMode(){
  var P=WeaponActionMode.PRIMARY; var A=WeaponActionMode.ALTERNATIVE;
  List<CrossModeAttackTransition> t=new ArrayList<>();
  String[] p={"2L","2H","2C","2J"}, a={"1L","1H","1C","1J"};
  for(String x:p)for(String y:a)t.add(x(P,x,A,y,crossQuality(x,y)));
  for(String x:a)for(String y:p)t.add(x(A,x,P,y,crossQuality(y,x)));
  return new CrossModeTransitionProfile(t);
 }
 private static CrossModeAttackTransition x(WeaponActionMode fm,String f,WeaponActionMode tm,String to,TransitionContinuity q){return new CrossModeAttackTransition(new ModeAttackRef(fm,f),new ModeAttackRef(tm,to),q,"Cambio de agarre durante la recuperación; calidad fijada por orientación, centro de masas y necesidad de recolocar hombros/pies.");}
 private static TransitionContinuity crossQuality(String two,String one){
  if((two.equals("2J")&&one.equals("1L"))||(two.equals("2H")&&one.equals("1C"))||(two.equals("2L")&&one.equals("1J"))) return TransitionContinuity.EXCELLENT;
  if((two.equals("2H")&&one.equals("1L"))||(two.equals("2C")&&one.equals("1J"))||(two.equals("2L")&&one.equals("1H"))) return TransitionContinuity.NATURAL;
  if((two.equals("2C")&&one.equals("1H"))||(two.equals("2J")&&one.equals("1C"))) return TransitionContinuity.FORCED;
  return TransitionContinuity.NEUTRAL;
 }
 private static MeleeAttackMotion m(String id,WeaponCombatAction a,int o,String s,String tr,String surf,String e,BodyAdvance ba){return new MeleeAttackMotion(id,a,o,s,tr,surf,e,ba);}
}

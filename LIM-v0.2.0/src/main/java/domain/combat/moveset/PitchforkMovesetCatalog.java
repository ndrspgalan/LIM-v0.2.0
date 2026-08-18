package domain.combat.moveset;
import domain.inventory.item.*; import java.util.List;
public final class PitchforkMovesetCatalog { private PitchforkMovesetCatalog(){}
 public static MeleeMovesetProfile horca(){
  var l1=new MeleeAttackMotion("L1",WeaponCombatAction.LIGHT_ATTACK,1,"Horca retraída frente al torso","Estocada frontal directa","Púas","Horca extendida",BodyAdvance.SLIGHT);
  var l2=new MeleeAttackMotion("L2",WeaponCombatAction.LIGHT_ATTACK,2,"Horca extendida","Retirada corta y segunda estocada sobre la misma línea","Púas","Horca extendida/recuperable",BodyAdvance.SLIGHT);
  var j=new MeleeAttackMotion("J1",WeaponCombatAction.JUMP_ATTACK,0,"Impulso","Estocada descendente/oblicua aprovechando el alcance","Púas","Púas bajas al aterrizar",BodyAdvance.COMMITTED);
  var d=new MeleeAttackMotion("D1",WeaponCombatAction.DESTABILIZE,0,"Guardia con horca","Patada frontal mientras las manos mantienen el asta fuera de la trayectoria de la pierna","Pie","Guardia recuperada",BodyAdvance.SLIGHT);
  return new MeleeMovesetProfile(List.of(l1,l2,j,d),List.of(new MeleeAttackTransition("L1","L2",TransitionContinuity.EXCELLENT,"La retracción mínima prepara inmediatamente la segunda punta."),new MeleeAttackTransition("L2","L1",TransitionContinuity.NATURAL,"La segunda retirada reconstruye la primera línea.")));
 }}

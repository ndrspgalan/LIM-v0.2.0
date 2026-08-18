package domain.combat.moveset;
import domain.inventory.item.*; import java.util.List;
public final class UnarmedMovesetCatalog {
 private UnarmedMovesetCatalog(){}
 public static MeleeMovesetProfile rightLead(){return rightLead(0);} public static MeleeMovesetProfile leftLead(){return leftLead(0);}
 public static MeleeMovesetProfile rightLead(int dexterity){return stance("D","I",dexterity);} public static MeleeMovesetProfile leftLead(int dexterity){return stance("I","D",dexterity);}
 private static MeleeMovesetProfile stance(String lead,String rear,int dex){
  var l1=new MeleeAttackMotion("L1",WeaponCombatAction.LIGHT_ATTACK,1,"Guardia adelantada "+lead,"Directo con brazo adelantado "+lead,"Puño","Guardia compacta",BodyAdvance.SLIGHT);
  var l2=new MeleeAttackMotion("L2",WeaponCombatAction.LIGHT_ATTACK,2,"Guardia compacta","Swing/hook con brazo "+rear,"Puño","Rotación lateral",BodyAdvance.SLIGHT);
  var l3=new MeleeAttackMotion("L3",WeaponCombatAction.LIGHT_ATTACK,3,"Rotación lateral","Gancho compacto con brazo "+lead,"Puño","Guardia recuperada",BodyAdvance.SLIGHT);
  String heavy=dex>=50?"Tornado kick 360° pivotando sobre el lado "+lead:"Patada baja al muslo desde guardia";
  String destabilize=dex>=35?"Back kick con extensión posterior y retorno":"Patada frontal de empuje y recuperación";
  var h=new MeleeAttackMotion("H1",WeaponCombatAction.HEAVY_ATTACK,0,"Guardia",heavy,"Pierna","Guardia espejada",BodyAdvance.COMMITTED);
  var d=new MeleeAttackMotion("D1",WeaponCombatAction.DESTABILIZE,0,"Guardia",destabilize,dex>=35?"Talón":"Planta del pie","Guardia",BodyAdvance.SLIGHT);
  return new MeleeMovesetProfile(List.of(l1,l2,l3,h,d),List.of(new MeleeAttackTransition("L1","L2",TransitionContinuity.EXCELLENT,"El directo retrae mientras carga el hombro contrario."),new MeleeAttackTransition("L2","L3",TransitionContinuity.EXCELLENT,"La rotación del swing deja cargado el gancho."),new MeleeAttackTransition("L3","L1",TransitionContinuity.NATURAL,"El gancho recupera la guardia adelantada."),new MeleeAttackTransition("H1","L1",TransitionContinuity.NATURAL,"La técnica fuerte devuelve una guardia utilizable.")));
 }
}

package domain.ability;
/** RESTAURAR: techo absoluto de PV ajenos igual a PA TOTALES del restaurador. */
public final class RestorePolicy {
 public Result resolve(double targetCurrentHealth,double targetMaximumHealth,double healerMaximumStamina,double targetStaminaRegen,double elapsedSeconds){
  if(targetCurrentHealth<=0)return new Result(false,targetCurrentHealth,0,"RESTAURAR no puede aplicarse a un organismo con 0 PV.");
  double ceiling=Math.min(targetMaximumHealth,healerMaximumStamina); if(targetCurrentHealth>=ceiling)return new Result(false,targetCurrentHealth,ceiling,"El objetivo ya está por encima del techo restaurable.");
  double restored=Math.max(0,targetStaminaRegen)*Math.max(0,elapsedSeconds); return new Result(true,Math.min(ceiling,targetCurrentHealth+restored),ceiling,"RESTAURAR progresa al ritmo del PA REGEN del receptor.");
 }
 public record Result(boolean applied,double healthAfter,double ceilingHealth,String reason){}
}

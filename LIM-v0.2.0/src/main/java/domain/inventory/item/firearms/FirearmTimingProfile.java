package domain.inventory.item.firearms;
/** Temporalidad mecánica explícita de una firearm. Cero significa no aplicable. */
public record FirearmTimingProfile(double reloadDurationSeconds,double shotIntervalSeconds,double pressureStepSeconds){
 public FirearmTimingProfile{if(reloadDurationSeconds<0||shotIntervalSeconds<0||pressureStepSeconds<0)throw new IllegalArgumentException("Los tiempos no pueden ser negativos.");}
 public double fullPressureRestoreSeconds(int missingSteps){return Math.max(0,missingSteps)*pressureStepSeconds;}
}

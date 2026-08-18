package domain.save.snapshot;
import java.io.Serializable;
/** estado de supervivencia suficiente para reanudar exactamente hambre/sed y deuda de sueño. */
public record SurvivalSnapshot(double health,double stamina,String hungerLevel,double hungerHoursUntilNextStage,String lastConsumedFood,int thirstLevel,double thirstHoursUntilNextLevel,double hydratedHoursRemaining,long wakeCount,long lastSleptCompletedDay,long wakeBaselineCompletedDay,boolean hasSlept) implements Serializable {
 public SurvivalSnapshot{hungerLevel=hungerLevel==null?"SATIATED":hungerLevel;lastConsumedFood=lastConsumedFood==null?"":lastConsumedFood;}
 public SurvivalSnapshot(double health,double stamina,double hunger,double thirst){this(health,stamina,"SATIATED",domain.survival.HungerState.ORDINARY_STAGE_HOURS,"",0,domain.survival.ThirstState.HOURS_PER_LEVEL,0,0,-1,0,false);}
}

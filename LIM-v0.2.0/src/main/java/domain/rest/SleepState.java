package domain.rest;
import domain.environment.time.EnvironmentalCycle;
import java.util.Objects;
public final class SleepState {
 public static final long MAX_COMPLETED_DAYS_WITHOUT_SLEEP=2L;
 private long lastSleptCompletedDay,wakeBaselineCompletedDay,wakeCount; private boolean hasSlept,forcedSleepDue;
 public SleepState(EnvironmentalCycle cycle){Objects.requireNonNull(cycle);lastSleptCompletedDay=-1L;wakeBaselineCompletedDay=cycle.completedDays();}
 public SleepState(EnvironmentalCycle cycle,long lastSleptCompletedDay,long wakeBaselineCompletedDay,long wakeCount,boolean hasSlept){Objects.requireNonNull(cycle);this.lastSleptCompletedDay=lastSleptCompletedDay;this.wakeBaselineCompletedDay=wakeBaselineCompletedDay;this.wakeCount=Math.max(0,wakeCount);this.hasSlept=hasSlept;synchronize(cycle);}
 public boolean hasSlept(){return hasSlept;} public long lastSleptCompletedDay(){return lastSleptCompletedDay;} public long wakeBaselineCompletedDay(){return wakeBaselineCompletedDay;} public long wakeCount(){return wakeCount;} public boolean forcedSleepDue(){return forcedSleepDue;}
 public boolean sleptDuringCurrentDay(EnvironmentalCycle cycle){return hasSlept&&lastSleptCompletedDay==cycle.completedDays();}
 public long completedDaysWithoutSleep(EnvironmentalCycle cycle){long baseline=hasSlept?lastSleptCompletedDay:wakeBaselineCompletedDay;return Math.max(0L,cycle.completedDays()-baseline);}
 public void synchronize(EnvironmentalCycle cycle){if(completedDaysWithoutSleep(cycle)>=MAX_COMPLETED_DAYS_WITHOUT_SLEEP)forcedSleepDue=true;}
 public void recordWake(EnvironmentalCycle cycle){Objects.requireNonNull(cycle);hasSlept=true;lastSleptCompletedDay=cycle.completedDays();wakeBaselineCompletedDay=cycle.completedDays();wakeCount++;forcedSleepDue=false;}
 public void recordSleep(EnvironmentalCycle cycle){recordWake(cycle);} // alias semántico interno
}

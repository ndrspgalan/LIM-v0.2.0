package domain.ability.event;
import domain.status.VitalResourceState;
/** Compatibilidad de evento antiguo con la condición  de golpe mortal melee. */
public record EnemyDiedEvent(VitalResourceState beneficiary,int enemyVitality,double enemyTotalHealth,boolean enemyHealthRegenerationInhibited,boolean killedByBeneficiaryMelee) implements MasteryEvent {
 public EnemyDiedEvent(VitalResourceState beneficiary,int enemyVitality,double enemyTotalHealth,boolean enemyHealthRegenerationInhibited){this(beneficiary,enemyVitality,enemyTotalHealth,enemyHealthRegenerationInhibited,true);}
}

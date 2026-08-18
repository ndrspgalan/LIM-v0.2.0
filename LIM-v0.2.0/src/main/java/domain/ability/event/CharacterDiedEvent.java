package domain.ability.event;
import domain.status.VitalResourceState;
/** DRENAR sólo es elegible si el beneficiario causó personalmente el golpe mortal cuerpo a cuerpo. */
public record CharacterDiedEvent(VitalResourceState beneficiary,int vitality,double totalHealth,boolean healthRegenerationInhibited,boolean killedByBeneficiaryMelee) implements MasteryEvent {
 public CharacterDiedEvent(VitalResourceState beneficiary,int vitality,double totalHealth,boolean healthRegenerationInhibited){this(beneficiary,vitality,totalHealth,healthRegenerationInhibited,true);}
}

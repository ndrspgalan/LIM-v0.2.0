package domain.combat.stamina;
import domain.ability.NullificationPolicy;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.runic.EffectImmunity;
/** Latencia : max(0, 1,20 - 0,01*AGUANTE); frío fuerza 1,20 s; cristal marrón fuerza 0. */
public final class StaminaRegenerationDelayPolicy {
 public static final double BASE_DELAY_SECONDS=1.20; public static final double PER_ENDURANCE_REDUCTION=0.01; public static final double FROST_DELAY_SECONDS=1.20;
 /** Compatibilidad nominal; representa el basal a AGUANTE 0. */ public static final double DELAY_SECONDS=BASE_DELAY_SECONDS;
 public double naturalDelaySeconds(CharacterSheet sheet){if(sheet==null)return BASE_DELAY_SECONDS;return Math.max(0,BASE_DELAY_SECONDS-PER_ENDURANCE_REDUCTION*sheet.valueOf(Attribute.AGUANTE));}
 public double delaySeconds(CharacterSheet sheet,EquipmentState equipment,NullificationPolicy.SuppressionState suppression){return delaySeconds(sheet,equipment,suppression,false);}
 public double delaySeconds(CharacterSheet sheet,EquipmentState equipment,NullificationPolicy.SuppressionState suppression,boolean bitingFrostActive){if(sheet!=null&&equipment!=null&&equipment.effectImmunities(sheet,suppression).contains(EffectImmunity.STAMINA_REGEN_DELAY))return 0;return bitingFrostActive?FROST_DELAY_SECONDS:naturalDelaySeconds(sheet);}
 public double delaySecondsWithConsumables(CharacterSheet sheet,EquipmentState equipment,NullificationPolicy.SuppressionState suppression,
                                             boolean bitingFrostActive,boolean meadActive,boolean irndAftereffect){
  if(irndAftereffect) return 1.20; // secuela no mitigable
  if(meadActive) return 1.20;
  return delaySeconds(sheet,equipment,suppression,bitingFrostActive);
 }

 public boolean canRegenerate(double elapsed,CharacterSheet sheet,EquipmentState equipment,NullificationPolicy.SuppressionState suppression){return elapsed>=delaySeconds(sheet,equipment,suppression);}
 public boolean canRegenerate(double elapsed){if(!Double.isFinite(elapsed)||elapsed<0)throw new IllegalArgumentException("Tiempo inválido.");return elapsed>=BASE_DELAY_SECONDS;}
 public double regenerate(double current,double maximum,double perSecond,double elapsed,double since){if(current<0||maximum<=0||current>maximum||perSecond<0||elapsed<0)throw new IllegalArgumentException("Valores inválidos.");return canRegenerate(since)?Math.min(maximum,current+perSecond*elapsed):current;}
}

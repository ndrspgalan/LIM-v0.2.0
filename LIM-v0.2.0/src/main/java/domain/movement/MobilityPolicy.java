package domain.movement;
import domain.ability.*; import domain.character.sheet.*; import domain.status.ActiveTherapeuticEffects; import domain.inventory.equipment.EquipmentState; import java.util.Objects;
public final class MobilityPolicy {
 private static final int FPS=60,FEINT_ANIMATION_FRAMES=20,BASE_INVULNERABILITY_FRAMES=0,MIRAGE_INVULNERABILITY_FRAMES=18;
 public MobilityProfile resolve(double h,CharacterSheet s,CharacterMasteryCollection m){return resolve(h,s,m,ActiveTherapeuticEffects.none());}
 public MobilityProfile resolve(double h,CharacterSheet s,CharacterMasteryCollection m,EquipmentState e){return resolve(h,s,m,ActiveTherapeuticEffects.none(),e);}
 public MobilityProfile resolve(double h,CharacterSheet s,CharacterMasteryCollection m,ActiveTherapeuticEffects t,EquipmentState e){return resolveInternal(h,s,m,t,e.effectiveAttributeValue(Attribute.DESTREZA,s));}
 public MobilityProfile resolve(double h,CharacterSheet s,CharacterMasteryCollection m,ActiveTherapeuticEffects t){return resolveInternal(h,s,m,t,s.valueOf(Attribute.DESTREZA));}
 private MobilityProfile resolveInternal(double h,CharacterSheet s,CharacterMasteryCollection m,ActiveTherapeuticEffects t,int dex){
  Objects.requireNonNull(s);Objects.requireNonNull(m);Objects.requireNonNull(t);if(h<=0)throw new IllegalArgumentException("La altura debe ser positiva.");
  int d=Math.max(35,Math.min(50,dex)); double feint=h*MasteryMath.linearMultiplier(d,35,50,.35,.50);
  boolean p=m.isPassiveActive("RECICLAJE DE PULSIÓN",s); double jump=h*new PulsionCombatPolicy().jumpHeightMultiplier(s.valueOf(Attribute.AGUANTE),p);
  int ifr=m.isActive("MIRAGE")?(int)Math.round(MIRAGE_INVULNERABILITY_FRAMES*t.mirageInvulnerabilityMultiplier()):BASE_INVULNERABILITY_FRAMES;
  return new MobilityProfile(feint*t.feintReachMultiplier(),jump,FEINT_ANIMATION_FRAMES,ifr,FPS);
 }
}

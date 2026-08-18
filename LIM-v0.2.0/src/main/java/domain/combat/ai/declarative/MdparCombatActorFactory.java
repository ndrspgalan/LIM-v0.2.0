package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.*;
import domain.character.Gender;
import domain.social.CanonicalSubprofessionProfile;
import java.util.Objects;

/**  — frontera de población del futuro endpoint: NPC de subprofesión + Ferae INTELIGENCIA.
 * Kenan, NPC canónicos, ANCIENT y ASPIRANT carecen deliberadamente de factoría aquí.
 */
public final class MdparCombatActorFactory {
 private MdparCombatActorFactory(){}
 public static CombatActorDecisionState fromSubprofession(String id, CanonicalSubprofessionProfile profile, Gender gender,double currentPa,double totalPa){
  Objects.requireNonNull(profile); Objects.requireNonNull(gender);
  if(!profile.genders().contains(gender)) throw new IllegalArgumentException("Sexo no canónico para "+profile.subprofession()+" / "+profile.characterClass());
  return new CombatActorDecisionState(id,gender,profile.attributes(),profile.subprofession().canonicalHeightMeters(gender),currentPa,totalPa,CombatActorOriginFact.subprofession(profile.subprofession()));
 }
 public static CombatActorDecisionState fromIntelligenceFerae(String id,FeraeProfile profile,double currentPa,double totalPa){
  Objects.requireNonNull(profile);
  if(profile.species().branch()!=FeraeBranch.INTELIGENCIA) throw new IllegalArgumentException("El endpoint  sólo admite Ferae de INTELIGENCIA.");
  Gender gender=profile.sex()==FeraeSex.MACHO?Gender.HOMBRE:Gender.MUJER;
  return new CombatActorDecisionState(id,gender,profile.attributes(),profile.canonicalHeightMeters(),currentPa,totalPa,CombatActorOriginFact.ferae(profile.species(),profile.sex()));
 }
}

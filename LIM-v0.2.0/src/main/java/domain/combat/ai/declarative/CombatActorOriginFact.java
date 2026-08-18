package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeSex;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.social.Subprofession;
import java.util.Objects;
import java.util.Optional;

/**  — identidad de dominio que acompaña al actor enviado al endpoint MDPAR. */
public record CombatActorOriginFact(CombatActorOriginKind kind, Optional<Subprofession> subprofession, Optional<FeraeSpecies> feraeSpecies, Optional<FeraeSex> feraeSex){
 public CombatActorOriginFact{ Objects.requireNonNull(kind); subprofession=subprofession==null?Optional.empty():subprofession; feraeSpecies=feraeSpecies==null?Optional.empty():feraeSpecies; feraeSex=feraeSex==null?Optional.empty():feraeSex; }
 public static CombatActorOriginFact unspecified(){return new CombatActorOriginFact(CombatActorOriginKind.UNSPECIFIED,Optional.empty(),Optional.empty(),Optional.empty());}
 public static CombatActorOriginFact subprofession(Subprofession s){return new CombatActorOriginFact(CombatActorOriginKind.SUBPROFESSION,Optional.of(Objects.requireNonNull(s)),Optional.empty(),Optional.empty());}
 public static CombatActorOriginFact ferae(FeraeSpecies s,FeraeSex sex){return new CombatActorOriginFact(CombatActorOriginKind.FERAE_INTELLIGENCE,Optional.empty(),Optional.of(Objects.requireNonNull(s)),Optional.of(Objects.requireNonNull(sex)));}
}

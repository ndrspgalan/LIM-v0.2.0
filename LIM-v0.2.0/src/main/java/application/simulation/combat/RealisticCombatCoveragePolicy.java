package application.simulation.combat;

import domain.bestiarium.physical_plane.ferae.*;
import domain.social.*;
import java.util.Objects;

/** profundidad de QA por probabilidad/escala realista de encuentro, nunca por importancia social. */
public final class RealisticCombatCoveragePolicy {
    private RealisticCombatCoveragePolicy(){}
    public static CombatCoverageDepth forSubprofession(Subprofession s){
        Objects.requireNonNull(s);
        if(s.profession()==Profession.SOLDIER || s.profession()==Profession.MERCENARY) return CombatCoverageDepth.EXHAUSTIVE_PAIRWISE;
        return switch(s.profession()){
            case HUNTER, SAILOR, NOBLE, BLACKSMITH -> CombatCoverageDepth.REPRESENTATIVE;
            default -> CombatCoverageDepth.SYMBOLIC;
        };
    }
    public static CombatCoverageDepth forFerae(FeraeSpecies s){
        Objects.requireNonNull(s);
        if(s.branch()!=FeraeBranch.INTELIGENCIA) throw new IllegalArgumentException("Sólo Ferae INTELIGENCIA pertenecen al contrato MDPAR.");
        return CombatCoverageDepth.HABITAT_EXHAUSTIVE;
    }
}

package domain.ability;

import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.inventory.item.armor.ArmorMaterial;
import java.util.*;

/** Servicios y estado mutable del mundo necesarios para resolver una maestría. */
public final class MasteryWorldContext {
    private final MasteryEffectRegistry effects;
    private final List<MasteryTargetContext> encounterTargets;
    private boolean coldBuildUpActive;
    private double actorHeightMeters=1.70;
    private boolean naked;
    private ArmorMaterial contactedMaterial = ArmorMaterial.CLOTH;
    private boolean intersticeTarget;
    private boolean spiritInfatigableActive;
    private boolean helicalReleaseActive;
    private boolean helicalOptimizationActive;
    private FeraeSpecies encounteredFerae;
    private AnimalEmpathyContext animalEmpathyContext;

    public MasteryWorldContext(MasteryEffectRegistry effects, List<MasteryTargetContext> encounterTargets) {
        this.effects=Objects.requireNonNull(effects); this.encounterTargets=new ArrayList<>(Objects.requireNonNull(encounterTargets));
    }
    public MasteryEffectRegistry effects(){return effects;} public List<MasteryTargetContext> encounterTargets(){return List.copyOf(encounterTargets);}
    public double actorHeightMeters(){return actorHeightMeters;} public MasteryWorldContext actorHeightMeters(double v){if(!Double.isFinite(v)||v<=0)throw new IllegalArgumentException("Altura inválida.");actorHeightMeters=v;return this;}
    public boolean coldBuildUpActive(){return coldBuildUpActive;} public MasteryWorldContext coldBuildUpActive(boolean v){coldBuildUpActive=v;return this;}
    public boolean naked(){return naked;} public MasteryWorldContext naked(boolean v){naked=v;return this;}
    public ArmorMaterial contactedMaterial(){return contactedMaterial;}
    public MasteryWorldContext contactedMaterial(ArmorMaterial material){contactedMaterial=Objects.requireNonNull(material);return this;}
    /** Compatibilidad histórica del contexto de pruebas. */
    public boolean touchingSteel(){return contactedMaterial==ArmorMaterial.STEEL;}
    public MasteryWorldContext touchingSteel(boolean v){contactedMaterial=v?ArmorMaterial.STEEL:ArmorMaterial.CLOTH;return this;}
    public boolean intersticeTarget(){return intersticeTarget;} public MasteryWorldContext intersticeTarget(boolean v){intersticeTarget=v;return this;}
    public boolean spiritInfatigableActive(){return spiritInfatigableActive;} public MasteryWorldContext spiritInfatigableActive(boolean v){spiritInfatigableActive=v;return this;}
    public boolean helicalReleaseActive(){return helicalReleaseActive;} public MasteryWorldContext helicalReleaseActive(boolean v){helicalReleaseActive=v;return this;}
    public boolean helicalOptimizationActive(){return helicalOptimizationActive;} public MasteryWorldContext helicalOptimizationActive(boolean v){helicalOptimizationActive=v;return this;}
    public Optional<FeraeSpecies> encounteredFerae(){return Optional.ofNullable(encounteredFerae);}
    public MasteryWorldContext encounteredFerae(FeraeSpecies s, AnimalEmpathyContext c){encounteredFerae=s;animalEmpathyContext=c;return this;}
    public Optional<AnimalEmpathyContext> animalEmpathyContext(){return Optional.ofNullable(animalEmpathyContext);}
}

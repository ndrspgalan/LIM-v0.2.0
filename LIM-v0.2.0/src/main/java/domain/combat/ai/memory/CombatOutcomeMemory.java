package domain.combat.ai.memory;

import domain.combat.ai.execution.CombatAction;
import domain.combat.ai.observation.AttackSourceType;
import domain.combat.ai.perception.CombatPerceptionSnapshot;
import domain.inventory.item.WeaponCombatAction;
import java.util.*;

/**
 * Memoria episódica . Conserva resultados continuos del encuentro y no
 * deduce etiquetas de vulnerabilidad, protección, confianza o personalidad.
 */
public final class CombatOutcomeMemory {
    private final Map<CombatActionKey, OffensiveStats> offense = new LinkedHashMap<>();
    private final Map<DefenseKey, DefensiveStats> defense = new LinkedHashMap<>();
    private final Set<RevealedCombatResource> revealed = new LinkedHashSet<>();
    private final List<OffensiveOutcome> offensiveEvents = new ArrayList<>();
    private final List<DefensiveOutcome> defensiveEvents = new ArrayList<>();
    private SensoryEvidenceMemory sensory = SensoryEvidenceMemory.empty();
    private double combatTimeSeconds;
    private String targetConfigurationSignature="";

    public void advanceTime(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        combatTimeSeconds += seconds;
    }
    public double combatTimeSeconds() { return combatTimeSeconds; }

    /** Cambiar de configuración visible invalida rendimiento táctico anterior, pero no borra recursos ya vistos ni rastros sensoriales. */
    public void observeTargetConfiguration(String signature){
        signature=signature==null?"":signature;
        if(!targetConfigurationSignature.isEmpty()&&!targetConfigurationSignature.equals(signature)){offense.clear();defense.clear();}
        targetConfigurationSignature=signature;
    }
    public String targetConfigurationSignature(){return targetConfigurationSignature;}

    public void observePerception(CombatPerceptionSnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        sensory = sensory.observe(snapshot.targetEvidence(), combatTimeSeconds);
    }
    public SensoryEvidenceMemory sensoryEvidence() { return sensory; }

    public void record(OffensiveOutcome outcome) {
        Objects.requireNonNull(outcome);
        offense.computeIfAbsent(outcome.key(), k -> new OffensiveStats()).add(outcome);
        offensiveEvents.add(outcome);
    }
    public void record(DefensiveOutcome outcome) {
        Objects.requireNonNull(outcome);
        defense.computeIfAbsent(new DefenseKey(outcome.sourceType(), outcome.incomingAction(), outcome.response()), k -> new DefensiveStats()).add(outcome);
        defensiveEvents.add(outcome);
    }
    public void reveal(String actorId, String resourceId) { revealed.add(new RevealedCombatResource(actorId, resourceId, combatTimeSeconds)); }
    public boolean hasObservedResource(String actorId, String resourceId) {
        return revealed.stream().anyMatch(r -> r.actorId().equals(actorId) && r.resourceId().equals(resourceId));
    }
    public Set<RevealedCombatResource> revealedResources() { return Set.copyOf(revealed); }
    public List<OffensiveOutcome> offensiveOutcomes() { return List.copyOf(offensiveEvents); }
    public List<DefensiveOutcome> defensiveOutcomes() { return List.copyOf(defensiveEvents); }

    /**
     * Ajuste continuo por rendimiento observado. Cero significa ausencia de
     * experiencia. Se limita para que la memoria module la táctica sin sustituir
     * las restricciones materiales actuales.
     */
    public double offensiveAdjustment(CombatActionKey key) {
        OffensiveStats s=offense.get(key); if(s==null||s.attempts==0)return 0;
        double hitRate=s.connected/(double)s.attempts;
        double efficiency=(s.damage + s.staggerSeconds*12.0)/(Math.max(1.0,s.resourceCost)+s.attempts);
        double recent=s.lastConnected ? (s.lastDamage+s.lastStagger*8.0) : -8.0;
        return clamp((hitRate-.5)*20.0 + efficiency*.35 + recent*.15,-35,60);
    }
    public double offensiveAdjustment(CombatAction action,String sourceName){return offensiveAdjustment(new CombatActionKey(action,sourceName));}

    public double defensiveAdjustment(AttackSourceType source, WeaponCombatAction incoming, CombatAction response) {
        DefensiveStats s=defense.get(new DefenseKey(source,incoming,response)); if(s==null||s.attempts==0)return 0;
        double avoid=s.avoided/(double)s.attempts;
        double burden=(s.residualDamage+s.residualStagger*12.0+s.resourceCost*.5)/s.attempts;
        return clamp((avoid-.5)*30.0 - burden*.25,-50,45);
    }

    public int offensiveObservations(CombatActionKey key){var s=offense.get(key);return s==null?0:s.attempts;}
    public void clear(){offense.clear();defense.clear();revealed.clear();offensiveEvents.clear();defensiveEvents.clear();sensory=SensoryEvidenceMemory.empty();combatTimeSeconds=0;targetConfigurationSignature="";}

    private static double clamp(double v,double lo,double hi){return Math.max(lo,Math.min(hi,v));}
    private record DefenseKey(AttackSourceType source,WeaponCombatAction incoming,CombatAction response){}
    private static final class OffensiveStats{
        int attempts,connected;double damage,staggerSeconds,resourceCost,lastDamage,lastStagger;boolean lastConnected;
        void add(OffensiveOutcome o){attempts++;if(o.connected())connected++;damage+=o.observedDamage();staggerSeconds+=o.observedStaggerSeconds();resourceCost+=o.resourceCost();lastDamage=o.observedDamage();lastStagger=o.observedStaggerSeconds();lastConnected=o.connected();}
    }
    private static final class DefensiveStats{
        int attempts,avoided;double residualDamage,residualStagger,resourceCost;
        void add(DefensiveOutcome o){attempts++;if(o.avoided())avoided++;residualDamage+=o.residualDamage();residualStagger+=o.residualStaggerSeconds();resourceCost+=o.resourceCost();}
    }
}

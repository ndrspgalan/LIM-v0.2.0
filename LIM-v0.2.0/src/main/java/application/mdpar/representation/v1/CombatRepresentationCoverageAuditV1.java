package application.mdpar.representation.v1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Matriz ejecutable de completitud : toda superficie capaz de alterar combate debe estar representada. */
public final class CombatRepresentationCoverageAuditV1 {
    private CombatRepresentationCoverageAuditV1(){}
    public static Map<String,String> requiredSurfaces(){
        Map<String,String> m=new LinkedHashMap<>();
        m.put("self.actor","identidad, atributos, antropometría, PA y origen");
        m.put("self.presence","presencia corporal/natural");
        m.put("self.loadout","armas visibles/equipadas");
        m.put("self.remoteArsenal","arsenal remoto y munición");
        m.put("self.meleeState","estado cinético/melee");
        m.put("self.locomotion","movilidad y transitabilidad");
        m.put("self.horizontalJump","salto horizontal");
        m.put("self.inventory","inventario, quick access, logística y armadura estratificada");
        m.put("self.abilities","maestrías y efectos");
        m.put("self.transport","transporte y monturas");
        m.put("world","clima, ciclo, terreno, cobertura, agua, elevación y peligros");
        m.put("knownExternalResources","recursos externos conocidos");
        m.put("perception.primary","objetivo principal observado/recordado");
        m.put("perception.actors","otros actores perceptibles");
        m.put("perception.intents","intenciones observadas");
        m.put("knownRelationships","relaciones conocidas");
        m.put("legal.melee","acciones melee legales");
        m.put("legal.locomotion","acciones locomotoras legales");
        m.put("legal.remote","acciones remotas legales/bloqueadas");
        m.put("legal.inventory","acciones de inventario");
        m.put("legal.abilities","acciones de maestría/capacidad");
        m.put("legal.transport","acciones de transporte");
        m.put("legal.directed","acciones dirigidas target-específicas");
        m.put("effects.active","efectos temporales activos");
        m.put("effects.abilities","efectos activos de maestrías");
        m.put("facts.transport","hechos materiales de transporte");
        m.put("facts.externalResources","fuentes externas conocidas");
        m.put("facts.externalInventoryActions","acciones sobre recursos externos");
        m.put("facts.feraeLoot","loot Ferae conocido");
        m.put("projected.areaConsequences","consecuencias físicas multi-actor");
        return Map.copyOf(m);
    }
    public static List<String> missing(LimCombatRepresentationV1 contract){
        Objects.requireNonNull(contract);
        var paths=contract.exhaustiveSelfAndKnownState().stream().map(KnowledgeFactV1::path).toList();
        return requiredSurfaces().keySet().stream().filter(prefix->paths.stream().noneMatch(p->p.equals(prefix)||p.startsWith(prefix+".")||p.startsWith(prefix+"["))).toList();
    }
}

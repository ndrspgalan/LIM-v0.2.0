package application.mdpar.representation.v1;

import domain.ability.CharacterMasteryCollection;
import domain.ability.MasteryId;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.*;

/**
 * Proyector determinista del estado legítimamente disponible para MDPAR.
 * Sólo se usa sobre superficies ya filtradas por LIM (self + contexto perceptivo conocido),
 * nunca sobre el estado oculto bruto de terceros.
 */
public final class RepresentationFactProjectorV1 {
    private static final int MAX_DEPTH = 14;

    public List<KnowledgeFactV1> exact(String root, Object value, String source) {
        List<KnowledgeFactV1> out = new ArrayList<>();
        project(root, value, EpistemicStateV1.EXACT, source, 1.0, out,
                Collections.newSetFromMap(new IdentityHashMap<>()), 0);
        return List.copyOf(out);
    }

    public List<KnowledgeFactV1> observed(String root, Object value, String source, double confidence) {
        List<KnowledgeFactV1> out = new ArrayList<>();
        project(root, value, EpistemicStateV1.OBSERVED, source, confidence, out,
                Collections.newSetFromMap(new IdentityHashMap<>()), 0);
        return List.copyOf(out);
    }

    private void project(String path, Object value, EpistemicStateV1 epistemic, String source, double confidence,
                         List<KnowledgeFactV1> out, Set<Object> visiting, int depth) {
        if (depth > MAX_DEPTH) { out.add(fact(path + ".truncated", "MAX_DEPTH", epistemic, source, confidence)); return; }
        if (value == null) { out.add(fact(path, "<null>", epistemic, source, confidence)); return; }
        if (terminal(value)) { out.add(fact(path, value, epistemic, source, confidence)); return; }
        if (value instanceof Optional<?> optional) {
            if (optional.isPresent()) project(path, optional.orElseThrow(), epistemic, source, confidence, out, visiting, depth + 1);
            else out.add(fact(path + ".present", false, epistemic, source, confidence));
            return;
        }
        if (value instanceof OptionalDouble optional) {
            if (optional.isPresent()) out.add(fact(path, optional.getAsDouble(), epistemic, source, confidence));
            else out.add(fact(path + ".present", false, epistemic, source, confidence));
            return;
        }
        if (value instanceof OptionalInt optional) {
            if (optional.isPresent()) out.add(fact(path, optional.getAsInt(), epistemic, source, confidence));
            else out.add(fact(path + ".present", false, epistemic, source, confidence));
            return;
        }
        if (value instanceof OptionalLong optional) {
            if (optional.isPresent()) out.add(fact(path, optional.getAsLong(), epistemic, source, confidence));
            else out.add(fact(path + ".present", false, epistemic, source, confidence));
            return;
        }
        if (value instanceof CharacterMasteryCollection masteries) {
            for (MasteryId id : MasteryId.values())
                out.add(fact(path + ".knowledge." + id.name(), masteries.knowledgeState(id).name(), epistemic, source, confidence));
            masteries.ownerGender().ifPresent(g -> out.add(fact(path + ".ownerGender", g, epistemic, source, confidence)));
            return;
        }
        if (value instanceof Map<?,?> map) {
            var entries = new ArrayList<>(map.entrySet());
            entries.sort(Comparator.comparing(e -> String.valueOf(e.getKey())));
            for (var e : entries) project(path + "[" + key(e.getKey()) + "]", e.getValue(), epistemic, source, confidence, out, visiting, depth + 1);
            if (entries.isEmpty()) out.add(fact(path + ".size", 0, epistemic, source, confidence));
            return;
        }
        if (value instanceof Collection<?> collection) {
            List<?> values = value instanceof Set<?> ? collection.stream().sorted(Comparator.comparing(String::valueOf)).toList() : new ArrayList<>(collection);
            out.add(fact(path + ".size", values.size(), epistemic, source, confidence));
            for (int i=0;i<values.size();i++) project(path + "[" + i + "]", values.get(i), epistemic, source, confidence, out, visiting, depth + 1);
            return;
        }
        if (value.getClass().isArray()) {
            int n=java.lang.reflect.Array.getLength(value); out.add(fact(path + ".size", n, epistemic, source, confidence));
            for(int i=0;i<n;i++) project(path+"["+i+"]",java.lang.reflect.Array.get(value,i),epistemic,source,confidence,out,visiting,depth+1);
            return;
        }
        if (!visiting.add(value)) { out.add(fact(path + ".cycle", value.getClass().getName(), epistemic, source, confidence)); return; }
        try {
            Class<?> type=value.getClass();
            if (type.isRecord()) {
                RecordComponent[] components=type.getRecordComponents();
                Arrays.sort(components, Comparator.comparing(RecordComponent::getName));
                for (RecordComponent c : components) {
                    try { project(path + "." + c.getName(), c.getAccessor().invoke(value), epistemic, source, confidence, out, visiting, depth + 1); }
                    catch (ReflectiveOperationException ex) { out.add(fact(path + "." + c.getName() + ".unavailable", ex.getClass().getSimpleName(), epistemic, source, confidence)); }
                }
                return;
            }
            if (type.getPackageName().startsWith("domain.") || type.getPackageName().startsWith("application.")) {
                List<Field> fields=new ArrayList<>();
                for(Class<?> c=type;c!=null&&c!=Object.class;c=c.getSuperclass()) fields.addAll(Arrays.asList(c.getDeclaredFields()));
                fields.removeIf(f -> Modifier.isStatic(f.getModifiers()) || f.isSynthetic());
                fields.sort(Comparator.comparing(Field::getName));
                if (fields.isEmpty()) { out.add(fact(path + ".type", type.getName(), epistemic, source, confidence)); return; }
                for(Field f:fields){
                    try{ if(!f.trySetAccessible()){out.add(fact(path+"."+f.getName()+".unavailable","INACCESSIBLE",epistemic,source,confidence));continue;}
                        project(path+"."+f.getName(),f.get(value),epistemic,source,confidence,out,visiting,depth+1);
                    }catch(IllegalAccessException ex){out.add(fact(path+"."+f.getName()+".unavailable",ex.getClass().getSimpleName(),epistemic,source,confidence));}
                }
                return;
            }
            out.add(fact(path, String.valueOf(value), epistemic, source, confidence));
        } finally { visiting.remove(value); }
    }

    private static boolean terminal(Object v){return v instanceof String||v instanceof Number||v instanceof Boolean||v instanceof Character||v instanceof Enum<?>||v instanceof java.time.temporal.TemporalAmount||v instanceof java.time.temporal.TemporalAccessor;}
    private static String key(Object k){return String.valueOf(k).replace(']','_');}
    private static KnowledgeFactV1 fact(String path,Object value,EpistemicStateV1 e,String source,double confidence){
        return switch(e){case EXACT->KnowledgeFactV1.exact(path,value,source);case OBSERVED->KnowledgeFactV1.observed(path,value,source,confidence);case INFERRED->KnowledgeFactV1.inferred(path,value,source,confidence);case LAST_KNOWN->KnowledgeFactV1.lastKnown(path,value,source,confidence);case UNKNOWN->KnowledgeFactV1.unknown(path,source);};
    }
}

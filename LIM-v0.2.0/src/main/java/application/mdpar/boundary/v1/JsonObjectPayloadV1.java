package application.mdpar.boundary.v1;

import java.util.*;

/**
 * Árbol JSON inmutable y agnóstico de dominio usado como payload en la frontera LIM -> MDPAR.
 * No contiene coordenadas SCC ni selecciona plugins/inteligencias locales de MDPAR.
 */
public final class JsonObjectPayloadV1 {
    private final Map<String,Object> fields;

    public JsonObjectPayloadV1(Map<String,Object> fields) {
        this.fields = freezeObject(Objects.requireNonNull(fields, "fields"));
    }

    public Map<String,Object> fields() { return fields; }

    @Override public boolean equals(Object o) { return o instanceof JsonObjectPayloadV1 other && fields.equals(other.fields); }
    @Override public int hashCode() { return fields.hashCode(); }
    @Override public String toString() { return fields.toString(); }

    private static Map<String,Object> freezeObject(Map<String,?> source) {
        LinkedHashMap<String,Object> out = new LinkedHashMap<>();
        for (var e : source.entrySet()) {
            String key = Objects.requireNonNull(e.getKey(), "JSON object key");
            if (key.isBlank()) throw new IllegalArgumentException("Las claves JSON no pueden estar vacías.");
            out.put(key, freeze(e.getValue()));
        }
        return Collections.unmodifiableMap(out);
    }

    private static Object freeze(Object value) {
        if (value == null || value instanceof String || value instanceof Boolean) return value;
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
            return Long.valueOf(((Number) value).longValue());
        if (value instanceof Float f) {
            if (!Float.isFinite(f)) throw new IllegalArgumentException("Payload JSON no admite NaN/Infinity.");
            return Double.valueOf(f.doubleValue());
        }
        if (value instanceof Double d) {
            if (!Double.isFinite(d)) throw new IllegalArgumentException("Payload JSON no admite NaN/Infinity.");
            return d;
        }
        if (value instanceof Number n) {
            double d = n.doubleValue();
            if (!Double.isFinite(d)) throw new IllegalArgumentException("Payload JSON no admite NaN/Infinity.");
            return d;
        }
        if (value instanceof Map<?,?> m) {
            LinkedHashMap<String,Object> normalized = new LinkedHashMap<>();
            for (var e : m.entrySet()) {
                if (!(e.getKey() instanceof String key)) throw new IllegalArgumentException("Payload JSON exige claves String.");
                normalized.put(key, freeze(e.getValue()));
            }
            return freezeObject(normalized);
        }
        if (value instanceof Collection<?> c) {
            ArrayList<Object> out = new ArrayList<>(c.size());
            for (Object x : c) out.add(freeze(x));
            return List.copyOf(out);
        }
        throw new IllegalArgumentException("Tipo no JSON en payload: " + value.getClass().getName());
    }
}

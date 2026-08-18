package application.mdpar.representation.v1;

import java.util.Objects;
import java.util.Optional;

/** Variable externa estable: path + valor tipado + procedencia epistemológica. */
public record KnowledgeFactV1(
        String path,
        FactValueTypeV1 valueType,
        Optional<String> value,
        EpistemicStateV1 epistemicState,
        String source,
        double confidence
) {
    public KnowledgeFactV1 {
        path = required(path); Objects.requireNonNull(valueType); value = Objects.requireNonNull(value);
        Objects.requireNonNull(epistemicState); source = required(source);
        if (!Double.isFinite(confidence) || confidence < 0 || confidence > 1) throw new IllegalArgumentException("Confianza inválida.");
        if (epistemicState == EpistemicStateV1.UNKNOWN && value.isPresent())
            throw new IllegalArgumentException("Un hecho UNKNOWN no puede filtrar el valor real.");
    }
    public static KnowledgeFactV1 exact(String path, Object value, String source) { return of(path,value,EpistemicStateV1.EXACT,source,1); }
    public static KnowledgeFactV1 observed(String path,Object value,String source,double confidence){return of(path,value,EpistemicStateV1.OBSERVED,source,confidence);}
    public static KnowledgeFactV1 inferred(String path,Object value,String source,double confidence){return of(path,value,EpistemicStateV1.INFERRED,source,confidence);}
    public static KnowledgeFactV1 lastKnown(String path,Object value,String source,double confidence){return of(path,value,EpistemicStateV1.LAST_KNOWN,source,confidence);}
    public static KnowledgeFactV1 unknown(String path,String source){return new KnowledgeFactV1(path,FactValueTypeV1.EMPTY,Optional.empty(),EpistemicStateV1.UNKNOWN,source,0);}
    private static KnowledgeFactV1 of(String path,Object value,EpistemicStateV1 e,String source,double confidence){
        Objects.requireNonNull(value); return new KnowledgeFactV1(path,type(value),Optional.of(String.valueOf(value)),e,source,confidence);
    }
    private static FactValueTypeV1 type(Object v){
        if(v instanceof Boolean)return FactValueTypeV1.BOOLEAN; if(v instanceof Byte||v instanceof Short||v instanceof Integer)return FactValueTypeV1.INTEGER;
        if(v instanceof Long)return FactValueTypeV1.LONG; if(v instanceof Float||v instanceof Double)return FactValueTypeV1.DOUBLE;
        if(v instanceof Enum<?>)return FactValueTypeV1.ENUM; return FactValueTypeV1.TEXT;
    }
    private static String required(String s){Objects.requireNonNull(s);s=s.trim();if(s.isEmpty())throw new IllegalArgumentException("Texto obligatorio.");return s;}
}

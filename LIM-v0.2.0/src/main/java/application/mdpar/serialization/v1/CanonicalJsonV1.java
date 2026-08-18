package application.mdpar.serialization.v1;

import application.mdpar.representation.v1.FactValueTypeV1;
import application.mdpar.representation.v1.KnowledgeFactV1;
import application.mdpar.boundary.v1.JsonObjectPayloadV1;
import java.lang.reflect.RecordComponent;
import java.util.*;

/** JSON mínimo, determinista y sin dependencias para el wire contract . */
final class CanonicalJsonV1 {
    private CanonicalJsonV1() {}

    static String write(Object value) {
        StringBuilder out = new StringBuilder(4096);
        append(out, value);
        return out.toString();
    }

    private static void append(StringBuilder out, Object value) {
        if (value == null) { out.append("null"); return; }
        if (value instanceof KnowledgeFactV1 f) { appendFact(out, f); return; }
        if (value instanceof JsonObjectPayloadV1 p) { append(out, p.fields()); return; }
        if (value instanceof Optional<?> o) { append(out, o.orElse(null)); return; }
        if (value instanceof String s) { quote(out, s); return; }
        if (value instanceof Character c) { quote(out, String.valueOf(c)); return; }
        if (value instanceof Boolean || value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) { out.append(value); return; }
        if (value instanceof Float f) { finite(f.doubleValue()); out.append(normalize(f.doubleValue())); return; }
        if (value instanceof Double d) { finite(d); out.append(normalize(d)); return; }
        if (value instanceof Enum<?> e) { quote(out, e.name()); return; }
        if (value instanceof Map<?,?> map) {
            out.append('{');
            var entries = new ArrayList<>(map.entrySet());
            entries.sort(Comparator.comparing(e -> String.valueOf(e.getKey())));
            boolean first = true;
            for (var e : entries) {
                if (!first) out.append(','); first = false;
                quote(out, String.valueOf(e.getKey())); out.append(':'); append(out, e.getValue());
            }
            out.append('}'); return;
        }
        if (value instanceof Collection<?> c) {
            out.append('['); boolean first = true;
            for (Object x : c) { if (!first) out.append(','); first = false; append(out, x); }
            out.append(']'); return;
        }
        if (value.getClass().isArray()) {
            out.append('['); int n = java.lang.reflect.Array.getLength(value);
            for (int i=0;i<n;i++){ if(i>0)out.append(','); append(out, java.lang.reflect.Array.get(value,i)); }
            out.append(']'); return;
        }
        if (value.getClass().isRecord()) {
            out.append('{');
            RecordComponent[] cs = value.getClass().getRecordComponents();
            boolean first = true;
            for (RecordComponent c : cs) {
                if (!first) out.append(','); first = false;
                quote(out, c.getName()); out.append(':');
                try { append(out, c.getAccessor().invoke(value)); }
                catch (ReflectiveOperationException ex) { throw new WireValidationExceptionV1("No se puede serializar " + c.getName(), ex); }
            }
            out.append('}'); return;
        }
        throw new WireValidationExceptionV1("Tipo JSON V1 no soportado: " + value.getClass().getName());
    }


    private static void appendFact(StringBuilder out, KnowledgeFactV1 f) {
        out.append('{');
        quote(out,"path"); out.append(':'); quote(out,f.path()); out.append(',');
        quote(out,"valueType"); out.append(':'); quote(out,f.valueType().name()); out.append(',');
        quote(out,"value"); out.append(':'); appendTypedFactValue(out,f); out.append(',');
        quote(out,"epistemicState"); out.append(':'); quote(out,f.epistemicState().name()); out.append(',');
        quote(out,"source"); out.append(':'); quote(out,f.source()); out.append(',');
        quote(out,"confidence"); out.append(':'); out.append(normalize(f.confidence()));
        out.append('}');
    }

    private static void appendTypedFactValue(StringBuilder out, KnowledgeFactV1 f) {
        if (f.value().isEmpty() || f.valueType() == FactValueTypeV1.EMPTY) { out.append("null"); return; }
        String v=f.value().orElseThrow();
        try {
            switch (f.valueType()) {
                case BOOLEAN -> out.append(Boolean.parseBoolean(v));
                case INTEGER -> out.append(Integer.parseInt(v));
                case LONG -> out.append(Long.parseLong(v));
                case DOUBLE -> { double d=Double.parseDouble(v); finite(d); out.append(normalize(d)); }
                case ENUM, TEXT -> quote(out,v);
                case EMPTY -> out.append("null");
            }
        } catch (NumberFormatException ex) { throw new WireValidationExceptionV1("Valor incompatible con "+f.valueType()+" en "+f.path(),ex); }
    }

    private static void finite(double v){ if(!Double.isFinite(v)) throw new WireValidationExceptionV1("JSON V1 no admite NaN/Infinity."); }
    private static String normalize(double v){
        if (v == 0d) return "0";
        String s = Double.toString(v);
        return s.endsWith(".0") ? s.substring(0, s.length()-2) : s;
    }
    private static void quote(StringBuilder out, String s){
        out.append('"');
        for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            switch(c){
                case '"' -> out.append("\\\""); case '\\' -> out.append("\\\\"); case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f"); case '\n' -> out.append("\\n"); case '\r' -> out.append("\\r"); case '\t' -> out.append("\\t");
                default -> { if(c<0x20) out.append(String.format("\\u%04x",(int)c)); else out.append(c); }
            }
        }
        out.append('"');
    }

    static Object parse(String json){ return new Parser(json).parse(); }

    private static final class Parser {
        private final String s; private int i;
        Parser(String s){ this.s=Objects.requireNonNull(s); }
        Object parse(){ skip(); Object v=value(); skip(); if(i!=s.length())fail("Contenido tras JSON"); return v; }
        Object value(){ skip(); if(i>=s.length())fail("Fin inesperado"); return switch(s.charAt(i)){
            case '{' -> object(); case '[' -> array(); case '"' -> string(); case 't' -> literal("true",Boolean.TRUE);
            case 'f' -> literal("false",Boolean.FALSE); case 'n' -> literal("null",null); default -> number(); }; }
        Map<String,Object> object(){ expect('{'); LinkedHashMap<String,Object> m=new LinkedHashMap<>(); skip(); if(take('}'))return m;
            while(true){ skip(); if(i>=s.length()||s.charAt(i)!='"')fail("Clave JSON esperada"); String k=string(); if(m.containsKey(k))fail("Clave duplicada: "+k); skip(); expect(':'); m.put(k,value()); skip(); if(take('}'))return m; expect(','); } }
        List<Object> array(){ expect('['); ArrayList<Object> a=new ArrayList<>(); skip(); if(take(']'))return a; while(true){ a.add(value()); skip(); if(take(']'))return a; expect(','); } }
        String string(){ expect('"'); StringBuilder b=new StringBuilder(); while(i<s.length()){ char c=s.charAt(i++); if(c=='"')return b.toString(); if(c=='\\'){ if(i>=s.length())fail("Escape incompleto"); char e=s.charAt(i++); switch(e){ case '"','\\','/'->b.append(e); case 'b'->b.append('\b'); case 'f'->b.append('\f'); case 'n'->b.append('\n'); case 'r'->b.append('\r'); case 't'->b.append('\t'); case 'u'->{ if(i+4>s.length())fail("Unicode incompleto"); try{b.append((char)Integer.parseInt(s.substring(i,i+4),16));}catch(NumberFormatException ex){fail("Unicode inválido");} i+=4;} default->fail("Escape inválido"); } } else { if(c<0x20)fail("Control sin escapar"); b.append(c);} } fail("String sin cerrar"); return null; }
        Object number(){ int st=i; if(take('-')){} if(i>=s.length())fail("Número inválido"); if(take('0')){} else { if(!digit())fail("Número inválido"); while(digit())i++; } boolean decimal=false; if(take('.')){decimal=true;if(!digit())fail("Decimal inválido");while(digit())i++;} if(i<s.length()&&(s.charAt(i)=='e'||s.charAt(i)=='E')){decimal=true;i++;if(i<s.length()&&(s.charAt(i)=='+'||s.charAt(i)=='-'))i++;if(!digit())fail("Exponente inválido");while(digit())i++;} String n=s.substring(st,i); try{if(decimal)return Double.valueOf(Double.parseDouble(n));return Long.valueOf(Long.parseLong(n));}catch(NumberFormatException ex){fail("Número fuera de rango");return null;} }
        Object literal(String token,Object value){ if(!s.startsWith(token,i))fail("Literal inválido");i+=token.length();return value; }
        boolean digit(){return i<s.length()&&s.charAt(i)>='0'&&s.charAt(i)<='9';}
        void skip(){while(i<s.length()&&Character.isWhitespace(s.charAt(i)))i++;}
        boolean take(char c){if(i<s.length()&&s.charAt(i)==c){i++;return true;}return false;}
        void expect(char c){if(!take(c))fail("Se esperaba '"+c+"'");}
        void fail(String m){throw new WireValidationExceptionV1(m+" en offset "+i);}
    }
}

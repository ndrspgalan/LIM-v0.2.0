package application.mdpar.serialization.v1;

import application.mdpar.boundary.v1.JsonObjectPayloadV1;
import application.mdpar.representation.v1.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** Codec de la representación LIM combat. No conoce SCC, routing ni transporte. */
public final class LimCombatRepresentationJsonV1 {
    public static final String REPRESENTATION_VERSION = LimCombatRepresentationV1.VERSION;

    public String write(LimCombatRepresentationV1 representation){ Objects.requireNonNull(representation); return CanonicalJsonV1.write(representation); }
    public LimCombatRepresentationV1 read(String json){ return decode(obj(CanonicalJsonV1.parse(json),"root")); }
    public JsonObjectPayloadV1 toPayload(LimCombatRepresentationV1 representation){ return new JsonObjectPayloadV1(obj(CanonicalJsonV1.parse(write(representation)),"payload")); }
    public LimCombatRepresentationV1 fromPayload(JsonObjectPayloadV1 payload){ Objects.requireNonNull(payload); return decode(new LinkedHashMap<>(payload.fields())); }
    public String sha256(LimCombatRepresentationV1 representation){ return sha256Text(write(representation)); }

    private static LimCombatRepresentationV1 decode(Map<String,Object> m){
        exactKeys(m, Set.of("schemaVersion","self","battlespace","knownActors","actions","projectedConsequences","exhaustiveSelfAndKnownState","targetingDoctrine"),"representation");
        String version=str(m,"schemaVersion"); if(!REPRESENTATION_VERSION.equals(version))throw new WireValidationExceptionV1("Versión de representación no soportada: "+version);
        return new LimCombatRepresentationV1(version, actor(obj(req(m,"self"),"self")), battlespace(obj(req(m,"battlespace"),"battlespace")),
                list(m,"knownActors",LimCombatRepresentationJsonV1::actor), list(m,"actions",LimCombatRepresentationJsonV1::action),
                list(m,"projectedConsequences",LimCombatRepresentationJsonV1::fact), list(m,"exhaustiveSelfAndKnownState",LimCombatRepresentationJsonV1::fact), doctrine(obj(req(m,"targetingDoctrine"),"targetingDoctrine")));
    }
    private static ActorKnowledgeV1 actor(Map<String,Object> m){ exactKeys(m,Set.of("actorId","origin","originKey","presenceState","facts"),"actor"); return new ActorKnowledgeV1(str(m,"actorId"),enm(m,"origin",ActorOriginV1.class),str(m,"originKey"),enm(m,"presenceState",EpistemicStateV1.class),list(m,"facts",LimCombatRepresentationJsonV1::fact)); }
    private static BattlespaceRepresentationV1 battlespace(Map<String,Object> m){ exactKeys(m,Set.of("scenarioId","seed","scenarioIndex","tick","encounterKind","encounterEnvironment","forces","squads","environmentFacts"),"battlespace"); return new BattlespaceRepresentationV1(str(m,"scenarioId"),lng(m,"seed"),lng(m,"scenarioIndex"),lng(m,"tick"),str(m,"encounterKind"),str(m,"encounterEnvironment"),list(m,"forces",LimCombatRepresentationJsonV1::force),list(m,"squads",LimCombatRepresentationJsonV1::squad),list(m,"environmentFacts",LimCombatRepresentationJsonV1::fact)); }
    private static ForceRepresentationV1 force(Map<String,Object> m){ exactKeys(m,Set.of("forceId","squadIds","ownForce","knowledgeState"),"force"); return new ForceRepresentationV1(str(m,"forceId"),strings(m,"squadIds"),bool(m,"ownForce"),enm(m,"knowledgeState",EpistemicStateV1.class)); }
    private static TacticalSquadRepresentationV1 squad(Map<String,Object> m){ exactKeys(m,Set.of("squadId","forceId","mission","compositionKind","memberActorIds","ownSquad","knowledgeState"),"squad"); return new TacticalSquadRepresentationV1(str(m,"squadId"),str(m,"forceId"),str(m,"mission"),str(m,"compositionKind"),strings(m,"memberActorIds"),bool(m,"ownSquad"),enm(m,"knowledgeState",EpistemicStateV1.class)); }
    private static ActionRepresentationV1 action(Map<String,Object> m){ exactKeys(m,Set.of("actionId","family","availability","targetKind","blockingReasons","facts"),"action"); return new ActionRepresentationV1(str(m,"actionId"),str(m,"family"),enm(m,"availability",ActionAvailabilityV1.class),enm(m,"targetKind",ActionTargetKindV1.class),strings(m,"blockingReasons"),list(m,"facts",LimCombatRepresentationJsonV1::fact)); }
    private static KnowledgeFactV1 fact(Map<String,Object> m){
        exactKeys(m,Set.of("path","valueType","value","epistemicState","source","confidence"),"fact");
        FactValueTypeV1 type=enm(m,"valueType",FactValueTypeV1.class); Object raw=reqAllowNull(m,"value");
        Optional<String> value=decodeFactValue(type,raw);
        return new KnowledgeFactV1(str(m,"path"),type,value,enm(m,"epistemicState",EpistemicStateV1.class),str(m,"source"),dbl(m,"confidence"));
    }
    private static Optional<String> decodeFactValue(FactValueTypeV1 type,Object raw){
        if(type==FactValueTypeV1.EMPTY){if(raw!=null)throw new WireValidationExceptionV1("EMPTY debe usar value=null");return Optional.empty();}
        if(raw==null)throw new WireValidationExceptionV1(type+" exige value no nulo");
        return Optional.of(switch(type){
            case BOOLEAN -> {if(!(raw instanceof Boolean b))throw type("value","boolean");yield Boolean.toString(b);}
            case INTEGER -> Integer.toString(Math.toIntExact(integral(raw,"value")));
            case LONG -> Long.toString(integral(raw,"value"));
            case DOUBLE -> {if(!(raw instanceof Number n)||!Double.isFinite(n.doubleValue()))throw type("value","finite number");yield Double.toString(n.doubleValue());}
            case ENUM,TEXT -> asString(raw,"value");
            case EMPTY -> throw new IllegalStateException();
        });
    }
    private static long integral(Object raw,String k){if(raw instanceof Long l)return l;if(raw instanceof Double d&&d==Math.rint(d)&&d>=Long.MIN_VALUE&&d<=Long.MAX_VALUE)return d.longValue();throw type(k,"integer");}
    private static TargetingDoctrineV1 doctrine(Map<String,Object> m){ exactKeys(m,Set.of("actorTargetingSupported","positionTargetingSupported","areaTargetingSupported","blindFireSchemaSupported","blindThrowSchemaSupported","predictiveInterceptAimingAllowed","postLaunchTrajectoryCorrectionAllowed","aimingBasis"),"targetingDoctrine"); return new TargetingDoctrineV1(bool(m,"actorTargetingSupported"),bool(m,"positionTargetingSupported"),bool(m,"areaTargetingSupported"),bool(m,"blindFireSchemaSupported"),bool(m,"blindThrowSchemaSupported"),bool(m,"predictiveInterceptAimingAllowed"),bool(m,"postLaunchTrajectoryCorrectionAllowed"),str(m,"aimingBasis")); }

    private interface Decoder<T>{T decode(Map<String,Object> m);}
    private static <T> List<T> list(Map<String,Object> m,String k,Decoder<T>d){Object v=req(m,k);if(!(v instanceof List<?> a))throw type(k,"array");List<T>o=new ArrayList<>();for(int i=0;i<a.size();i++)o.add(d.decode(obj(a.get(i),k+"["+i+"]")));return List.copyOf(o);}
    private static List<String> strings(Map<String,Object>m,String k){Object v=req(m,k);if(!(v instanceof List<?>a))throw type(k,"array");List<String>o=new ArrayList<>();for(Object x:a)o.add(asString(x,k));return List.copyOf(o);}
    private static String str(Map<String,Object>m,String k){return asString(req(m,k),k);} private static String asString(Object v,String k){if(!(v instanceof String s))throw type(k,"string");return s;}
    private static long lng(Map<String,Object>m,String k){Object v=req(m,k);if(v instanceof Long l)return l;if(v instanceof Double d&&d==Math.rint(d)&&d>=Long.MIN_VALUE&&d<=Long.MAX_VALUE)return d.longValue();throw type(k,"integer");}
    private static double dbl(Map<String,Object>m,String k){Object v=req(m,k);if(v instanceof Number n){double d=n.doubleValue();if(Double.isFinite(d))return d;}throw type(k,"finite number");}
    private static boolean bool(Map<String,Object>m,String k){Object v=req(m,k);if(v instanceof Boolean b)return b;throw type(k,"boolean");}
    private static <E extends Enum<E>>E enm(Map<String,Object>m,String k,Class<E>t){String s=str(m,k);try{return Enum.valueOf(t,s);}catch(IllegalArgumentException ex){throw new WireValidationExceptionV1("Enum inválido "+k+"="+s);}}
    @SuppressWarnings("unchecked") private static Map<String,Object> obj(Object v,String k){if(!(v instanceof Map<?,?>m))throw type(k,"object");for(Object x:m.keySet())if(!(x instanceof String))throw type(k,"object with string keys");return (Map<String,Object>)m;}
    private static Object req(Map<String,Object>m,String k){if(!m.containsKey(k)||m.get(k)==null)throw new WireValidationExceptionV1("Campo obligatorio ausente/null: "+k);return m.get(k);} private static Object reqAllowNull(Map<String,Object>m,String k){if(!m.containsKey(k))throw new WireValidationExceptionV1("Campo obligatorio ausente: "+k);return m.get(k);}
    private static void exactKeys(Map<String,Object>m,Set<String>expected,String where){Set<String>extra=new TreeSet<>(m.keySet());extra.removeAll(expected);if(!extra.isEmpty())throw new WireValidationExceptionV1("Campos desconocidos en "+where+": "+extra);Set<String>missing=new TreeSet<>(expected);missing.removeAll(m.keySet());if(!missing.isEmpty())throw new WireValidationExceptionV1("Campos ausentes en "+where+": "+missing);}
    private static WireValidationExceptionV1 type(String k,String t){return new WireValidationExceptionV1("Campo "+k+" debe ser "+t);}
    private static String sha256Text(String s){try{byte[]h=MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));return HexFormat.of().formatHex(h);}catch(NoSuchAlgorithmException ex){throw new IllegalStateException(ex);}}
}

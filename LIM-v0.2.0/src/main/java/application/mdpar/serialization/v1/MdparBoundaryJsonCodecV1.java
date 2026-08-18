package application.mdpar.serialization.v1;

import application.mdpar.boundary.v1.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** Codec canónico de la frontera global LIM <-> MDPAR. No conoce combat, SCC ni HTTP. */
public final class MdparBoundaryJsonCodecV1 {
    public static final String CONTENT_TYPE = "application/json";
    public static final String BOUNDARY_VERSION = MdparRequestEnvelopeV1.VERSION;

    public String writeRequest(MdparRequestEnvelopeV1 request){ Objects.requireNonNull(request); return CanonicalJsonV1.write(request); }
    public String writeResponse(MdparResponseEnvelopeV1 response){ Objects.requireNonNull(response); return CanonicalJsonV1.write(response); }
    public MdparRequestEnvelopeV1 readRequest(String json){ return decodeRequest(obj(CanonicalJsonV1.parse(json),"root")); }
    public MdparResponseEnvelopeV1 readResponse(String json){ return decodeResponse(obj(CanonicalJsonV1.parse(json),"root")); }
    public String requestSha256(MdparRequestEnvelopeV1 request){ return sha256(writeRequest(request)); }
    public String responseSha256(MdparResponseEnvelopeV1 response){ return sha256(writeResponse(response)); }

    private static MdparRequestEnvelopeV1 decodeRequest(Map<String,Object> m){
        exactKeys(m,Set.of("boundaryVersion","requestId","producer","representationVersion","stateRevision","payload"),"requestEnvelope");
        String version=str(m,"boundaryVersion"); requireVersion(version);
        return new MdparRequestEnvelopeV1(version,str(m,"requestId"),str(m,"producer"),str(m,"representationVersion"),lng(m,"stateRevision"),new JsonObjectPayloadV1(obj(req(m,"payload"),"payload")));
    }

    private static MdparResponseEnvelopeV1 decodeResponse(Map<String,Object> m){
        exactKeys(m,Set.of("boundaryVersion","requestId","routing","operational"),"responseEnvelope");
        String version=str(m,"boundaryVersion"); requireVersion(version);
        return new MdparResponseEnvelopeV1(version,str(m,"requestId"),routing(obj(req(m,"routing"),"routing")),operational(obj(req(m,"operational"),"operational")));
    }

    private static MdparRoutingMetadataV1 routing(Map<String,Object> m){
        exactKeys(m,Set.of("outputMode","routingStatus","closure","runCompletedMs","details"),"routing");
        return new MdparRoutingMetadataV1(str(m,"outputMode"),str(m,"routingStatus"),str(m,"closure"),lng(m,"runCompletedMs"),new JsonObjectPayloadV1(obj(req(m,"details"),"details")));
    }

    private static MdparOperationalPublicationV1 operational(Map<String,Object> m){
        exactKeys(m,Set.of("ACTION","WHY","HOW","CONCLUSION","SUPPORT"),"operational");
        return new MdparOperationalPublicationV1(str(m,"ACTION"),str(m,"WHY"),str(m,"HOW"),str(m,"CONCLUSION"),new JsonObjectPayloadV1(obj(req(m,"SUPPORT"),"SUPPORT")));
    }

    private static void requireVersion(String version){if(!BOUNDARY_VERSION.equals(version))throw new WireValidationExceptionV1("Versión boundary no soportada: "+version);}
    private static List<String> strings(Map<String,Object>m,String k){Object v=req(m,k);if(!(v instanceof List<?>a))throw type(k,"array");List<String>o=new ArrayList<>();for(Object x:a)o.add(asString(x,k));return List.copyOf(o);}
    private static String str(Map<String,Object>m,String k){return asString(req(m,k),k);} private static String asString(Object v,String k){if(!(v instanceof String s))throw type(k,"string");return s;}
    private static long lng(Map<String,Object>m,String k){Object v=req(m,k);if(v instanceof Long l)return l;if(v instanceof Double d&&d==Math.rint(d)&&d>=Long.MIN_VALUE&&d<=Long.MAX_VALUE)return d.longValue();throw type(k,"integer");}
    @SuppressWarnings("unchecked") private static Map<String,Object> obj(Object v,String k){if(!(v instanceof Map<?,?>m))throw type(k,"object");for(Object x:m.keySet())if(!(x instanceof String))throw type(k,"object with string keys");return (Map<String,Object>)m;}
    private static Object req(Map<String,Object>m,String k){if(!m.containsKey(k)||m.get(k)==null)throw new WireValidationExceptionV1("Campo obligatorio ausente/null: "+k);return m.get(k);}
    private static void exactKeys(Map<String,Object>m,Set<String>expected,String where){Set<String>extra=new TreeSet<>(m.keySet());extra.removeAll(expected);if(!extra.isEmpty())throw new WireValidationExceptionV1("Campos desconocidos en "+where+": "+extra);Set<String>missing=new TreeSet<>(expected);missing.removeAll(m.keySet());if(!missing.isEmpty())throw new WireValidationExceptionV1("Campos ausentes en "+where+": "+missing);}
    private static WireValidationExceptionV1 type(String k,String t){return new WireValidationExceptionV1("Campo "+k+" debe ser "+t);}
    private static String sha256(String s){try{byte[]h=MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));return HexFormat.of().formatHex(h);}catch(NoSuchAlgorithmException ex){throw new IllegalStateException(ex);}}
}

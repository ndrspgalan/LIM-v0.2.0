package domain.knowledge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  — authoritative internal truth of Valerian's contemporary order.
 * This class is intentionally not a player-facing encyclopedia.
 */
public final class WorldOrderCanon {
    private WorldOrderCanon(){}

    public static CanonVisibility visibility(){ return CanonVisibility.INTERNAL_CANON; }

    public static Map<String,String> all(){
        LinkedHashMap<String,String> m=new LinkedHashMap<>();
        m.put("intersticio",IntersticeTopologyDoctrine.interstice());
        m.put("velo",IntersticeTopologyDoctrine.veil());
        m.put("lineas-teluricas",IntersticeTopologyDoctrine.telluricLines());
        m.put("esfera-del-progreso",SphereOfProgressDoctrine.truth());
        m.put("caos-social-controlado",ControlledSocialChaosDoctrine.truth());
        m.put("longevidad-noble",NobleLongevityDoctrine.truth());
        m.put("ciencia-de-continuidad",ContinuityScienceDoctrine.truth());
        m.put("economia-politica-guerra-v881",V881WarPoliticalEconomyDoctrine.truth());
        m.put("kenan-kiara-clases-iniciales",ProtagonistSpiritDoctrine.truth());
        return Map.copyOf(m);
    }
}

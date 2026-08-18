package domain.bestiarium.physical_plane.ancient;

import java.util.*;

public final class AncientCatalog {
    private static final Map<AncientArchetype,AncientProfile> ALL=build();
    private AncientCatalog(){}
    public static AncientProfile require(AncientArchetype a){return ALL.get(Objects.requireNonNull(a));}
    public static List<AncientProfile> all(){return List.copyOf(ALL.values());}
    private static Map<AncientArchetype,AncientProfile> build(){
        EnumMap<AncientArchetype,AncientProfile> m=new EnumMap<>(AncientArchetype.class);
        for(var a:AncientArchetype.values())m.put(a,new AncientProfile(a));
        return Map.copyOf(m);
    }
}

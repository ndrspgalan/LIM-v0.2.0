package domain.economy;

import java.util.*;

/** Estado OGC común de las piezas singulares del Guerrero de Ébano, fijado. */
public final class EbonyWarriorArmorEconomicPolicy {
    private static final Map<String,EconomicValuation> DATA=build();
    private EbonyWarriorArmorEconomicPolicy(){}
    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Pieza ajena al conjunto económico del Guerrero de Ébano: "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}
    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        pending(m,"Coraza del Guerrero de Ébano","Pieza histórica singular: la OGC mantiene pendiente la tasación de la coraza, brazales y polainas como conjunto histórico; no existe precio minorista canónico.");
        pending(m,"Brazales del Guerrero de Ébano","Brazales históricos singulares: su tasación permanece pendiente por la OGC junto con el resto del conjunto histórico y no se finge un precio de mercado.");
        pending(m,"Polainas del Guerrero de Ébano","Polainas históricas singulares de quince estratos de ébano sobre soporte textil: su tasación permanece pendiente por la OGC junto con el resto del conjunto histórico y no se finge un precio minorista ordinario.");
        pending(m,"Coraza del Guerrero de Ébano V881","Pieza singular V881 de ébano mineralizado, wolframio y textil: la OGC mantiene pendiente su tasación junto con el brazal izquierdo V881.");
        pending(m,"Brazal izquierdo del Guerrero de Ébano V881","Brazal singular V881 del Guerrero de Ébano: su tasación permanece pendiente por la OGC junto con la coraza V881 y no admite precio ordinario.");
        return Map.copyOf(m);
    }
    private static void pending(Map<String,EconomicValuation> m,String n,String r){
        m.put(n,EconomicValuation.ogcPending(n,EconomicGoodType.PRIVATE_USE,r));
    }
}

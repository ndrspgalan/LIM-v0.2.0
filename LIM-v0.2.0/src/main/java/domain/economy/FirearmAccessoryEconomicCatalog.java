package domain.economy;

import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;
import java.util.*;

/**  — tasación de los cinco accesorios desmontables de arma de fuego. */
public final class FirearmAccessoryEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private FirearmAccessoryEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Accesorio de firearm sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Correa de Arma V881",120,
                "Correa de servicio de tejido/cuero resistente, hebillas, anillas y herrajes normalizados capaces de soportar repetidamente el peso de un arma larga sin soltarse durante marcha o combate. La materia es ordinaria; costuras de carga, regulación y anclajes fiables explican la prima sobre una correa civil.");
        p(m,"Bípode de Arma V881",520,
                "Dos patas plegables, articulación central, bloqueo de despliegue y montaje inferior deben soportar masa de armas pesadas y transferir reacción al terreno sin holguras. Acero mecanizado, resortes, juntas y tolerancias de alineación convierten el bípode en un mecanismo reutilizable de precisión, no en dos simples varillas.");
        p(m,"Mirilla Fiedler V881",850,
                "Óptica desmontable ×3 con tubo mecanizado, lentes seleccionadas, retícula, regulación y montura V881. Pulido, centrado y alineación óptica dominan el coste; su precio no surge de un multiplicador de alcance, que mecánicamente no existe.");
        p(m,"Mirilla Zeiss V881",1250,
                "Óptica ×4 que exige mayor calidad de vidrio, tratamiento superficial, control de aberraciones y ajuste mecánico que la Fiedler. La prima responde al rendimiento óptico reproducible y a la precisión de montaje, no a una mejora abstracta de daño o alcance.");
        p(m,"Mirilla Winchester A5 V881",1750,
                "Óptica ×5 de cuerpo más largo, mayor masa de vidrio y recorrido de ajuste fino, adaptada al estándar V881. Fabricación de lentes, alineación, retícula y montaje de precisión elevan el coste al extremo superior de los accesorios desmontables, sin alterar la letalidad intrínseca del arma.");
        if(m.size()!=FirearmAccessoryCatalog.all().size()) throw new IllegalStateException(" debe tasar cinco firearm accessories.");
        return Map.copyOf(m);
    }
    private static void p(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}

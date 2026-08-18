package domain.maintenance;

import domain.inventory.item.armor.ArmorMaterial;
import java.util.*;

public final class ElectromechanicalCompositeRecipe {
    private static final LinkedHashMap<ArmorMaterial,Integer> INPUTS=build();
    private ElectromechanicalCompositeRecipe(){}

    public static Map<ArmorMaterial,Integer> inputs(){ return Collections.unmodifiableMap(INPUTS); }

    public static String technicalSummary(){
        return "1 Acero de placas + 1 Bronce + 1 Cuero endurecido + 1 Tela + 1 Caucho vulcanizado + 1 Tela dieléctrica "
                + "+ Maletín profesional de Alicia e Iván -> 1 módulo de Compuesto Electromecánico.";
    }

    private static LinkedHashMap<ArmorMaterial,Integer> build(){
        LinkedHashMap<ArmorMaterial,Integer> m=new LinkedHashMap<>();
        m.put(ArmorMaterial.STEEL,1);
        m.put(ArmorMaterial.BRONZE,1);
        m.put(ArmorMaterial.HARDENED_LEATHER,1);
        m.put(ArmorMaterial.CLOTH,1);
        m.put(ArmorMaterial.VULCANIZED_RUBBER,1);
        m.put(ArmorMaterial.DIELECTRIC_CLOTH,1);
        return m;
    }
}

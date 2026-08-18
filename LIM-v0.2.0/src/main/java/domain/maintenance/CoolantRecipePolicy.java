package domain.maintenance;

import domain.inventory.item.misc.StackableMiscellaneousItem;
import java.util.*;

public final class CoolantRecipePolicy {
    private CoolantRecipePolicy(){}

    public static boolean canManufacture(StackableMiscellaneousItem waterskin,StackableMiscellaneousItem mead){
        return waterskin!=null && mead!=null
                && waterskin.name().equalsIgnoreCase("Odre") && waterskin.currentUses()>=1
                && mead.name().equalsIgnoreCase("Petaca de hidromiel") && mead.currentUses()>=1;
    }

    public static boolean consumeInputs(StackableMiscellaneousItem waterskin,StackableMiscellaneousItem mead){
        if(!canManufacture(waterskin,mead)) return false;
        return waterskin.consumeOne() && mead.consumeOne();
    }

    public static String technicalSummary(){
        return "1 uso de Odre + 120 mL de Petaca de hidromiel, destilados, medidos y estabilizados con el Maletín profesional de Alicia e Iván -> 1 uso de Líquido Refrigerante.";
    }
}

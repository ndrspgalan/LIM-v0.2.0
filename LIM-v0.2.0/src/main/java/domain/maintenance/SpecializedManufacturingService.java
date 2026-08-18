package domain.maintenance;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.InventoryCompartmentType;

import java.util.*;

/**
 * Manufactura especializada que requiere el Maletín profesional.
 * El servicio no coloca automáticamente el producto: devuelve la nueva instancia para que
 * la política universal de colocación decida dónde cabe.
 */
public final class SpecializedManufacturingService {

    public ManufacturingResult manufactureElectromechanicalComposite(
            PortableLaboratoryItem briefcase, InventoryState inventory) {
        Objects.requireNonNull(briefcase); Objects.requireNonNull(inventory);
        if (!briefcase.name().equals("Maletín profesional de Alicia e Iván"))
            return ManufacturingResult.rejected("La receta exige el Maletín profesional de Alicia e Iván.");
        if (!isCarried(inventory,briefcase))
            return ManufacturingResult.rejected("El Maletín profesional debe estar físicamente disponible en el inventario.");

        Map<ArmorMaterial,Integer> recipe=ElectromechanicalCompositeRecipe.inputs();
        for(var e:recipe.entrySet()){
            if(availableMaterialUnits(inventory,e.getKey())<e.getValue())
                return ManufacturingResult.rejected("Falta "+e.getKey().label()+" para fabricar Compuesto Electromecánico.");
        }
        ArrayList<String> consumed=new ArrayList<>();
        for(var e:recipe.entrySet()){
            consumeMaterialUnits(inventory,e.getKey(),e.getValue());
            consumed.add(e.getValue()+" × "+e.getKey().label());
        }
        MaterialItem product=MaterialCatalog.electromechanicalComposite();
        return ManufacturingResult.completed(product,
                "Compuesto Electromecánico fabricado y calibrado con el Maletín profesional de Alicia e Iván.",
                consumed);
    }

    /**
     * La receta del refrigerante conserva el contrato: 1 uso de Odre + 1 uso
     * de Petaca de hidromiel, destilados/medidos con el Maletín -> 1 uso de refrigerante.
     */
    public ManufacturingResult manufactureCoolant(
            PortableLaboratoryItem briefcase,
            StackableMiscellaneousItem waterskin,
            StackableMiscellaneousItem mead,
            InventoryState inventory) {
        Objects.requireNonNull(briefcase); Objects.requireNonNull(waterskin); Objects.requireNonNull(mead);
        Objects.requireNonNull(inventory);
        if (!isCarried(inventory,briefcase))
            return ManufacturingResult.rejected("El Maletín profesional debe estar físicamente disponible en el inventario.");
        if (!isCarried(inventory,waterskin) || !isCarried(inventory,mead))
            return ManufacturingResult.rejected("Odre y Petaca deben estar físicamente disponibles en inventario.");
        if (!CoolantRecipePolicy.canManufacture(waterskin,mead))
            return ManufacturingResult.rejected("La receta exige un uso de Odre y 120 mL de Petaca de hidromiel.");
        CoolantRecipePolicy.consumeInputs(waterskin,mead);
        CoolantBottleItem product=new CoolantBottleItem(1);
        return ManufacturingResult.completed(product,
                "Un uso de Líquido Refrigerante ha sido destilado, medido y estabilizado con el Maletín profesional.",
                List.of("1 uso de Odre","1 uso (120 mL) de Petaca de hidromiel"));
    }

    private static boolean isCarried(InventoryState inventory,InventoryEntry item){
        for(InventoryCompartmentType type:InventoryCompartmentType.values()){
            var c=inventory.logistics().compartment(type);
            if(c!=null && c.available() && c.entries().stream().anyMatch(e->e==item)) return true;
        }
        return false;
    }

    private static int availableMaterialUnits(InventoryState inventory,ArmorMaterial material){
        return materialItems(inventory,material).stream().mapToInt(MaterialItem::currentUses).sum();
    }

    private static void consumeMaterialUnits(InventoryState inventory,ArmorMaterial material,int units){
        int remaining=units;
        for(MaterialItem item:materialItems(inventory,material)){
            while(remaining>0 && item.currentUses()>0){
                if(!item.consumeOne()) throw new IllegalStateException("No se pudo consumir "+material.label());
                remaining--;
            }
            if(remaining==0) return;
        }
        throw new IllegalStateException("Consumo no atómico de "+material.label());
    }

    private static List<MaterialItem> materialItems(InventoryState inventory,ArmorMaterial material){
        ArrayList<MaterialItem> out=new ArrayList<>();
        for(InventoryCompartmentType type:InventoryCompartmentType.values()){
            var c=inventory.logistics().compartment(type);
            if(c==null || !c.available()) continue;
            for(InventoryEntry entry:c.entries())
                if(entry instanceof MaterialItem item && item.material()==material) out.add(item);
        }
        return out;
    }
}

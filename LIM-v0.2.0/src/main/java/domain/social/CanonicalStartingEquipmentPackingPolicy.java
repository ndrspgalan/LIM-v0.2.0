package domain.social;

import domain.inventory.InventoryFootprint;
import domain.inventory.InventoryGridDefinition;
import domain.inventory.catalog.PhysicalObjectDimensionsCatalog;
import domain.inventory.item.armor.MaterialCatalog;
import domain.inventory.logistics.GarmentStorageCatalog;
import java.util.*;

/** un loadout canónico sólo existe si todo su contenido cabe físicamente en sus rejillas 2D reales. */
public final class CanonicalStartingEquipmentPackingPolicy {
    private CanonicalStartingEquipmentPackingPolicy(){}

    public static CanonicalStartingEquipment requireValid(CanonicalStartingEquipment e){
        Objects.requireNonNull(e);
        validate(e.wornGarments(),e.inventoryObjectNames(),e.ammunitionNames(),e.inventoryExpanders(),e.currencyStacks(),e.materialUnits());
        return e;
    }

    public static void validate(
            java.util.List<domain.inventory.item.armor.ArmorPiece> wornGarments,
            java.util.List<String> inventoryObjectNames,
            java.util.List<String> ammunitionNames,
            java.util.List<domain.inventory.logistics.InventoryCompartmentType> inventoryExpanders,
            java.util.List<domain.inventory.item.misc.CurrencyStack> currencyStacks,
            java.util.List<domain.inventory.item.armor.ArmorMaterial> materialUnits){
        List<InventoryGridDefinition> modules=new ArrayList<>();
        wornGarments.forEach(piece -> GarmentStorageCatalog.profileFor(piece)
                .ifPresent(profile -> profile.modules().forEach(m -> modules.add(m.grid()))));
        inventoryExpanders.forEach(t -> modules.add(t.grid()));

        List<InventoryFootprint> items=new ArrayList<>();
        inventoryObjectNames.forEach(n -> items.add(PhysicalObjectDimensionsCatalog.auditedFootprintForName(n)));
        ammunitionNames.forEach(n -> items.add(PhysicalObjectDimensionsCatalog.auditedFootprintForName(n)));
        currencyStacks.forEach(c -> items.add(c.canonicalFootprint()));
        materialUnits.forEach(material -> {
            var item=MaterialCatalog.allCanonicalUnits().stream().filter(i->i.material()==material).findFirst().orElseThrow();
            items.add(item.canonicalFootprint());
        });
        if(!fits(items,modules))
            throw new IllegalArgumentException("el equipamiento inicial no cabe en el inventario bidimensional disponible.");
    }

    /** Entrada compacta equivalente al contrato canónico. */
    public static void validate(
            java.util.List<domain.inventory.item.armor.ArmorPiece> wornGarments,
            java.util.List<String> inventoryObjectNames,
            java.util.List<domain.inventory.logistics.InventoryCompartmentType> inventoryExpanders,
            java.util.List<domain.inventory.item.misc.CurrencyStack> currencyStacks,
            java.util.List<domain.inventory.item.armor.ArmorMaterial> materialUnits){
        validate(wornGarments,inventoryObjectNames,java.util.List.of(),inventoryExpanders,currencyStacks,materialUnits);
    }

    public static boolean fits(List<InventoryFootprint> input,List<InventoryGridDefinition> modules){
        if(input.isEmpty()) return true;
        if(modules.isEmpty()) return false;
        List<InventoryFootprint> items=new ArrayList<>(input);
        items.sort(Comparator.comparingInt(InventoryFootprint::occupiedSlots).reversed());
        int[] remaining=modules.stream().mapToInt(InventoryGridDefinition::capacity).toArray();
        return assign(items,0,modules,remaining);
    }
    private static boolean assign(List<InventoryFootprint> items,int index,List<InventoryGridDefinition> modules,int[] remaining){
        if(index==items.size()) return true;
        InventoryFootprint f=items.get(index); int area=f.occupiedSlots();
        for(int i=0;i<modules.size();i++){
            InventoryGridDefinition g=modules.get(i);
            boolean shape=f.fitsInside(g) || (!f.isSquare() && f.rotated90().fitsInside(g));
            if(!shape || remaining[i]<area) continue;
            remaining[i]-=area;
            if(assign(items,index+1,modules,remaining)) return true;
            remaining[i]+=area;
        }
        return false;
    }
}
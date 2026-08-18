package domain.social;

import domain.inventory.InventoryFootprint;
import domain.inventory.InventoryGridDefinition;
import domain.inventory.catalog.PhysicalObjectDimensionsCatalog;
import domain.inventory.item.armor.MaterialCatalog;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.QuickAccessPolicy;

import java.util.*;

/**
 * : ubicación física canónica del patrimonio inicial.
 * Separa fighting load de carga logística y hace explícito qué objeto alimenta cada acceso rápido.
 */
public record CanonicalLoadoutPlacementPlan(
        Map<InventoryCompartmentType,List<String>> compartmentContents,
        Map<Integer,String> quickAccessBindings
) {
    public CanonicalLoadoutPlacementPlan {
        Objects.requireNonNull(compartmentContents);
        Objects.requireNonNull(quickAccessBindings);
        EnumMap<InventoryCompartmentType,List<String>> c=new EnumMap<>(InventoryCompartmentType.class);
        compartmentContents.forEach((k,v)->c.put(Objects.requireNonNull(k),List.copyOf(Objects.requireNonNull(v))));
        compartmentContents=Map.copyOf(c);
        quickAccessBindings=Map.copyOf(quickAccessBindings);
        for(int slot:quickAccessBindings.keySet()) if(slot<1||slot>4)
            throw new IllegalArgumentException("Quick access fuera de 1..4.");
    }

    public List<String> contents(InventoryCompartmentType type){
        return compartmentContents.getOrDefault(type,List.of());
    }

    public void validateAgainst(CanonicalStartingEquipment equipment){
        Objects.requireNonNull(equipment);
        Set<InventoryCompartmentType> available=new HashSet<>(equipment.inventoryExpanders());
        for(var e:compartmentContents.entrySet()){
            InventoryCompartmentType type=e.getKey();
            if(type==InventoryCompartmentType.CHEST_STORAGE || type==InventoryCompartmentType.LEGGINGS_STORAGE) {
                List<InventoryGridDefinition> garmentGrids=garmentGrids(type,equipment);
                if(garmentGrids.isEmpty() && !e.getValue().isEmpty())
                    throw new IllegalArgumentException("Plan usa "+type+" sin una prenda equipada que aporte ese almacenamiento.");
                validateFit(type,e.getValue(),garmentGrids);
            } else {
                if(!available.contains(type)) throw new IllegalArgumentException("Plan usa compartimento no equipado: "+type);
                validateFit(type,e.getValue(),List.of(type.grid()));
            }
        }

        List<String> expected=new ArrayList<>();
        expected.addAll(equipment.inventoryObjectNames());
        expected.addAll(equipment.ammunitionNames());
        for(var m:equipment.materialUnits()) expected.add(m.label());
        for(var c:equipment.currencyStacks()) expected.add(c.name());

        List<String> placed=compartmentContents.values().stream().flatMap(Collection::stream).toList();
        if(!multiset(expected).equals(multiset(placed)))
            throw new IllegalArgumentException("El plan de colocación no representa exactamente todos los objetos transportados.");

        for(var q:quickAccessBindings.entrySet()){
            InventoryCompartmentType source=QuickAccessPolicy.sourceCompartment(q.getKey());
            if(!contents(source).contains(q.getValue()))
                throw new IllegalArgumentException("Quick "+q.getKey()+" debe enlazar un objeto alojado en "+source.label()+": "+q.getValue());
        }
    }

    private static Map<String,Long> multiset(List<String> xs){
        Map<String,Long> m=new HashMap<>();
        xs.forEach(x->m.merge(x,1L,Long::sum));
        return m;
    }

    private static void validateFit(InventoryCompartmentType type,List<String> names,List<InventoryGridDefinition> grids){
        if(names.isEmpty()) return;
        List<InventoryFootprint> fps=new ArrayList<>();
        for(String n:names) fps.add(footprint(n));
        if(grids.isEmpty() || !CanonicalStartingEquipmentPackingPolicy.fits(fps,grids))
            throw new IllegalArgumentException("Contenido no cabe en "+type.label()+": "+names);
    }

    private static List<InventoryGridDefinition> garmentGrids(InventoryCompartmentType type,CanonicalStartingEquipment equipment){
        List<InventoryGridDefinition> grids=new ArrayList<>();
        for(var piece:equipment.wornGarments()){
            domain.inventory.logistics.GarmentStorageCatalog.profileFor(piece).ifPresent(profile -> {
                boolean matches = type==InventoryCompartmentType.CHEST_STORAGE
                        ? profile.category()==domain.inventory.item.armor.ArmorInventoryCategory.CHEST
                          || profile.category()==domain.inventory.item.armor.ArmorInventoryCategory.INTEGRAL_SUIT
                        : profile.category()==domain.inventory.item.armor.ArmorInventoryCategory.LEGGINGS;
                if(matches) profile.modules().forEach(m->grids.add(m.grid()));
            });
        }
        return List.copyOf(grids);
    }

    private static InventoryFootprint footprint(String name){
        try { return PhysicalObjectDimensionsCatalog.auditedFootprintForName(name); }
        catch(IllegalArgumentException ignored){}
        for(var m:MaterialCatalog.allCanonicalUnits()) if(m.name().equals(name)) return m.canonicalFootprint();
        // Moneda: todo stack inicial ocupa 1x1.
        for(var c:domain.inventory.item.misc.CurrencyType.values()) if(c.label().equals(name)) return new InventoryFootprint(1,1);
        throw new IllegalArgumentException(" carece de footprint canónico para "+name);
    }
}

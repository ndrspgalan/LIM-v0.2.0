package domain.social;

import domain.inventory.*;
import domain.inventory.catalog.PhysicalObjectDimensionsCatalog;
import domain.inventory.item.armor.MaterialCatalog;
import domain.inventory.logistics.*;
import java.util.*;

/** colocación canónica civil usando la política real de Quick 1..4. */
final class CivilianStartingEquipmentSupport {
    private CivilianStartingEquipmentSupport(){}

    static CanonicalLoadoutPlacementPlan placement(CanonicalStartingEquipment e){
        EnumMap<InventoryCompartmentType,List<String>> placed=new EnumMap<>(InventoryCompartmentType.class);
        if(hasGarmentProvider(e,InventoryCompartmentType.LEGGINGS_STORAGE)) placed.put(InventoryCompartmentType.LEGGINGS_STORAGE,new ArrayList<>());
        if(hasGarmentProvider(e,InventoryCompartmentType.CHEST_STORAGE)) placed.put(InventoryCompartmentType.CHEST_STORAGE,new ArrayList<>());
        for(var t:e.inventoryExpanders()) if(t!=InventoryCompartmentType.DORSAL_ROTOR_SYSTEM) placed.put(t,new ArrayList<>());

        List<String> all=new ArrayList<>();
        all.addAll(e.inventoryObjectNames()); all.addAll(e.ammunitionNames());
        e.materialUnits().forEach(m->all.add(m.label())); e.currencyStacks().forEach(c->all.add(c.name()));

        LinkedHashMap<Integer,String> quick=new LinkedHashMap<>();

        // Quick 2: linterna mecánica, obligatoriamente en almacenamiento pectoral real.
        String lamp=all.stream().filter(CivilianStartingEquipmentSupport::isMechanicalLamp).findFirst().orElse(null);
        if(lamp!=null){
            if(!placed.containsKey(InventoryCompartmentType.CHEST_STORAGE))
                throw new IllegalArgumentException("La linterna mecánica exige una prenda con CHEST_STORAGE.");
            move(all,placed,InventoryCompartmentType.CHEST_STORAGE,lamp,e);
            quick.put(2,lamp);
        }

        // Quick 1: primera cura ordinaria en polainas/pantalón/falda cuando existe almacenamiento.
        if(placed.containsKey(InventoryCompartmentType.LEGGINGS_STORAGE)){
            String q=first(all,List.of("Apósito de musgo de turbera","Emplasto de milenrama","Corteza de sauce"));
            if(q!=null){ move(all,placed,InventoryCompartmentType.LEGGINGS_STORAGE,q,e); quick.put(1,q); }
        }

        // Quick 3: Pernera, si existe, prioriza cura y después alimento/agua.
        if(placed.containsKey(InventoryCompartmentType.LEG_POUCH)){
            String q=first(all,List.of("Emplasto de milenrama","Apósito de musgo de turbera","Corteza de sauce","Fruta","Pan","Odre"));
            if(q!=null){ move(all,placed,InventoryCompartmentType.LEG_POUCH,q,e); quick.put(3,q); }
        }

        // Quick 4: Bandolera, acceso inmediato de jornada.
        if(placed.containsKey(InventoryCompartmentType.BANDOLIER)){
            String q=first(all,List.of("Odre","Fruta","Pan","Cecina","Frutos secos","Uva deshidratada","Petaca de hidromiel"));
            if(q!=null){ move(all,placed,InventoryCompartmentType.BANDOLIER,q,e); quick.put(4,q); }
        }

        InventoryCompartmentType saddle=e.inventoryExpanders().stream().filter(CivilianStartingEquipmentSupport::isSaddlebag).findFirst().orElse(null);
        List<String> ordered=new ArrayList<>(all);
        ordered.sort(Comparator.comparingInt((String n)->footprint(n).occupiedSlots()).reversed());

        for(String n:ordered){
            boolean heavy=footprint(n).occupiedSlots()>=8 || isMaterial(n) || n.contains("Caja") || n.contains("Refrigerante");
            List<InventoryCompartmentType> candidates=new ArrayList<>();
            if(isArrow(n)&&placed.containsKey(InventoryCompartmentType.ARROW_QUIVER)) candidates.add(InventoryCompartmentType.ARROW_QUIVER);
            if(heavy&&saddle!=null&&!isArrow(n))candidates.add(saddle);
            if(placed.containsKey(InventoryCompartmentType.BACKPACK))candidates.add(InventoryCompartmentType.BACKPACK);
            if(placed.containsKey(InventoryCompartmentType.BANDOLIER))candidates.add(InventoryCompartmentType.BANDOLIER);
            if(placed.containsKey(InventoryCompartmentType.LEG_POUCH))candidates.add(InventoryCompartmentType.LEG_POUCH);
            if(placed.containsKey(InventoryCompartmentType.LEGGINGS_STORAGE))candidates.add(InventoryCompartmentType.LEGGINGS_STORAGE);
            if(placed.containsKey(InventoryCompartmentType.CHEST_STORAGE))candidates.add(InventoryCompartmentType.CHEST_STORAGE);
            if(saddle!=null&&!candidates.contains(saddle))candidates.add(saddle);
            boolean ok=false;
            for(var c:candidates){
                var trial=new ArrayList<>(placed.get(c)); trial.add(n);
                if(fits(c,trial,e)){placed.put(c,trial);ok=true;break;}
            }
            if(!ok)throw new IllegalArgumentException("No cabe "+n+" en la logística civil .");
        }

        EnumMap<InventoryCompartmentType,List<String>> frozen=new EnumMap<>(InventoryCompartmentType.class);
        placed.forEach((k,v)->frozen.put(k,List.copyOf(v)));
        var plan=new CanonicalLoadoutPlacementPlan(frozen,quick);
        plan.validateAgainst(e);
        CanonicalActiveInventoryEquipmentPolicy.validate(e,plan);
        return plan;
    }

    private static String first(List<String> all,List<String> wanted){
        for(String w:wanted) if(all.contains(w)) return w;
        return null;
    }
    private static void move(List<String> all,Map<InventoryCompartmentType,List<String>> placed,InventoryCompartmentType t,String n,CanonicalStartingEquipment e){
        if(!all.remove(n))return;
        var trial=new ArrayList<>(placed.getOrDefault(t,List.of())); trial.add(n);
        if(!fits(t,trial,e))throw new IllegalArgumentException(n+" no cabe en "+t);
        placed.put(t,trial);
    }
    private static boolean fits(InventoryCompartmentType t,List<String> names,CanonicalStartingEquipment e){
        List<InventoryGridDefinition> grids=grids(t,e);
        return !grids.isEmpty() && CanonicalStartingEquipmentPackingPolicy.fits(names.stream().map(CivilianStartingEquipmentSupport::footprint).toList(),grids);
    }
    private static List<InventoryGridDefinition> grids(InventoryCompartmentType t,CanonicalStartingEquipment e){
        if(t!=InventoryCompartmentType.CHEST_STORAGE&&t!=InventoryCompartmentType.LEGGINGS_STORAGE)return List.of(t.grid());
        List<InventoryGridDefinition> out=new ArrayList<>();
        for(var piece:e.wornGarments()) GarmentStorageCatalog.profileFor(piece).ifPresent(p->{
            boolean match=t==InventoryCompartmentType.CHEST_STORAGE
                    ? p.category()==domain.inventory.item.armor.ArmorInventoryCategory.CHEST||p.category()==domain.inventory.item.armor.ArmorInventoryCategory.INTEGRAL_SUIT
                    : p.category()==domain.inventory.item.armor.ArmorInventoryCategory.LEGGINGS;
            if(match)p.modules().forEach(m->out.add(m.grid()));
        });
        return List.copyOf(out);
    }
    private static boolean hasGarmentProvider(CanonicalStartingEquipment e,InventoryCompartmentType t){return !grids(t,e).isEmpty();}
    private static InventoryFootprint footprint(String n){
        try{return PhysicalObjectDimensionsCatalog.auditedFootprintForName(n);}catch(Exception ignored){}
        for(var m:MaterialCatalog.allCanonicalUnits())if(m.name().equals(n))return m.canonicalFootprint();
        for(var c:domain.inventory.item.misc.CurrencyType.values())if(c.label().equals(n))return new InventoryFootprint(1,1);
        throw new IllegalArgumentException("Sin footprint canónico : "+n);
    }
    private static boolean isMechanicalLamp(String n){return n.equals("MAGNETLAMPE")||n.equals("KNIJPKAT");}
    private static boolean isArrow(String n){return n.startsWith("Flecha ");}
    private static boolean isSaddlebag(InventoryCompartmentType t){return t.name().startsWith("SADDLEBAGS_");}
    private static boolean isMaterial(String n){return Arrays.stream(domain.inventory.item.armor.ArmorMaterial.values()).anyMatch(m->m.label().equals(n));}
}
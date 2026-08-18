package domain.social;

import domain.character.CharacterClass;
import domain.inventory.InventoryFootprint;
import domain.inventory.catalog.PhysicalObjectDimensionsCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;
import domain.inventory.logistics.*;
import java.util.*;

/**  support for sex-specific combat loadouts and explicit placement. */
final class CombatStartingEquipmentSupport {
    private CombatStartingEquipmentSupport(){}

    static List<ArmorPiece> armor(boolean male, ArmorPiece chest, ArmorPiece bracers, ArmorPiece legs, ArmorPiece feet, ArmorPiece head){
        List<ArmorPiece> a=new ArrayList<>();
        if(male){
            a.add(ArmorCatalog.innerUndershirt()); a.add(ArmorCatalog.innerWorkShirt());
            a.add(ArmorCatalog.innerLongDrawersV881()); a.add(ArmorCatalog.middleWorkTrousersV881());
            a.add(ArmorCatalog.innerFeetHeavyWorkSocksV881());
        }else{
            a.add(ArmorCatalog.innerChemise()); a.add(ArmorCatalog.innerBlouse());
            a.add(ArmorCatalog.innerWomensDrawersV881()); a.add(ArmorCatalog.innerDividedPetticoatV881());
            a.add(ArmorCatalog.middleDividedSkirtV881()); a.add(ArmorCatalog.innerFeetHeavyKnitStockingsV881());
        }
        for(ArmorPiece p:new ArmorPiece[]{chest,bracers,legs,feet,head}) if(p!=null)a.add(p);
        return List.copyOf(a);
    }

    static List<String> commonInventory(String throwable, String...extra){
        ArrayList<String>x=new ArrayList<>(List.of(
            "Pan","Odre","Apósito de musgo de turbera",
            "Inyección estimulante","Inyección estimulante","Inyección estimulante","Inyección estimulante"
        ));
        if(throwable!=null&&!throwable.isBlank())x.add(throwable);
        x.addAll(List.of(extra)); return List.copyOf(x);
    }

    static List<String> ammoFor(List<String> weapons){
        ArrayList<String>a=new ArrayList<>();
        for(String w:weapons){
            switch(w){
                case "Rifle Neumático de Repetición V881" -> a.add("Cartucho .46 de plomo");
                case "Fusil Bifilar Electromagnético V881" -> a.add("Cargador bifilar .46 V881");
                case "Pistola Autocargadora V881" -> a.add("Cargador .45 de Pistola V881");
                case "Subfusil Automático V881" -> a.add("Cargador de 9 mm V881");
                case "Fusil de Repetición V881" -> a.add("Cartucho completo 7,92×57 mm V881");
                case "Cañón Antimaterial V881" -> a.add("Cartucho de 4 proyectiles de 20 mm V881");
                case "Cañón de Racimo V881" -> a.add("Cohete de Racimo V881 de 85 mm");
                case "Rociador de Cal Viva V881" -> a.add("Estuche de Cartuchos de Cal Viva V881");
                case "Honda" -> a.add("Guijarro");
                case "Arco Simple Recurvo","Arco Compuesto" -> {
                    a.add("Flecha perforante");a.add("Flecha de Púas");a.add("Flecha de Hoja");
                }
            }
        } return List.copyOf(a);
    }


    static List<String> weaponAccessoriesFor(List<String> weapons){
        LinkedHashSet<String> a=new LinkedHashSet<>();
        for(String w:weapons){
            switch(w){
                case "Rifle Neumático de Repetición V881" -> { a.add("Correa de Arma V881"); a.add("Mirilla Fiedler V881"); }
                case "Fusil Bifilar Electromagnético V881" -> { a.add("Correa de Arma V881"); a.add("Bípode de Arma V881"); a.add("Mirilla Zeiss V881"); }
                case "Fusil de Repetición V881" -> { a.add("Correa de Arma V881"); a.add("Mirilla Fiedler V881"); }
                case "Cañón Antimaterial V881" -> { a.add("Correa de Arma V881"); a.add("Bípode de Arma V881"); a.add("Mirilla Winchester A5 V881"); }
                case "Cañón de Racimo V881" -> { a.add("Correa de Arma V881"); a.add("Bípode de Arma V881"); a.add("Mirilla Zeiss V881"); }
                case "Lanza-Arcos Electrodinámico V881" -> a.add("Correa de Arma V881");
                case "Subfusil Automático V881" -> a.add("Correa de Arma V881");
                case "Rociador de Cal Viva V881" -> a.add("Correa de Arma V881");
                default -> { }
            }
        }
        return List.copyOf(a);
    }

    static Optional<PersonalTransportType> transport(PersonalTransportType t){return Optional.ofNullable(t);}
    static List<InventoryCompartmentType> expanders(PersonalTransportType t, boolean rotor, boolean bow){
        ArrayList<InventoryCompartmentType>x=new ArrayList<>();
        x.add(InventoryCompartmentType.LEG_POUCH);x.add(InventoryCompartmentType.BANDOLIER);
        x.add(rotor?InventoryCompartmentType.DORSAL_ROTOR_SYSTEM:InventoryCompartmentType.BACKPACK);
        if(bow)x.add(InventoryCompartmentType.ARROW_QUIVER);
        if(t!=null){
            switch(t){
                case HORSE_LEISURE->x.add(InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE);
                case HORSE_RACING->x.add(InventoryCompartmentType.SADDLEBAGS_HORSE_RACING);
                case HORSE_DRAFT->x.add(InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT);
                case BICYCLE_MILITARY_V881->x.add(InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY);
                case MOTORCYCLE_CARDAN_V881->x.add(InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN);
                case BICYCLE_FOLDING_V881->{}
            }
        }
        return List.copyOf(x);
    }

    static CanonicalLoadoutPlacementPlan placement(CanonicalStartingEquipment e){
        EnumMap<InventoryCompartmentType,List<String>> placed=new EnumMap<>(InventoryCompartmentType.class);
        for(var t:e.inventoryExpanders()) if(t!=InventoryCompartmentType.DORSAL_ROTOR_SYSTEM) placed.put(t,new ArrayList<>());
        List<String> all=new ArrayList<>();
        all.addAll(e.inventoryObjectNames());all.addAll(e.ammunitionNames());
        e.materialUnits().forEach(m->all.add(m.label()));e.currencyStacks().forEach(c->all.add(c.name()));

        // Quick access fixed: stimulant in pernera; first throwing weapon in bandolera.
        String throwable=all.stream().filter(CombatStartingEquipmentSupport::isThrowing).findFirst().orElse(null);
        while(all.contains("Inyección estimulante"))
            moveNamed(all,placed,InventoryCompartmentType.LEG_POUCH,"Inyección estimulante");
        if(throwable!=null)moveNamed(all,placed,InventoryCompartmentType.BANDOLIER,throwable);

        // Heavy logistics first to saddlebags when available.
        InventoryCompartmentType saddle=e.inventoryExpanders().stream().filter(CombatStartingEquipmentSupport::isSaddlebag).findFirst().orElse(null);
        List<String> ordered=new ArrayList<>(all);
        ordered.sort(Comparator.comparingInt((String n)->footprint(n).occupiedSlots()).reversed());
        for(String n:ordered){
            boolean heavy=footprint(n).occupiedSlots()>=8 || isMaterial(n) || n.contains("Caja") || n.contains("Refrigerante");
            List<InventoryCompartmentType> candidates=new ArrayList<>();
            if(heavy&&saddle!=null)candidates.add(saddle);
            candidates.add(InventoryCompartmentType.BACKPACK);
            candidates.add(InventoryCompartmentType.BANDOLIER);
            candidates.add(InventoryCompartmentType.LEG_POUCH);
            if(saddle!=null&&!candidates.contains(saddle))candidates.add(saddle);
            if(e.inventoryExpanders().contains(InventoryCompartmentType.ARROW_QUIVER)&&n.startsWith("Flecha"))
                candidates.add(0,InventoryCompartmentType.ARROW_QUIVER);
            boolean ok=false;
            for(var c:candidates){
                if(!placed.containsKey(c))continue;
                ArrayList<String> trial=new ArrayList<>(placed.get(c));trial.add(n);
                if(fits(c,trial)){placed.put(c,trial);ok=true;break;}
            }
            if(!ok)throw new IllegalArgumentException("No cabe "+n+" en la logística canónica .");
        }
        EnumMap<InventoryCompartmentType,List<String>> frozen=new EnumMap<>(InventoryCompartmentType.class);
        placed.forEach((k,v)->frozen.put(k,List.copyOf(v)));
        Map<Integer,String> quick=throwable==null?Map.of(3,"Inyección estimulante"):Map.of(3,"Inyección estimulante",4,throwable);
        CanonicalLoadoutPlacementPlan p=new CanonicalLoadoutPlacementPlan(frozen,quick);p.validateAgainst(e);return p;
    }

    private static void moveNamed(List<String> all,Map<InventoryCompartmentType,List<String>> p,InventoryCompartmentType t,String n){
        int i=all.indexOf(n); if(i<0)return; all.remove(i);
        ArrayList<String>x=new ArrayList<>(p.getOrDefault(t,List.of()));x.add(n);
        if(!fits(t,x))throw new IllegalArgumentException(n+" no cabe en "+t);p.put(t,x);
    }
    private static boolean fits(InventoryCompartmentType t,List<String> names){
        return CanonicalStartingEquipmentPackingPolicy.fits(names.stream().map(CombatStartingEquipmentSupport::footprint).toList(),List.of(t.grid()));
    }
    private static InventoryFootprint footprint(String n){
        try{return PhysicalObjectDimensionsCatalog.auditedFootprintForName(n);}catch(Exception ignored){}
        for(var m:MaterialCatalog.allCanonicalUnits())if(m.name().equals(n))return m.canonicalFootprint();
        for(var c:CurrencyType.values())if(c.label().equals(n))return new InventoryFootprint(1,1);
        throw new IllegalArgumentException("Sin footprint : "+n);
    }
    private static boolean isThrowing(String n){return n.equals("Cápsula de Gas Amonio V881")||n.equals("Granada Incendiaria de Terracota V881")||n.equals("Granada de Huevo con Fósforo y Azufre V881")||n.equals("Cuchillo Arrojadizo V881");}
    private static boolean isSaddlebag(InventoryCompartmentType t){return t.name().startsWith("SADDLEBAGS_");}
    private static boolean isMaterial(String n){return Arrays.stream(ArmorMaterial.values()).anyMatch(m->m.label().equals(n));}
}

package domain.inventory.item.ammunition;

import domain.inventory.InventoryEntry;
import domain.inventory.catalog.CanonicalObjectTypeId;

import java.util.*;

/**
 * : contenido especializado del carcaj. No es un stack de inventario general:
 * agrega exclusivamente flechas ya contenidas físicamente en ARROW_QUIVER.
 */
public final class ArrowQuiverContents {
    public static final int MAX_ARROWS=12;
    private final LinkedHashMap<CanonicalObjectTypeId,Integer> counts;
    private final LinkedHashMap<CanonicalObjectTypeId,String> labels;
    private final double weightKg;

    private ArrowQuiverContents(LinkedHashMap<CanonicalObjectTypeId,Integer> counts,
                                LinkedHashMap<CanonicalObjectTypeId,String> labels,double weightKg){
        this.counts=new LinkedHashMap<>(counts);
        this.labels=new LinkedHashMap<>(labels);
        this.weightKg=weightKg;
    }

    public static ArrowQuiverContents from(List<InventoryEntry> entries){
        Objects.requireNonNull(entries);
        LinkedHashMap<CanonicalObjectTypeId,Integer> counts=new LinkedHashMap<>();
        LinkedHashMap<CanonicalObjectTypeId,String> labels=new LinkedHashMap<>();
        double weight=0;
        int total=0;
        for(InventoryEntry entry:entries){
            if(!(entry instanceof ProjectileAmmunitionItem arrow)
                    || arrow.ammunitionDescriptor().family()!=AmmunitionFamily.ARROW)
                throw new IllegalArgumentException("El carcaj sólo agrega flechas.");
            CanonicalObjectTypeId id=entry.canonicalTypeId();
            counts.merge(id,1,Integer::sum);
            labels.putIfAbsent(id,entry.name());
            weight+=entry.weightKg();
            total++;
        }
        if(total>MAX_ARROWS) throw new IllegalArgumentException("El carcaj admite como máximo 12 flechas globales.");
        return new ArrowQuiverContents(counts,labels,weight);
    }

    public int totalArrows(){ return counts.values().stream().mapToInt(Integer::intValue).sum(); }
    public int remainingCapacity(){ return MAX_ARROWS-totalArrows(); }
    public double weightKg(){ return weightKg; }
    public Map<CanonicalObjectTypeId,Integer> counts(){ return Map.copyOf(counts); }

    public List<String> displayLines(){
        ArrayList<String> lines=new ArrayList<>();
        for(var e:counts.entrySet()) lines.add(labels.get(e.getKey())+" ×"+e.getValue());
        return List.copyOf(lines);
    }
}

package domain.inventory.logistics;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryGridDefinition;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.ArmorInventoryCategory;
import domain.inventory.item.armor.ArmorMaterialClass;
import domain.inventory.item.armor.ArmorPiece;

import java.util.*;

/** Compartimento físico de inventario.  distingue módulos independientes de bolsillos/prendas. */
public record InventoryCompartment(
        InventoryCompartmentType type,
        boolean available,
        InventoryGridDefinition grid,
        List<InventoryEntry> entries,
        Optional<ArmorPiece> externallyCarriedHelmet,
        List<InventoryStorageModule> storageModules
) {
    public InventoryCompartment {
        Objects.requireNonNull(type, "El tipo de compartimento no puede ser nulo.");
        Objects.requireNonNull(grid, "La rejilla no puede ser nula.");
        Objects.requireNonNull(entries, "El contenido del compartimento no puede ser nulo.");
        Objects.requireNonNull(externallyCarriedHelmet, "El casco externo no puede ser nulo.");
        Objects.requireNonNull(storageModules, "Los módulos no pueden ser nulos.");
        if (entries.stream().anyMatch(Objects::isNull) || storageModules.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Un compartimento no puede contener valores nulos.");
        if (!available && (!entries.isEmpty() || externallyCarriedHelmet.isPresent() || !storageModules.isEmpty()))
            throw new IllegalArgumentException("Un compartimento no disponible no puede conservar contenido o módulos.");
        if (available && storageModules.isEmpty() && !entries.isEmpty())
            throw new IllegalArgumentException("Un compartimento sin módulos no puede contener objetos.");
        validateSpecializedAdmission(type, entries);
        if (type != InventoryCompartmentType.ARROW_QUIVER && !entriesFitModules(entries, storageModules))
            throw new IllegalArgumentException("El contenido no cabe en los módulos físicos de " + type.label() + ".");
        double totalWeightKg = entries.stream().mapToDouble(InventoryEntry::weightKg).sum();
        if (type.maximumWeightKg().isPresent() && totalWeightKg > type.maximumWeightKg().getAsDouble() + 1e-9)
            throw new IllegalArgumentException(type.label() + " admite un máximo no negociable de " + type.maximumWeightKg().getAsDouble() + " kg.");
        if (externallyCarriedHelmet.isPresent()) validateExternalHelmet(type, externallyCarriedHelmet.get());
        entries = List.copyOf(entries);
        storageModules = List.copyOf(storageModules);
    }

    /** Compatibilidad /obsoleto: la rejilla se considera un único módulo físico. */
    public InventoryCompartment(InventoryCompartmentType type, boolean available, InventoryGridDefinition grid,
                                List<InventoryEntry> entries, Optional<ArmorPiece> externallyCarriedHelmet) {
        this(type, available, grid, entries, externallyCarriedHelmet,
                available && !grid.isEmpty() ? List.of(InventoryStorageModule.fromGrid(type.label(), grid)) : List.of());
    }
    public InventoryCompartment(InventoryCompartmentType type, boolean available, List<InventoryEntry> entries) {
        this(type, available, type.grid(), entries, Optional.empty());
    }

    public static InventoryCompartment empty(InventoryCompartmentType type, boolean available) {
        return new InventoryCompartment(type, available, type.grid(), List.of(), Optional.empty());
    }
    public static InventoryCompartment empty(InventoryCompartmentType type, boolean available, InventoryGridDefinition grid) {
        return new InventoryCompartment(type, available, grid, List.of(), Optional.empty());
    }
    public static InventoryCompartment modular(InventoryCompartmentType type, List<InventoryStorageModule> modules, List<InventoryEntry> entries) {
        Objects.requireNonNull(modules);
        InventoryGridDefinition summary = summaryGrid(modules);
        return new InventoryCompartment(type, !modules.isEmpty(), summary, entries, Optional.empty(), modules);
    }

    public InventoryCompartment withGrid(InventoryGridDefinition newGrid) {
        return new InventoryCompartment(type, available, newGrid, entries, externallyCarriedHelmet);
    }
    public InventoryCompartment withExternalHelmet(ArmorPiece helmet) {
        if (!available) throw new IllegalStateException(type.label() + " debe estar equipado antes de fijar un casco.");
        return new InventoryCompartment(type, true, grid, entries, Optional.of(Objects.requireNonNull(helmet)), storageModules);
    }
    public InventoryCompartment withoutExternalHelmet() {
        return new InventoryCompartment(type, available, grid, entries, Optional.empty(), storageModules);
    }
    public InventoryCompartment withEntries(List<InventoryEntry> newEntries) {
        return new InventoryCompartment(type, available, grid, newEntries, externallyCarriedHelmet, storageModules);
    }

    public double contentsWeightKg() { return entries.stream().mapToDouble(InventoryEntry::weightKg).sum(); }
    public double externalLoadWeightKg() { return externallyCarriedHelmet.map(ArmorPiece::weightKg).orElse(0.0); }
    public double totalWeightKg() { return (available ? type.structuralWeightKg() : 0.0) + contentsWeightKg() + externalLoadWeightKg(); }
    public int occupiedSlots() {
        return type == InventoryCompartmentType.ARROW_QUIVER
                ? arrowQuiverContents().orElseThrow().totalArrows()
                : entries.stream().mapToInt(entry -> entry.footprint().occupiedSlots()).sum();
    }
    public int capacitySlots() {
        return type == InventoryCompartmentType.ARROW_QUIVER
                ? domain.inventory.item.ammunition.ArrowQuiverContents.MAX_ARROWS
                : storageModules.stream().mapToInt(InventoryStorageModule::capacity).sum();
    }
    public int freeSlots() { return capacitySlots() - occupiedSlots(); }
    public Optional<domain.inventory.item.ammunition.ArrowQuiverContents> arrowQuiverContents() {
        return type == InventoryCompartmentType.ARROW_QUIVER
                ? Optional.of(domain.inventory.item.ammunition.ArrowQuiverContents.from(entries))
                : Optional.empty();
    }


    private static InventoryGridDefinition summaryGrid(List<InventoryStorageModule> modules) {
        if (modules.isEmpty()) return InventoryGridDefinition.empty();
        int capacity = modules.stream().mapToInt(InventoryStorageModule::capacity).sum();
        int commonWidth = modules.stream().mapToInt(m -> m.grid().horizontalSlots()).reduce(0, InventoryCompartment::gcd);
        if (commonWidth <= 0 || capacity % commonWidth != 0) commonWidth = 1;
        return new InventoryGridDefinition(capacity / commonWidth, commonWidth);
    }
    private static int gcd(int a,int b){ if(a==0)return b; while(b!=0){int t=a%b;a=b;b=t;}return a; }

    /** Bin-packing discreto por módulo: cada entrada debe caber físicamente y consumir capacidad de un módulo concreto. */
    private static boolean entriesFitModules(List<InventoryEntry> entries, List<InventoryStorageModule> modules) {
        if (entries.isEmpty()) return true;
        if (modules.isEmpty()) return false;
        List<InventoryEntry> ordered = new ArrayList<>(entries);
        ordered.sort(Comparator.comparingInt((InventoryEntry e)->e.footprint().occupiedSlots()).reversed());
        int[] remaining = modules.stream().mapToInt(InventoryStorageModule::capacity).toArray();
        return assign(ordered,0,modules,remaining);
    }
    private static boolean assign(List<InventoryEntry> entries,int index,List<InventoryStorageModule> modules,int[] remaining){
        if(index==entries.size()) return true;
        InventoryEntry e=entries.get(index); int area=e.footprint().occupiedSlots();
        for(int i=0;i<modules.size();i++){
            InventoryGridDefinition g=modules.get(i).grid();
            if(remaining[i] < area || !e.footprint().fitsInside(g)) continue;
            remaining[i]-=area;
            if(assign(entries,index+1,modules,remaining)) return true;
            remaining[i]+=area;
        }
        return false;
    }

    private static void validateExternalHelmet(InventoryCompartmentType type, ArmorPiece helmet) {
        if (!type.supportsExternalHelmetCarrier()) throw new IllegalArgumentException(type.label() + " no dispone de portacasco exterior.");
        ArmorInventoryCategory category = helmet.inventoryCategory().orElse(null);
        if (category != ArmorInventoryCategory.HEAD) throw new IllegalArgumentException("El soporte exterior sólo admite cascos HEAD.");
        if (helmet.hasProperty(ItemPropertyId.EYEWEAR)) throw new IllegalArgumentException("Las gafas no se fijan al portacasco exterior.");
        ArmorMaterialClass materialClass = helmet.materialClass();
        if (materialClass != ArmorMaterialClass.MEDIUM && materialClass != ArmorMaterialClass.HEAVY)
            throw new IllegalArgumentException("El soporte exterior está concebido para cascos MEDIUM o HEAVY.");
    }

    private static void validateSpecializedAdmission(InventoryCompartmentType type, List<InventoryEntry> entries) {
        if (type == InventoryCompartmentType.ARROW_QUIVER) {
            if (entries.size() > 12) throw new IllegalArgumentException("El carcaj admite como máximo 12 flechas.");
            boolean invalid = entries.stream().anyMatch(entry -> !(entry instanceof domain.inventory.item.ammunition.ProjectileAmmunitionItem projectile)
                    || projectile.ammunitionDescriptor().family() != domain.inventory.item.ammunition.AmmunitionFamily.ARROW);
            if (invalid) throw new IllegalArgumentException("El carcaj para flechas solo admite objetos FLECHA.");
        }
        if (type == InventoryCompartmentType.DORSAL_ROTOR_SYSTEM && !entries.isEmpty()) {
            throw new IllegalArgumentException("el sistema dorsal no guarda objetos en su grid; habilita la ranura BACK_HAND para el Rotor.");
        }
    }
}

package domain.runic.transposition;

import domain.character.progression.MucusType;
import domain.character.progression.MucusWallet;
import domain.character.sheet.CharacterSheet;
import domain.inventory.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;

import java.util.*;
import java.util.function.Supplier;

/** materializa Transposición en el primer almacenamiento correspondiente a Quick 1→4 que admita el resultado. */
public final class TranspositionInventoryService {
    private final InventoryAutoPlacementService autoPlacement = new InventoryAutoPlacementService();

    public List<InventoryCompartmentType> placementPriority() { return autoPlacement.priority(); }

    public InventoryTranspositionResult transpose(MucusType type, MucusWallet wallet, InventoryState inventory,
                                                   CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(type); Objects.requireNonNull(wallet); Objects.requireNonNull(inventory);
        Objects.requireNonNull(sheet); Objects.requireNonNull(equipment);
        if (!equipment.hasAwakenedRunicMark(domain.runic.RunicMarkId.TRANSPOSICION, sheet))
            return rejected(wallet, inventory, "Transposición permanece latente.");
        if (!wallet.contains(type)) return rejected(wallet, inventory, "No queda " + type.label() + " para transponer.");
        return type == MucusType.BLANCO ? transposeWhite(wallet, inventory) : transposeCrystal(type, wallet, inventory);
    }

    private InventoryTranspositionResult transposeWhite(MucusWallet wallet, InventoryState inventory) {
        double exact = wallet.quantityMlOf(MucusType.BLANCO);
        int available = (int)Math.round(exact);
        if (Math.abs(exact-available)>1e-9) throw new IllegalStateException("El mucus blanco debe materializarse en mL completos.");
        if (available<=0) return rejected(wallet,inventory,"No queda Mucus Blanco.");

        // una Lágrima individual nunca supera 100 mL. Antes de consumir el wallet
        // se simula toda la operación para garantizar atomicidad: o cabe el resultado completo o no cambia nada.
        InventoryState staged=inventory;
        int remaining=available;
        int created=0;

        // Primero aprovecha capacidad residual de lágrimas existentes sólo cuando su crecimiento sigue cabiendo.
        java.util.List<MucusTearItem> grown=new java.util.ArrayList<>();
        java.util.List<Integer> grownBy=new java.util.ArrayList<>();
        outer:
        for (InventoryCompartmentType type : autoPlacement.priority()) {
            InventoryCompartment c=staged.logistics().compartment(type);
            if(!c.available()) continue;
            for(InventoryEntry entry:c.entries()){
                if(!(entry instanceof MucusTearItem tear) || tear.currentUses()>=MucusTearItem.MAXIMUM_AGGREGATE_USES) continue;
                int add=Math.min(remaining,MucusTearItem.MAXIMUM_AGGREGATE_USES-tear.currentUses());
                if(add<=0) continue;
                if(!tear.addUnits(add)) throw new IllegalStateException("Capacidad de Lágrima incoherente.");
                try {
                    staged=new InventoryState(staged.equipment(),staged.quickAccessBar(),
                            staged.logistics().withCompartment(c.withEntries(c.entries())),staged.armorLayout());
                    grown.add(tear); grownBy.add(add); remaining-=add;
                    if(remaining==0) break outer;
                } catch(IllegalArgumentException ex) {
                    tear.removeUnits(add); // crecer aquí haría que dejase de caber
                }
            }
        }

        // El excedente se divide automáticamente en nuevas lágrimas de hasta 100 mL.
        while(remaining>0){
            int quantity=Math.min(remaining,MucusTearItem.MAXIMUM_AGGREGATE_USES);
            MucusTearItem tear=new MucusTearItem(quantity);
            InventoryAdmissionResult r=autoPlacement.admit(staged,tear,InventoryAdmissionSource.TRANSPOSITION);
            if(!r.accepted()){
                // rollback de todo crecimiento previo: el wallet y el inventario quedan idénticos.
                for(int i=0;i<grown.size();i++) grown.get(i).removeUnits(grownBy.get(i));
                return rejected(wallet,inventory,InventoryAutoPlacementService.NO_SPACE_MESSAGE);
            }
            staged=r.inventory();
            remaining-=quantity;
            created++;
        }

        return new InventoryTranspositionResult(true,available,created,wallet.consume(MucusType.BLANCO,available),staged,
                "Transposición completada: "+available+" mL de Mucus Blanco distribuidos en Lágrimas de máximo 100 mL; "
                        +created+" nueva(s) Lágrima(s) creada(s).");
    }

    private InventoryTranspositionResult transposeCrystal(MucusType type, MucusWallet wallet, InventoryState inventory) {
        double precursor=TranspositionYieldPolicy.precursorMlPerCrystal(type);
        if(wallet.quantityMlOf(type)+1e-9 < precursor)
            return rejected(wallet,inventory,"Transposición exige "+formatMl(precursor)+" mL de "+type.label()+" por cristal.");

        Supplier<MucusCrystalItem> factory = switch (type) {
            case AMARILLENTO -> MucusCrystalCatalog::yellow;
            case VERDOSO -> MucusCrystalCatalog::greenish;
            case MARRON -> MucusCrystalCatalog::brown;
            case ENSANGRENTADO -> MucusCrystalCatalog::bloodied;
            case NEGRUZCO -> MucusCrystalCatalog::blackish;
            case BLANCO -> throw new IllegalArgumentException("El mucus blanco produce Lágrimas.");
        };
        Placement placement = firstPlacement(inventory.logistics(), factory);
        if (placement == null) return rejected(wallet, inventory, InventoryAutoPlacementService.NO_SPACE_MESSAGE);
        InventoryState next = new InventoryState(inventory.equipment(), inventory.quickAccessBar(), placement.logistics(),inventory.armorLayout());
        return new InventoryTranspositionResult(true, precursor, 1, wallet.consume(type,precursor), next,
                "Transposición completada: "+formatMl(precursor)+" mL de "+type.label()+" concentrados en "+((MucusCrystalItem) placement.item()).geometry().label()+".");
    }

    private static String formatMl(double ml){
        return Math.abs(ml-Math.rint(ml))<1e-9 ? Long.toString(Math.round(ml)) : String.format(java.util.Locale.ROOT,"%.1f",ml);
    }

    private Placement firstPlacement(LogisticsState logistics, Supplier<? extends InventoryEntry> factory) {
        InventoryEntry item=factory.get();
        InventoryState base=new InventoryState(EquipmentState.empty(),QuickAccessBar.empty(),logistics);
        InventoryAdmissionResult result=autoPlacement.admit(base,item,InventoryAdmissionSource.TRANSPOSITION);
        if(!result.accepted()) return null;
        return new Placement(result.inventory().logistics(),item);
    }

    private static InventoryTranspositionResult rejected(MucusWallet wallet, InventoryState inventory, String message) {
        return new InventoryTranspositionResult(false, 0, 0, wallet, inventory, message);
    }
    private record Placement(LogisticsState logistics, InventoryEntry item) {}
}

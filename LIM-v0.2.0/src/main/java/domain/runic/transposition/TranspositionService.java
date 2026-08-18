package domain.runic.transposition;

import domain.character.progression.MucusType;
import domain.character.progression.MucusWallet;
import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.misc.MucusTearItem;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.inventory.item.misc.MucusCrystalItem;
import domain.runic.RunicMarkId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/** Caso de uso atómico de Transposición. Nunca destruye mucus que no pueda materializarse. */
public final class TranspositionService {

    public TranspositionResult transposeWhite(MucusWallet wallet, InventoryCompartment compartment,
                                               CharacterSheet sheet, EquipmentState equipment) {
        validate(wallet, compartment, sheet, equipment);
        if (!active(sheet, equipment)) return rejected(wallet, compartment, "Transposición permanece latente.");
        int available = (int)Math.round(wallet.quantityOf(MucusType.BLANCO));
        if (available == 0) return rejected(wallet, compartment, "No hay mucus blanco.");

        int remaining=available;
        List<Integer> additions=new ArrayList<>();
        List<MucusTearItem> existing=new ArrayList<>();
        List<InventoryEntry> preview=new ArrayList<>();

        for(InventoryEntry entry:compartment.entries()){
            if(entry instanceof MucusTearItem tear){
                int add=Math.min(remaining,MucusTearItem.MAXIMUM_AGGREGATE_USES-tear.currentUses());
                existing.add(tear); additions.add(add); remaining-=add;
                preview.add(new MucusTearItem(tear.currentUses()+add));
            }else preview.add(entry);
        }
        List<MucusTearItem> created=new ArrayList<>();
        while(remaining>0){
            int q=Math.min(remaining,MucusTearItem.MAXIMUM_AGGREGATE_USES);
            MucusTearItem tear=new MucusTearItem(q);
            created.add(tear); preview.add(tear); remaining-=q;
        }

        try{
            new InventoryCompartment(compartment.type(),compartment.available(),compartment.grid(),preview,
                    compartment.externallyCarriedHelmet(),compartment.storageModules());
        }catch(IllegalArgumentException ex){
            return rejected(wallet,compartment,domain.inventory.InventoryAutoPlacementService.NO_SPACE_MESSAGE);
        }

        for(int i=0;i<existing.size();i++) if(additions.get(i)>0) existing.get(i).addUnits(additions.get(i));
        List<InventoryEntry> updated=new ArrayList<>(compartment.entries());
        updated.addAll(created);
        InventoryCompartment result=new InventoryCompartment(compartment.type(),compartment.available(),compartment.grid(),updated,
                compartment.externallyCarriedHelmet(),compartment.storageModules());
        return new TranspositionResult(true,available,created.size(),wallet.consume(MucusType.BLANCO,available),result,
                "Todo el mucus blanco ha sido transpuesto en Lágrimas de máximo 100 mL.");
    }

    public TranspositionResult transposeOne(MucusType type, MucusWallet wallet, InventoryCompartment compartment,
                                            CharacterSheet sheet, EquipmentState equipment) {
        validate(wallet, compartment, sheet, equipment);
        Objects.requireNonNull(type, "El tipo de mucus no puede ser nulo.");
        if (type == MucusType.BLANCO) throw new IllegalArgumentException("El mucus blanco usa la operación de Lágrimas.");
        if (!active(sheet, equipment)) return rejected(wallet, compartment, "Transposición permanece latente.");
        double precursor=TranspositionYieldPolicy.precursorMlPerCrystal(type);
        if (wallet.quantityMlOf(type)+1e-9 < precursor)
            return rejected(wallet, compartment, "Transposición exige " + precursor + " mL de " + type.label() + " por cristal.");
        MucusCrystalItem crystal = crystalFactory(type).get();
        if (!canAdd(compartment, crystal)) return rejected(wallet, compartment, domain.inventory.InventoryAutoPlacementService.NO_SPACE_MESSAGE);
        List<InventoryEntry> updated = new ArrayList<>(compartment.entries());
        updated.add(crystal);
        return new TranspositionResult(true, precursor, 1, wallet.consume(type,precursor),
                new InventoryCompartment(compartment.type(), compartment.available(), compartment.grid(), updated, compartment.externallyCarriedHelmet(), compartment.storageModules()),
                "Se ha creado " + crystal.name() + " consumiendo " + precursor + " mL.");
    }

    private static boolean active(CharacterSheet sheet, EquipmentState equipment) {
        return equipment.hasAwakenedRunicMark(RunicMarkId.TRANSPOSICION, sheet);
    }

    private static boolean canAdd(InventoryCompartment compartment, InventoryEntry entry) {
        return compartment.available()
                && entry.footprint().fitsInside(compartment.grid())
                && compartment.freeSlots() >= entry.footprint().occupiedSlots()
                && (compartment.type().maximumWeightKg().isEmpty()
                || compartment.contentsWeightKg() + entry.weightKg() <= compartment.type().maximumWeightKg().getAsDouble() + 1e-9);
    }

    private static Supplier<MucusCrystalItem> crystalFactory(MucusType type) {
        return switch (type) {
            case AMARILLENTO -> MucusCrystalCatalog::yellow;
            case VERDOSO -> MucusCrystalCatalog::greenish;
            case MARRON -> MucusCrystalCatalog::brown;
            case ENSANGRENTADO -> MucusCrystalCatalog::bloodied;
            case NEGRUZCO -> MucusCrystalCatalog::blackish;
            case BLANCO -> throw new IllegalArgumentException("El mucus blanco no produce cristales.");
        };
    }

    private static int countNewTearStacks(List<InventoryEntry> before, List<InventoryEntry> after) {
        long b = before.stream().filter(MucusTearItem.class::isInstance).count();
        long a = after.stream().filter(MucusTearItem.class::isInstance).count();
        return Math.toIntExact(a - b);
    }

    private static void validate(MucusWallet wallet, InventoryCompartment compartment,
                                 CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(wallet); Objects.requireNonNull(compartment);
        Objects.requireNonNull(sheet); Objects.requireNonNull(equipment);
        if (!compartment.available()) throw new IllegalArgumentException("El compartimento debe estar disponible.");
    }

    private static TranspositionResult rejected(MucusWallet wallet, InventoryCompartment compartment, String message) {
        return new TranspositionResult(false, 0, 0, wallet, compartment, message);
    }
}

package domain.social;

import domain.inventory.QuickAccessPolicy;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/** Invariante dura  para patrimonio activo: colocación, slots rápidos, accesorio y transporte. */
public final class CanonicalActiveInventoryEquipmentPolicy {
    private CanonicalActiveInventoryEquipmentPolicy(){}

    public static void validate(CanonicalStartingEquipment e,CanonicalLoadoutPlacementPlan p){
        Objects.requireNonNull(e);Objects.requireNonNull(p);
        p.validateAgainst(e);

        if(e.equippedAccessory().isEmpty())
            throw new IllegalArgumentException(" exige un abalorio narrativo equipado por perfil activo.");
        var a=e.equippedAccessory().orElseThrow();
        var valuation=domain.economy.AccessoryEconomicCatalog.valuation(a.name());
        if(valuation.priceValeritas().isEmpty() || valuation.priceValeritas().getAsLong()<0)
            throw new IllegalArgumentException("Abalorio activo sin precio canónico: "+a.name());
        if(!(a instanceof ArtifactAccessory) && !a.narrativeDescription().trim().startsWith("Yo") && !firstPerson(a.narrativeDescription()))
            throw new IllegalArgumentException("El abalorio  debe narrarse en primera persona: "+a.name());

        // Todo binding rápido debe estar en su fuente física exacta.
        for(var q:p.quickAccessBindings().entrySet()){
            var source=QuickAccessPolicy.sourceCompartment(q.getKey());
            if(!p.contents(source).contains(q.getValue()))
                throw new IllegalArgumentException("Quick "+q.getKey()+" no apunta a una instancia almacenada en "+source);
        }

        // Las linternas mecánicas, si se portan, son siempre Quick 2 / CHEST.
        for(String lamp:List.of("MAGNETLAMPE","KNIJPKAT")) if(e.inventoryObjectNames().contains(lamp)){
            if(!lamp.equals(p.quickAccessBindings().get(2)) || !p.contents(InventoryCompartmentType.CHEST_STORAGE).contains(lamp))
                throw new IllegalArgumentException(lamp+" debe estar en CHEST_STORAGE y Quick 2.");
        }

        // Un contenedor de acceso rápido con fighting load no puede quedar semánticamente huérfano.
        for(int q=1;q<=4;q++){
            var source=QuickAccessPolicy.sourceCompartment(q);
            if(!p.contents(source).isEmpty() && !p.quickAccessBindings().containsKey(q))
                throw new IllegalArgumentException(source+" contiene carga activa pero no alimenta su Quick "+q+".");
        }

        // Los expansores personales siguen siendo ranuras únicas; las alforjas exigen transporte acoplado por constructor.
        if(new HashSet<>(e.inventoryExpanders()).size()!=e.inventoryExpanders().size())
            throw new IllegalArgumentException("Expansor personal duplicado.");
    }

    private static boolean firstPerson(String s){
        String x=" "+s.toLowerCase(Locale.ROOT)+" ";
        return x.contains(" yo ")||x.contains(" me ")||x.contains(" mi ")||x.contains(" conmigo ")||x.contains(" tengo ")||x.contains(" llevo ")||x.contains(" guardo ");
    }
}
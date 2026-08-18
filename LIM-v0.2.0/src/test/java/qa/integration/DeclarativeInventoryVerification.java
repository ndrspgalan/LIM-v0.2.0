package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.combat.ai.declarative.*;
import domain.inventory.*;
import domain.inventory.logistics.*;
import domain.status.TherapeuticEffectTracker;
import java.util.List;

/** Contratos acumulativos . No se ejecuta en el ciclo normal. */
public final class DeclarativeInventoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){ verify(); }
    private DeclarativeInventoryVerification() {}

    public static void verify() {
        var food=MiscellaneousItemCatalog.fruit();
        InventoryCompartment base=new InventoryCompartment(InventoryCompartmentType.LEG_POUCH,true,
                InventoryCompartmentType.LEG_POUCH.grid(),List.of(food),java.util.Optional.empty());
        LogisticsState logistics=LogisticsState.emptyWithoutPersonalTransport().withCompartment(base);
        InventoryState inventory=new InventoryState(domain.inventory.equipment.EquipmentState.empty(),QuickAccessBar.empty(),logistics);
        var resolver=new InventoryActionCandidateResolver();
        var state=InventoryDecisionState.of(inventory,new TherapeuticEffectTracker());
        var actions=resolver.resolve(state);
        if(actions.stream().noneMatch(a->a.action()==InventoryActionType.EQUIP_QUICK_ACCESS))
            throw new AssertionError(": un alimento almacenado debe declarar la operación física de asignarlo a un Quick compatible.");
        if(actions.stream().anyMatch(a->a.action()==InventoryActionType.USE))
            throw new AssertionError(": un alimento que requiere Quick no debe declararse utilizable antes de asignarlo.");
        if(actions.stream().anyMatch(a->a.preconditions().stream().anyMatch(s->s.toLowerCase().contains("prioridad")||s.toLowerCase().contains("score"))))
            throw new AssertionError(" no puede introducir scoring táctico en precondiciones declarativas.");
    }
}

package qa.regression;
import domain.inventory.*;import domain.inventory.logistics.*;import domain.inventory.item.misc.PortableFuelCanItem;import domain.worldmemory.spatial.WorldCoordinate;import java.util.*;
public final class PersonalTransportVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
  WorldCoordinate origin=new WorldCoordinate(0,0,0);PersonalTransportState state=PersonalTransportState.allAcquired(origin);
  org.junit.jupiter.api.Assertions.assertTrue(state.ownedTypes().size()==6,"La rueda debe exponer seis transportes.");
  EnumMap<InventoryCompartmentType,InventoryCompartment> c=new EnumMap<>(InventoryCompartmentType.class);for(InventoryCompartmentType t:InventoryCompartmentType.values())c.put(t,InventoryCompartment.empty(t,t==InventoryCompartmentType.BODY||t==InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN));
  LogisticsState l=new LogisticsState(c,state.select(PersonalTransportType.MOTORCYCLE_CARDAN_V881));
  var near=l.callSelectedPersonalTransport(new WorldCoordinate(0,0,0),7.5);org.junit.jupiter.api.Assertions.assertTrue(near.call().status()==PersonalTransportCallStatus.RESPONDED_AT_PERSISTENT_POSITION,"Dentro de render no debe teletransportarse.");
  PersonalTransportUnitState far=state.unit(PersonalTransportType.MOTORCYCLE_CARDAN_V881).withCoordinate(new WorldCoordinate(1600,0,0));
  LogisticsState lf=new LogisticsState(c,state.update(far).select(PersonalTransportType.MOTORCYCLE_CARDAN_V881));org.junit.jupiter.api.Assertions.assertTrue(lf.callSelectedPersonalTransport(origin,7.5).call().status()==PersonalTransportCallStatus.MATERIALIZED_NEAR_PLAYER,"Fuera de render debe materializarse.");
  org.junit.jupiter.api.Assertions.assertTrue(!l.saddlebagsAccessibleFromInventory(),"Las alforjas no se abren desde inventario.");org.junit.jupiter.api.Assertions.assertTrue(l.saddlebagsAccessibleAt(PersonalTransportType.MOTORCYCLE_CARDAN_V881),"Las alforjas se abren físicamente.");
  MotorcycleFuelState fuel=MotorcycleFuelState.full(MotorcycleFuelType.ETHANOL).consumeDistance(255);org.junit.jupiter.api.Assertions.assertTrue(fuel.normalLiters()==0,"ON agota el depósito normal sin consumir reserva.");
  org.junit.jupiter.api.Assertions.assertTrue(new PortableFuelCanItem(MotorcycleFuelType.ETHANOL).footprint().equals(new InventoryFootprint(3,2)),"Bidón 3x2.");
  org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(InventoryCompartmentType.values()).noneMatch(t->t.name().contains("BOLT")),"Debe desaparecer BOLT_CASE.");
 }
 
}

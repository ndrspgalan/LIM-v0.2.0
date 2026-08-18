package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.QuickAccessBar;
import domain.inventory.QuickAccessConsumptionPolicy;
import domain.status.*;
import domain.survival.*;
import java.util.List;
import java.util.Optional;

/** Verificación histórica sincronizada con el canon terapéutico . */
public final class TherapeuticSystemVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){physicalContracts();healingExecution();duration();meadSurvivalFlags();universalQuickAccessIdentity();}
 private static void physicalContracts(){
  org.junit.jupiter.api.Assertions.assertTrue(close(MiscellaneousItemCatalog.stimulantInjection().weightKg(),0.080),"Peso inyección");
  org.junit.jupiter.api.Assertions.assertTrue(close(MiscellaneousItemCatalog.yarrow().weightKg(),0.080),"Peso milenrama");
  org.junit.jupiter.api.Assertions.assertTrue(!domain.inventory.catalog.PhysicalObjectCatalog.containsName("Parche de llantén"),"Llantén eliminado");
  org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousItemCatalog.bogMoss().maximumUses()==1,"Cada apósito de musgo es una unidad física");
  org.junit.jupiter.api.Assertions.assertTrue(close(MiscellaneousItemCatalog.lucidityEssence().weightKg(),0.040),"Peso esencia");
 }
 private static void healingExecution(){
  var policy=new TherapeuticConsumptionPolicy(); var y=MiscellaneousItemCatalog.yarrow();
  var reduced=new HealthState(60,100,HealthProtection.none(),true,12,false);
  var r=policy.consume(reduced,ActiveTherapeuticEffects.none(),y); org.junit.jupiter.api.Assertions.assertTrue(r.consumed()&&!r.healthState().healthRegenerationReduced(),"Milenrama elimina una inhibición");
  var hit=new HealthState(60,100,HealthProtection.none(),true,18,false); var moss=MiscellaneousItemCatalog.bogMoss();
  var mr=policy.consume(hit,ActiveTherapeuticEffects.none(),moss); org.junit.jupiter.api.Assertions.assertTrue(mr.consumed()&&close(mr.healthState().protection().currentCapacity(),18),"Musgo crea barrera del último golpe");
  var y2=MiscellaneousItemCatalog.yarrow(); var yr=policy.consume(mr.healthState(),ActiveTherapeuticEffects.none(),y2); org.junit.jupiter.api.Assertions.assertTrue(yr.healthState().healthRegenerationReduced()&&yr.healthState().yarrowInhibitionDeferred(),"Milenrama con barrera difiere el fin de inhibición");
 }
 private static void duration(){
  var tracker=new TherapeuticEffectTracker(); tracker.add(MiscellaneousItemCatalog.willowBark().therapeuticEffect()); tracker.add(MiscellaneousItemCatalog.mead().therapeuticEffect()); tracker.add(MiscellaneousItemCatalog.lucidityEssence().therapeuticEffect());
  tracker.advanceGameHours(29.0/60.0);org.junit.jupiter.api.Assertions.assertTrue(tracker.activeCount()==3,"30 min");tracker.advanceGameHours(1.0/60.0);org.junit.jupiter.api.Assertions.assertTrue(tracker.activeCount()==0,"expiran 30 min");
  var inj=new TherapeuticEffectTracker();inj.add(MiscellaneousItemCatalog.stimulantInjection().therapeuticEffect());inj.advanceRealSeconds(5.9);org.junit.jupiter.api.Assertions.assertTrue(inj.activeCount()==1,"inyección 6 s");inj.advanceRealSeconds(.1);org.junit.jupiter.api.Assertions.assertTrue(inj.activeCount()==0,"inyección expira");
 }
 private static void meadSurvivalFlags(){var thirst=new ThirstState();var hunger=HungerState.initiallySatiated();var mead=MiscellaneousItemCatalog.mead();var result=new ConsumptionPolicy().consumeTherapeutic(hunger,thirst,mead);org.junit.jupiter.api.Assertions.assertTrue(result.consumed(),"hidromiel consume");org.junit.jupiter.api.Assertions.assertTrue(!result.hydratedActivated()&&!result.satiatedActivated(),"sin estados positivos");}
 private static void universalQuickAccessIdentity(){org.junit.jupiter.api.Assertions.assertTrue(java.util.Arrays.stream(QuickAccessConsumptionPolicy.class.getDeclaredMethods()).noneMatch(m->java.util.Arrays.asList(m.getParameterTypes()).contains(QuickAccessBar.class))," elimina la API QuickAccessBar aislada; el consumo exige InventoryState.");}
 private static boolean close(double a,double b){return Math.abs(a-b)<.0001;} 
}

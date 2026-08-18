package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.ability.DrainPolicy;
import domain.character.sheet.Attribute;
import domain.combat.DamageType;
import domain.consumable.*;
import domain.environment.time.DayPhase;
import domain.status.*;
import domain.targeting.TargetLockRangePolicy;

public final class ConsumablesAndTargetingVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
  var inj=new StimulantInjectionPolicy(); org.junit.jupiter.api.Assertions.assertTrue(inj.healthRegenTickSeconds(true)==1&&inj.staminaCost(18,true)==0,"Inyección: PV/s y PA cero"); org.junit.jupiter.api.Assertions.assertTrue(StimulantInjectionPolicy.INJECTION_SITE.equals("muslo derecho"),"Muslo derecho");
  var willow=new WillowBarkPolicy(); close(willow.resolve(30,DamageType.POISON,true),20,"Corteza veneno x2/3"); close(willow.resolve(30,DamageType.CURSE,true),30,"Corteza no maldición");
  var lucid=new LucidityEssencePolicy(); close(lucid.immediateRegeneration(4,50),50,"Lucidez completa PA"); org.junit.jupiter.api.Assertions.assertTrue(lucid.mirageInvulnerabilityMultiplier(true,true)==0&&lucid.mirageInvulnerabilityMultiplier(true,false)==1,"Lucidez sólo rival actual");
  var mead=new MeadPolicy(); close(mead.attackStaminaMultiplier(1.3,true),1,"Hidromiel x1"); close(mead.fullStaminaRecoverySeconds(true,5),1,"Hidromiel sin penalizador carga"); close(mead.regenerationDelaySeconds(true,.7),1.2,"Hidromiel latencia"); org.junit.jupiter.api.Assertions.assertTrue(!mead.canTargetLock(true)&&mead.constantSway(true),"Hidromiel lock/sway");
  var lock=new TargetLockRangePolicy(); org.junit.jupiter.api.Assertions.assertTrue(lock.canLock(30,DayPhase.DAY,true)&&!lock.canLock(30.01,DayPhase.DAY,true),"Lock 30m"); org.junit.jupiter.api.Assertions.assertTrue(lock.canLock(6,DayPhase.NIGHT,true)&&!lock.canLock(6.01,DayPhase.NIGHT,true),"Lock noche 6m");
  org.junit.jupiter.api.Assertions.assertTrue(!domain.inventory.catalog.PhysicalObjectCatalog.containsName("Parche de llantén"),"Llantén fuera del catálogo");
  var hp=new HealthState(70,100,HealthProtection.none(),true,17,false); var consume=new TherapeuticConsumptionPolicy(); var moss=consume.consume(hp,ActiveTherapeuticEffects.none(),MiscellaneousItemCatalog.bogMoss()); org.junit.jupiter.api.Assertions.assertTrue(moss.consumed()&&moss.healthState().protection().currentCapacity()==17,"Musgo=último golpe"); var moss2=consume.consume(moss.healthState(),ActiveTherapeuticEffects.none(),MiscellaneousItemCatalog.bogMoss()); org.junit.jupiter.api.Assertions.assertTrue(!moss2.consumed(),"No segundo musgo"); var y=consume.consume(moss.healthState(),ActiveTherapeuticEffects.none(),MiscellaneousItemCatalog.yarrow()); org.junit.jupiter.api.Assertions.assertTrue(y.healthState().healthRegenerationReduced()&&y.healthState().yarrowInhibitionDeferred(),"Milenrama difiere inhibición con barrera"); var gone=y.healthState().registerHit(17); org.junit.jupiter.api.Assertions.assertTrue(!gone.protection().active()&&!gone.healthRegenerationReduced(),"Al caer barrera termina inhibición diferida");
  org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousItemCatalog.waterskin().narrativeDescription().contains("cobre y latón"),"Odre cobre/latón");
  var irnd=new IrndPolicy(); org.junit.jupiter.api.Assertions.assertTrue(irnd.phase(0)==IrndPolicy.Phase.BENEFIT&&irnd.effectiveAttribute(Attribute.CARISMA,10,5)==75&&irnd.effectiveAttribute(Attribute.DESTREZA,60,5)==60,"I-RND primera fase sólo bonus"); org.junit.jupiter.api.Assertions.assertTrue(!irnd.canSleep(5)&&!irnd.canSleep(35),"Insomnio ambas fases"); org.junit.jupiter.api.Assertions.assertTrue(irnd.phase(35)==IrndPolicy.Phase.AFTEREFFECT&&irnd.effectiveAttribute(Attribute.CARISMA,10,35)==10&&irnd.effectiveAttribute(Attribute.DESTREZA,60,35)==20,"I-RND segunda fase sólo penalizadores"); close(irnd.fullStaminaRecoverySeconds(35,1),5,"I-RND tres tercios"); close(irnd.staminaRegenDelaySeconds(35,.5),1.2,"I-RND latencia"); org.junit.jupiter.api.Assertions.assertTrue(!irnd.canClimb(35)&&!irnd.canSwim(35)&&!irnd.canUsePersonalTransport(35)&&irnd.vitalityForHealthRegeneration(70,35)==1,"I-RND secuelas");
  var res=new domain.status.VitalResourceState(10,100,20,20); var d=DrainPolicy.onCharacterDeath(res,20,60,false,true,false); org.junit.jupiter.api.Assertions.assertTrue(d.restoredHealth()==0,"DRENAR no activa sin killing blow melee"); d=DrainPolicy.onCharacterDeath(res,20,60,false,true,true); org.junit.jupiter.api.Assertions.assertTrue(d.restoredHealth()>0,"DRENAR activa con killing blow melee");
  org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousItemCatalog.irndFlask().footprint().equals(MiscellaneousItemCatalog.lucidityEssence().footprint()),"I-RND mismo tamaño que Lucidez");
 }
 static void close(double a,double b,String m){if(Math.abs(a-b)>1e-9)throw new AssertionError(m+" "+a+" != "+b);} 
}

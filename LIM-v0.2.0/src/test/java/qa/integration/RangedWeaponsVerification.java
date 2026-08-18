package qa.integration;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.rangedWeapons.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.misc.MiscellaneousItemCatalog;
import java.util.*;

public final class RangedWeaponsVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
  var all=RangedWeaponCatalog.all();org.junit.jupiter.api.Assertions.assertTrue(all.size()==3," debe dejar tres armas.");
  var sling=all.stream().filter(w->w.type()==RangedWeaponType.SLING).findFirst().orElseThrow();
  var recurve=all.stream().filter(w->w.type()==RangedWeaponType.SIMPLE_RECURVE_BOW).findFirst().orElseThrow();
  var composite=all.stream().filter(w->w.type()==RangedWeaponType.COMPOSITE_BOW).findFirst().orElseThrow();
  org.junit.jupiter.api.Assertions.assertTrue(sling.footprint().equals(new InventoryFootprint(1,2))&&sling.weightKg()==0.18&&sling.currentEffectiveRangeMeters()==65,"Honda canónica.");
  org.junit.jupiter.api.Assertions.assertTrue(recurve.footprint().equals(new InventoryFootprint(2,12))&&recurve.strengthRequirement()==6&&recurve.dexterityRequirement()==12,"Recurvo canónico.");
  org.junit.jupiter.api.Assertions.assertTrue(composite.currentEffectiveRangeMeters()==180&&composite.recoverySeconds()==1,"Compuesto canónico.");
  for(int i=0;i<1000;i++){recurve.registerUse();composite.registerUse();}
  org.junit.jupiter.api.Assertions.assertTrue(Math.abs(recurve.currentEffectiveRangeMeters()-102)<0.0001,"Recurvo se detiene al 85%.");
  org.junit.jupiter.api.Assertions.assertTrue(Math.abs(composite.currentEffectiveRangeMeters()-153)<0.0001,"Compuesto se detiene al 85%.");
  org.junit.jupiter.api.Assertions.assertTrue(recurve.repairWithResin()&&recurve.currentEffectiveRangeMeters()==120,"Resina restaura.");
  org.junit.jupiter.api.Assertions.assertTrue(sling.lethalityFor(AmmunitionCatalog.pebble().ammunitionDescriptor()).blunt()==35,"Guijarro Ct35.");
  org.junit.jupiter.api.Assertions.assertTrue(sling.lethalityFor(AmmunitionCatalog.pneumaticLead46Cartridge().ammunitionDescriptor()).blunt()==60,"Plomo .46 Ct60.");
  org.junit.jupiter.api.Assertions.assertTrue(composite.lethalityFor(ArrowVariant.PIERCING.descriptor()).piercing()==70,"Compuesto +10P.");
  org.junit.jupiter.api.Assertions.assertTrue(composite.lethalityFor(ArrowVariant.BARBED.descriptor()).slashing()==70,"Compuesto +10C.");
  var tinder=AmmunitionCatalog.tinderArrow();org.junit.jupiter.api.Assertions.assertTrue(tinder.activeDescriptor().variant().equals(ArrowVariant.TINDER_UNLIT.name()),"Yesca apagada.");
  var amadou=MiscellaneousItemCatalog.amadou();
  var flint=MiscellaneousItemCatalog.flint();
  org.junit.jupiter.api.Assertions.assertTrue(tinder.ignite(amadou,flint)&&tinder.activeDescriptor().variant().equals(ArrowVariant.TINDER_LIT.name()),"Yesca encendida.");
  org.junit.jupiter.api.Assertions.assertTrue(composite.burnFor(tinder.activeDescriptor())==100,"Quemadura 100.");
  org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(AmmunitionFamily.values()).noneMatch(v->v.name().equals("BOLT")),"BOLT eliminado.");
  org.junit.jupiter.api.Assertions.assertTrue(sling.hasProperty(ItemPropertyId.COPILOT)&&sling.hasProperty(ItemPropertyId.EQUESTRIAN)&&sling.hasProperty(ItemPropertyId.BICYCLAR)&&sling.hasProperty(ItemPropertyId.MOTORCYCLAR),"Honda compatible con transporte.");
  org.junit.jupiter.api.Assertions.assertTrue(recurve.hasProperty(ItemPropertyId.COPILOT)&&recurve.hasProperty(ItemPropertyId.EQUESTRIAN)&&!recurve.hasProperty(ItemPropertyId.BICYCLAR),"Arcos solo copiloto y ecuestre.");
 }
 
}

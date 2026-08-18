package qa.domain;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.hud.EngineerSpineProjectionService;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.movement.*;
import domain.inventory.item.aeronautics.DisposableGliderItem;
import java.util.Map;

/** migración y saneamiento de armaduras pesadas. */
public final class HeavyArmorCanonicalVerification {
    private HeavyArmorCanonicalVerification() {}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        materialTaxonomy(); engineer(); layering(); historicalHeavy(); v881Heavy(); headgear(); glider();
    }

    static void materialTaxonomy() {
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE.materialClass()==ArmorMaterialClass.HEAVY,"Compuesto electromecánico HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.PAPER.materialClass()==ArmorMaterialClass.MEDIUM,"Papel MEDIUM");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterialClass.values().length==3,"Sólo LIGHT/MEDIUM/HEAVY");
    }

    static void engineer() {
        ArmorPiece suit=ArmorCatalog.engineerSuit();
        close(suit.weightKg(),25,"Ingeniero 25 kg");
        close(suit.headSupportedWeightKg(),3.5,"Ingeniero masa cervical 3,5");
        org.junit.jupiter.api.Assertions.assertTrue(suit.materialClass()==ArmorMaterialClass.HEAVY,"Ingeniero HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(suit.hasProperty(ItemPropertyId.BIOMECHANICAL_RIGIDITY),"Rigidez biomecánica");
        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.CHEST,suit));
        CharacterSheet sheet=CharacterSheet.of(27,40,12,30,75,30,3,25,11);
        org.junit.jupiter.api.Assertions.assertTrue(eq.effectiveAttributeValue(Attribute.DESTREZA,sheet)==20,"DEX cap 20");
        TerrainSurface flat=new TerrainSurface(0,true);
        LocomotionProfile locomotion=new LocomotionPolicy().resolve(flat,sheet,eq);
        org.junit.jupiter.api.Assertions.assertTrue(locomotion.allows(LocomotionMode.WALKING),"Ingeniero puede caminar");
        org.junit.jupiter.api.Assertions.assertTrue(locomotion.allows(LocomotionMode.TROTTING)&&locomotion.allows(LocomotionMode.RUNNING),"Ingeniero permite trotar/correr");
        org.junit.jupiter.api.Assertions.assertTrue(!locomotion.allows(LocomotionMode.CROUCH_WALKING)&&!locomotion.allows(LocomotionMode.CRAWLING)&&!locomotion.allows(LocomotionMode.CLIMBING),"Sin agacharse/gatear/escalar");
        org.junit.jupiter.api.Assertions.assertTrue(!new ArmorMobilityRestrictionPolicy().allowsSwimming(eq),"Sin nadar");
        org.junit.jupiter.api.Assertions.assertTrue(!new ArmorMobilityRestrictionPolicy().allowsSliding(eq),"Sin deslizarse");
        org.junit.jupiter.api.Assertions.assertTrue(new EngineerSpineProjectionService().project(eq).visible(),"HUD por capacidad, no nombre obsoleto");
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,suit);
        close(layout.headWeightKg(),3.5,"Layout cuenta masa cervical integral");
        expectFail(()->layout.equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.laborerHatV881()));
    }

    static void layering() {
        ArmorEquipmentLayout legs=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.MIDDLE,ArmorCatalog.middleWorkTrousersV881())
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,ArmorCatalog.historicalKnightLeggings());
        org.junit.jupiter.api.Assertions.assertTrue(legs.piecesAt(EquipmentSlot.LEGGINGS).size()==2,"LEGS: tela inferior + heavy superior");
        expectFail(()->ArmorEquipmentLayout.empty().equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.INNER,ArmorCatalog.historicalKnightLeggings()));
        expectFail(()->ArmorEquipmentLayout.empty().equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,ArmorCatalog.middleWorkTrousersV881()));
    }

    static void historicalHeavy() {
        ArmorPiece kc=ArmorCatalog.historicalKnightChest(), kb=ArmorCatalog.historicalKnightBracers(), kl=ArmorCatalog.historicalKnightLeggings(), kh=ArmorCatalog.historicalKnightHelmet();
        close(kc.weightKg()+kb.weightKg()+kl.weightKg()+kh.weightKg(),25,"Caballero histórico total");
        close(kh.weightKg(),2.5,"Casco histórico 2,5");
        close(kc.bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.50,"Knight chest50");
        close(kb.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.15,"Knight arms15");
        close(kl.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.30,"Knight legs30");
        close(kl.bodyRegionCoverageRatio(BodyArmorRegion.FEET),.05,"Knight feet5");

        ArmorPiece ec=ArmorCatalog.historicalEbonyWarriorChest(), eb=ArmorCatalog.historicalEbonyWarriorBracers(), el=ArmorCatalog.historicalEbonyWarriorLeggings();
        close(ec.bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.50,"Ebony chest50");
        close(eb.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.05,"Ebony arms5");
        close(el.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"Ebony legs12");
        org.junit.jupiter.api.Assertions.assertTrue(ec.statistics().stream().anyMatch(x->x.contains("ÉBANO x15")),"Ébano histórico x15");

        ArmorPiece lc=ArmorCatalog.historicalHeavyLamellarChest(), lb=ArmorCatalog.historicalHeavyLamellarBracers(), ll=ArmorCatalog.historicalHeavyLamellarLeggings();
        close(lc.weightKg()+lb.weightKg()+ll.weightKg(),30,"Lamelar 30 kg");
        org.junit.jupiter.api.Assertions.assertTrue(lc.protection().equals(new ArmorProtectionProfile(95,95,50)),"Lamelar P95/C95/B50");
        close(lc.bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.50,"Lamellar chest50");
        close(lb.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.05,"Lamellar arms5");
        close(ll.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"Lamellar legs12");
    }

    static void v881Heavy() {
        ArmorPiece c=ArmorCatalog.knightV881Chest(), b=ArmorCatalog.knightV881Bracers(), l=ArmorCatalog.knightV881Leggings();
        close(c.weightKg()+b.weightKg()+l.weightKg(),25,"Caballero V881 total 25");
        close(b.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.10,"Caballero V881 brazos10");
        close(l.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"Caballero V881 piernas12");
        org.junit.jupiter.api.Assertions.assertTrue(!c.hasProperty(ItemPropertyId.GROUNDING)&&!b.hasProperty(ItemPropertyId.GROUNDING)&&!l.hasProperty(ItemPropertyId.GROUNDING),"Caballero V881 ya no incorpora TOMA A TIERRA");
        org.junit.jupiter.api.Assertions.assertTrue(c.hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR)&&b.hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR)&&l.hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR),"Caballero V881 conductor sin FEET adecuado");
        ArmorPiece ebony=ArmorCatalog.ebonyWarriorV881LeftBracer();
        close(ebony.weightKg(),EbonyArmorMassPolicy.v881LeftBracerMassKg(),"Brazal V881 masa derivada");
        org.junit.jupiter.api.Assertions.assertTrue(ebony.weightKg()>2.0,"Brazal V881 ya no usa 1,294 kg");
    }

    static void headgear() {
        ArmorPiece aero=ArmorCatalog.retractableAeronautHelmet();
        close(aero.weightKg(),3.5,"Aeronauta 3,5");
        org.junit.jupiter.api.Assertions.assertTrue(!aero.hasProperty(ItemPropertyId.GROUNDING),"Aeronauta depende de TOMA A TIERRA externa");
        org.junit.jupiter.api.Assertions.assertTrue(!aero.containsMaterial(ArmorMaterial.TUNGSTEN)&&!aero.containsMaterial(ArmorMaterial.TUNGSTEN_PLATES_2_5_MM),"Aeronauta sin wolframio");
        ArmorPiece pano=ArmorCatalog.enlightenedPanopticon();
        close(pano.weightKg(),3.5,"Panóptico 3,5");
        org.junit.jupiter.api.Assertions.assertTrue(pano.materials().equals(java.util.Set.of(ArmorMaterial.LAMINATED_GLASS)),"Acero del Panóptico sólo accesorio");
    }

    static void glider(){ DisposableGliderItem g=new DisposableGliderItem(); close(g.weightKg(),9,"Planeador conserva 9 kg"); org.junit.jupiter.api.Assertions.assertTrue(g.footprint().verticalSlots()==7&&g.footprint().horizontalSlots()==4,"Planeador 7x4"); }
    static void close(double a,double b,String m){if(Math.abs(a-b)>1e-6)throw new IllegalStateException(m+": "+a+" != "+b);}  static void expectFail(Runnable r){try{r.run();throw new IllegalStateException("Debía fallar");}catch(IllegalArgumentException expected){}}
}

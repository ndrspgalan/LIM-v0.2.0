package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import java.util.Map;

/**  — FEET INNER/OUTER, grounding, ergonomía y calzado integrado. */
public final class FeetFashionVerification {
    private FeetFashionVerification() {}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allInnerFeetGarments().size()==7,"7 prendas INNER FEET");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allOuterFeetGarments().size()==11,"11 prendas OUTER FEET");
        ArmorCatalog.allInnerFeetGarments().forEach(p->{
            org.junit.jupiter.api.Assertions.assertTrue(p.materialClass()==ArmorMaterialClass.LIGHT,"INNER FEET LIGHT: "+p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.feetLayer().orElseThrow()==FeetLayer.INNER,"INNER declarado: "+p.name());
            close(p.bodyRegionCoverageRatio(BodyArmorRegion.FEET),.05,"FEET5 inner");
            close(p.effectiveWeightKg(true),p.weightKg()*3.0,"SOAKED x3 inner");
        });
        ArmorCatalog.allOuterFeetGarments().forEach(p->{
            org.junit.jupiter.api.Assertions.assertTrue(p.feetLayer().orElseThrow()==FeetLayer.OUTER,"OUTER declarado: "+p.name());
            close(p.bodyRegionCoverageRatio(BodyArmorRegion.FEET),.05,"FEET5 outer");
        });

        var layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.FEET,ArmorLayerPosition.INNER,ArmorCatalog.innerFeetSocksV881())
                .equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,ArmorCatalog.leatherOxfordBrogueShoesV881());
        org.junit.jupiter.api.Assertions.assertTrue(layout.piecesAt(EquipmentSlot.FEET).size()==2,"INNER+OUTER FEET coexistentes");
        close(layout.effectiveCoverage(BodyArmorRegion.FEET),.05,"INNER+OUTER no crean 10% anatómico");
        fail(()->layout.equip(EquipmentSlot.FEET,ArmorLayerPosition.INNER,ArmorCatalog.innerFeetStockingsV881()),"segundo INNER");
        fail(()->layout.equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,ArmorCatalog.outerMoccasinsV881()),"segundo OUTER");

        var historical=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,ArmorCatalog.historicalKnightLeggings())
                .equip(EquipmentSlot.FEET,ArmorLayerPosition.INNER,ArmorCatalog.innerFeetHeavyWorkSocksV881());
        fail(()->historical.equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,ArmorCatalog.leatherOxfordBrogueShoesV881()),"sabatón integrado ocupa sólo OUTER");

        EquipmentState barefoot=EquipmentState.empty();
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.groundedByFeet(barefoot),"descalzo conecta a tierra");
        EquipmentState socksOnly=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.innerFeetSocksV881()));
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.groundedByFeet(socksOnly),"INNER textil mantiene toma a tierra");
        EquipmentState oxford=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.leatherOxfordBrogueShoesV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.groundedByFeet(oxford) && GroundingPolicy.insulatedByFeet(oxford),"Oxford con caucho debe aislar la ruta al terreno");
        EquipmentState heavyBoots=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.groundedByFeet(heavyBoots) && GroundingPolicy.insulatedByFeet(heavyBoots),"bota industrial aísla por suela de caucho, no por HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.leatherHeavyWorkBootsV881().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR),"bota HEAVY no es conductora");

        ArmorPiece aero=ArmorCatalog.retractableAeronautHelmet();
        EquipmentState aeroSocks=new EquipmentState(Map.of(EquipmentSlot.HEAD,aero,EquipmentSlot.FEET,ArmorCatalog.innerFeetSocksV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!aeroSocks.terrestrialIntercomAvailable(),"INNER FEET no habilita intercom");
        EquipmentState aeroMoccasins=new EquipmentState(Map.of(EquipmentSlot.HEAD,aero,EquipmentSlot.FEET,ArmorCatalog.outerMoccasinsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(aeroMoccasins.terrestrialIntercomAvailable(),"OUTER sin suela dieléctrica + acoplamiento a tierra habilita intercom");
        EquipmentState aeroHeavy=new EquipmentState(Map.of(EquipmentSlot.HEAD,aero,EquipmentSlot.FEET,ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!aeroHeavy.terrestrialIntercomAvailable(),"OUTER HEAVY sin grounding no habilita intercom");

        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"bota alta invade LEGGINGS12");
        double mediumPa=ArmorErgonomicsPolicy.lowerLimbStaminaMultiplier(java.util.List.of(ArmorCatalog.leatherOxfordBrogueShoesV881()));
        org.junit.jupiter.api.Assertions.assertTrue(mediumPa>1.0,"OUTER FEET MEDIUM participa en PA");
        close(ArmorErgonomicsPolicy.lowerLimbStaminaMultiplier(java.util.List.of(ArmorCatalog.innerFeetHeavyWorkSocksV881())),1.0,"INNER FEET LIGHT no penaliza PA");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorErgonomicsPolicy.eligibleForEquippedWeightBonus(BodyArmorRegion.FEET),"FEET sin bonificación logística");

        close(ArmorCatalog.outerEspadrillesV881().effectiveWeightKg(true),ArmorCatalog.outerEspadrillesV881().weightKg()*3.0,"alpargata SOAKED x3");
        close(ArmorCatalog.outerLeatherAnkleBootsV881().effectiveWeightKg(true),ArmorCatalog.outerLeatherAnkleBootsV881().weightKg()*1.2,"botín cuero SOAKED x1.2");
        close(ArmorCatalog.leatherHeavyWorkBootsV881().effectiveWeightKg(true),ArmorCatalog.leatherHeavyWorkBootsV881().weightKg(),"bota HEAVY sin multiplicador genérico");
    }
    
    private static void close(double a,double b,String m){if(Math.abs(a-b)>1e-8)throw new IllegalStateException(m+" "+a+" != "+b);}
    private static void fail(Runnable r,String m){try{r.run();throw new IllegalStateException("Debía fallar: "+m);}catch(IllegalArgumentException expected){}}
}

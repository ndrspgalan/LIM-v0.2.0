package qa.architecture;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;

import java.nio.file.*;
import java.util.*;

public final class LeggingsXyzAndIntegratedSabatonVerification {
    private LeggingsXyzAndIntegratedSabatonVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var inner=ArmorCatalog.allInnerLeggingsGarments();
        var middle=ArmorCatalog.allMiddleLeggingsGarments();
        var outer=ArmorCatalog.allOuterLeggings();

        org.junit.jupiter.api.Assertions.assertTrue(inner.size()==9,"INNER LEGGINGS debe conservar 9 entradas funcionales.");
        org.junit.jupiter.api.Assertions.assertTrue(middle.size()==20,"MIDDLE LEGGINGS debe conservar 20 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(outer.size()==12,"OUTER LEGGINGS debe conservar 12 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.innerLeggingsProfileCount()==8,
                "Sólo las 8 INNER independientes deben declarar XYZ propio.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.middleLeggingsProfileCount()==20,"Faltan XYZ MIDDLE.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.outerLeggingsProfileCount()==12,"Faltan XYZ OUTER.");

        for(ArmorPiece p:inner){
            if(p.name().equals("Combinación interior V881")) continue;
            var d=ArmorPhysicalDimensionsCatalog.innerLeggingsDimensionsFor(p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.footprint().equals(InventoryVolumeProjectionPolicy.footprint(d)),
                    "INNER LEGGINGS debe derivar de XYZ: "+p.name());
        }
        for(ArmorPiece p:middle){
            var d=ArmorPhysicalDimensionsCatalog.middleLeggingsDimensionsFor(p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.footprint().equals(InventoryVolumeProjectionPolicy.footprint(d)),
                    "MIDDLE LEGGINGS debe derivar de XYZ: "+p.name());
        }
        for(ArmorPiece p:outer){
            var d=ArmorPhysicalDimensionsCatalog.outerLeggingsDimensionsFor(p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.footprint().equals(InventoryVolumeProjectionPolicy.footprint(d)),
                    "OUTER LEGGINGS debe derivar de XYZ: "+p.name());
        }

        // La Combinación interior es un solo objeto físico ya dimensionado por INNER CHEST.
        ArmorPiece combination=ArmorCatalog.innerCombinationV881();
        org.junit.jupiter.api.Assertions.assertTrue(combination.footprint().equals(ArmorPhysicalDimensionsCatalog.innerChestFootprintFor(combination.name())),
                "Combinación interior debe reutilizar su único contrato XYZ de INNER CHEST.");
        boolean duplicateRejected=false;
        try { ArmorPhysicalDimensionsCatalog.innerLeggingsDimensionsFor(combination.name()); }
        catch(IllegalArgumentException expected){ duplicateRejected=true; }
        org.junit.jupiter.api.Assertions.assertTrue(duplicateRejected,"No debe existir un segundo contrato XYZ para la Combinación interior.");

        fp(ArmorCatalog.innerLongDrawersV881(),2,2);
        fp(ArmorCatalog.innerKneeDrawersV881(),2,2);
        fp(ArmorCatalog.innerKnittedTrousersV881(),2,2);
        fp(ArmorCatalog.innerWomensDrawersV881(),2,2);
        fp(ArmorCatalog.innerPetticoatV881(),3,2);
        fp(ArmorCatalog.innerReinforcedPetticoatV881(),3,2);
        fp(ArmorCatalog.innerPaddedPetticoatV881(),6,6);
        fp(ArmorCatalog.innerDividedPetticoatV881(),3,2);

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.innerPaddedPetticoatV881().statistics().stream()
                .anyMatch(s->s.contains("5 capas efectivas")),
                "Enagua acolchada debe formalizar cinco capas efectivas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleKiltV881().protection().equals(new ArmorProtectionProfile(3,8,3)),
                "Kilt conserva 3/8/3.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleKiltV881().statistics().stream()
                .anyMatch(s->s.contains("PLISADA") && s.contains("SOLAPE")),
                "Kilt debe declarar construcción plisada/solapada.");

        fp(ArmorCatalog.hardenedLeatherLeggings(),8,6);
        fp(ArmorCatalog.leatherStrapBuckleGaitersV881(),12,4);
        fp(ArmorCatalog.leatherRigidSideClosureGaitersV881(),12,4);
        fp(ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),12,4);
        fp(ArmorCatalog.leatherShotgunChapsV881(),14,6);
        fp(ArmorCatalog.leatherBatwingChapsV881(),14,8);
        fp(ArmorCatalog.leatherCharroChapsV881(),16,8);
        fp(ArmorCatalog.paperLeggingsV881(),12,6);
        fp(ArmorCatalog.historicalEbonyWarriorLeggings(),14,6);
        fp(ArmorCatalog.historicalKnightLeggings(),24,12);
        fp(ArmorCatalog.knightV881Leggings(),14,6);
        fp(ArmorCatalog.historicalHeavyLamellarLeggings(),14,6);

        // El volumen representa pares/conjuntos completos; el peso canónico no se duplica.
        close(ArmorCatalog.leatherStrapBuckleGaitersV881().weightKg(),.800,"Peso par polainas correas");
        close(ArmorCatalog.historicalKnightLeggings().weightKg(),7.875,"Peso conjunto Caballero + sabatones");
        close(ArmorCatalog.historicalHeavyLamellarLeggings().weightKg(),5.373,"Peso par lamelar");

        ArmorPiece paper=ArmorCatalog.paperLeggingsV881();
        org.junit.jupiter.api.Assertions.assertTrue(paper.protection().equals(new ArmorProtectionProfile(66,78,26)),
                "Polainas de Papel conservan 66/78/26.");
        org.junit.jupiter.api.Assertions.assertTrue(paper.statistics().stream().anyMatch(s->s.contains("Propiedad emergente")),
                "Polainas de Papel deben explicar el paquete multicapa.");

        ArmorPiece knightLegs=ArmorCatalog.historicalKnightLeggings();
        org.junit.jupiter.api.Assertions.assertTrue(knightLegs.hasActiveProperty(domain.inventory.item.ItemPropertyId.INTEGRATED_FOOTWEAR),
                "Los sabatones históricos deben ocupar OUTER FEET.");
        EquipmentState knightState=new EquipmentState(Map.of(EquipmentSlot.LEGGINGS,knightLegs));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(knightState)==FeetElectricalContact.INTEGRATED_CONDUCTIVE,
                "Sabatones de acero integrados deben ser conductores/acoplados, no aislantes.");
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.groundedByFeet(knightState),
                "Los sabatones conductores establecen contacto con terreno.");

        EquipmentState engineer=new EquipmentState(Map.of(EquipmentSlot.CHEST,ArmorCatalog.engineerSuit()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(engineer)==FeetElectricalContact.INTEGRATED_ISOLATED,
                "El Conjunto del Ingeniero debe conservar calzado integrado aislante por sellado.");

        // La ocupación anatómica integrada bloquea calzado OUTER independiente en ambos sentidos.
        ArmorEquipPolicy equipPolicy=new ArmorEquipPolicy();
        var knightLayer=new EquippedArmorLayer(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,knightLegs);
        var shoeLayer=new EquippedArmorLayer(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,ArmorCatalog.outerMoccasinsV881());
        boolean blockedShoe=false;
        try { equipPolicy.validate(List.of(knightLayer),shoeLayer); }
        catch(IllegalArgumentException expected){ blockedShoe=true; }
        org.junit.jupiter.api.Assertions.assertTrue(blockedShoe,"Sabatones integrados deben bloquear OUTER FEET independiente.");

        boolean blockedKnight=false;
        try { equipPolicy.validate(List.of(shoeLayer),knightLayer); }
        catch(IllegalArgumentException expected){ blockedKnight=true; }
        org.junit.jupiter.api.Assertions.assertTrue(blockedKnight,"OUTER FEET existente debe bloquear polainas con sabatones integrados.");

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        int innerStart=source.indexOf("// ----------------  · INNER LEGGINGS ----------------");
        int middleEnd=source.indexOf("// ----------------  · MIDDLE CHEST ----------------");
        org.junit.jupiter.api.Assertions.assertTrue(!source.substring(innerStart,middleEnd).contains("new InventoryFootprint("),
                "INNER/MIDDLE LEGGINGS no deben contener footprints manuales.");
    }

    private static void fp(ArmorPiece p,int v,int h){
        org.junit.jupiter.api.Assertions.assertTrue(p.footprint().verticalSlots()==v && p.footprint().horizontalSlots()==h,
                p.name()+" footprint inesperado: "+p.footprint());
    }
    private static void close(double a,double b,String m){
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(a-b)<1e-9,m+": "+a);
    }
    
}

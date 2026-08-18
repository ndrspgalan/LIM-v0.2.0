package qa.architecture;

import domain.inventory.item.armor.*;
import domain.inventory.logistics.ArmorPhysicalDimensionsCatalog;
import domain.inventory.logistics.InventoryVolumeProjectionPolicy;

import java.nio.file.Files;
import java.nio.file.Path;

public final class MiddleChestXyzAndTextileAssemblyVerification {
    private MiddleChestXyzAndTextileAssemblyVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var garments=ArmorCatalog.allMiddleChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(garments.size()==13,"MIDDLE CHEST debe conservar 13 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.middleChestProfileCount()==13,
                "Cada pieza MIDDLE CHEST debe declarar XYZ.");

        for(ArmorPiece piece:garments){
            var dims=ArmorPhysicalDimensionsCatalog.middleChestDimensionsFor(piece.name());
            var expected=InventoryVolumeProjectionPolicy.footprint(dims);
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(expected),
                    "Footprint MIDDLE CHEST debe derivarse de XYZ: "+piece.name());
        }

        assertFootprint(ArmorCatalog.middleWaistcoat(),3,2);
        assertFootprint(ArmorCatalog.middleLongWaistcoat(),3,2);
        assertFootprint(ArmorCatalog.middleWorkWaistcoat(),3,2);
        assertFootprint(ArmorCatalog.middlePaddedWaistcoat(),3,3);
        assertFootprint(ArmorCatalog.middleRidingWaistcoat(),3,2);
        assertFootprint(ArmorCatalog.middleBodice(),3,3);
        assertFootprint(ArmorCatalog.middleRegionalBodice(),3,3);
        assertFootprint(ArmorCatalog.middleSpencer(),3,3);
        assertFootprint(ArmorCatalog.middlePaddedJacket(),8,6);
        assertFootprint(ArmorCatalog.middleDoubletV881(),3,3);
        assertFootprint(ArmorCatalog.middleCardiganV881(),3,2);
        assertFootprint(ArmorCatalog.middleKnittedJerseyV881(),3,2);
        assertFootprint(ArmorCatalog.middleGambesonV881(),8,6);

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleBodice().protection().equals(new ArmorProtectionProfile(4,10,4)),
                "Corpiño ordinario debe corresponder a TELA x2 sin B artificial.");
        ArmorPiece regional=ArmorCatalog.middleRegionalBodice();
        org.junit.jupiter.api.Assertions.assertTrue(regional.protection().equals(new ArmorProtectionProfile(3,8,4)),
                "Corpiño regional conserva 3/8/4.");
        org.junit.jupiter.api.Assertions.assertTrue(regional.statistics().stream().anyMatch(s->s.contains("REFUERZOS TEXTILES PARCIALES")),
                "Corpiño regional debe explicar el segundo estrato parcial.");

        TextileAssemblyProfile padded5=TextileAssemblyProfile.padded(5);
        org.junit.jupiter.api.Assertions.assertTrue(padded5.protection().equals(new ArmorProtectionProfile(10,25,10)),
                "5 capas acolchadas deben producir 10/25/10.");
        TextileAssemblyProfile padded6=TextileAssemblyProfile.padded(6);
        org.junit.jupiter.api.Assertions.assertTrue(padded6.protection().equals(new ArmorProtectionProfile(12,30,12)),
                "6 capas acolchadas deben producir 12/30/12.");
        TextileAssemblyProfile padded18=TextileAssemblyProfile.padded(18);
        org.junit.jupiter.api.Assertions.assertTrue(padded18.protection().equals(new ArmorProtectionProfile(36,90,36)),
                "18 capas acolchadas deben producir 36/90/36.");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middlePaddedWaistcoat().statistics().stream().anyMatch(s->s.contains("5 capas efectivas")),
                "Chaleco acolchado debe declarar 5 capas efectivas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middlePaddedJacket().statistics().stream().anyMatch(s->s.contains("6 capas efectivas")),
                "Chaqueta acolchada debe declarar 6 capas efectivas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleGambesonV881().statistics().stream().anyMatch(s->s.contains("18 capas efectivas")),
                "Gambesón debe declarar 18 capas efectivas.");

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        int start=source.indexOf("// ----------------  · MIDDLE CHEST ----------------");
        int end=source.indexOf("// ---  | OUTER CHEST");
        String block=source.substring(start,end);
        org.junit.jupiter.api.Assertions.assertTrue(!block.contains("new InventoryFootprint("),
                "MIDDLE CHEST no debe contener footprints 2D manuales.");
    }

    private static void assertFootprint(ArmorPiece piece,int vertical,int horizontal){
        org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().verticalSlots()==vertical && piece.footprint().horizontalSlots()==horizontal,
                piece.name()+" footprint inesperado: "+piece.footprint());
    }

    
}

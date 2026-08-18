package qa.architecture;

import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;
import java.nio.file.*;
import java.util.*;

public final class OuterChestXyzVerification {
    private OuterChestXyzVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        List<ArmorPiece> pieces=ArmorCatalog.allOuterChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(pieces.size()==20,"OUTER CHEST debe conservar 20 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.outerChestProfileCount()==20,
                "Cada pieza OUTER CHEST debe declarar XYZ.");

        for(ArmorPiece piece:pieces){
            var dims=ArmorPhysicalDimensionsCatalog.outerChestDimensionsFor(piece.name());
            var expected=InventoryVolumeProjectionPolicy.footprint(dims);
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(expected),"Footprint debe derivarse de XYZ: "+piece.name());
        }

        profile(ArmorCatalog.outerFrockCoatV881(),6,15,6);
        profile(ArmorCatalog.outerTailcoatV881(),4,10,4);
        profile(ArmorCatalog.outerMorningCoatV881(),4,10,4);
        profile(ArmorCatalog.outerSackCoatV881(),4,10,4);
        profile(ArmorCatalog.outerNorfolkV881(),6,15,6);
        profile(ArmorCatalog.outerWorkSmockV881(),3,7,3);
        profile(ArmorCatalog.outerGreatcoatV881(),10,25,10);
        profile(ArmorCatalog.outerOvercoatV881(),8,20,8);
        profile(ArmorCatalog.outerUlsterV881(),12,30,12);
        profile(ArmorCatalog.outerDusterV881(),2,5,2);
        profile(ArmorCatalog.outerTrenchV881(),6,15,6);
        profile(ArmorCatalog.outerRidingJacketV881(),4,10,4);
        profile(ArmorCatalog.outerBoleroV881(),2,5,2);
        profile(ArmorCatalog.outerKnightCloak(),6,15,6);
        profile(ArmorCatalog.outerTravelerCloak(),2,5,2);
        profile(ArmorCatalog.outerInvernessV881(),8,20,8);
        profile(ArmorCatalog.outerPonchoV881(),2,5,2);
        profile(ArmorCatalog.outerBurnousV881(),4,10,4);
        profile(ArmorCatalog.outerDolmanV881(),6,15,6);
        profile(ArmorCatalog.outerManteletV881(),4,10,4);

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerWorkSmockV881().statistics().stream()
                .anyMatch(s->s.contains("PAÑO DENSO x1")),
                "Blusón debe declarar paño denso, no TELA x1 ordinaria.");

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        int start=source.indexOf("// ---  | OUTER CHEST");
        int end=source.indexOf("public static ArmorPiece hardenedLeatherJetHelmet()",start);
        org.junit.jupiter.api.Assertions.assertTrue(!source.substring(start,end).contains("new InventoryFootprint("),
                "OUTER CHEST no debe contener footprints 2D manuales.");
    }

    private static void profile(ArmorPiece p,int a,int b,int c){
        org.junit.jupiter.api.Assertions.assertTrue(p.protection().equals(new ArmorProtectionProfile(a,b,c)),
                "Perfil alterado: "+p.name()+" -> "+p.protection());
    }
    
}

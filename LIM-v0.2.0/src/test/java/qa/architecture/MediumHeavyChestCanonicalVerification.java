package qa.architecture;

import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;

import java.nio.file.*;
import java.util.*;

public final class MediumHeavyChestCanonicalVerification {
    private MediumHeavyChestCanonicalVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        List<ArmorPiece> pieces=ArmorCatalog.allProtectiveMiddleChest();
        org.junit.jupiter.api.Assertions.assertTrue(pieces.size()==10," debe conservar 10 CHEST MEDIUM/HEAVY canónicas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.protectiveMiddleChestProfileCount()==10,
                "Cada CHEST MEDIUM/HEAVY debe declarar XYZ.");

        for(ArmorPiece piece:pieces){
            org.junit.jupiter.api.Assertions.assertTrue(piece.materialClass()!=ArmorMaterialClass.LIGHT,
                    piece.name()+" no puede ser LIGHT dentro del catálogo .");
            var dims=ArmorPhysicalDimensionsCatalog.mediumHeavyChestDimensionsFor(piece.name());
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(InventoryVolumeProjectionPolicy.footprint(dims)),
                    "Footprint debe derivarse de XYZ: "+piece.name());
        }

        fp(ArmorCatalog.hardenedLeatherChest(),8,6);
        fp(ArmorCatalog.hardenedLeatherAviatorJacketV881(),3,3);
        fp(ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),8,8);
        fp(ArmorCatalog.workshopLeatherApronV881(),4,3);
        fp(ArmorCatalog.paperChestV881(),10,12);
        fp(ArmorCatalog.historicalEbonyWarriorChest(),15,18);
        fp(ArmorCatalog.ebonyWarriorV881Chest(),18,21);
        fp(ArmorCatalog.historicalKnightChest(),15,18);
        fp(ArmorCatalog.knightV881Chest(),18,21);
        fp(ArmorCatalog.historicalHeavyLamellarChest(),12,14);

        profile(ArmorCatalog.historicalEbonyWarriorChest(),75,55,60);
        profile(ArmorCatalog.ebonyWarriorV881Chest(),95,100,85);
        profile(ArmorCatalog.historicalKnightChest(),85,100,75);
        profile(ArmorCatalog.knightV881Chest(),100,100,80);
        profile(ArmorCatalog.historicalHeavyLamellarChest(),95,95,50);

        // /: duplicar el volumen de un par de brazales no duplica su peso canónico.
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorCatalog.historicalEbonyWarriorBracers().weightKg(),1.053),
                "El par de brazales de Ébano histórico sigue pesando 1,053 kg en total.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorCatalog.historicalKnightBracers().weightKg(),3.375),
                "El par de brazales de Caballero histórico sigue pesando 3,375 kg en total.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorCatalog.knightV881Bracers().weightKg(),3.472),
                "El par de brazales de Caballero V881 sigue pesando 3,472 kg en total.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorCatalog.historicalHeavyLamellarBracers().weightKg(),2.239),
                "El par lamelar pesado sigue pesando 2,239 kg en total.");

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("workshopChest()"),"Camisa/Blusa + Delantal obsoleto debe desaparecer.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("mercenaryChest()"),"Chaleco lamelar V881 obsoleto debe desaparecer.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("soldierChest()"),"Chaleco lamelar con gabardina V881 obsoleto debe desaparecer.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("\"Chaleco lamelar V881\""),"No debe sobrevivir el objeto obsoleto Mercenario.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("\"Chaleco lamelar cubierto con una gabardina V881\""),
                "No debe sobrevivir el objeto obsoleto Soldado.");
    }

    private static void fp(ArmorPiece p,int v,int h){
        org.junit.jupiter.api.Assertions.assertTrue(p.footprint().verticalSlots()==v && p.footprint().horizontalSlots()==h,
                p.name()+" footprint inesperado: "+p.footprint());
    }
    private static void profile(ArmorPiece p,int a,int b,int c){
        org.junit.jupiter.api.Assertions.assertTrue(p.protection().equals(new ArmorProtectionProfile(a,b,c)),
                p.name()+" perfil inesperado: "+p.protection());
    }
    private static boolean close(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}

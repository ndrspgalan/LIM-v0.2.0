package qa.architecture;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;

import java.nio.file.*;
import java.util.List;

public final class BracersCanonicalVerification {
    private BracersCanonicalVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        List<ArmorPiece> canonical=List.of(
                ArmorCatalog.hardenedLeatherBracers(),
                ArmorCatalog.hardenedLeatherFingerlessGloves(),
                ArmorCatalog.workshopBracers(),
                ArmorCatalog.paperBracersV881(),
                ArmorCatalog.historicalEbonyWarriorBracers(),
                ArmorCatalog.ebonyWarriorV881LeftBracer(),
                ArmorCatalog.historicalKnightBracers(),
                ArmorCatalog.knightV881Bracers(),
                ArmorCatalog.historicalHeavyLamellarBracers()
        );
        org.junit.jupiter.api.Assertions.assertTrue(canonical.size()==9 && ArmorPhysicalDimensionsCatalog.bracersProfileCount()==9,
                " debe conservar 9 conceptos BRACERS canónicos.");

        for(ArmorPiece piece:canonical){
            var dims=ArmorPhysicalDimensionsCatalog.bracersDimensionsFor(piece.name());
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(InventoryVolumeProjectionPolicy.footprint(dims)),
                    "Footprint BRACERS debe derivarse de XYZ: "+piece.name());
        }

        footprint(ArmorCatalog.hardenedLeatherBracers(),2,2);
        footprint(ArmorCatalog.hardenedLeatherFingerlessGloves(),2,2);
        footprint(ArmorCatalog.workshopBracers(),2,2);
        footprint(ArmorCatalog.paperBracersV881(),6,2);
        footprint(ArmorCatalog.historicalEbonyWarriorBracers(),12,4);
        footprint(ArmorCatalog.ebonyWarriorV881LeftBracer(),6,4);
        footprint(ArmorCatalog.historicalKnightBracers(),16,6);
        footprint(ArmorCatalog.knightV881Bracers(),16,6);
        footprint(ArmorCatalog.historicalHeavyLamellarBracers(),16,6);

        profile(ArmorCatalog.historicalEbonyWarriorBracers(),75,55,60);
        profile(ArmorCatalog.ebonyWarriorV881LeftBracer(),95,100,85);
        profile(ArmorCatalog.historicalKnightBracers(),85,100,75);
        profile(ArmorCatalog.knightV881Bracers(),100,100,80);

        // Los perfiles corregidos son canónicos del arnés completo, no sólo del par BRACERS.
        profile(ArmorCatalog.historicalEbonyWarriorChest(),75,55,60);
        profile(ArmorCatalog.historicalEbonyWarriorLeggings(),75,55,60);
        profile(ArmorCatalog.historicalKnightChest(),85,100,75);
        profile(ArmorCatalog.historicalKnightHelmet(),85,100,75);
        profile(ArmorCatalog.historicalKnightLeggings(),85,100,75);
        profile(ArmorCatalog.knightV881Chest(),100,100,80);
        profile(ArmorCatalog.knightV881Leggings(),100,100,80);

        ArmorPiece ebony=ArmorCatalog.historicalEbonyWarriorBracers();
        ArmorPiece ebonyLeft=ArmorCatalog.ebonyWarriorV881LeftBracer();
        ArmorPiece knight=ArmorCatalog.knightV881Bracers();
        org.junit.jupiter.api.Assertions.assertTrue(ebony.hasProperty(ItemPropertyId.IMPROVISED_SHIELD)
                        && ebonyLeft.hasProperty(ItemPropertyId.IMPROVISED_SHIELD)
                        && knight.hasProperty(ItemPropertyId.IMPROVISED_SHIELD),
                "ESCUDO IMPROVISADO debe conservarse donde el brazal izquierdo permite interposición.");
        org.junit.jupiter.api.Assertions.assertTrue(close(ebonyLeft.bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.025),
                "La pieza unilateral V881 debe conservar cobertura BRACERS 2,5%.");

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("mercenaryBracers()"),"Guantes obsoleto de Mercenario deben desaparecer.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("soldierBracers()"),"Guantes obsoleto de Soldado deben desaparecer.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("0.606, new InventoryFootprint(2, 1), ArmorInventoryCategory.BRACERS"),
                "No debe sobrevivir la física obsoleto del guante Mercenario.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("0.060, new InventoryFootprint(2, 1), ArmorInventoryCategory.BRACERS"),
                "No debe sobrevivir la física obsoleto del guante Soldado.");
    }

    private static void footprint(ArmorPiece p,int v,int h){
        org.junit.jupiter.api.Assertions.assertTrue(p.footprint().verticalSlots()==v && p.footprint().horizontalSlots()==h,
                p.name()+" footprint: "+p.footprint());
    }
    private static void profile(ArmorPiece p,int a,int b,int c){
        org.junit.jupiter.api.Assertions.assertTrue(p.protection().equals(new ArmorProtectionProfile(a,b,c)),
                p.name()+" perfil: "+p.protection());
    }
    private static boolean close(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}

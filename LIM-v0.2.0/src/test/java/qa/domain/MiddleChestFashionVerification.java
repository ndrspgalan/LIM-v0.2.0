package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;

public final class MiddleChestFashionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var all = ArmorCatalog.allMiddleChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==13, " debe cerrar 12 prendas LIGHT MIDDLE más el gambesón legado migrado");
        for (ArmorPiece p : all) {
            org.junit.jupiter.api.Assertions.assertTrue(p.material()==ArmorMaterial.CLOTH, p.name()+" debe tener TELA como material principal");
            org.junit.jupiter.api.Assertions.assertTrue(p.materialClass()==ArmorMaterialClass.LIGHT, p.name()+" debe permanecer LIGHT");
            org.junit.jupiter.api.Assertions.assertTrue(p.inventoryCategory().orElseThrow()==ArmorInventoryCategory.CHEST, p.name()+" debe equiparse desde CHEST");
            close(p.effectiveWeightKg(true), p.weightKg()*3.0, p.name()+" EMPAPADO x3");
            org.junit.jupiter.api.Assertions.assertTrue(!p.narrativeDescription().contains("PROTECCIÓN |") && !p.narrativeDescription().contains("PESO |"), "Narrativa sin estadísticas: "+p.name());
        }
        profile(ArmorCatalog.middleWaistcoat(),2,5,2);
        profile(ArmorCatalog.middlePaddedWaistcoat(),10,25,10);
        profile(ArmorCatalog.middleBodice(),4,10,4);
        profile(ArmorCatalog.middlePaddedJacket(),12,30,12);
        profile(ArmorCatalog.middleDoubletV881(),6,15,6);
        profile(ArmorCatalog.middleGambesonV881(),36,90,36);
        close(ArmorCatalog.middleSpencer().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS),.10,"Spencer mangas");
        close(ArmorCatalog.middleWaistcoat().bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.50,"Chaleco torso");

        ArmorEquipmentLayout inner = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerShirt());
        ArmorEquipmentLayout middle = inner.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleWaistcoat());
        org.junit.jupiter.api.Assertions.assertTrue(middle.piecesAt(EquipmentSlot.CHEST).size()==2, "INNER + MIDDLE deben coexistir");
        fail(() -> middle.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleCardiganV881()), "No cabe segundo MIDDLE LIGHT");
        fail(() -> middle.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.hardenedLeatherChest()), "LIGHT MIDDLE y MEDIUM MIDDLE son incompatibles");

        ArmorEquipmentLayout armored = inner.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.hardenedLeatherChest());
        fail(() -> armored.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleWaistcoat()), "MEDIUM MIDDLE y LIGHT MIDDLE son incompatibles");
        ArmorEquipmentLayout heavy = inner.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.historicalKnightChest());
        fail(() -> heavy.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleWaistcoat()), "HEAVY MIDDLE y LIGHT MIDDLE son incompatibles");
    }
    private static void profile(ArmorPiece p,double a,double b,double c){ close(p.protection().piercing(),a,p.name()+" P"); close(p.protection().slashing(),b,p.name()+" C"); close(p.protection().blunt(),c,p.name()+" B"); }
    
    private static void fail(Runnable r,String m){ try{r.run(); throw new AssertionError(m);}catch(IllegalArgumentException ok){} }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>1e-9) throw new AssertionError(m+": "+a+" != "+b); }
}

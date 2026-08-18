package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;

public final class InnerLeggingsFashionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var all = ArmorCatalog.allInnerLeggingsGarments();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==9, " debe cerrar ocho piezas nuevas más la Combinación V881");
        for (ArmorPiece p : all) {
            org.junit.jupiter.api.Assertions.assertTrue(p.material()==ArmorMaterial.CLOTH, p.name()+" debe usar TELA como material principal");
            org.junit.jupiter.api.Assertions.assertTrue(p.materialClass()==ArmorMaterialClass.LIGHT, p.name()+" debe ser LIGHT");
            org.junit.jupiter.api.Assertions.assertTrue(p.innerLeggingsLayer().isPresent(), p.name()+" debe reservar BASE o COVER de INNER LEGGINGS");
            close(p.effectiveWeightKg(true), p.weightKg()*3.0, p.name()+" EMPAPADO x3");
            org.junit.jupiter.api.Assertions.assertTrue(!p.narrativeDescription().contains("PROTECCIÓN |") && !p.narrativeDescription().contains("PESO |"), "Narrativa sin estadísticas: "+p.name());
        }
        profile(ArmorCatalog.innerLongDrawersV881(),2,5,2);
        profile(ArmorCatalog.innerReinforcedPetticoatV881(),4,10,4);
        profile(ArmorCatalog.innerPaddedPetticoatV881(),10,25,10);
        close(ArmorCatalog.innerKneeDrawersV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.15,"calzoncillo rodilla 15");
        close(ArmorCatalog.innerWomensDrawersV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.20,"drawers 20");
        close(ArmorCatalog.innerPetticoatV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.30,"enagua 30");

        ArmorEquipmentLayout underwear = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerLongDrawersV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPetticoatV881());
        org.junit.jupiter.api.Assertions.assertTrue(underwear.piecesAt(EquipmentSlot.LEGGINGS).size()==2, "BASE+COVER deben coexistir");
        fail(() -> underwear.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerKnittedTrousersV881()), "No cabe segundo BASE");
        fail(() -> underwear.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPaddedPetticoatV881()), "No cabe segundo COVER");

        ArmorEquipmentLayout combo = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerCombinationV881());
        fail(() -> combo.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerLongDrawersV881()), "Combinación debe reservar BASE en LEGGINGS");
        var comboCover = combo.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPetticoatV881());
        org.junit.jupiter.api.Assertions.assertTrue(comboCover.piecesAt(EquipmentSlot.CHEST).size()==1 && comboCover.piecesAt(EquipmentSlot.LEGGINGS).size()==1,
                "Combinación + COVER no duplica la pieza multirregional");

        ArmorEquipmentLayout layered = underwear
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleWorkTrousersV881())
                .equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, ArmorCatalog.paperLeggingsV881());
        org.junit.jupiter.api.Assertions.assertTrue(layered.piecesAt(EquipmentSlot.LEGGINGS).size()==4, "INNER BASE/COVER + MIDDLE + OUTER");
        fail(() -> layered.equip(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleStraightTrousersV881()), "No cabe segundo MIDDLE");
    }
    private static void profile(ArmorPiece p,double a,double b,double c){ close(p.protection().piercing(),a,p.name()+" P"); close(p.protection().slashing(),b,p.name()+" C"); close(p.protection().blunt(),c,p.name()+" B"); }
    
    private static void fail(Runnable r,String m){ try{r.run(); throw new AssertionError(m);}catch(IllegalArgumentException ok){} }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>1e-9) throw new AssertionError(m+": "+a+" != "+b); }
}

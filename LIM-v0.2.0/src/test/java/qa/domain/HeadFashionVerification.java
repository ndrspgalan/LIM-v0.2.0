package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

public final class HeadFashionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var glasses=ArmorCatalog.normalVisionGlassesV881();
        org.junit.jupiter.api.Assertions.assertTrue(glasses.materialClass()==ArmorMaterialClass.MEDIUM,"Las gafas deben ser MEDIUM por vidrio laminado");
        org.junit.jupiter.api.Assertions.assertTrue(glasses.headLayer().orElseThrow()==HeadLayer.TACTICAL,"Las gafas pertenecen a TACTICAL HEAD");
        org.junit.jupiter.api.Assertions.assertTrue(glasses.hasActiveProperty(ItemPropertyId.EYEWEAR),"Las gafas deben declarar EYEWEAR");

        var three=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.headScarfV881())
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,glasses)
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.hunterHatV881());
        org.junit.jupiter.api.Assertions.assertTrue(three.piecesAt(EquipmentSlot.HEAD).size()==3,"Bufanda+gafas+sombrero debe ser válido");
        org.junit.jupiter.api.Assertions.assertTrue(three.headWeightKg()<=3.5,"HEAD no supera 3,5 kg");

        expectFail(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.paperHelmetV881())
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.headScarfV881()),
                "Casco MEDIUM debe bloquear accesorios");
        expectFail(() -> ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.historicalKnightHelmet())
                .equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.travelerHoodV881()),
                "Casco HEAVY debe bloquear accesorios");

        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.travelerNeckGaiter().properties().stream().anyMatch(p -> p.id().name().equals("NATURAL_FILTER")),"FILTRO NATURAL eliminado");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.travelerHoodV881().hasActiveProperty(ItemPropertyId.WATERPROOF),"Capucha impermeable");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.hunterHatV881().hasActiveProperty(ItemPropertyId.WATERPROOF),"Sombrero de cazador impermeable");
        org.junit.jupiter.api.Assertions.assertTrue(!HeadSoakedProtectionPolicy.headSoaked(true, ArmorEquipmentLayout.empty().equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.hunterHatV881())),"Sombrero impide EMPAPADO en HEAD");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.integralRespirator().headCoverageRatio()==1.0,"Respirador integral cubre toda HEAD");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.paddedCoif().materials().equals(java.util.Set.of(ArmorMaterial.CLOTH)),"Cofia ya no lleva gafas integradas");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.beardedHelmetV881().name().equals("Casco Barbudo V881"),"Casco Barbudo presente");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.crusaderHelmetV881().name().equals("Casco del Cruzado V881"),"Casco del Cruzado presente");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.spartanHelmetV881().name().equals("Casco Espartano V881"),"Casco Espartano presente");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allHeadArmor().stream().noneMatch(p -> p.name().contains("Faraam")),"Sin denominación Faraam");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allHeadArmor().stream().noneMatch(p -> p.name().toLowerCase().contains("táctico")),"Sin Casco táctico en nombre");
    }
    
    private static void expectFail(Runnable r,String m){try{r.run();throw new AssertionError(m);}catch(IllegalArgumentException expected){}}
}

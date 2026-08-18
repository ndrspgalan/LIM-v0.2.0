package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.aeronautics.DisposableGliderItem;
import domain.inventory.item.armor.*;

public final class OuterChestFashionVerification {
    private static int n;
    
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var all=ArmorCatalog.allOuterChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==20," debe cerrar 20 prendas OUTER.");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().allMatch(p->p.materialClass()==ArmorMaterialClass.LIGHT),"Todas las prendas OUTER canónicas son LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().allMatch(p->p.material()==ArmorMaterial.CLOTH),"Todas derivan de TELA.");
        var l=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,ArmorCatalog.outerTravelerCloak());
        boolean rejected=false; try { l.equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,ArmorCatalog.outerUlsterV881()); } catch(IllegalArgumentException e){ rejected=true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"OUTER CHEST sólo admite una prenda.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerTravelerCloak().properties().isEmpty(),"La Capa del Viajero queda cosmética, sin propiedades mágicas.");
        org.junit.jupiter.api.Assertions.assertTrue(!new DisposableGliderItem().canEquipOverTorso(l),"Planeador no comparte volumen con OUTER CHEST.");
        var free=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.INNER,ArmorCatalog.innerShirt());
        org.junit.jupiter.api.Assertions.assertTrue(new DisposableGliderItem().canEquipOverTorso(free),"Planeador puede ocupar torso exterior si OUTER está libre.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerDolmanV881().bodyRegionCoverageRatio(BodyArmorRegion.BRACERS)==.15,"Dolman cubre 15% BRACERS.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerManteletV881().bodyRegionCoverageRatio(BodyArmorRegion.CHEST)==.35,"Manteleta cubre CHEST parcial.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.outerUlsterV881().effectiveWeightKg(true) > ArmorCatalog.outerUlsterV881().weightKg()*2.9,"OUTER textil empapado x3.");
    }
}

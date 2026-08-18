package qa.domain;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.combat.ai.observation.AttackSourceType;

import java.util.ArrayList;
import java.util.List;

public final class MiddleLeggingsFashionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var all = ArmorCatalog.allMiddleLeggingsGarments();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==20, " debe cerrar 20 prendas MIDDLE LEGGINGS");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().allMatch(p -> p.materialClass()==ArmorMaterialClass.LIGHT), "Todas deben ser LIGHT");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().allMatch(p -> p.material()==ArmorMaterial.CLOTH), "Todas deben derivar de CLOTH");
        org.junit.jupiter.api.Assertions.assertTrue(all.stream().allMatch(p -> p.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS) <= .30 + 1e-9), "Ninguna puede superar LEGGINGS 30%");

        ArmorEquipPolicy policy = new ArmorEquipPolicy();
        List<EquippedArmorLayer> eq = new ArrayList<>();
        var base = new EquippedArmorLayer(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerLongDrawersV881());
        policy.validate(eq, base); eq.add(base);
        var cover = new EquippedArmorLayer(EquipmentSlot.LEGGINGS, ArmorLayerPosition.INNER, ArmorCatalog.innerPetticoatV881());
        policy.validate(eq, cover); eq.add(cover);
        var middle = new EquippedArmorLayer(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleStraightTrousersV881());
        policy.validate(eq, middle); eq.add(middle);
        var outer = new EquippedArmorLayer(EquipmentSlot.LEGGINGS, ArmorLayerPosition.OUTER, ArmorCatalog.hardenedLeatherLeggings());
        policy.validate(eq, outer); eq.add(outer);
        org.junit.jupiter.api.Assertions.assertTrue(eq.size()==4, "Debe admitirse INNER BASE + INNER COVER + MIDDLE + OUTER");

        boolean rejected=false;
        try { policy.validate(eq, new EquippedArmorLayer(EquipmentSlot.LEGGINGS, ArmorLayerPosition.MIDDLE, ArmorCatalog.middleFormalTrousersV881())); }
        catch (IllegalArgumentException expected) { rejected=true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "MIDDLE LEGGINGS debe ser una única plaza");

        var trouser = ArmorCatalog.middleStraightTrousersV881();
        var ulster = ArmorCatalog.outerUlsterV881();
        double cov = BodyArmorCoverageCompositionPolicy.effectiveCoverage(List.of(trouser, ulster), BodyArmorRegion.LEGGINGS);
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(cov-.30)<1e-9, "Pantalón 30% + Ulster no puede crear más de 30% anatómico");
        var p1 = BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(trouser), BodyArmorRegion.LEGGINGS, AttackSourceType.MELEE);
        var p2 = BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(trouser, ulster), BodyArmorRegion.LEGGINGS, AttackSourceType.MELEE);
        org.junit.jupiter.api.Assertions.assertTrue(p2.blunt() > p1.blunt(), "El Ulster debe añadir protección sobre la zona solapada");

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleBreechesV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS)==.20, "Breeches 20%");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleKiltV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS)==.15, "Kilt 15%");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.middleOverskirtV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS)==.20, "Sobrefalda 20%");
    }
    
}

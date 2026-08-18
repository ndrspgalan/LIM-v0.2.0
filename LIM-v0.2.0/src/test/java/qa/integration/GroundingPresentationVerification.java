package qa.integration;

import domain.combat.*;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import java.util.Map;

/** TOMA A TIERRA global, footprints y narrativa de prendas BODY MEDIUM. */
public final class GroundingPresentationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        grounding(); footprints(); narratives();
    }
    static void grounding() {
        EquipmentState barefoot = new EquipmentState(Map.of(EquipmentSlot.CHEST, ArmorCatalog.knightV881Chest()));
        org.junit.jupiter.api.Assertions.assertTrue(GroundingPolicy.groundedByFeet(barefoot), "Descalzo debe poner a tierra");
        org.junit.jupiter.api.Assertions.assertTrue(barefoot.hasArmorProperty(ItemPropertyId.GROUNDING), "TOMA A TIERRA global visible");
        EquipmentState riding = new EquipmentState(Map.of(EquipmentSlot.CHEST, ArmorCatalog.knightV881Chest(), EquipmentSlot.FEET, ArmorCatalog.leatherHighRidingBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.groundedByFeet(riding) && GroundingPolicy.insulatedByFeet(riding), "Botas con suela de caucho aíslan aunque sean MEDIUM");
        EquipmentState work = new EquipmentState(Map.of(EquipmentSlot.CHEST, ArmorCatalog.knightV881Chest(), EquipmentSlot.FEET, ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.groundedByFeet(work) && GroundingPolicy.insulatedByFeet(work), "Botas industriales aíslan por caucho, no por clase HEAVY");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.leatherHeavyWorkBootsV881().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR), "Botas de trabajo no son conductoras");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.knightV881Chest().hasProperty(ItemPropertyId.GROUNDING), "Caballero V881 sin TOMA A TIERRA intrínseca");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.knightV881Chest().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR), "Caballero V881 conductor");
        var resolver = new NonConventionalDamageResolver();
        double grounded = resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.BODY,barefoot,0,false).materialAdjustedDamage();
        double ungrounded = resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.BODY,work,0,false).materialAdjustedDamage();
        org.junit.jupiter.api.Assertions.assertTrue(closeValue(grounded,20) && closeValue(ungrounded,20), "El contacto con tierra no elimina la vulnerabilidad x2 de la pieza de acero");
        EquipmentState headBare = new EquipmentState(Map.of(EquipmentSlot.HEAD, ArmorCatalog.historicalKnightHelmet()));
        double headGrounded=resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.HEAD,headBare,0,false).materialAdjustedDamage();
        org.junit.jupiter.api.Assertions.assertTrue(closeValue(headGrounded,20), "El casco histórico de acero conserva x2 incluso con contacto por FEET");
    }
    static void footprints() {
        fp(ArmorCatalog.hardenedLeatherChest(),8,6); fp(ArmorCatalog.hardenedLeatherAviatorJacketV881(),3,3);
        fp(ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),8,8); fp(ArmorCatalog.hardenedLeatherBracers(),2,2);
        fp(ArmorCatalog.hardenedLeatherLeggings(),8,6); fp(ArmorCatalog.workshopLeatherApronV881(),4,3);
        fp(ArmorCatalog.leatherStrapBuckleGaitersV881(),12,4); fp(ArmorCatalog.leatherShotgunChapsV881(),14,6);
        fp(ArmorCatalog.leatherHighRidingBootsV881(),10,8); fp(ArmorCatalog.leatherHeavyWorkBootsV881(),8,6);
        fp(ArmorCatalog.leatherOxfordBrogueShoesV881(),8,6);
    }
    static void narratives(){
        for (ArmorPiece p : java.util.List.of(ArmorCatalog.hardenedLeatherChest(), ArmorCatalog.hardenedLeatherAviatorJacketV881(), ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(), ArmorCatalog.workshopLeatherApronV881(), ArmorCatalog.leatherShotgunChapsV881(), ArmorCatalog.leatherHighRidingBootsV881(), ArmorCatalog.leatherOxfordBrogueShoesV881())) {
            String d=p.narrativeDescription(); org.junit.jupiter.api.Assertions.assertTrue(!d.contains("PESO")&&!d.contains("PROTECCIÓN |")&&!d.contains("COBERTURA |"), "Narrativa técnica sin ficha mecánica: "+p.name());
        }
    }
    static void fp(ArmorPiece p,int v,int h){ org.junit.jupiter.api.Assertions.assertTrue(p.footprint().verticalSlots()==v&&p.footprint().horizontalSlots()==h,p.name()+" footprint"); }
    static boolean closeValue(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}

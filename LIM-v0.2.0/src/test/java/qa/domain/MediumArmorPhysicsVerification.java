package qa.domain;

import domain.inventory.item.armor.*;
import java.util.List;

public final class MediumArmorPhysicsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        close(ArmorCatalog.hardenedLeatherChest().weightKg(),2.8,"Chaqueta viaje");
        close(ArmorCatalog.workshopLeatherApronV881().bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.25,"Delantal chest25");
        close(ArmorCatalog.workshopLeatherApronV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.10,"Delantal legs10");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.FEET),.05,"Bota feet5");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"Bota legs12");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.leatherHeavyWorkBootsV881().materialClass()==ArmorMaterialClass.HEAVY,"Bota trabajo con puntera acero debe emerger HEAVY");
        close(ArmorCatalog.hardenedLeatherChest().effectiveWeightKg(true),3.36,"Cuero soaked +20%");
        close(SoakedEquipmentWeightPolicy.effectiveWeightKg(ArmorCatalog.innerWorkShirt(),true),ArmorCatalog.innerWorkShirt().weightKg()*3.0,"Tela soaked +200%");
        close(ArmorCatalog.engineerSuit().effectiveWeightKg(true),ArmorCatalog.engineerSuit().weightKg(),"HEAVY excluido de soaked");
        org.junit.jupiter.api.Assertions.assertTrue(BodyArmorRegion.FEET.contributesToErgonomics(),"FEET participa en PA");
        close(ArmorErgonomicsPolicy.lowerLimbStaminaMultiplier(List.of(ArmorCatalog.historicalKnightLeggings())),2.0,"Heavy legs+feet35 => x2");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorErgonomicsPolicy.eligibleForEquippedWeightBonus(BodyArmorRegion.FEET),"FEET sin bonus de peso");
        ArmorProtectionProfile base=BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(ArmorCatalog.hardenedLeatherLeggings()),BodyArmorRegion.LEGGINGS);
        ArmorProtectionProfile layered=BodyArmorCoverageCompositionPolicy.effectiveProtection(List.of(ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.leatherHighRidingBootsV881()),BodyArmorRegion.LEGGINGS);
        org.junit.jupiter.api.Assertions.assertTrue(layered.piercing()>base.piercing() && layered.slashing()>base.slashing() && layered.blunt()>base.blunt(),"Bota alta suma protección sobre pantalón");
        close(BodyArmorCoverageCompositionPolicy.effectiveCoverage(List.of(ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.leatherHighRidingBootsV881()),BodyArmorRegion.LEGGINGS),.30,"Solapamiento no crea cobertura >30");
    }
    
    static void close(double a,double b,String m){if(Math.abs(a-b)>1e-6)throw new AssertionError(m+" expected="+b+" actual="+a);}
}

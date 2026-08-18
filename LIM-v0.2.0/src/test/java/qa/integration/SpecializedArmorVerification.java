package qa.integration;

import domain.combat.*;
import domain.environment.*;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

import java.util.Map;

public final class SpecializedArmorVerification {
    
    private static void close(double a, double b, String m) { if (Math.abs(a-b) > 1e-9) throw new AssertionError(m+": "+a); }
    private static void profile(ArmorProtectionProfile p, double a, double b, double c, String m) {
        close(p.piercing(),a,m+" P"); close(p.slashing(),b,m+" C"); close(p.blunt(),c,m+" Ct");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(MaterialCatalog.allCanonicalUnits().size()==17, " debe publicar diecisiete materiales.");
        profile(ArmorMaterial.LAMINATED_GLASS.canonicalProtection(),40,85,35,"Vidrio laminado");
        profile(ArmorMaterial.MINERAL_MULTILAYER_FABRIC.canonicalProtection(),38,85,38,"Tejido mineral");
        profile(ArmorMaterial.RUBBER.canonicalProtection(),10,25,20,"Caucho");
        profile(ArmorMaterial.VULCANIZED_RUBBER.canonicalProtection(),15,30,15,"Caucho vulcanizado");
        profile(ArmorMaterial.DIELECTRIC_CLOTH.canonicalProtection(),5,15,5,"Tela dieléctrica");
        org.junit.jupiter.api.Assertions.assertTrue(MaterialCatalog.laminatedGlass(1).footprint().verticalSlots()==4, "Vidrio inventariable");


        ArmorPiece respirator=ArmorCatalog.integralRespirator();
        close(respirator.weightKg(),1.35,"Peso respirador");
        close(respirator.headCoverageRatio(),1,"Cobertura respirador");
        profile(respirator.protection(),30,52,35,"Respirador ponderado");
        org.junit.jupiter.api.Assertions.assertTrue(respirator.hasProperty(ItemPropertyId.ASSISTED_FILTER),"FILTRO ASISTIDO");
        EquipmentState respEq=new EquipmentState(Map.of(EquipmentSlot.HEAD,respirator));
        EnvironmentalProtectionPolicy env=new EnvironmentalProtectionPolicy();
        close(env.exposureMultiplier(EnvironmentalAdversity.VIRULENT_TOXICITY,respEq),0,"Inmunidad toxicidad");
        close(env.exposureMultiplier(EnvironmentalAdversity.SUFFOCATING_HEAT,respEq),.5,"Quemadura asfixiante x0,5");

        ArmorPiece fire=ArmorCatalog.fireproofSuit();
        ArmorPiece insulating=ArmorCatalog.insulatingSuit();
        profile(fire.protection(),40,90,40,"Mono ignífugo");
        profile(insulating.protection(),40,90,40,"Mono aislante");
        close(fire.weightKg(),6,"Peso ignífugo"); close(insulating.weightKg(),6,"Peso aislante");
        org.junit.jupiter.api.Assertions.assertTrue(fire.hasProperty(ItemPropertyId.FIREPROOF),"IGNIFUGO");
        org.junit.jupiter.api.Assertions.assertTrue(insulating.hasProperty(ItemPropertyId.INSULATING),"AISLANTE");
        NonConventionalDamageResolver resolver=new NonConventionalDamageResolver();
        EquipmentState fireEq=new EquipmentState(Map.of(EquipmentSlot.CHEST,fire));
        EquipmentState insEq=new EquipmentState(Map.of(EquipmentSlot.CHEST,insulating));
        close(resolver.resolve(DamageType.BURN,50,ArmorHitLocation.BODY,fireEq,0,false).netDamage(),0,"Inmunidad fuego");
        close(resolver.resolve(DamageType.ELECTRICITY,50,ArmorHitLocation.BODY,insEq,0,false).netDamage(),0,"Inmunidad electricidad");

        ArmorPiece engineer=ArmorCatalog.engineerSuit();
        profile(engineer.protection(),75,85,80,"Ingeniero");
        close(engineer.weightKg(),25,"Peso Ingeniero");
        org.junit.jupiter.api.Assertions.assertTrue(engineer.materials().equals(java.util.Set.of(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE)),"Ingeniero solo compuesto");
    }
}

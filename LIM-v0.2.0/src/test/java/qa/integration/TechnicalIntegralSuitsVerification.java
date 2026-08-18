package qa.integration;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.environment.*;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.ArmorPhysicalDimensionsCatalog;
import domain.movement.*;

import java.util.Map;

public final class TechnicalIntegralSuitsVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        specializedSuits();
        engineerSystems();
        layering();
        mobility();
    }

    private static void specializedSuits(){
        ArmorPiece fire=ArmorCatalog.fireproofSuit();
        ArmorPiece ins=ArmorCatalog.insulatingSuit();

        org.junit.jupiter.api.Assertions.assertTrue(fire.footprint().equals(ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor(fire.name())),"Ignífugo XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(ins.footprint().equals(ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor(ins.name())),"Aislante XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(fire.protection().equals(new ArmorProtectionProfile(40,90,40)),"Ignífugo 40/90/40");
        org.junit.jupiter.api.Assertions.assertTrue(ins.protection().equals(new ArmorProtectionProfile(40,90,40)),"Aislante 40/90/40");
        close(fire.weightKg(),6,"Ignífugo 6 kg"); close(ins.weightKg(),6,"Aislante 6 kg");

        org.junit.jupiter.api.Assertions.assertTrue(fire.hasActiveProperty(ItemPropertyId.FIREPROOF),"Ignífugo activo");
        org.junit.jupiter.api.Assertions.assertTrue(fire.hasActiveProperty(ItemPropertyId.INTEGRAL_WATERPROOF),"Ignífugo impermeable");
        org.junit.jupiter.api.Assertions.assertTrue(ins.hasActiveProperty(ItemPropertyId.INSULATING),"Aislante activo");
        org.junit.jupiter.api.Assertions.assertTrue(ins.hasActiveProperty(ItemPropertyId.INTEGRAL_WATERPROOF),"Aislante impermeable");

        NonConventionalDamageResolver r=new NonConventionalDamageResolver();
        EquipmentState fe=new EquipmentState(Map.of(EquipmentSlot.CHEST,fire));
        EquipmentState ie=new EquipmentState(Map.of(EquipmentSlot.CHEST,ins));
        close(r.resolve(DamageType.BURN,50,ArmorHitLocation.BODY,fe,0,false).netDamage(),0,"Ignífugo inmune BURN");
        close(r.resolve(DamageType.ELECTRICITY,50,ArmorHitLocation.BODY,ie,0,false).netDamage(),0,"Aislante inmune electricidad");
        EnvironmentalProtectionPolicy env=new EnvironmentalProtectionPolicy();
        close(env.exposureMultiplier(EnvironmentalAdversity.SOAKED,fe),0,"Ignífugo inmune EMPAPADO");
        close(env.exposureMultiplier(EnvironmentalAdversity.SOAKED,ie),0,"Aislante inmune EMPAPADO");
    }

    private static void engineerSystems(){
        ArmorPiece suit=ArmorCatalog.engineerSuit();
        org.junit.jupiter.api.Assertions.assertTrue(suit.footprint().equals(ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor(suit.name())),"Ingeniero XYZ");
        close(suit.weightKg(),25,"Ingeniero 25 kg");
        close(suit.headSupportedWeightKg(),3.5,"Ingeniero masa cervical");
        org.junit.jupiter.api.Assertions.assertTrue(suit.protection().equals(new ArmorProtectionProfile(75,85,80)),"Ingeniero 75/85/80");
        for(ItemPropertyId id:new ItemPropertyId[]{
                ItemPropertyId.INTEGRAL_SEAL,
                ItemPropertyId.INTEGRAL_WATERPROOF,
                ItemPropertyId.DIELECTRIC_ENVELOPE,
                ItemPropertyId.THERMAL_CONTROL,
                ItemPropertyId.SERVOMOTOR_CAPACITY,
                ItemPropertyId.ELECTROMECHANICAL_STABILITY,
                ItemPropertyId.HYDROMECHANICAL_ASSISTANCE,
                ItemPropertyId.MATERIAL_SYNERGY,
                ItemPropertyId.BIOMECHANICAL_RIGIDITY}){
            org.junit.jupiter.api.Assertions.assertTrue(suit.hasActiveProperty(id),"Ingeniero falta "+id);
        }

        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.CHEST,suit));
        NonConventionalDamageResolver r=new NonConventionalDamageResolver();
        for(DamageType type:new DamageType[]{DamageType.POISON,DamageType.BURN,DamageType.FROST,DamageType.ELECTRICITY}){
            close(r.resolve(type,50,ArmorHitLocation.BODY,eq,0,false).netDamage(),0,"Ingeniero inmunidad "+type);
        }
        EnvironmentalProtectionPolicy env=new EnvironmentalProtectionPolicy();
        close(env.exposureMultiplier(EnvironmentalAdversity.SOAKED,eq),0,"Ingeniero EMPAPADO");
        close(env.exposureMultiplier(EnvironmentalAdversity.VIRULENT_TOXICITY,eq),0,"Ingeniero toxicidad");
        close(env.exposureMultiplier(EnvironmentalAdversity.SUFFOCATING_HEAT,eq),0,"Ingeniero calor");
        close(env.exposureMultiplier(EnvironmentalAdversity.BITING_FROST,eq),0,"Ingeniero frío");

        String n=suit.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(n.length()>3000,"La descripción del Ingeniero debe ser exhaustiva y técnica.");
        for(String term:new String[]{"largueros","fuelle","pelvis","dieléctrica","costillas","actuadores","vidrio cian","herramienta"}){
            org.junit.jupiter.api.Assertions.assertTrue(n.toLowerCase().contains(term.toLowerCase()),"Narrativa del Ingeniero debe explicar "+term);
        }
    }

    private static void layering(){
        ArmorPiece inner=ArmorCatalog.innerUndershirt();
        ArmorPiece middle=ArmorCatalog.middleWaistcoat();
        ArmorPiece outer=ArmorCatalog.outerFrockCoatV881();

        for(ArmorPiece suit:new ArmorPiece[]{ArmorCatalog.fireproofSuit(),ArmorCatalog.insulatingSuit()}){
            ArmorEquipmentLayout ok=ArmorEquipmentLayout.empty()
                    .equip(EquipmentSlot.CHEST,ArmorLayerPosition.INNER,inner)
                    .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,suit);
            org.junit.jupiter.api.Assertions.assertTrue(ok.piecesAt(EquipmentSlot.CHEST).size()==2,"Mono especializado admite INNER");
            expectFail(()->ok.equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer));
            expectFail(()->ArmorEquipmentLayout.empty()
                    .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle)
                    .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,suit));
        }

        ArmorPiece engineer=ArmorCatalog.engineerSuit();
        ArmorEquipmentLayout eng=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.INNER,inner)
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.INNER,ArmorCatalog.innerLongDrawersV881())
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,engineer);
        expectFail(()->eng.equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.laborerHatV881()));
        expectFail(()->eng.equip(EquipmentSlot.BRACERS,ArmorLayerPosition.UNSPECIFIED,ArmorCatalog.workshopBracers()));
        expectFail(()->eng.equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer));
        expectFail(()->eng.equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,ArmorCatalog.outerMoccasinsV881()));
    }

    private static void mobility(){
        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.CHEST,ArmorCatalog.engineerSuit()));
        CharacterSheet sheet=CharacterSheet.of(27,40,12,30,75,30,3,25,11);
        org.junit.jupiter.api.Assertions.assertTrue(eq.effectiveAttributeValue(Attribute.DESTREZA,sheet)==20,"Ingeniero cap DEX 20");
        ArmorMobilityRestrictionPolicy p=new ArmorMobilityRestrictionPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(p.allows(eq,LocomotionMode.WALKING),"caminar");
        org.junit.jupiter.api.Assertions.assertTrue(p.allows(eq,LocomotionMode.TROTTING),"trotar");
        org.junit.jupiter.api.Assertions.assertTrue(p.allows(eq,LocomotionMode.RUNNING),"correr");
        org.junit.jupiter.api.Assertions.assertTrue(!p.allows(eq,LocomotionMode.CROUCH_WALKING),"sin agacharse");
        org.junit.jupiter.api.Assertions.assertTrue(!p.allows(eq,LocomotionMode.CRAWLING),"sin gatear");
        org.junit.jupiter.api.Assertions.assertTrue(!p.allows(eq,LocomotionMode.CLIMBING),"sin escalar");
        org.junit.jupiter.api.Assertions.assertTrue(!p.allowsSwimming(eq),"sin nadar");
        org.junit.jupiter.api.Assertions.assertTrue(!p.allowsSliding(eq),"sin deslizarse");
    }

    private static void expectFail(Runnable r){
        try { r.run(); throw new AssertionError("Debía fallar"); }
        catch(IllegalArgumentException expected){}
    }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>EPS) throw new AssertionError(m+": "+a); }
    
}

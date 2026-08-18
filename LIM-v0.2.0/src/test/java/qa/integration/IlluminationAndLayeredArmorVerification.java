package qa.integration;

import domain.combat.*;
import domain.illumination.*;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;

import java.util.*;

/** iluminación, Quick CHEST, sincronización de armadura y pipeline exterior->interior. */
public final class IlluminationAndLayeredArmorVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        mechanicalLightingUsesChestQuick();
        accessoryLightingIsPassiveAndDistinct();
        armorSlotsProjectTheLayeredLayout();
        bodyHitTraversesAllLayersAndLeavesUncoveredDamage();
        headCanReachFullCoverage();
    }

    private static void mechanicalLightingUsesChestQuick(){
        var magnet=MechanicalLampCatalog.magnetlampe();
        var cat=MechanicalLampCatalog.knijpkat();
        org.junit.jupiter.api.Assertions.assertTrue(close(magnet.illuminationMeters(),2.5)&&close(magnet.lightSecondsPerAction(),5),"Magnetlampe: 2,5 m y cinco segundos por tirón.");
        org.junit.jupiter.api.Assertions.assertTrue(close(cat.illuminationMeters(),4.5)&&close(cat.lightSecondsPerAction(),0),"Knijpkat: 4,5 m y luz mantenida por bombeo.");
        InventoryState magnetState=chestQuick(magnet);
        var activation=MechanicalLampUsePolicy.activate(magnet,magnetState);
        org.junit.jupiter.api.Assertions.assertTrue(activation.activated()&&close(activation.lightSeconds(),5)&&activation.profile().color()==IlluminationColor.YELLOW,"Magnetlampe debe activarse desde Quick CHEST.");
        boolean rejected=false;
        try { new InventoryState(EquipmentState.empty(),new QuickAccessBar(List.of(Optional.of(magnet),Optional.empty(),Optional.empty(),Optional.empty())),logisticsWith(InventoryCompartmentType.LEGGINGS_STORAGE,magnet)); }
        catch(IllegalArgumentException expected){ rejected=true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"Una linterna de pecho no puede asignarse a otro Quick.");
    }

    private static void accessoryLightingIsPassiveAndDistinct(){
        var portable=AccessoryCatalog.portableLantern();
        var lunar=AccessoryCatalog.lunarLantern();
        var p=IlluminationPolicy.accessoryProfile(new EquipmentState(Map.of(EquipmentSlot.ACCESSORY,portable)));
        var l=IlluminationPolicy.accessoryProfile(new EquipmentState(Map.of(EquipmentSlot.ACCESSORY,lunar)));
        org.junit.jupiter.api.Assertions.assertTrue(p.active()&&close(p.radiusMeters(),2)&&p.color()==IlluminationColor.WARM_ORANGE,"Farolillo portátil: 2 m anaranjado cálido.");
        org.junit.jupiter.api.Assertions.assertTrue(l.active()&&close(l.radiusMeters(),2)&&l.color()==IlluminationColor.CYAN_BLUE,"Farolillo lunar: 2 m azul cian.");
        org.junit.jupiter.api.Assertions.assertTrue(portable.narrativeDescription().toLowerCase().contains("queroseno"),"El portátil debe declarar queroseno.");
    }

    private static void armorSlotsProjectTheLayeredLayout(){
        ArmorPiece outer=body("Exterior",new ArmorProtectionProfile(10,10,10),ArmorMaterial.CLOTH,.5);
        ArmorPiece middle=body("Media",new ArmorProtectionProfile(20,20,20),ArmorMaterial.HARDENED_LEATHER,.5);
        var state=LayeredEquipmentState.empty().equipArmor(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equipArmor(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle);
        var active=state.activeItems(EquipmentSlot.CHEST);
        org.junit.jupiter.api.Assertions.assertTrue(active.size()==2&&active.get(0)==outer&&active.get(1)==middle,"CHEST activo debe ser una proyección exterior->interior del layout, no una segunda ranura independiente.");
    }

    private static void bodyHitTraversesAllLayersAndLeavesUncoveredDamage(){
        ArmorPiece outer=body("Exterior 50",new ArmorProtectionProfile(50,0,0),ArmorMaterial.CLOTH,.5);
        ArmorPiece middle=body("Media 50",new ArmorProtectionProfile(50,0,0),ArmorMaterial.HARDENED_LEATHER,.5);
        var layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.CHEST,layout,999);
        // 50% queda descubierto: 50. En el 50% cubierto: 100 -> 50 -> 25; ponderado = 12,5. Total = 62,5.
        org.junit.jupiter.api.Assertions.assertTrue(close(r.netDamage().piercing(),62.5),"BODY debe conservar daño descubierto y resolver todas las capas secuencialmente.");
        org.junit.jupiter.api.Assertions.assertTrue(outer.currentPiercingProtection()==49&&middle.currentPiercingProtection()==50,"Cada capa alcanzada evalúa desgaste de forma independiente: la exterior supera umbral y la interior queda justo en igualdad.");
    }

    private static void headCanReachFullCoverage(){
        ArmorPiece helmet=new ArmorPiece("Casco total","Casco de prueba.",1,new InventoryFootprint(1,1),ArmorHitLocation.HEAD,1.0,
                new ArmorProtectionProfile(100,0,0),ArmorMaterial.STEEL,ArmorForm.STANDARD,List.of(),List.of()).withHeadLayer(HeadLayer.TACTICAL);
        var layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,helmet);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.HELMET,layout,999);
        org.junit.jupiter.api.Assertions.assertTrue(close(r.netDamage().piercing(),0),"HEAD sí puede alcanzar cobertura anatómica total.");
    }

    private static ArmorPiece body(String name,ArmorProtectionProfile p,ArmorMaterial m,double coverage){
        return new ArmorPiece(name,name+".",1,new InventoryFootprint(1,1),ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,coverage),p,m,Set.of(m),ArmorForm.STANDARD,List.of(),List.of());
    }
    private static InventoryState chestQuick(InventoryEntry item){
        return new InventoryState(EquipmentState.empty(),new QuickAccessBar(List.of(Optional.empty(),Optional.of(item),Optional.empty(),Optional.empty())),logisticsWith(InventoryCompartmentType.CHEST_STORAGE,item));
    }
    private static LogisticsState logisticsWith(InventoryCompartmentType type,InventoryEntry item){
        EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);
        m.put(type,new InventoryCompartment(type,true,new InventoryGridDefinition(4,4),List.of(item),Optional.empty()));
        return new LogisticsState(m,PersonalTransportState.none());
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<EPS;}
    
}

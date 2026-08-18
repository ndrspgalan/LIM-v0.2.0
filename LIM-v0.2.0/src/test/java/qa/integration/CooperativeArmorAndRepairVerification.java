package qa.integration;

import domain.combat.*;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.maintenance.MaintenanceService;

import java.util.*;

/** Regresión : capas secuenciales exterior->interior y reparación material. */
public final class CooperativeArmorAndRepairVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        outerAndInnerResolveSequentially();
        exactTieStillTraversesBothLayers();
        allChannelsTraverseEveryReachedLayer();
        weaponCanReachInnerHeavyLayer();
        repairRequiresConstituentMaterialAndRestoresAllProfiles();
    }

    private static void outerAndInnerResolveSequentially(){
        ArmorPiece outer=body("Outer P1",new ArmorProtectionProfile(1,0,0),ArmorMaterial.CLOTH);
        ArmorPiece middle=body("Middle P20",new ArmorProtectionProfile(20,0,0),ArmorMaterial.HARDENED_LEATHER);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle);
        new ArmorDamageResolver().resolve(new PhysicalDamage(30,0,0),ArmorCombatHitbox.CHEST,layout,999);
        close(outer.currentPiercingProtection(),0,"La capa exterior debe recibir primero el impacto y poder desgastarse");
        close(middle.currentPiercingProtection(),19,"El daño residual debe alcanzar después la capa interior");
    }

    private static void exactTieStillTraversesBothLayers(){
        ArmorPiece outer=body("Outer P20",new ArmorProtectionProfile(20,0,0),ArmorMaterial.CLOTH);
        ArmorPiece middle=body("Middle P20",new ArmorProtectionProfile(20,0,0),ArmorMaterial.HARDENED_LEATHER);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle);
        new ArmorDamageResolver().resolve(new PhysicalDamage(30,0,0),ArmorCombatHitbox.CHEST,layout,999);
        close(outer.currentPiercingProtection(),19,"La exterior se resuelve primero");
        close(middle.currentPiercingProtection(),19,"La interior también recibe el residual si éste supera su perfil");
    }

    private static void allChannelsTraverseEveryReachedLayer(){
        ArmorPiece outer=body("Outer",new ArmorProtectionProfile(10,40,20),ArmorMaterial.CLOTH);
        ArmorPiece middle=body("Middle",new ArmorProtectionProfile(35,15,30),ArmorMaterial.HARDENED_LEATHER);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,middle);
        new ArmorDamageResolver().resolve(new PhysicalDamage(100,100,100),ArmorCombatHitbox.CHEST,layout,999);
        close(outer.currentPiercingProtection(),9,"P atraviesa OUTER primero");
        close(outer.currentSlashingProtection(),39,"C atraviesa OUTER primero");
        close(outer.currentBluntProtection(),19,"B atraviesa OUTER primero");
        close(middle.currentPiercingProtection(),34,"P residual alcanza MIDDLE");
        close(middle.currentSlashingProtection(),14,"C residual alcanza MIDDLE");
        close(middle.currentBluntProtection(),29,"B residual alcanza MIDDLE");
    }

    private static void weaponCanReachInnerHeavyLayer(){
        ArmorPiece outer=body("Medium P30",new ArmorProtectionProfile(30,0,0),ArmorMaterial.CLOTH);
        ArmorPiece heavy=body("Heavy P10",new ArmorProtectionProfile(10,0,0),ArmorMaterial.STEEL);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,outer)
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,heavy);
        WeaponItem weapon=new WeaponItem("Estoque prueba","Arma de verificación .",1,new InventoryFootprint(1,1),1,
                List.of(new WeaponMode("Estocada",new LethalityProfile(65,0,0))),List.of(),List.of(),List.of());
        WeaponMode mode=weapon.modes().getFirst();
        new ArmorDamageResolver().resolveMelee(ArmorCombatHitbox.CHEST,layout,999,weapon,mode);
        close(weapon.currentLethality(mode).piercing(),64,"El arma que penetra la capa exterior puede contactar con la HEAVY interior y desgastarse");
    }

    private static void repairRequiresConstituentMaterialAndRestoresAllProfiles(){
        ArmorPiece armor=body("Cuero dañado",new ArmorProtectionProfile(25,45,35),ArmorMaterial.HARDENED_LEATHER);
        armor.applyProfileWear(1,100,100,100);
        ReusableRepairToolItem artisan=new ReusableRepairToolItem(ReusableRepairToolItem.Kind.ARTISAN_BOX,
                "Herramienta artesanal de verificación","Herramienta artesanal de verificación .",0.5,new InventoryFootprint(1,1),List.of());
        MaintenanceService service=new MaintenanceService();
        var without=inventory(armor,artisan,List.of(artisan));
        var rejected=service.repairArmor(armor,artisan,without,new HostileEncounterState());
        org.junit.jupiter.api.Assertions.assertTrue(!rejected.successful(),"Sin cuero en bruto la reparación debe rechazarse");
        close(armor.currentPiercingProtection(),24,"El rechazo no puede restaurar P");

        MaterialItem leather=MaterialCatalog.hardenedLeather(1);
        MaterialItem spareLeather=MaterialCatalog.hardenedLeather(1);
        var with=inventory(armor,artisan,List.of(artisan,leather,spareLeather));
        var completed=service.repairArmor(armor,artisan,with,new HostileEncounterState());
        org.junit.jupiter.api.Assertions.assertTrue(completed.successful(),"Herramienta + material deben reparar");
        close(armor.currentPiercingProtection(),25,"Debe restaurar P");
        close(armor.currentSlashingProtection(),45,"Debe restaurar C");
        close(armor.currentBluntProtection(),35,"Debe restaurar B");
        org.junit.jupiter.api.Assertions.assertTrue(leather.currentUses()+spareLeather.currentUses()==1,"Debe consumir exactamente una unidad física del material constituyente");
    }

    private static InventoryState inventory(ArmorPiece armor, InventoryEntry tool, List<InventoryEntry> bodyItems){
        EquipmentState equipment=new EquipmentState(Map.of(EquipmentSlot.CHEST,armor));
        QuickAccessBar bar=new QuickAccessBar(List.of(Optional.of(tool),Optional.empty(),Optional.empty(),Optional.empty()));
        EnumMap<InventoryCompartmentType,InventoryCompartment> compartments=new EnumMap<>(InventoryCompartmentType.class);
        for(InventoryCompartmentType type:InventoryCompartmentType.values())
            compartments.put(type,InventoryCompartment.empty(type,type==InventoryCompartmentType.BODY || type==InventoryCompartmentType.BANDOLIER));
        compartments.put(InventoryCompartmentType.LEGGINGS_STORAGE,new InventoryCompartment(InventoryCompartmentType.LEGGINGS_STORAGE,true,new InventoryGridDefinition(2,7),List.of(tool),Optional.empty()));
        List<InventoryEntry> materials=bodyItems.stream().filter(e->e!=tool).toList();
        compartments.put(InventoryCompartmentType.BANDOLIER,new InventoryCompartment(InventoryCompartmentType.BANDOLIER,true,materials));
        return new InventoryState(equipment,bar,new LogisticsState(compartments,PersonalTransportState.none()));
    }

    private static ArmorPiece body(String name,ArmorProtectionProfile p,ArmorMaterial material){
        return new ArmorPiece(name,name,1,new InventoryFootprint(1,1),ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,.50),p,material,Set.of(material),ArmorForm.STANDARD,List.of(),List.of());
    }
    private static void close(double a,double e,String m){if(Math.abs(a-e)>EPS)throw new AssertionError(m+": "+a+" != "+e);}
    
}

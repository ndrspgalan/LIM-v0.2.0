package qa.integration;

import domain.combat.*;
import domain.combat.ai.observation.AttackSourceType;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import java.util.*;

public final class PaperArmorAndV881GroundingVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.PAPER.canonicalProtection().equals(new ArmorProtectionProfile(1,3,1)),"PAPER debe ser 1/3/1");
        java.util.List<ArmorPiece> paperPieces=java.util.List.of(ArmorCatalog.paperHelmetV881(),ArmorCatalog.paperChestV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.paperLeggingsV881());
        org.junit.jupiter.api.Assertions.assertTrue(paperPieces.size()==4,"Papel debe tener cuatro piezas canónicas");
        org.junit.jupiter.api.Assertions.assertTrue(close(paperPieces.stream().mapToDouble(ArmorPiece::weightKg).sum(),5.0),"Papel seco debe pesar 5 kg");

        ArmorPiece helmet=ArmorCatalog.paperHelmetV881();
        ArmorPiece chest=ArmorCatalog.paperChestV881();
        ArmorPiece bracers=ArmorCatalog.paperBracersV881();
        ArmorPiece leggings=ArmorCatalog.paperLeggingsV881();
        org.junit.jupiter.api.Assertions.assertTrue(helmet.protection().equals(new ArmorProtectionProfile(60,78,22)),"Perfil casco papel");
        org.junit.jupiter.api.Assertions.assertTrue(chest.protection().equals(new ArmorProtectionProfile(80,92,35)),"Perfil coraza papel ponderado");
        org.junit.jupiter.api.Assertions.assertTrue(bracers.protection().equals(new ArmorProtectionProfile(65,82,25)),"Perfil brazales papel ponderado");
        org.junit.jupiter.api.Assertions.assertTrue(leggings.protection().equals(new ArmorProtectionProfile(66,78,26)),"Perfil polainas papel ponderado");
        for(ArmorPiece p: paperPieces) {
            org.junit.jupiter.api.Assertions.assertTrue(p.hasProperty(ItemPropertyId.VARNISHED),"Pieza papel barnizada");
            org.junit.jupiter.api.Assertions.assertTrue(p.hasProperty(ItemPropertyId.LACQUERED),"Pieza papel lacada");
            org.junit.jupiter.api.Assertions.assertTrue(p.hasProperty(ItemPropertyId.INSULATING),"Papel seco aislante");
            org.junit.jupiter.api.Assertions.assertTrue(p.exposeToSoaked()==SoakedArmorResult.BECAME_WET,"Papel barnizado -> WET");
            org.junit.jupiter.api.Assertions.assertTrue(p.hasProperty(ItemPropertyId.INSULATING),"Lacado conserva aislamiento WET");
        }
        org.junit.jupiter.api.Assertions.assertTrue(close(paperPieces.stream().mapToDouble(ArmorPiece::weightKg).sum(),12.5),"Papel WET debe pesar 12,5 kg");
        chest.exposeToSoaked();
        org.junit.jupiter.api.Assertions.assertTrue(close(chest.currentProtection(AttackSourceType.RANGED_PROJECTILE).piercing(),28.0),"Coraza WET ranged P -65%");
        org.junit.jupiter.api.Assertions.assertTrue(close(chest.currentProtection(AttackSourceType.FIREARM_PROJECTILE).piercing(),52.0),"Coraza WET firearm P -35%");
        double before=chest.currentBluntProtection();
        chest.applyBluntWear(1,ArmorHitLocation.BODY);
        org.junit.jupiter.api.Assertions.assertTrue(close(before-chest.currentBluntProtection(),0.5),"WET debe inhibir FRÁGIL y dejar desgaste material x1");

        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorCatalog.enlightenedPanopticon().weightKg(),3.5),"Panóptico debe pesar 3,5 kg");

        ArmorPiece knight=ArmorCatalog.knightV881Chest();
        org.junit.jupiter.api.Assertions.assertTrue(!knight.hasProperty(ItemPropertyId.GROUNDING),"Caballero V881 ya no debe incorporar TOMA A TIERRA");
        org.junit.jupiter.api.Assertions.assertTrue(knight.hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR),"Caballero V881 debe conservar CONDUCTOR ELÉCTRICO");
        NonConventionalDamageResolver nc=new NonConventionalDamageResolver();
        double v881=nc.resolve(DamageType.ELECTRICITY,100,ArmorHitLocation.BODY,
                new EquipmentState(Map.of(EquipmentSlot.CHEST,knight)),0,false).netDamage();
        double historical=nc.resolve(DamageType.ELECTRICITY,100,ArmorHitLocation.BODY,
                new EquipmentState(Map.of(EquipmentSlot.CHEST,ArmorCatalog.historicalKnightChest(), EquipmentSlot.FEET, ArmorCatalog.leatherHeavyWorkBootsV881())),0,false).netDamage();
        org.junit.jupiter.api.Assertions.assertTrue(close(v881,200),"El Caballero V881 conserva la vulnerabilidad x2 de su coraza de acero aunque esté descalzo");
        org.junit.jupiter.api.Assertions.assertTrue(close(historical,200),"La coraza histórica de acero conserva x2 con independencia de la suela");

        org.junit.jupiter.api.Assertions.assertTrue(java.util.List.of(ArmorCatalog.ebonyWarriorV881Chest(),ArmorCatalog.ebonyWarriorV881LeftBracer()).size()==2,"Ébano V881 consta de coraza y brazal izquierdo");
        ArmorPiece left=ArmorCatalog.ebonyWarriorV881LeftBracer();
        org.junit.jupiter.api.Assertions.assertTrue(close(left.bodyCoverageRatio(),.025),"Brazal Ébano V881 +2,5% BODY");
        org.junit.jupiter.api.Assertions.assertTrue(left.supportsImprovisedBlock() && left.hasProperty(ItemPropertyId.IMPROVISED_SHIELD),"Brazal Ébano V881 debe ser escudo improvisado");
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<EPS;}
    
}

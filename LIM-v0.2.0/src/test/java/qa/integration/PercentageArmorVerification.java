package qa.integration;

import domain.combat.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import java.util.*;

/** Regresión  migrada a : porcentajes + hitbox concreta + stagger por momentum. */
public final class PercentageArmorVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        percentageMitigationRemains(); strictWearThreshold(); staggerRemainsIndependent();
    }
    private static void percentageMitigationRemains(){
        ArmorPiece a=piece(new ArmorProtectionProfile(75,0,0),ArmorMaterial.STEEL);
        var l=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,a);
        // CHEST sólo puede interponer 50% global; sobre esa mitad P75 absorbe 75%.
        close(new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.CHEST,l,999).netDamage().piercing(),62.5,"P75 en CHEST50");
    }
    private static void strictWearThreshold(){
        ArmorPiece a=piece(new ArmorProtectionProfile(75,0,0),ArmorMaterial.STEEL);
        var l=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,a);
        new ArmorDamageResolver().resolve(new PhysicalDamage(75,0,0),ArmorCombatHitbox.CHEST,l,999);
        close(a.currentPiercingProtection(),75,"Igualdad no desgasta");
        new ArmorDamageResolver().resolve(new PhysicalDamage(76,0,0),ArmorCombatHitbox.CHEST,l,999);
        close(a.currentPiercingProtection(),74,"Superioridad estricta desgasta");
    }
    private static void staggerRemainsIndependent(){
        ArmorPiece a=piece(new ArmorProtectionProfile(0,0,100),ArmorMaterial.STEEL);
        var l=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,a);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(0,0,50),ArmorCombatHitbox.CHEST,l,10);
        org.junit.jupiter.api.Assertions.assertTrue(r.stagger().staggered(),"B100 no cancela momentum");
    }
    private static ArmorPiece piece(ArmorProtectionProfile p,ArmorMaterial m){return new ArmorPiece("Prueba","Prueba .",1,new InventoryFootprint(1,1),ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.5),p,m,Set.of(m),ArmorForm.STANDARD,List.of(),List.of());}
    private static void close(double a,double e,String m){if(Math.abs(a-e)>EPS)throw new AssertionError(m+": "+a+" != "+e);}
    
}

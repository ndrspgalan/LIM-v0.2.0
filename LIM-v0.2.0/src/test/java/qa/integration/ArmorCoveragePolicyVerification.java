package qa.integration;

import domain.combat.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import java.util.*;

/** Cobertura : cada hitbox tiene su techo anatómico y las capas se superponen. */
public class ArmorCoveragePolicyVerification {
    private static void close(double a,double e,String m){if(Math.abs(a-e)>1e-9)throw new AssertionError(m+": "+a+" != "+e);}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        ArmorPiece chest=body("Coraza",ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.5),new ArmorProtectionProfile(25,45,35));
        ArmorPiece legs=body("Polainas",ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,.12),new ArmorProtectionProfile(22,42,32));
        ArmorPiece coat=new ArmorPiece("Gabardina","Gabardina test",1,new InventoryFootprint(1,1),ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.5,BodyArmorRegion.LEGGINGS,.10),new ArmorProtectionProfile(2,5,2),ArmorMaterial.CLOTH,Set.of(ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of(),List.of());
        var chestLayout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,chest);
        close(new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.CHEST,chestLayout,999).netDamage().piercing(),87.5,"CHEST50 P25");
        var legsLayout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,coat).equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,legs);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.LEGGINGS,legsLayout,999);
        if(r.netDamage().piercing()>=100)throw new AssertionError("Las capas de LEGGINGS deben mitigar una parte real.");
    }
    private static ArmorPiece body(String n,ArmorInventoryCategory c,Map<BodyArmorRegion,Double> cov,ArmorProtectionProfile p){return new ArmorPiece(n,n+" test",1,new InventoryFootprint(1,1),c,cov,p,ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER),ArmorForm.STANDARD,List.of(),List.of());}
}

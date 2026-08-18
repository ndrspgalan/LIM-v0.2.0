package qa.domain;

import domain.combat.DamageType;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import java.util.*;

public final class MaterialCatalogVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.CLOTH), new double[]{2,5,2}), "Tela");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.HARDENED_LEATHER), new double[]{25,45,35}), "Cuero");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.WOOD), new double[]{25,20,15}), "Madera");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.BRONZE), new double[]{70,60,60}), "Bronce");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.STEEL), new double[]{75,100,75}), "Acero");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.EBONY_WOOD), new double[]{75,55,60}), "Ébano");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(profile(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE), new double[]{75,85,80}), "Compuesto");

        var all=MaterialCatalog.allCanonicalUnits(); org.junit.jupiter.api.Assertions.assertTrue(all.size()==17,"Diecisiete materiales canónicos");
        var cloth=all.get(0); org.junit.jupiter.api.Assertions.assertTrue(cloth.footprint().verticalSlots()==3&&cloth.footprint().horizontalSlots()==2&&cloth.maximumStack()==1&&close(cloth.unitWeightKg(),.3),"Tela logística");
        org.junit.jupiter.api.Assertions.assertTrue(has(cloth,ItemPropertyId.WARMTH)&&has(cloth,ItemPropertyId.MATERIAL_COMPATIBILITY),"Tela propiedades");
        org.junit.jupiter.api.Assertions.assertTrue(MaterialCatalog.hardenedLeather(1).properties().isEmpty(),"Cuero neutral");
        org.junit.jupiter.api.Assertions.assertTrue(has(MaterialCatalog.wood(1),ItemPropertyId.FLAMMABLE)&&ArmorMaterial.WOOD.incomingDamageMultiplier(DamageType.BURN)==2,"Madera inflamable");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.BRONZE.bluntWearMultiplier()==1.0&&ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.POISON)==.75&&ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.BURN)==1.0&&ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.ELECTRICITY)==2,"Bronce ");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.STEEL.incomingDamageMultiplier(DamageType.ELECTRICITY)==2,"Acero conductor");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.EBONY_WOOD.bluntWearMultiplier()==0&&ArmorMaterial.EBONY_WOOD.incomingDamageMultiplier(DamageType.BURN)==2,"Ébano");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE.bluntWearMultiplier()==2,"Compuesto mantiene desgaste contundente x2");
        org.junit.jupiter.api.Assertions.assertTrue(close(cloth.weightKg(),0.3),"Una unidad física de tela pesa 0,300 kg");

        var artisan=RepairToolCatalog.artisanBox(); var toolbox=RepairToolCatalog.toolbox(); var special=new PortableLaboratoryItem();
        org.junit.jupiter.api.Assertions.assertTrue(artisan.hasInfiniteUses()&&close(artisan.weightKg(),3)&&artisan.footprint().verticalSlots()==6&&artisan.footprint().horizontalSlots()==4,"Caja artesano");
        org.junit.jupiter.api.Assertions.assertTrue(toolbox.hasInfiniteUses()&&close(toolbox.weightKg(),6)&&toolbox.footprint().verticalSlots()==8&&toolbox.footprint().horizontalSlots()==6,"Caja herramientas");
        org.junit.jupiter.api.Assertions.assertTrue(close(special.weightKg(),12)&&special.footprint().verticalSlots()==10&&special.footprint().horizontalSlots()==8,"Caja Alicia e Iván");
        org.junit.jupiter.api.Assertions.assertTrue(new ResinJarItem(1).name().equals("Tarro de Resina"),"Resina existente");
        org.junit.jupiter.api.Assertions.assertTrue(new CoolantBottleItem(1).name().equals("Botella de Líquido Refrigerante"),"Refrigerante existente");
    }
    private static double[] profile(ArmorMaterial m){var p=m.canonicalProtection();return new double[]{p.piercing(),p.slashing(),p.blunt()};}
    private static boolean has(MaterialItem i,ItemPropertyId id){return i.properties().stream().anyMatch(p->p.id()==id);}
    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;}
    
}

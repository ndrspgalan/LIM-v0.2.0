package qa.integration;

import domain.combat.*;
import domain.combat.ai.observation.AttackSourceType;
import domain.environment.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.ReusableRepairToolItem;
import domain.maintenance.MaintenanceService;
import java.util.*;

public final class PaperMaterialInfrastructureVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.BRONZE.bluntWearMultiplier()==1.0,"Bronce ya no conserva desgaste x0,5");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.POISON),.75),"Bronce veneno x0,75");
        org.junit.jupiter.api.Assertions.assertTrue(close(ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.BURN),1),"Bronce pierde resistencia a Quemadura");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterialPropertyPolicy.has(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE,ItemPropertyId.INTRICATE_MANUFACTURE),"Manufactura debe nacer del compuesto");
        org.junit.jupiter.api.Assertions.assertTrue(MaterialCatalog.paper(1).properties().stream().anyMatch(p->p.id()==ItemPropertyId.FRAGILE),"Papel FRÁGIL");

        ArmorPiece paper = paperPiece(true,true);
        org.junit.jupiter.api.Assertions.assertTrue(paper.hasProperty(ItemPropertyId.INSULATING),"Papel seco aislante");
        org.junit.jupiter.api.Assertions.assertTrue(paper.exposeToSoaked()==SoakedArmorResult.BECAME_WET,"Barnizado debe sobrevivir y adquirir WET");
        org.junit.jupiter.api.Assertions.assertTrue(close(paper.weightKg(),2.5),"WET debe multiplicar masa x2,5");
        org.junit.jupiter.api.Assertions.assertTrue(paper.hasProperty(ItemPropertyId.INSULATING),"Lacado conserva aislamiento mojado");
        org.junit.jupiter.api.Assertions.assertTrue(close(paper.currentProtection(AttackSourceType.RANGED_PROJECTILE).piercing(),35),"WET ranged P x0,35");
        org.junit.jupiter.api.Assertions.assertTrue(close(paper.currentProtection(AttackSourceType.FIREARM_PROJECTILE).piercing(),65),"WET firearm P x0,65");

        ArmorPiece unLacquered=paperPiece(true,false); unLacquered.exposeToSoaked();
        org.junit.jupiter.api.Assertions.assertTrue(!unLacquered.hasProperty(ItemPropertyId.INSULATING),"WET sin lacado pierde aislamiento");
        ArmorPiece raw=paperPiece(false,false);
        org.junit.jupiter.api.Assertions.assertTrue(raw.exposeToSoaked()==SoakedArmorResult.STRUCTURAL_FAILURE && raw.structurallyFailed(),"Papel sin barniz falla estructuralmente");

        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.CHEST,paper));
        var layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,paper);
        ArmorImpactResult impact=new ArmorDamageResolver().resolve(new PhysicalDamage(0,0,10),ArmorCombatHitbox.CHEST,layout,10,AttackSourceType.MELEE);
        org.junit.jupiter.api.Assertions.assertTrue(close(impact.stagger().staggerDurationSeconds(),StaggerPolicy.resolve(4.5).staggerDurationSeconds()),"WET debe reducir estabilidad física x0,55");

        ArmorPiece bronze=new ArmorPiece("Bronce","Prueba",1,new InventoryFootprint(1,1),ArmorHitLocation.BODY,1,new ArmorProtectionProfile(70,60,60),ArmorMaterial.BRONZE,ArmorForm.STANDARD,List.of(),List.of());
        org.junit.jupiter.api.Assertions.assertTrue(new CorrosiveWearPolicy().apply(bronze,2)==0,"ANTI-CORROSIVO debe anular CORROSIVO");
        org.junit.jupiter.api.Assertions.assertTrue(close(new NonConventionalDamageResolver().resolve(DamageType.POISON,100,ArmorHitLocation.BODY,new EquipmentState(Map.of(EquipmentSlot.CHEST,bronze)),0,false).netDamage(),75),"ANTI-CORROSIVO veneno x0,75");

        org.junit.jupiter.api.Assertions.assertTrue(new EnvironmentalProtectionPolicy().exposureMultiplier(EnvironmentalAdversity.SOAKED,eq)>0,"AISLANTE ya no inmuniza EMPAPADO");
    }
    private static ArmorPiece paperPiece(boolean varnished, boolean lacquered){
        List<ItemProperty> props=new ArrayList<>();
        if(varnished) props.add(ItemProperty.alwaysActive(ItemPropertyId.VARNISHED,"BARNIZADO","Sellado superficial que evita el fallo estructural inmediato por EMPAPADO.","EMPAPADO | Evita fallo estructural"));
        if(lacquered) props.add(ItemProperty.alwaysActive(ItemPropertyId.LACQUERED,"LACADO","Recubrimiento dieléctrico que conserva el aislamiento del paquete humedecido.","WET | Conserva AISLANTE ELÉCTRICO"));
        return new ArmorPiece("Papel prueba","Probeta constructiva ",1,new InventoryFootprint(1,1),ArmorInventoryCategory.CHEST,java.util.Map.of(BodyArmorRegion.CHEST,.50),new ArmorProtectionProfile(100,50,50),ArmorMaterial.PAPER,java.util.Set.of(ArmorMaterial.PAPER),ArmorForm.STANDARD,List.of(),props);
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<EPS;}
    
}

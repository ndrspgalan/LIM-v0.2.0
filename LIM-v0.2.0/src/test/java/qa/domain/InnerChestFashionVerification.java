package qa.domain;

import domain.character.Gender;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import java.util.Map;

public final class InnerChestFashionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var all = ArmorCatalog.allInnerChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==15, " debe cerrar 15 construcciones INNER CHEST");
        for (ArmorPiece p : all) {
            org.junit.jupiter.api.Assertions.assertTrue(p.material()==ArmorMaterial.CLOTH, p.name()+" usa TELA como material principal");
            org.junit.jupiter.api.Assertions.assertTrue(p.materialClass()==ArmorMaterialClass.LIGHT, p.name()+" debe ser LIGHT");
            org.junit.jupiter.api.Assertions.assertTrue(p.inventoryCategory().orElseThrow()==ArmorInventoryCategory.CHEST, p.name()+" debe equiparse desde CHEST");
            org.junit.jupiter.api.Assertions.assertTrue(p.innerChestLayer().isPresent(), p.name()+" debe declarar subestrato INNER");
            close(p.effectiveWeightKg(true), p.weightKg()*3.0, p.name()+" EMPAPADO x3");
            org.junit.jupiter.api.Assertions.assertTrue(!p.narrativeDescription().contains("PROTECCIÓN |") && !p.narrativeDescription().contains("PESO |"), "Narrativa sin estadísticas: "+p.name());
        }
        profile(ArmorCatalog.innerShirt(),2,5,2);
        profile(ArmorCatalog.innerWorkShirt(),3,7,3);
        profile(ArmorCatalog.innerCorset(),4,10,6);
        close(ArmorCatalog.innerCorset().bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.35,"Corsé chest35");
        close(ArmorCatalog.innerChemisette().bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.15,"Chemisette chest15");
        close(ArmorCatalog.innerDickey().bodyRegionCoverageRatio(BodyArmorRegion.CHEST),.12,"Dickey chest12");
        close(ArmorCatalog.innerCombinationV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.30,"Combinación multirregional");

        ArmorEquipmentLayout triple = ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerChemise())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerCorset())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerCorsetCover());
        org.junit.jupiter.api.Assertions.assertTrue(triple.piecesAt(EquipmentSlot.CHEST).size()==3, "BASE+STRUCTURAL+COVER deben coexistir");
        fail(() -> triple.equip(EquipmentSlot.CHEST, ArmorLayerPosition.INNER, ArmorCatalog.innerShirt()), "No cabe segundo BASE");
        var full = triple.equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, ArmorCatalog.hardenedLeatherChest())
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.OUTER, ArmorCatalog.outerWorkSmockV881());
        org.junit.jupiter.api.Assertions.assertTrue(full.piecesAt(EquipmentSlot.CHEST).size()==5, "INNER triple + MIDDLE + OUTER");

        var male = BaselineUnderwearPolicy.resolve(Gender.HOMBRE,false,false);
        org.junit.jupiter.api.Assertions.assertTrue(male.visibleGarments().equals(java.util.List.of("Taparrabos")), "Fallback masculino");
        var female = BaselineUnderwearPolicy.resolve(Gender.MUJER,false,false);
        org.junit.jupiter.api.Assertions.assertTrue(female.visibleGarments().contains("Taparrabos") && female.visibleGarments().contains("Venda pectoral"), "Fallback femenino");
        org.junit.jupiter.api.Assertions.assertTrue(BaselineUnderwearPolicy.resolve(Gender.MUJER,true,true).visibleGarments().isEmpty(), "Sin fallback si ambas regiones están vestidas");

        ArmorPiece aero = ArmorCatalog.retractableAeronautHelmet();
        org.junit.jupiter.api.Assertions.assertTrue(!aero.hasProperty(ItemPropertyId.GROUNDING), "Aeronauta no incorpora TOMA A TIERRA");
        EquipmentState barefoot = new EquipmentState(Map.of(EquipmentSlot.HEAD,aero));
        org.junit.jupiter.api.Assertions.assertTrue(barefoot.hasArmorProperty(ItemPropertyId.GROUNDING), "Descalzo sí está puesto a tierra defensivamente");
        org.junit.jupiter.api.Assertions.assertTrue(!barefoot.terrestrialIntercomAvailable(), "Descalzo no habilita intercom");
        EquipmentState leatherFeet = new EquipmentState(Map.of(EquipmentSlot.HEAD,aero, EquipmentSlot.FEET, ArmorCatalog.outerMoccasinsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(leatherFeet.terrestrialIntercomAvailable(), "Calzado OUTER sin suela dieléctrica + acoplamiento a tierra habilita intercom");
        EquipmentState heavyFeet = new EquipmentState(Map.of(EquipmentSlot.HEAD,aero, EquipmentSlot.FEET, ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(!heavyFeet.terrestrialIntercomAvailable(), "FEET con suela de caucho no habilita intercom");
    }
    private static void profile(ArmorPiece p,double a,double b,double c){ close(p.protection().piercing(),a,p.name()+" P"); close(p.protection().slashing(),b,p.name()+" C"); close(p.protection().blunt(),c,p.name()+" B"); }
    
    private static void fail(Runnable r,String m){ try{r.run(); throw new AssertionError(m);}catch(IllegalArgumentException ok){} }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>1e-9) throw new AssertionError(m+": "+a+" != "+b); }
}

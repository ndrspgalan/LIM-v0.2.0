package qa.architecture;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;

import java.nio.file.*;
import java.util.*;

public final class FeetXyzAndElectricalContactVerification {
    private FeetXyzAndElectricalContactVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var inner=ArmorCatalog.allInnerFeetGarments();
        var outer=ArmorCatalog.allOuterFeetGarments();
        org.junit.jupiter.api.Assertions.assertTrue(inner.size()==7,"INNER FEET debe conservar 7 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(outer.size()==11,"OUTER FEET debe conservar 11 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.innerFeetProfileCount()==7,"Faltan XYZ INNER FEET.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.outerFeetProfileCount()==11,"Faltan XYZ OUTER FEET.");

        for(ArmorPiece p:inner){
            var d=ArmorPhysicalDimensionsCatalog.innerFeetDimensionsFor(p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.footprint().equals(InventoryVolumeProjectionPolicy.footprint(d)),
                    "INNER FEET debe derivar footprint de XYZ: "+p.name());
        }
        for(ArmorPiece p:outer){
            var d=ArmorPhysicalDimensionsCatalog.outerFeetDimensionsFor(p.name());
            org.junit.jupiter.api.Assertions.assertTrue(p.footprint().equals(InventoryVolumeProjectionPolicy.footprint(d)),
                    "OUTER FEET debe derivar footprint de XYZ: "+p.name());
        }

        fp(ArmorCatalog.innerFeetSocksV881(),2,1);
        fp(ArmorCatalog.innerFeetHeavyWorkSocksV881(),2,2);
        fp(ArmorCatalog.innerFeetStockingsV881(),2,2);
        fp(ArmorCatalog.innerFeetHighStockingsV881(),3,2);
        fp(ArmorCatalog.innerFeetHeavyKnitStockingsV881(),3,2);
        fp(ArmorCatalog.innerFeetWrapsV881(),2,1);
        fp(ArmorCatalog.innerFeetTextileSlippersV881(),3,2);

        fp(ArmorCatalog.outerEspadrillesV881(),3,2);
        fp(ArmorCatalog.outerCanvasShoesV881(),3,3);
        fp(ArmorCatalog.outerLeatherWorkShoesV881(),8,6);
        fp(ArmorCatalog.outerLeatherAnkleBootsV881(),8,6);
        fp(ArmorCatalog.outerShortFieldBootsV881(),8,6);
        fp(ArmorCatalog.leatherHighRidingBootsV881(),10,8);
        fp(ArmorCatalog.leatherHeavyWorkBootsV881(),8,6);
        fp(ArmorCatalog.leatherOxfordBrogueShoesV881(),8,6);
        fp(ArmorCatalog.outerCourtShoesV881(),3,2);
        fp(ArmorCatalog.outerMoccasinsV881(),3,3);
        fp(ArmorCatalog.outerBabouchesV881(),3,2);

        // El par ocupa el volumen del par pero conserva el peso canónico total.
        close(ArmorCatalog.leatherHighRidingBootsV881().weightKg(),1.600,"Peso par botas altas");
        close(ArmorCatalog.leatherHeavyWorkBootsV881().weightKg(),1.800,"Peso par botas industriales");
        close(ArmorCatalog.outerLeatherWorkShoesV881().weightKg(),0.780,"Peso par zapatos trabajo");

        // Coberturas canónicas.
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.FEET),.05,"FEET botas altas");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"LEGGINGS botas altas");

        EquipmentState barefoot=new EquipmentState(Map.of());
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(barefoot)==FeetElectricalContact.EARTH_COUPLED,
                "Descalzo conserva contacto directo con terreno.");

        EquipmentState moccasins=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.outerMoccasinsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(moccasins)==FeetElectricalContact.EARTH_COUPLED,
                "Cuero/tela sin suela dieléctrica conserva acoplamiento físico.");

        EquipmentState workShoes=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.outerLeatherWorkShoesV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(workShoes)==FeetElectricalContact.INSULATED,
                "La suela de caucho debe aislar aunque el material primario sea cuero.");

        EquipmentState industrial=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(industrial)==FeetElectricalContact.INSULATED,
                "La bota industrial aísla por caucho; la puntera de acero no crea una ruta a tierra.");
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.groundedByFeet(industrial) && GroundingPolicy.insulatedByFeet(industrial),
                "AISLADO y TOMA A TIERRA deben ser estados distintos.");

        // La clase de armadura no decide la electricidad.
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.leatherHeavyWorkBootsV881().materialClass()==ArmorMaterialClass.HEAVY,
                "La bota industrial debe seguir siendo HEAVY por su acero.");
        org.junit.jupiter.api.Assertions.assertTrue(!Files.readString(Path.of("src/main/java/domain/inventory/equipment/GroundingPolicy.java"))
                .contains("materialClass() == ArmorMaterialClass.HEAVY"),
                "GroundingPolicy no puede depender de LIGHT/MEDIUM/HEAVY.");

        // Corrección pendiente.
        profile(ArmorCatalog.ebonyWarriorV881LeftBracer(),95,100,85);

        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        int start=source.indexOf("//  — FEET LIGHT/OUTER");
        int end=source.indexOf("public static ArmorPiece workshopGoggles()",start);
        org.junit.jupiter.api.Assertions.assertTrue(!source.substring(start,end).contains("new InventoryFootprint("),
                "El bloque FEET  no debe declarar footprints manuales.");
    }

    private static void fp(ArmorPiece p,int v,int h){
        org.junit.jupiter.api.Assertions.assertTrue(p.footprint().verticalSlots()==v && p.footprint().horizontalSlots()==h,
                p.name()+" footprint inesperado: "+p.footprint());
    }
    private static void profile(ArmorPiece p,int a,int b,int c){
        org.junit.jupiter.api.Assertions.assertTrue(p.protection().equals(new ArmorProtectionProfile(a,b,c)),
                p.name()+" perfil inesperado: "+p.protection());
    }
    private static void close(double a,double b,String m){
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(a-b)<1e-9,m+": "+a);
    }
    
}

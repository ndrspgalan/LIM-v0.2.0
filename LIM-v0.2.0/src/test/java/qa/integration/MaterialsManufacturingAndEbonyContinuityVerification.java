package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.*;
import domain.inventory.catalog.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.maintenance.*;
import domain.social.Profession;

import java.util.*;

public final class MaterialsManufacturingAndEbonyContinuityVerification {
    private static final double EPS=1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        materialsAreCanonicalPhysicalUnits();
        everyMaterialHasMarketReference();
        ebonyContinuityIsSingleAndConsistent();
        repairToolsMatchTheirActualScopes();
        professionalBriefcaseManufacturesComposite();
        coolantUsesSingleCanonicalRecipe();
    }

    private static void materialsAreCanonicalPhysicalUnits(){
        var materials=MaterialCatalog.allCanonicalUnits();
        org.junit.jupiter.api.Assertions.assertTrue(materials.size()==17,"Deben existir 17 materiales canónicos.");
        for(MaterialItem item:materials){
            org.junit.jupiter.api.Assertions.assertTrue(item.maximumStack()==1 && item.currentUses()==1,item.name()+" debe ser una unidad física.");
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.containsName(item.name()),"Material no registrado: "+item.name());
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(item)==PhysicalStorageSemantics.INDIVIDUAL,
                    item.name()+" no puede conservar stacking genérico.");
            org.junit.jupiter.api.Assertions.assertTrue(item.physicalDimensions().xSlots()>0 && item.physicalDimensions().ySlots()>0 && item.physicalDimensions().zSlots()>0,
                    item.name()+" debe poseer XYZ.");
            org.junit.jupiter.api.Assertions.assertTrue(item.footprint().equals(InventoryVolumeProjectionPolicy.footprint(item.physicalDimensions())),
                    item.name()+" debe derivar su footprint desde XYZ.");
            org.junit.jupiter.api.Assertions.assertTrue(item.weightKg()>0,item.name()+" necesita masa unitaria.");
        }
        expectFail(()->MaterialCatalog.hardenedLeather(2));
    }

    private static void everyMaterialHasMarketReference(){
        org.junit.jupiter.api.Assertions.assertTrue(MaterialMarketCatalog.all().size()==ArmorMaterial.values().length,
                "Todo material debe tener referencia de mercado.");
        for(ArmorMaterial material:ArmorMaterial.values()){
            var p=MaterialMarketCatalog.profile(material);
            org.junit.jupiter.api.Assertions.assertTrue(p.referencePriceValeritasPerUnit()>0,material.label()+" sin precio.");
            org.junit.jupiter.api.Assertions.assertTrue(p.marketNarrative().length()>80,material.label()+" necesita justificación de mercado.");
            org.junit.jupiter.api.Assertions.assertTrue(!p.interestedProfessions().isEmpty(),material.label()+" necesita demanda profesional.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(MaterialMarketCatalog.profile(ArmorMaterial.EBONY_WOOD).referencePriceValeritasPerUnit()
                <= MaterialMarketCatalog.profile(ArmorMaterial.STEEL).referencePriceValeritasPerUnit(),
                "La casi extinción del ébano no debe haber generado una prima especulativa automática.");
        org.junit.jupiter.api.Assertions.assertTrue(MaterialMarketCatalog.profile(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE).referencePriceValeritasPerUnit()>1000,
                "El Compuesto Electromecánico debe superar un Sueldo de referencia por unidad técnica.");
        var ebony=MaterialCatalog.ebonyWood(1);
        org.junit.jupiter.api.Assertions.assertTrue(ebony.narrativeDescription().contains("Primera Marcha Exaltada"),"Ébano debe anclar la discontinuidad histórica.");
        org.junit.jupiter.api.Assertions.assertTrue(!ebony.narrativeDescription().contains("Segunda de Amatista"),"Debe desaparecer el lore no canónico anterior.");
    }

    private static void ebonyContinuityIsSingleAndConsistent(){
        var historicalChest=ArmorCatalog.historicalEbonyWarriorChest();
        var historicalBracers=ArmorCatalog.historicalEbonyWarriorBracers();
        var historicalLegs=ArmorCatalog.historicalEbonyWarriorLeggings();
        var v881=ArmorCatalog.ebonyWarriorV881Chest();
        var v881Bracer=ArmorCatalog.ebonyWarriorV881LeftBracer();

        org.junit.jupiter.api.Assertions.assertTrue(historicalChest.narrativeDescription().contains("muertos, desaparecidos, exiliados o reintegrados"),
                "La coraza histórica debe explicar el fin social de la antigua casta.");
        org.junit.jupiter.api.Assertions.assertTrue(historicalBracers.narrativeDescription().contains("inflamable"),
                "Los brazales históricos deben concentrarse en la construcción y debilidad material.");
        org.junit.jupiter.api.Assertions.assertTrue(historicalLegs.narrativeDescription().contains("nunca fue equipamiento ordinario"),
                "Las polainas deben explicar la barrera física de la antigua élite.");
        org.junit.jupiter.api.Assertions.assertTrue(v881.narrativeDescription().contains("Kenan")
                        && v881.narrativeDescription().contains("OGC")
                        && v881.narrativeDescription().contains("primer miembro de una nueva casta")
                        && v881.narrativeDescription().contains("Caballero V881"),
                "La coraza V881 debe narrar la resurrección de la figura, no un reciclaje institucional.");
        org.junit.jupiter.api.Assertions.assertTrue(v881Bracer.narrativeDescription().contains("continuidad histórica se preserva en la función"),
                "El brazal V881 debe explicar continuidad funcional, no repetir la historia completa.");
    }

    private static void repairToolsMatchTheirActualScopes(){
        var artisan=RepairToolCatalog.artisanBox();
        var toolbox=RepairToolCatalog.toolbox();
        var professional=new PortableLaboratoryItem();

        org.junit.jupiter.api.Assertions.assertTrue(artisan.narrativeDescription().contains("papel técnico")
                        && artisan.narrativeDescription().contains("tela dieléctrica"),
                "Caja del Artesano debe describir todo su alcance blando.");
        org.junit.jupiter.api.Assertions.assertTrue(toolbox.narrativeDescription().contains("bronce")
                        && toolbox.narrativeDescription().contains("acero")
                        && toolbox.narrativeDescription().contains("vidrio laminado"),
                "Caja de Herramientas debe describir su alcance mecánico.");
        org.junit.jupiter.api.Assertions.assertTrue(professional.name().equals("Maletín profesional de Alicia e Iván"),"Renombrado canónico del maletín.");
        org.junit.jupiter.api.Assertions.assertTrue(professional.narrativeDescription().length()>2000,"El Maletín debe tener una descripción exhaustiva.");
        for(String term:List.of("micrómetros","manómetros","continuidad","alambiques","servomecanismos","Compuesto Electromecánico","Conjunto del Ingeniero")){
            org.junit.jupiter.api.Assertions.assertTrue(professional.narrativeDescription().contains(term),"Falta instrumental/función: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(!domain.inventory.QuickAccessUsePolicy.requiresQuickAccess(professional),
                "Un maletín 10x8 no debe fingir que cabe en acceso rápido.");
    }

    private static void professionalBriefcaseManufacturesComposite(){
        PortableLaboratoryItem briefcase=new PortableLaboratoryItem();
        var inputs=List.<InventoryEntry>of(
                briefcase,
                MaterialCatalog.plateSteel(1),
                MaterialCatalog.bronze(1),
                MaterialCatalog.hardenedLeather(1),
                MaterialCatalog.cloth(1),
                MaterialCatalog.vulcanizedRubber(1),
                MaterialCatalog.dielectricCloth(1)
        );
        InventoryState inventory=inventoryWith(inputs);
        var result=new SpecializedManufacturingService().manufactureElectromechanicalComposite(briefcase,inventory);
        org.junit.jupiter.api.Assertions.assertTrue(result.successful(),"La receta completa debe fabricar el compuesto.");
        org.junit.jupiter.api.Assertions.assertTrue(result.product().orElseThrow() instanceof MaterialItem,"El producto debe ser un material inventariable.");
        MaterialItem product=(MaterialItem)result.product().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(product.material()==ArmorMaterial.ELECTROMECHANICAL_COMPOSITE,"Producto incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(result.consumedInputs().size()==6,"La receta debe consumir seis familias de entrada.");
        org.junit.jupiter.api.Assertions.assertTrue(ElectromechanicalCompositeRecipe.technicalSummary().contains("Maletín profesional"),
                "La receta debe declarar su requisito instrumental.");
        for(InventoryEntry entry:inputs){
            if(entry instanceof MaterialItem m) org.junit.jupiter.api.Assertions.assertTrue(m.isDepleted(),m.name()+" debe consumirse.");
        }
    }

    private static void coolantUsesSingleCanonicalRecipe(){
        PortableLaboratoryItem briefcase=new PortableLaboratoryItem();
        StackableMiscellaneousItem water=MiscellaneousItemCatalog.waterskin();
        StackableMiscellaneousItem mead=MiscellaneousItemCatalog.mead();
        InventoryState inventory=inventoryWith(List.of(briefcase,water,mead));

        int waterBefore=water.currentUses(), meadBefore=mead.currentUses();
        var result=new SpecializedManufacturingService().manufactureCoolant(briefcase,water,mead,inventory);
        org.junit.jupiter.api.Assertions.assertTrue(result.successful() && result.product().orElseThrow() instanceof CoolantBottleItem,
                "Debe producir un uso de refrigerante.");
        org.junit.jupiter.api.Assertions.assertTrue(water.currentUses()==waterBefore-1 && mead.currentUses()==meadBefore-1,
                "Debe consumir exactamente un uso de cada precursor.");
        org.junit.jupiter.api.Assertions.assertTrue(CoolantRecipePolicy.technicalSummary().contains("120 mL")
                        && CoolantRecipePolicy.technicalSummary().contains("Maletín profesional"),
                "La receta de refrigerante debe ser única y explícita.");
    }

    private static InventoryState inventoryWith(List<InventoryEntry> entries){
        EnumMap<InventoryCompartmentType,InventoryCompartment> map=new EnumMap<>(InventoryCompartmentType.class);
        for(InventoryCompartmentType type:InventoryCompartmentType.values())
            map.put(type,InventoryCompartment.empty(type,false));
        map.put(InventoryCompartmentType.BACKPACK,
                new InventoryCompartment(InventoryCompartmentType.BACKPACK,true,
                        new InventoryGridDefinition(60,60),entries,Optional.empty()));
        return new InventoryState(EquipmentState.empty(),QuickAccessBar.empty(),
                new LogisticsState(map,PersonalTransportState.none()));
    }

    private static void expectFail(Runnable r){
        try{r.run();throw new AssertionError("Debía fallar");}
        catch(IllegalArgumentException expected){}
    }
    
}

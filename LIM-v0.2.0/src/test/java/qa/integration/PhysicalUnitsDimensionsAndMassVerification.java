package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.*;
import domain.inventory.catalog.*;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.logistics.*;

import java.util.*;

public final class PhysicalUnitsDimensionsAndMassVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        currenciesRemainDeliberatelyStackable();
        discreteUnitsAreActuallyDiscrete();
        everyRegisteredTypeHasPhysicalDimensions();
        transposedMucusUsesConvertedVolumeAndGeometry();
        tearGrowsInMassAndFootprint();
        ammunitionKeepsStructuralMass();
        quiverHasRealStructureAndAggregatePayload();
        expanderStoredSizeDerivesFromXyz();
    }

    private static void currenciesRemainDeliberatelyStackable(){
        for(CurrencyType type:CurrencyType.values()){
            CurrencyStack c=new CurrencyStack(type,1000);
            org.junit.jupiter.api.Assertions.assertTrue(c.maximumStack()==1000 && c.currentUses()==1000,"Moneda apilable 1000: "+type);
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(c)==PhysicalStorageSemantics.CURRENCY_STACK,
                    "La moneda debe conservar su excepción stackable explícita.");
            close(c.weightKg(),1.0,"1000 monedas de 1 g");
        }
    }

    private static void discreteUnitsAreActuallyDiscrete(){
        for(StackableMiscellaneousItem item:List.of(
                MiscellaneousItemCatalog.stimulantInjection(),
                MiscellaneousItemCatalog.yarrow(),
                MiscellaneousItemCatalog.bogMoss(),
                MiscellaneousItemCatalog.willowBark(),
                MiscellaneousItemCatalog.lucidityEssence(),
                MiscellaneousItemCatalog.irndFlask(),
                ThrowingWeaponCatalog.throwingKnifeV881())){
            org.junit.jupiter.api.Assertions.assertTrue(item.maximumStack()==1 && item.currentUses()==1,
                    item.name()+" debe ser una sola unidad física.");
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(item)==PhysicalStorageSemantics.INDIVIDUAL,
                    item.name()+" debe ocupar su propio rectángulo.");
        }
        close(MiscellaneousItemCatalog.stimulantInjection().weightKg(),.080,"autoinyector");
        close(MiscellaneousItemCatalog.yarrow().weightKg(),.080,"milenrama");
        close(MiscellaneousItemCatalog.bogMoss().weightKg(),.180,"musgo");
        close(MiscellaneousItemCatalog.willowBark().weightKg(),.050,"sauce");
        close(MiscellaneousItemCatalog.lucidityEssence().weightKg(),.040,"lucidez");
        close(MiscellaneousItemCatalog.irndFlask().weightKg(),.050,"I-RND");
        close(ThrowingWeaponCatalog.throwingKnifeV881().weightKg(),.100,"cuchillo");
    }

    private static void everyRegisteredTypeHasPhysicalDimensions(){
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.all().size()>=98,"El catálogo  no puede perder tipos.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectDimensionsCatalog.explicitCount()>=80,
                "La mayoría del repertorio debe tener XYZ auditado explícitamente.");
        for(PhysicalObjectDefinition d:PhysicalObjectCatalog.all()){
            var xyz=PhysicalObjectDimensionsCatalog.dimensionsFor(d.displayName(),new InventoryFootprint(1,1));
            org.junit.jupiter.api.Assertions.assertTrue(xyz.xSlots()>0 && xyz.ySlots()>0 && xyz.zSlots()>0,"XYZ inválido: "+d.displayName());
            var fp=PhysicalObjectDimensionsCatalog.footprintFor(d.displayName(),new InventoryFootprint(1,1));
            org.junit.jupiter.api.Assertions.assertTrue(fp.verticalSlots()>0 && fp.horizontalSlots()>0,"Footprint derivado inválido: "+d.displayName());
        }
        for(var a:AccessoryCatalog.all()){
            org.junit.jupiter.api.Assertions.assertTrue(a.physicalDimensions().xSlots()>0,"Abalorio sin XYZ: "+a.name());
            org.junit.jupiter.api.Assertions.assertTrue(a.weightKg()>0,"Abalorio/trofeo sin masa: "+a.name());
        }
    }

    private static void transposedMucusUsesConvertedVolumeAndGeometry(){
        var yellow=MucusCrystalCatalog.yellow();
        var green=MucusCrystalCatalog.greenish();
        var brown=MucusCrystalCatalog.brown();
        var blood=MucusCrystalCatalog.bloodied();
        var black=MucusCrystalCatalog.blackish();
        close(yellow.weightKg(),.050,"amarillo 50mL");
        close(green.weightKg(),.020,"verde 20mL");
        close(brown.weightKg(),.005,"marrón 5mL");
        close(blood.weightKg(),.0025,"sangre 2,5mL");
        close(black.weightKg(),.001,"negro 1mL");

        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.convertedVolumeMl(MucusCrystalGeometry.TETRAEDRO)==50,"tetra 50mL");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.convertedVolumeMl(MucusCrystalGeometry.OCTAEDRO)==20,"octa 20mL");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.convertedVolumeMl(MucusCrystalGeometry.CUBO)==5,"cubo 5mL");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.convertedVolumeMl(MucusCrystalGeometry.ESFERA)==2.5,"esfera 2,5mL");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.convertedVolumeMl(MucusCrystalGeometry.DODECAEDRO)==1,"dodeca 1mL");

        // El volumen convertido y la fórmula del sólido regular, no una referencia visual, fijan el tamaño.
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalPhysicalPolicy.boundingDimensionMeters(MucusCrystalGeometry.TETRAEDRO)
                > MucusCrystalPhysicalPolicy.boundingDimensionMeters(MucusCrystalGeometry.DODECAEDRO),
                "50mL tetraedro debe poseer mayor envolvente que 1mL dodecaedro.");
        for(var c:List.of(yellow,green,brown,blood,black))
            org.junit.jupiter.api.Assertions.assertTrue(c.footprint().equals(InventoryVolumeProjectionPolicy.footprint(c.physicalDimensions())),
                    "Cristal debe proyectar XYZ: "+c.name());
    }

    private static void tearGrowsInMassAndFootprint(){
        MucusTearItem one=new MucusTearItem(1);
        MucusTearItem full=new MucusTearItem(100);
        close(one.weightKg(),.001,"Lágrima 1mL");
        close(full.weightKg(),.100,"Lágrima 100mL");
        org.junit.jupiter.api.Assertions.assertTrue(full.footprint().occupiedSlots()>=one.footprint().occupiedSlots(),
                "La Lágrima saturada no puede ocupar menos que 1mL.");
        boolean rejected=false; try{new MucusTearItem(101);}catch(IllegalArgumentException e){rejected=true;}
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"Más de 100mL deben nuclear otra Lágrima.");
    }

    private static void ammunitionKeepsStructuralMass(){
        AmmunitionCartridge mag=AmmunitionCatalog.submachineGun9mmMagazine();
        close(mag.weightKg(),.400,"9mm lleno");
        close(mag.structuralWeightKg(),.150,"9mm vacío");
        close(mag.roundWeightKg(),.010,"9mm cartucho");
        org.junit.jupiter.api.Assertions.assertTrue(mag.consumeShots(25),"debe vaciar cargador");
        close(mag.weightKg(),.150,"el cargador vacío no desaparece");

        AmmunitionCartridge anti=AmmunitionCatalog.antiMateriel20mmCartridge();
        close(anti.weightKg(),.650,"20mm lleno");
        org.junit.jupiter.api.Assertions.assertTrue(anti.consumeShots(1),"consume un 20mm");
        close(anti.weightKg(),.520,"sólo desaparecen 0,130kg de proyectil");
        org.junit.jupiter.api.Assertions.assertTrue(anti.footprint().equals(new InventoryFootprint(2,1)),"cassette 20mm XYZ");
    }

    private static void quiverHasRealStructureAndAggregatePayload(){
        var arrows=List.<InventoryEntry>of(
                AmmunitionCatalog.piercingArrow(),
                AmmunitionCatalog.piercingArrow(),
                AmmunitionCatalog.barbedArrow());
        InventoryCompartment q=new InventoryCompartment(InventoryCompartmentType.ARROW_QUIVER,true,arrows);
        org.junit.jupiter.api.Assertions.assertTrue(q.capacitySlots()==12 && q.occupiedSlots()==3 && q.freeSlots()==9,"carcaj 3/12");
        close(InventoryCompartmentType.ARROW_QUIVER.structuralWeightKg(),.650,"masa carcaj vacío");
        double payload=arrows.stream().mapToDouble(InventoryEntry::weightKg).sum();
        close(q.totalWeightKg(),.650+payload,"carcaj + flechas");
        org.junit.jupiter.api.Assertions.assertTrue(q.arrowQuiverContents().orElseThrow().counts().size()==2,"agregación por variante");
    }

    private static void expanderStoredSizeDerivesFromXyz(){
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM.physicalDimensions().isPresent(),"Rotor debe tener XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM.storedFootprint()
                .equals(InventoryVolumeProjectionPolicy.footprint(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM.physicalDimensions().orElseThrow())),
                "Rotor desequipado deriva footprint de XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.ARROW_QUIVER.physicalDimensions().isPresent(),"Carcaj debe tener XYZ");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.ARROW_QUIVER.storedFootprint()
                .equals(InventoryVolumeProjectionPolicy.footprint(InventoryCompartmentType.ARROW_QUIVER.physicalDimensions().orElseThrow())),
                "Carcaj desequipado deriva footprint de XYZ");
    }

    private static void close(double a,double b,String m){ if(Math.abs(a-b)>EPS) throw new AssertionError(m+": "+a+" != "+b); }
    
}

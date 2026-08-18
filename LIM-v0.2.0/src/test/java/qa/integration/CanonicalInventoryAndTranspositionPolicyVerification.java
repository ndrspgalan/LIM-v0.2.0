package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.character.progression.*;
import domain.character.sheet.CharacterSheet;
import domain.inventory.*;
import domain.inventory.catalog.*;
import domain.inventory.equipment.*;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.logistics.*;
import domain.runic.*;
import domain.runic.transposition.*;

import java.util.*;

public final class CanonicalInventoryAndTranspositionPolicyVerification {
    private static final double EPS=1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        canonicalCatalogCoversPhysicalTypes();
        canonicalIdentityIsStable();
        storageSemanticsArePhysical();
        quickAccessRebindsOnlyInsideSource();
        persistentFlaskSurvivesConsumption();
        quiverAggregatesByArrowType();
        mucusUsesCanonicalYieldPolicy();
        crystalsRequireClairvoyance33();
    }

    private static void canonicalCatalogCoversPhysicalTypes(){
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.all().size()>=70,"El catálogo físico unificado debe cubrir el repertorio completo.");

        ArrayList<InventoryEntry> entries=new ArrayList<>();
        entries.add(MiscellaneousItemCatalog.reconnaissanceMonocular());
        entries.add(MiscellaneousItemCatalog.portableLaboratory());
        entries.add(MiscellaneousItemCatalog.artisanBox());
        entries.add(MiscellaneousItemCatalog.toolbox());
        entries.add(MiscellaneousItemCatalog.resinJar());
        entries.add(MiscellaneousItemCatalog.coolantBottle());
        entries.add(MiscellaneousItemCatalog.mercuryStone());
        entries.add(new PortableFuelCanItem(MotorcycleFuelType.ETHANOL));
        entries.add(new PortableFuelCanItem(MotorcycleFuelType.LIGHT_KEROSENE));
        entries.addAll(AccessoryCatalog.all());
        entries.addAll(ThrowingWeaponCatalog.all());
        entries.addAll(List.of(
                AmmunitionCatalog.pneumaticLead46Cartridge(),
                AmmunitionCatalog.autoloadingPistol45Magazine(),
                AmmunitionCatalog.submachineGun9mmMagazine(),
                AmmunitionCatalog.repeatingRifle792x57Clip(),
                AmmunitionCatalog.bifilar46Magazine(),
                AmmunitionCatalog.antiMateriel20mmCartridge(),
                AmmunitionCatalog.clusterRocket85mm(),
                AmmunitionCatalog.limeCartridgeCase(),
                AmmunitionCatalog.pebble(),
                AmmunitionCatalog.piercingArrow(),
                AmmunitionCatalog.barbedArrow(),
                AmmunitionCatalog.bladedArrow(),
                AmmunitionCatalog.tinderArrow(),
                new MucusTearItem(1),
                MucusCrystalCatalog.yellow(),MucusCrystalCatalog.greenish(),MucusCrystalCatalog.brown(),
                MucusCrystalCatalog.bloodied(),MucusCrystalCatalog.blackish()));

        for(InventoryEntry e:entries)
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.containsName(e.name()),"Tipo físico sin registrar: "+e.name());

        for(InventoryCompartmentType t:List.of(
                InventoryCompartmentType.LEG_POUCH,InventoryCompartmentType.BANDOLIER,
                InventoryCompartmentType.BACKPACK,InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,
                InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,
                InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,
                InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN,InventoryCompartmentType.ARROW_QUIVER))
            org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.containsName(t.label()),"Expansor sin registrar: "+t.label());

        long trophies=AccessoryCatalog.all().stream().filter(a->a.name().matches(
                "COLA DE RATA|PLUMA DE CUERVO|PEZUÑA DE CERDO|CERDA DE CABALLO|CAPARAZÓN DE ARMADILLO|CORNAMENTA DE CIERVO|OREJA DE TORO|PIEL DE SERPIENTE|COLMILLO DE JABALÍ|OJO DE LINCE|GARRAS DE ÁGUILA|CRÁNEO DE LOBO|CRIN DE LEÓN|ZARPA DE OSO|CUERNO DE RINOCERONTE")).count();
        org.junit.jupiter.api.Assertions.assertTrue(trophies==15,"Deben existir y estar catalogados los 15 trofeos Ferae.");
    }

    private static void canonicalIdentityIsStable(){
        var a=MiscellaneousItemCatalog.stimulantInjection();
        var b=MiscellaneousItemCatalog.stimulantInjection();
        org.junit.jupiter.api.Assertions.assertTrue(a!=b,"Deben ser instancias físicas distintas.");
        org.junit.jupiter.api.Assertions.assertTrue(a.canonicalTypeId().equals(b.canonicalTypeId()),"El tipo canónico debe sobrevivir al cambio de instancia.");
        org.junit.jupiter.api.Assertions.assertTrue(!a.canonicalTypeId().equals(MiscellaneousItemCatalog.yarrow().canonicalTypeId()),"Tipos distintos no comparten identidad.");
    }

    private static void storageSemanticsArePhysical(){
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(MiscellaneousItemCatalog.stimulantInjection())==PhysicalStorageSemantics.INDIVIDUAL,
                "Inyección = objeto individual.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(MiscellaneousItemCatalog.mead())==PhysicalStorageSemantics.PERSISTENT_CONTAINER,
                "Petaca = recipiente persistente.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.definitionForName("Carcaj para flechas").storageSemantics()==PhysicalStorageSemantics.SPECIALIZED_CONTAINER,
                "Carcaj = contenedor especializado.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(MucusCrystalCatalog.blackish())==PhysicalStorageSemantics.INDIVIDUAL,
                "Cristal = objeto físico individual.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalStoragePolicy.semanticsOf(new MucusTearItem(5))==PhysicalStorageSemantics.PERSISTENT_CONTAINER,
                "Lágrima = masa fusionable persistente, no stack de objetos.");
    }

    private static void quickAccessRebindsOnlyInsideSource(){
        var first=MiscellaneousItemCatalog.stimulantInjection();
        var second=MiscellaneousItemCatalog.stimulantInjection();

        InventoryCompartment pouch=new InventoryCompartment(InventoryCompartmentType.LEG_POUCH,true,List.of(first,second));
        EnumMap<InventoryCompartmentType,InventoryCompartment> map=new EnumMap<>(InventoryCompartmentType.class);
        map.put(InventoryCompartmentType.LEG_POUCH,pouch);
        LogisticsState logistics=new LogisticsState(map,PersonalTransportState.none());
        QuickAccessBar bar=QuickAccessBar.empty().assign(3,first);
        InventoryState state=new InventoryState(EquipmentState.empty(),bar,logistics);

        QuickAccessBinding binding=bar.binding(3).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(binding.sourceCompartment()==InventoryCompartmentType.LEG_POUCH,"Quick 3 debe quedar vinculado a Pernera.");
        org.junit.jupiter.api.Assertions.assertTrue(binding.typeId().equals(first.canonicalTypeId()),"Binding conserva tipo.");

        InventoryState next=new QuickAccessConsumptionPolicy().consume(state,3);
        org.junit.jupiter.api.Assertions.assertTrue(next.quickAccessBar().slots().get(2).orElseThrow()==second,
                "Al consumirse una unidad, Quick debe enlazar la siguiente del mismo tipo en el mismo expansor.");
        org.junit.jupiter.api.Assertions.assertTrue(next.logistics().compartment(InventoryCompartmentType.LEG_POUCH).entries().size()==1,
                "La unidad agotada debe abandonar físicamente el expansor.");
    }

    private static void persistentFlaskSurvivesConsumption(){
        var flask=MiscellaneousItemCatalog.mead(1);
        org.junit.jupiter.api.Assertions.assertTrue(flask.name().equals("Petaca de hidromiel"),"Nombre canónico Petaca de hidromiel.");
        org.junit.jupiter.api.Assertions.assertTrue(flask.statistics().stream().anyMatch(s->s.contains("240 mL"))
                && flask.statistics().stream().anyMatch(s->s.contains("120 mL")),"Petaca 240 mL / 2×120 mL.");

        InventoryCompartment pouch=new InventoryCompartment(InventoryCompartmentType.LEG_POUCH,true,List.of(flask));
        EnumMap<InventoryCompartmentType,InventoryCompartment> map=new EnumMap<>(InventoryCompartmentType.class);
        map.put(InventoryCompartmentType.LEG_POUCH,pouch);
        InventoryState state=new InventoryState(EquipmentState.empty(),QuickAccessBar.empty().assign(3,flask),
                new LogisticsState(map,PersonalTransportState.none()));
        InventoryState next=new QuickAccessConsumptionPolicy().consume(state,3);
        org.junit.jupiter.api.Assertions.assertTrue(flask.isDepleted(),"El contenido debe agotarse.");
        org.junit.jupiter.api.Assertions.assertTrue(next.quickAccessBar().slots().get(2).orElseThrow()==flask,"La petaca vacía persiste en Quick.");
        org.junit.jupiter.api.Assertions.assertTrue(next.logistics().compartment(InventoryCompartmentType.LEG_POUCH).entries().contains(flask),
                "La petaca vacía persiste físicamente.");
    }

    private static void quiverAggregatesByArrowType(){
        List<InventoryEntry> arrows=List.of(
                AmmunitionCatalog.piercingArrow(),AmmunitionCatalog.piercingArrow(),AmmunitionCatalog.piercingArrow(),
                AmmunitionCatalog.barbedArrow(),AmmunitionCatalog.barbedArrow(),
                AmmunitionCatalog.tinderArrow());
        InventoryCompartment quiver=new InventoryCompartment(InventoryCompartmentType.ARROW_QUIVER,true,arrows);
        ArrowQuiverContents c=quiver.arrowQuiverContents().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(c.totalArrows()==6 && c.remainingCapacity()==6,"Carcaj global 6/12.");
        org.junit.jupiter.api.Assertions.assertTrue(c.counts().size()==3,"La UI del carcaj debe agregar por tres tipos presentes.");
        org.junit.jupiter.api.Assertions.assertTrue(c.displayLines().stream().anyMatch(s->s.toLowerCase(java.util.Locale.ROOT).contains("flecha perforante")&&s.contains("×3")),"Perforantes ×3.");
        org.junit.jupiter.api.Assertions.assertTrue(c.displayLines().stream().anyMatch(s->s.contains("×2")),"Barbadas ×2.");

        ArrayList<InventoryEntry> thirteen=new ArrayList<>();
        for(int i=0;i<13;i++) thirteen.add(AmmunitionCatalog.piercingArrow());
        expectFail(()->new InventoryCompartment(InventoryCompartmentType.ARROW_QUIVER,true,thirteen));
    }

    private static void mucusUsesCanonicalYieldPolicy(){
        close(TranspositionYieldPolicy.precursorMlPerCrystal(MucusType.AMARILLENTO),50,"amarillo");
        close(TranspositionYieldPolicy.precursorMlPerCrystal(MucusType.VERDOSO),20,"verde");
        close(TranspositionYieldPolicy.precursorMlPerCrystal(MucusType.MARRON),5,"marrón");
        close(TranspositionYieldPolicy.precursorMlPerCrystal(MucusType.ENSANGRENTADO),2.5,"ensangrentado");
        close(TranspositionYieldPolicy.precursorMlPerCrystal(MucusType.NEGRUZCO),1,"negruzco");

        MucusTearItem tear=new MucusTearItem(1);
        InventoryCompartment body=new InventoryCompartment(InventoryCompartmentType.BODY,true,List.of(tear));
        var white=new TranspositionService().transposeWhite(MucusWallet.of(20,0,0,0,0,0),body,sheet33(),transposition());
        org.junit.jupiter.api.Assertions.assertTrue(white.allowed() && white.mucusConsumed()==20 && tear.currentUses()==21,
                "Todo el mucus blanco debe fundirse en la Lágrima preexistente.");

        MucusWallet bloody=new MucusWallet(Map.of(MucusType.ENSANGRENTADO,5.0));
        var result=new TranspositionService().transposeOne(MucusType.ENSANGRENTADO,bloody,
                InventoryCompartment.empty(InventoryCompartmentType.BODY,true),sheet33(),transposition());
        org.junit.jupiter.api.Assertions.assertTrue(result.allowed(),"5 mL ensangrentados deben permitir un cristal.");
        close(result.mucusConsumed(),2.5,"consumo ensangrentado");
        close(result.wallet().quantityMlOf(MucusType.ENSANGRENTADO),2.5,"remanente ensangrentado");
        org.junit.jupiter.api.Assertions.assertTrue(TranspositionYieldPolicy.doctrineSummary().contains("El volumen no es la materia"),
                "La explicación material debe vivir junto a la política.");
    }

    private static void crystalsRequireClairvoyance33(){
        var crystal=MucusCrystalCatalog.yellow();
        org.junit.jupiter.api.Assertions.assertTrue(crystal.conditionalAttribute()==domain.character.sheet.Attribute.CLARIVIDENCIA
                && crystal.conditionalMinimum()==33,"Cristal: sólo CLARIVIDENCIA 33.");
        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.ACCESSORY,crystal));
        org.junit.jupiter.api.Assertions.assertTrue(eq.effectImmunities(CharacterSheet.of(30,30,30,30,30,30,1,30,32)).values().isEmpty(),
                "FE 1 no activa nada y CLARIVIDENCIA 32 tampoco.");
        org.junit.jupiter.api.Assertions.assertTrue(!eq.effectImmunities(CharacterSheet.of(30,30,30,30,30,30,1,30,33)).values().isEmpty(),
                "CLARIVIDENCIA 33 activa el cristal incluso con FE 1.");
    }

    private static EquipmentState transposition(){
        return new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK,RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION)));
    }
    private static CharacterSheet sheet33(){ return CharacterSheet.of(30,30,30,30,30,30,30,30,33); }
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>EPS) throw new AssertionError(m+": "+a); }
    private static void expectFail(Runnable r){ try{r.run();throw new AssertionError("Debía fallar");}catch(IllegalArgumentException expected){} }
    
}

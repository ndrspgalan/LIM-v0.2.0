package qa.domain;

import domain.bestiarium.BestiaryTaxon;
import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.charisma.*;
import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;
import java.util.*;

public final class CharismaFeraeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        taxonomy(); profiles(); levelIsSum(); sexAndLoot();
    }
    private static void taxonomy(){
        org.junit.jupiter.api.Assertions.assertTrue(Set.of(BestiaryTaxon.values()).equals(EnumSet.of(BestiaryTaxon.FERAE,BestiaryTaxon.ASPIRANT,BestiaryTaxon.ANCIENT,BestiaryTaxon.TRANSCENDED)),"Taxonomía nueva de cuatro familias.");
    }
    private static void profiles(){
        org.junit.jupiter.api.Assertions.assertTrue(FeraeCatalog.branch(FeraeBranch.CARISMA).size()==21,"CARISMA contiene 21 especies tras formalizar las tres yeguas.");
        org.junit.jupiter.api.Assertions.assertTrue(CharismaFeraeProfiles.all().size()==42,"Cada especie CARISMA tiene macho y hembra.");
        for(FeraeSpecies s:FeraeCatalog.branch(FeraeBranch.CARISMA)){
            org.junit.jupiter.api.Assertions.assertTrue(CharismaFeraeProfiles.of(s).size()==2,"Dos sexos: "+s.label());
            org.junit.jupiter.api.Assertions.assertTrue(CharismaFeraeProfiles.of(s,FeraeSex.MACHO).taxon()==BestiaryTaxon.FERAE,"Taxón FERAE.");
        }
    }
    private static void levelIsSum(){
        for(FeraeProfile p:CharismaFeraeProfiles.all())
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel=sumatorio: "+p.species().label()+" "+p.sex());
        org.junit.jupiter.api.Assertions.assertTrue(CharismaFeraeProfiles.of(FeraeSpecies.RATON,FeraeSex.MACHO).canonicalLevel()<CharismaFeraeProfiles.of(FeraeSpecies.ELEFANTE,FeraeSex.MACHO).canonicalLevel()," conserva escala biológica Ratón < Elefante sin fijar niveles históricos.");
        for(FeraeProfile p:CharismaFeraeProfiles.all()) org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(domain.character.sheet.Attribute.FE)==1,": FE animal basal.");
    }
    private static void sexAndLoot(){
        for(FeraeProfile p:CharismaFeraeProfiles.all()){
            if(p.sex()==FeraeSex.HEMBRA) org.junit.jupiter.api.Assertions.assertTrue(p.equippedTrophy().isEmpty(),"Hembra sin trofeo.");
        }
        FeraeLootPolicy loot=new FeraeLootPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(loot.canLoot(ExternalInventoryOwnerState.DEAD),"Muerto habilita pillaje.");
        org.junit.jupiter.api.Assertions.assertTrue(loot.canLoot(ExternalInventoryOwnerState.UNCONSCIOUS),"Inconsciente habilita pillaje.");
        org.junit.jupiter.api.Assertions.assertTrue(!loot.canLoot(ExternalInventoryOwnerState.SLEEPING),"Dormido no habilita pillaje por sí solo; requiere la ruta contextual de Invisibilidad.");
        org.junit.jupiter.api.Assertions.assertTrue(!loot.canLoot(ExternalInventoryOwnerState.CONSCIOUS),"Consciente no habilita pillaje.");
    }
    
}

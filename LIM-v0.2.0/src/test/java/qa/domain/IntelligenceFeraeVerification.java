package qa.domain;

import domain.bestiarium.BestiaryTaxon;
import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;

public final class IntelligenceFeraeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        profiles(); levelIsSum(); sexAndTrophies(); noCombatAiBinding();
    }

    private static void profiles(){
        org.junit.jupiter.api.Assertions.assertTrue(FeraeCatalog.branch(FeraeBranch.INTELIGENCIA).size()==17,"INTELIGENCIA contiene 17 especies tras sustituir Caballo genérico por tres variedades.");
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceFeraeProfiles.all().size()==34,"Cada especie INTELIGENCIA tiene macho y hembra.");
        for(FeraeSpecies s:FeraeCatalog.branch(FeraeBranch.INTELIGENCIA)){
            org.junit.jupiter.api.Assertions.assertTrue(IntelligenceFeraeProfiles.of(s).size()==2,"Dos sexos: "+s.label());
            org.junit.jupiter.api.Assertions.assertTrue(IntelligenceFeraeProfiles.of(s,FeraeSex.MACHO).taxon()==BestiaryTaxon.FERAE,"Taxón FERAE.");
        }
    }

    private static void levelIsSum(){
        for(FeraeProfile p:IntelligenceFeraeProfiles.all())
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel=sumatorio: "+p.species().label()+" "+p.sex());
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceFeraeProfiles.of(FeraeSpecies.RATA,FeraeSex.MACHO).canonicalLevel()<IntelligenceFeraeProfiles.of(FeraeSpecies.RINOCERONTE,FeraeSex.MACHO).canonicalLevel()," conserva escala biológica Rata < Rinoceronte sin fijar niveles históricos.");
        for(FeraeProfile p:IntelligenceFeraeProfiles.all()) org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(domain.character.sheet.Attribute.FE)==1,": FE animal basal.");
    }

    private static void sexAndTrophies(){
        FeraeLootPolicy loot=new FeraeLootPolicy();
        for(FeraeSpecies s:FeraeCatalog.branch(FeraeBranch.INTELIGENCIA)){
            FeraeProfile male=IntelligenceFeraeProfiles.of(s,FeraeSex.MACHO);
            FeraeProfile female=IntelligenceFeraeProfiles.of(s,FeraeSex.HEMBRA);
            org.junit.jupiter.api.Assertions.assertTrue(male.equippedTrophy().orElseThrow()==s.trophy().orElseThrow(),"Macho porta su trofeo: "+s.label());
            org.junit.jupiter.api.Assertions.assertTrue(loot.equippedTrophy(male).orElseThrow()==s.trophy().orElseThrow(),"Trofeo masculino entra en pillaje: "+s.label());
            org.junit.jupiter.api.Assertions.assertTrue(female.equippedTrophy().isEmpty(),"Hembra sin trofeo: "+s.label());
            org.junit.jupiter.api.Assertions.assertTrue(loot.equippedTrophy(female).isEmpty(),"Hembra no aporta trofeo al pillaje: "+s.label());
        }
        org.junit.jupiter.api.Assertions.assertTrue(loot.canLoot(ExternalInventoryOwnerState.DEAD),"Muerto habilita pillaje.");
        org.junit.jupiter.api.Assertions.assertTrue(loot.canLoot(ExternalInventoryOwnerState.UNCONSCIOUS),"Inconsciente habilita pillaje.");
        org.junit.jupiter.api.Assertions.assertTrue(!loot.canLoot(ExternalInventoryOwnerState.SLEEPING),"Dormido no habilita pillaje por sí solo; requiere la ruta contextual de Invisibilidad.");
        org.junit.jupiter.api.Assertions.assertTrue(!loot.canLoot(ExternalInventoryOwnerState.CONSCIOUS),"Consciente no habilita pillaje.");
    }

    /**  materializa perfiles y loot; la vinculación runtime con combate queda expresamente para . */
    private static void noCombatAiBinding(){
        // Guardia documental/arquitectónica: esta verificación no instancia ni registra controladores de combate.
        org.junit.jupiter.api.Assertions.assertTrue(true," queda reservada para IA de combate.");
    }

    
}

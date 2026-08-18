package qa.integration;

import domain.character.CharacterClass;
import domain.economy.AccessoryEconomicCatalog;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.social.*;

import java.util.*;

/**  — contrato de refinamiento canónico de Soldado y Mercenario. */
public final class SoldierMercenaryCanonicalVerification {
    private SoldierMercenaryCanonicalVerification(){}

    private static final Map<Subprofession,Set<CharacterClass>> SOLDIER=Map.ofEntries(
        Map.entry(Subprofession.V881_RIFLEMAN,Set.of(CharacterClass.LUCHADOR)),
        Map.entry(Subprofession.V881_CAMPAIGN_SAPPER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.V881_HEAVY_WEAPONS_SPECIALIST,Set.of(CharacterClass.LUCHADOR,CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.INSTITUTIONAL_SHOCK_COMBATANT,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO)),
        Map.entry(Subprofession.KINGDOM_AGENT,Set.of(CharacterClass.HERALDO)),
        Map.entry(Subprofession.V881_SUPPORT_MARKSWOMAN,Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN,Set.of(CharacterClass.APODERADO)),
        Map.entry(Subprofession.RAILWAY_GUARD,Set.of(CharacterClass.HERALDO,CharacterClass.ESPECIALISTA))
    );

    private static final Map<Subprofession,Set<CharacterClass>> MERCENARY=Map.ofEntries(
        Map.entry(Subprofession.COMPANY_CONTRACTOR,Set.of(CharacterClass.LUCHADOR,CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.CONTRACTUAL_SHOCK_COMBATANT,Set.of(CharacterClass.LUCHADOR)),
        Map.entry(Subprofession.CONVOY_ESCORT,Set.of(CharacterClass.INDOMITO)),
        Map.entry(Subprofession.EXCEPTIONAL_ASSET_RECOVERER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO)),
        Map.entry(Subprofession.MERCENARY_COMPANY_DIRECTOR,Set.of(CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.MOTORCYCLE_COURIER,Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.FRONTIER_SKIRMISHER,Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.MOBILE_ESCORT,Set.of(CharacterClass.APODERADO,CharacterClass.HERALDO)),
        Map.entry(Subprofession.TECHNICAL_RECOVERY_OPERATOR,Set.of(CharacterClass.ESPECIALISTA,CharacterClass.APODERADO)),
        Map.entry(Subprofession.SABOTAGE_DENIAL_SPECIALIST,Set.of(CharacterClass.ESPECIALISTA,CharacterClass.APODERADO))
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyTaxonomy();
        verifyProfiles();
        verifyLoadouts();
        verifyNarrativeAccessories();
        verifyStimulantLegPouch();
        verifyWeaponAccessories();
        verifyEquipmentLevelCoherence();
    }

    private static void verifyTaxonomy(){
        org.junit.jupiter.api.Assertions.assertTrue(SoldierCanonicalProfiles.all().keySet().equals(SOLDIER.keySet()),"Taxonomía Soldado incompleta o extra.");
        org.junit.jupiter.api.Assertions.assertTrue(MercenaryCanonicalProfiles.all().keySet().equals(MERCENARY.keySet()),"Taxonomía Mercenario incompleta o extra.");
        for(var e:SOLDIER.entrySet()) verifySex(e.getKey(),e.getValue(),true);
        for(var e:MERCENARY.entrySet()) verifySex(e.getKey(),e.getValue(),false);
    }

    private static void verifySex(Subprofession s,Set<CharacterClass> active,boolean soldier){
        for(CharacterClass c:CharacterClass.values()){
            boolean expected=active.contains(c);
            boolean actual=!(soldier?SoldierCanonicalProfiles.isDeprecated(s,c):MercenaryCanonicalProfiles.isDeprecated(s,c));
            org.junit.jupiter.api.Assertions.assertTrue(expected==actual,"Estado active/deprecated incorrecto: "+s+"/"+c);
            if(expected) org.junit.jupiter.api.Assertions.assertTrue(!(c==CharacterClass.MAESTRO)," no introduce Maestro.");
        }
    }

    private static void verifyProfiles(){
        verifyProfiles(SOLDIER.keySet(),true);
        verifyProfiles(MERCENARY.keySet(),false);
    }

    private static void verifyProfiles(Set<Subprofession> subs,boolean soldier){
        for(Subprofession s:subs){
            for(CharacterClass c:(soldier?SOLDIER.get(s):MERCENARY.get(s))){
                var p=soldier?SoldierCanonicalProfiles.profile(s,c):MercenaryCanonicalProfiles.profile(s,c);
                org.junit.jupiter.api.Assertions.assertTrue(p.attributes().totalAttributeLevel()>0,"Perfil sin nivel.");
                org.junit.jupiter.api.Assertions.assertTrue(p.genders().size()==1,"Perfil no sexuado correctamente: "+s+"/"+c);
                if(c==CharacterClass.LUCHADOR||c==CharacterClass.INTELECTUAL||c==CharacterClass.INDOMITO)
                    org.junit.jupiter.api.Assertions.assertTrue(p.genders().contains(domain.character.Gender.HOMBRE),"Clase masculina.");
                else org.junit.jupiter.api.Assertions.assertTrue(p.genders().contains(domain.character.Gender.MUJER),"Clase femenina.");
            }
        }
    }

    private static void verifyLoadouts(){
        verifyLoadouts(SOLDIER.keySet(),true);
        verifyLoadouts(MERCENARY.keySet(),false);
    }

    private static void verifyLoadouts(Set<Subprofession> subs,boolean soldier){
        for(Subprofession s:subs) for(CharacterClass c:(soldier?SOLDIER.get(s):MERCENARY.get(s))){
            var e=soldier?SoldierStartingEquipmentCatalog.equipment(s,c):MercenaryStartingEquipmentCatalog.equipment(s,c);
            var p=soldier?SoldierStartingEquipmentCatalog.placement(s,c):MercenaryStartingEquipmentCatalog.placement(s,c);
            p.validateAgainst(e);
            org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().isPresent(),"Falta abalorio: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().stream().filter(x->x.equals("Inyección estimulante")).count()==4,
                    "Cada combatiente debe portar cuatro inyecciones: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(p.contents(InventoryCompartmentType.LEG_POUCH).stream().filter(x->x.equals("Inyección estimulante")).count()==4,
                    "Las cuatro inyecciones deben quedar en la pernera: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue("Inyección estimulante".equals(p.quickAccessBindings().get(3)),"Quick 3 incorrecto: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(e.weaponNames().size()<=2,"Más de dos armas: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(e.weaponNames().stream().noneMatch(x->x.contains("Cuchillo Arrojadizo")||x.contains("Granada")||x.contains("Cápsula")),
                    "Arrojadiza consumiendo slot de arma: "+s+"/"+c);
        }
    }

    private static void verifyNarrativeAccessories(){
        for(var entry:OccupationalNarrativeAccessoryCatalog.allPriced().entrySet()){
            String key=entry.getKey();
            if(!(key.contains("V881_RIFLEMAN")||key.contains("CAMPAIGN_SAPPER")||key.contains("HEAVY_WEAPONS_SPECIALIST")||
                 key.contains("INSTITUTIONAL_SHOCK_COMBATANT")||key.contains("KINGDOM_AGENT")||key.contains("SUPPORT_MARKSWOMAN")||
                 key.contains("STRATEGIC_INSTALLATION_CUSTODIAN")||key.contains("RAILWAY_GUARD")||key.contains("COMPANY_CONTRACTOR")||
                 key.contains("CONTRACTUAL_SHOCK_COMBATANT")||key.contains("CONVOY_ESCORT")||key.contains("EXCEPTIONAL_ASSET_RECOVERER")||
                 key.contains("MERCENARY_COMPANY_DIRECTOR")||key.contains("MOTORCYCLE_COURIER")||key.contains("FRONTIER_SKIRMISHER")||
                 key.contains("MOBILE_ESCORT")||key.contains("TECHNICAL_RECOVERY_OPERATOR")||key.contains("SABOTAGE_DENIAL_SPECIALIST"))) continue;
            var pa=entry.getValue();
            org.junit.jupiter.api.Assertions.assertTrue(pa.priceValeritas()>0,"Abalorio  sin precio.");
            String n=" "+pa.item().narrativeDescription().toLowerCase(Locale.ROOT)+" ";
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(" yo ")||n.contains(" me ")||n.contains(" mi ")||n.contains(" llevo ")||n.contains(" guardo ")||n.contains(" conservo "),
                    "Abalorio no está narrado en primera persona: "+key);
        }
    }

    private static void verifyStimulantLegPouch(){
        for(boolean soldier:List.of(true,false)){
            var map=soldier?SOLDIER:MERCENARY;
            for(var e:map.entrySet()) for(CharacterClass c:e.getValue()){
                var plan=soldier?SoldierStartingEquipmentCatalog.placement(e.getKey(),c):MercenaryStartingEquipmentCatalog.placement(e.getKey(),c);
                org.junit.jupiter.api.Assertions.assertTrue(plan.compartmentContents().containsKey(InventoryCompartmentType.LEG_POUCH),"Falta pernera.");
                org.junit.jupiter.api.Assertions.assertTrue(plan.contents(InventoryCompartmentType.LEG_POUCH).size()>=4,"Pernera sin reserva de estimulantes.");
            }
        }
    }

    private static void verifyWeaponAccessories(){
        verifyWeaponAccessories(SOLDIER.keySet(),true);
        verifyWeaponAccessories(MERCENARY.keySet(),false);
    }

    private static void verifyWeaponAccessories(Set<Subprofession> subs,boolean soldier){
        for(Subprofession s:subs) for(CharacterClass c:(soldier?SOLDIER.get(s):MERCENARY.get(s))){
            var e=soldier?SoldierStartingEquipmentCatalog.equipment(s,c):MercenaryStartingEquipmentCatalog.equipment(s,c);
            for(String w:e.weaponNames()){
                if(w.equals("Cañón Antimaterial V881")){
                    requireItem(e,"Correa de Arma V881");requireItem(e,"Bípode de Arma V881");requireItem(e,"Mirilla Winchester A5 V881");
                } else if(w.equals("Fusil Bifilar Electromagnético V881")){
                    requireItem(e,"Correa de Arma V881");requireItem(e,"Bípode de Arma V881");requireItem(e,"Mirilla Zeiss V881");
                } else if(w.equals("Fusil de Repetición V881")||w.equals("Rifle Neumático de Repetición V881")){
                    requireItem(e,"Correa de Arma V881");requireItem(e,"Mirilla Fiedler V881");
                } else if(w.equals("Lanza-Arcos Electrodinámico V881")||w.equals("Subfusil Automático V881")||w.equals("Rociador de Cal Viva V881")){
                    requireItem(e,"Correa de Arma V881");
                }
            }
        }
    }

    private static void requireItem(CanonicalStartingEquipment e,String name){
        org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().contains(name),"Falta accesorio de arma: "+name);
    }

    private static void verifyEquipmentLevelCoherence(){
        verifyEquipmentLevelCoherence(SOLDIER.keySet(),true);
        verifyEquipmentLevelCoherence(MERCENARY.keySet(),false);
    }

    private static void verifyEquipmentLevelCoherence(Set<Subprofession> subs,boolean soldier){
        for(Subprofession s:subs) for(CharacterClass c:(soldier?SOLDIER.get(s):MERCENARY.get(s))){
            var sheet=soldier?SoldierCanonicalProfiles.profile(s,c).attributes():MercenaryCanonicalProfiles.profile(s,c).attributes();
            var e=soldier?SoldierStartingEquipmentCatalog.equipment(s,c):MercenaryStartingEquipmentCatalog.equipment(s,c);
            int level=sheet.totalAttributeLevel();
            if(e.weaponNames().contains("Cañón Antimaterial V881")||e.weaponNames().contains("Fusil Bifilar Electromagnético V881")
                    ||e.weaponNames().contains("Lanza-Arcos Electrodinámico V881"))
                org.junit.jupiter.api.Assertions.assertTrue(level>=390,"Arma de máxima exigencia en nivel insuficiente: "+s+"/"+c+"="+level);
            if(e.weaponNames().contains("Espadón de Rotor"))
                org.junit.jupiter.api.Assertions.assertTrue(level>=330,"Espadón de Rotor incompatible con nivel bajo: "+s+"/"+c+"="+level);
            if(e.personalTransport().isPresent() && level<290)
                org.junit.jupiter.api.Assertions.assertTrue(level>=290,"Transporte personal incompatible con nivel bajo.");
        }
    }

    
}

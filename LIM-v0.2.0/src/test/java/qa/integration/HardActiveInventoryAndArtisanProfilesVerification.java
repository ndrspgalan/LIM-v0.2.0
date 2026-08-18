package qa.integration;

import domain.character.CharacterClass;
import domain.inventory.QuickAccessPolicy;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.social.*;
import java.util.*;

/**
 *  — verificación dura del bloque artesanal.
 *
 * No se ejecuta automáticamente durante la iteración normal. Protege la taxonomía aprobada,
 * las hojas explícitas, el abalorio biográfico, el patrimonio y la colocación física real.
 */
public final class HardActiveInventoryAndArtisanProfilesVerification {
    private HardActiveInventoryAndArtisanProfilesVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        exactTaxonomyAndActiveMatrix();
        everyActiveProfileIsExplicitAndPriced();
        everyActiveLoadoutHasPhysicalPlacement();
        noForbiddenCivilConsumablesOrThrowablesAsWeapons();
        noDuplicateCanonicalProfilesWithinProfession();
    }

    private static void exactTaxonomyAndActiveMatrix(){
        exact(Profession.HAIRDRESSER,Set.of(Subprofession.BARBER,Subprofession.SALON_HAIRDRESSER));
        exact(Profession.TANNER,Set.of(Subprofession.HIDE_PREPARER,Subprofession.INDUSTRIAL_TANNER,Subprofession.LEATHER_FINISHER_GRADER));
        exact(Profession.DRESSMAKER,Set.of(Subprofession.WORK_TAILOR,Subprofession.PRECISION_PATTERNMAKER,Subprofession.SALON_DRESSMAKER));
        exact(Profession.STONEMASON,Set.of(Subprofession.STONE_SETTER,Subprofession.STONEWORK_MASTER,Subprofession.PRECISION_STONECUTTER));
        exact(Profession.CARPENTER,Set.of(Subprofession.STRUCTURAL_CARPENTER,Subprofession.BENCH_CARPENTER,Subprofession.CABINETMAKER));

        active(Subprofession.BARBER,Set.of(CharacterClass.INTELECTUAL));
        active(Subprofession.SALON_HAIRDRESSER,Set.of(CharacterClass.HERALDO));
        active(Subprofession.HIDE_PREPARER,Set.of(CharacterClass.LUCHADOR));
        active(Subprofession.INDUSTRIAL_TANNER,Set.of(CharacterClass.INTELECTUAL));
        active(Subprofession.LEATHER_FINISHER_GRADER,Set.of(CharacterClass.ESPECIALISTA));
        active(Subprofession.WORK_TAILOR,Set.of(CharacterClass.INTELECTUAL));
        active(Subprofession.PRECISION_PATTERNMAKER,Set.of(CharacterClass.ESPECIALISTA));
        active(Subprofession.SALON_DRESSMAKER,Set.of(CharacterClass.HERALDO));
        active(Subprofession.STONE_SETTER,Set.of(CharacterClass.LUCHADOR));
        active(Subprofession.STONEWORK_MASTER,Set.of(CharacterClass.INTELECTUAL));
        active(Subprofession.PRECISION_STONECUTTER,Set.of(CharacterClass.ESPECIALISTA));
        active(Subprofession.STRUCTURAL_CARPENTER,Set.of(CharacterClass.LUCHADOR));
        active(Subprofession.BENCH_CARPENTER,Set.of(CharacterClass.ESPECIALISTA));
        active(Subprofession.CABINETMAKER,Set.of(CharacterClass.INTELECTUAL));

        int total=artisanSubprofessions().stream().mapToInt(s->activeProfiles(s).size()).sum();
        org.junit.jupiter.api.Assertions.assertTrue(total==14," refina  a exactamente 14 perfiles artesanales activos.");
        for(Subprofession s:artisanSubprofessions())org.junit.jupiter.api.Assertions.assertTrue(isDeprecated(s,CharacterClass.MAESTRO),"Maestro deprecated: "+s);
    }

    private static void everyActiveProfileIsExplicitAndPriced(){
        for(Subprofession s:artisanSubprofessions())for(var entry:activeProfiles(s).entrySet()){
            CharacterClass c=entry.getKey();var p=entry.getValue();
            org.junit.jupiter.api.Assertions.assertTrue(p.attributes().attributeValues().size()==9,"Hoja no explícita de nueve atributos: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().attributeValues().values().stream().mapToInt(Integer::intValue).sum(),"Nivel no derivado: "+s+"/"+c);
            var e=equipment(s,c);var a=e.equippedAccessory().orElseThrow();
            org.junit.jupiter.api.Assertions.assertTrue(a.name().contains(s.name().replace('_',' '))||OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name()).name().equals(a.name()),"Abalorio no asociado al perfil: "+s+"/"+c);
            long price=OccupationalNarrativeAccessoryCatalog.priceValeritasFor(s.name(),c.name());org.junit.jupiter.api.Assertions.assertTrue(price>0,"Precio no positivo: "+s+"/"+c);
            String n=" "+a.narrativeDescription().toLowerCase(Locale.ROOT)+" ";
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(" mi ")||n.contains(" me ")||n.contains(" yo ")||n.contains(" llevo ")||n.contains(" guardo ")||n.contains(" conservo "),"Abalorio no narrado en primera persona: "+s+"/"+c);
        }
    }

    private static void everyActiveLoadoutHasPhysicalPlacement(){
        for(Subprofession s:artisanSubprofessions())for(CharacterClass c:activeProfiles(s).keySet()){
            var e=equipment(s,c);var p=placement(s,c);
            CanonicalActiveInventoryEquipmentPolicy.validate(e,p);
            for(var q:p.quickAccessBindings().entrySet()){
                var source=QuickAccessPolicy.sourceCompartment(q.getKey());
                org.junit.jupiter.api.Assertions.assertTrue(p.contents(source).contains(q.getValue()),"Quick sin fuente física exacta: "+s+"/"+c);
            }
            boolean saddle=e.inventoryExpanders().stream().anyMatch(x->x.name().startsWith("SADDLEBAGS_"));
            if(saddle)org.junit.jupiter.api.Assertions.assertTrue(e.personalTransport().isPresent(),"Alforjas sin transporte: "+s+"/"+c);
        }
    }

    private static void noForbiddenCivilConsumablesOrThrowablesAsWeapons(){
        Set<String> throwing=Set.of("Cuchillo Arrojadizo V881","Cápsula de Gas Amonio V881","Granada Incendiaria de Terracota V881","Granada de Huevo con Fósforo y Azufre V881");
        for(Subprofession s:artisanSubprofessions())for(CharacterClass c:activeProfiles(s).keySet()){
            var e=equipment(s,c);String inv=String.join("|",e.inventoryObjectNames());
            org.junit.jupiter.api.Assertions.assertTrue(!inv.contains("Inyección estimulante")&&!inv.contains("Esencia de lucidez")&&!inv.contains("Frasco de I-RND"),"Consumible excepcional introducido: "+s+"/"+c);
            org.junit.jupiter.api.Assertions.assertTrue(e.weaponNames().stream().noneMatch(throwing::contains),"Arrojadiza consumiendo weapon slot: "+s+"/"+c);
        }
    }

    private static void noDuplicateCanonicalProfilesWithinProfession(){
        for(Profession profession:List.of(Profession.HAIRDRESSER,Profession.TANNER,Profession.DRESSMAKER,Profession.STONEMASON,Profession.CARPENTER)){
            Set<String> signatures=new HashSet<>();
            for(Subprofession s:Subprofession.forProfession(profession))for(var e:activeProfiles(s).entrySet()){
                CharacterClass c=e.getKey();var load=equipment(s,c);var profile=e.getValue();
                String sig=profile.attributes().attributeValues()+"|"+load.wornGarments()+"|"+load.inventoryObjectNames()+"|"+load.weaponNames()+"|"+load.materialUnits()+"|"+load.equippedAccessory().orElseThrow().name();
                org.junit.jupiter.api.Assertions.assertTrue(signatures.add(sig),"Perfiles artesanales idénticos en "+profession+": "+s+"/"+c);
            }
        }
    }

    private static Set<Subprofession> artisanSubprofessions(){
        LinkedHashSet<Subprofession>x=new LinkedHashSet<>();for(Profession p:List.of(Profession.HAIRDRESSER,Profession.TANNER,Profession.DRESSMAKER,Profession.STONEMASON,Profession.CARPENTER))x.addAll(Subprofession.forProfession(p));return Set.copyOf(x);
    }
    private static void exact(Profession p,Set<Subprofession> expected){org.junit.jupiter.api.Assertions.assertTrue(new HashSet<>(Subprofession.forProfession(p)).equals(expected),"Taxonomía  divergente: "+p);}
    private static void active(Subprofession s,Set<CharacterClass> expected){org.junit.jupiter.api.Assertions.assertTrue(activeProfiles(s).keySet().equals(expected),"Matriz activa divergente: "+s+" -> "+activeProfiles(s).keySet());}

    private static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return switch(s.profession()){
        case HAIRDRESSER -> HairdresserCanonicalProfiles.activeProfiles(s);
        case TANNER -> TannerCanonicalProfiles.activeProfiles(s);
        case DRESSMAKER -> DressmakerCanonicalProfiles.activeProfiles(s);
        case STONEMASON -> StonemasonCanonicalProfiles.activeProfiles(s);
        case CARPENTER -> CarpenterCanonicalProfiles.activeProfiles(s);
        default -> throw new IllegalArgumentException("Fuera de : "+s);
    };}
    private static boolean isDeprecated(Subprofession s,CharacterClass c){return switch(s.profession()){
        case HAIRDRESSER -> HairdresserCanonicalProfiles.isDeprecated(s,c);
        case TANNER -> TannerCanonicalProfiles.isDeprecated(s,c);
        case DRESSMAKER -> DressmakerCanonicalProfiles.isDeprecated(s,c);
        case STONEMASON -> StonemasonCanonicalProfiles.isDeprecated(s,c);
        case CARPENTER -> CarpenterCanonicalProfiles.isDeprecated(s,c);
        default -> true;
    };}
    private static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){return switch(s.profession()){
        case HAIRDRESSER -> HairdresserStartingEquipmentCatalog.equipment(s,c);
        case TANNER -> TannerStartingEquipmentCatalog.equipment(s,c);
        case DRESSMAKER -> DressmakerStartingEquipmentCatalog.equipment(s,c);
        case STONEMASON -> StonemasonStartingEquipmentCatalog.equipment(s,c);
        case CARPENTER -> CarpenterStartingEquipmentCatalog.equipment(s,c);
        default -> throw new IllegalArgumentException("Fuera de : "+s);
    };}
    private static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return switch(s.profession()){
        case HAIRDRESSER -> HairdresserStartingEquipmentCatalog.placement(s,c);
        case TANNER -> TannerStartingEquipmentCatalog.placement(s,c);
        case DRESSMAKER -> DressmakerStartingEquipmentCatalog.placement(s,c);
        case STONEMASON -> StonemasonStartingEquipmentCatalog.placement(s,c);
        case CARPENTER -> CarpenterStartingEquipmentCatalog.placement(s,c);
        default -> throw new IllegalArgumentException("Fuera de : "+s);
    };}
    
}

package qa.architecture;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.AttributeCapPolicy;
import domain.character.progression.CharacterClassDefinition;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.social.*;
import java.util.*;

/**  — contrato duro de migración de Comerciante y Jurista. */
public final class MerchantJuristCanonicalVerification {
    private MerchantJuristCanonicalVerification(){}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        verifyTaxonomy(); verifyProfilesAndCaps(); verifyNoObsoletoGeneration(); verifyEquipment(); verifyAccessories(); verifyLevelMateriality();
    }
    private static void verifyTaxonomy(){
        org.junit.jupiter.api.Assertions.assertTrue(MerchantCanonicalProfiles.all().keySet().equals(new HashSet<>(Subprofession.forProfession(Profession.MERCHANT))),"Comerciante incompleto.");
        org.junit.jupiter.api.Assertions.assertTrue(JuristCanonicalProfiles.all().keySet().equals(new HashSet<>(Subprofession.forProfession(Profession.JURIST))),"Jurista incompleto.");
        org.junit.jupiter.api.Assertions.assertTrue(MerchantCanonicalProfiles.all().size()==13,"Comerciante debe tener 13 subprofesiones tras separar el bloque industrial.");
        org.junit.jupiter.api.Assertions.assertTrue(JuristCanonicalProfiles.all().size()==4,"Jurista debe tener 4 subprofesiones.");
    }
    private static void verifyProfilesAndCaps(){
        var policy=new AttributeCapPolicy(GenderSoftcapProfile.canonical(), CharacterClassDefinition.canonicalDefinitions());
        for(var family:List.of(MerchantCanonicalProfiles.all(),JuristCanonicalProfiles.all())) for(var entry:family.entrySet()) for(var e:entry.getValue().entrySet()){
            var p=e.getValue(); org.junit.jupiter.api.Assertions.assertTrue(p.attributes().totalAttributeLevel()>0,"Nivel vacío: "+entry.getKey()+"/"+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(p.genders().size()==1," no materializa Maestro: "+entry.getKey()+"/"+e.getKey());
            Gender g=p.genders().iterator().next();
            for(Attribute a:Attribute.values()) org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(a)<=policy.maximumFor(g,e.getKey(),p.attributes(),a),"Softcap excedido: "+entry.getKey()+"/"+e.getKey()+"/"+a);
            if(e.getKey()==CharacterClass.LUCHADOR||e.getKey()==CharacterClass.INTELECTUAL||e.getKey()==CharacterClass.INDOMITO) org.junit.jupiter.api.Assertions.assertTrue(g==Gender.HOMBRE,"Clase masculina mal sexada.");
            else if(e.getKey()==CharacterClass.ESPECIALISTA||e.getKey()==CharacterClass.APODERADO||e.getKey()==CharacterClass.HERALDO) org.junit.jupiter.api.Assertions.assertTrue(g==Gender.MUJER,"Clase femenina mal sexada.");
        }
    }
    private static void verifyNoObsoletoGeneration(){
        String[] forbidden={"affinityGain","express(","affinity(c)","for (CharacterClass c : CharacterClass.values())"};
        for(String file:List.of("src/main/java/domain/social/MerchantCanonicalProfiles.java","src/main/java/domain/social/JuristCanonicalProfiles.java")){
            try{String s=java.nio.file.Files.readString(java.nio.file.Path.of(file)); for(String x:forbidden) org.junit.jupiter.api.Assertions.assertTrue(!s.contains(x),"Arquitectura heredada en "+file+": "+x);}catch(Exception ex){throw new IllegalStateException(ex);}
        }
    }
    private static void verifyEquipment(){
        for(var family:List.of(MerchantCanonicalProfiles.all(),JuristCanonicalProfiles.all())) for(var entry:family.entrySet()) for(var e:entry.getValue().entrySet()){
            CanonicalStartingEquipment ce=entry.getKey().profession()==Profession.MERCHANT?MerchantStartingEquipmentCatalog.equipment(entry.getKey(),e.getKey()):JuristStartingEquipmentCatalog.equipment(entry.getKey(),e.getKey());
            CanonicalLoadoutPlacementPlan plan=entry.getKey().profession()==Profession.MERCHANT?MerchantStartingEquipmentCatalog.placement(entry.getKey(),e.getKey()):JuristStartingEquipmentCatalog.placement(entry.getKey(),e.getKey());
            plan.validateAgainst(ce); org.junit.jupiter.api.Assertions.assertTrue(ce.equippedAccessory().isPresent(),"Falta abalorio en loadout.");
            org.junit.jupiter.api.Assertions.assertTrue(ce.weaponNames().size()<=2,"Más de dos armas.");
            if(ce.personalTransport().isPresent()) org.junit.jupiter.api.Assertions.assertTrue(ce.inventoryExpanders().stream().anyMatch(x->x.name().startsWith("SADDLEBAGS_")),"Transporte sin alforjas.");
        }
        var rural=MerchantStartingEquipmentCatalog.placement(Subprofession.RURAL_AGGREGATOR,CharacterClass.INDOMITO);
        org.junit.jupiter.api.Assertions.assertTrue(rural.compartmentContents().containsKey(InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT),"Acopiador rural sin logística ecuestre.");
    }
    private static void verifyAccessories(){
        for(var family:List.of(MerchantCanonicalProfiles.all(),JuristCanonicalProfiles.all())) for(var entry:family.entrySet()) for(var c:entry.getValue().keySet()){
            var a=OccupationalNarrativeAccessoryCatalog.forProfile(entry.getKey().name(),c.name());
            org.junit.jupiter.api.Assertions.assertTrue(a!=null,"Abalorio ausente."); org.junit.jupiter.api.Assertions.assertTrue(OccupationalNarrativeAccessoryCatalog.priceValeritasFor(entry.getKey().name(),c.name())>0,"Abalorio sin precio.");
            String n=a.narrativeDescription().toLowerCase(Locale.ROOT); org.junit.jupiter.api.Assertions.assertTrue(n.contains("yo")||n.contains("me")||n.contains("mi")||n.contains("conservo")||n.contains("guardo"),"Narrativa no primera persona.");
        }
    }
    private static void verifyLevelMateriality(){
        // El nivel es siempre la suma real; los perfiles económicos no pueden inyectar nivel desde la renta.
        for(var family:List.of(MerchantCanonicalProfiles.all(),JuristCanonicalProfiles.all())) for(var entry:family.entrySet()) for(var p:entry.getValue().values())
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel no deriva de atributos.");
        org.junit.jupiter.api.Assertions.assertTrue(MerchantCanonicalProfiles.profile(Subprofession.RESTRICTED_MATERIALS_BROKER,CharacterClass.INTELECTUAL).canonicalLevel()>=350,"El corredor restringido carece de escala compatible con su patrimonio profesional.");
        org.junit.jupiter.api.Assertions.assertTrue(JuristCanonicalProfiles.profile(Subprofession.DOCTRINE_CUSTODIAN,CharacterClass.INTELECTUAL).canonicalLevel()>=350,"El custodio de doctrina carece de escala compatible con su patrimonio profesional.");
    }
    
}

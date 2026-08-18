package qa.integration;

import domain.ability.*;
import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.armor.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

public final class ElectricityTribogenesisAndMeleeBalanceVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        canonicalCharacters();
        meleeProfiles();
        linearEvolutiveDamage();
        electricStunUsesNetDamage();
        tribogenesisMaterials();
        electrogenesisAndTribogenesisCanOverlap();
    }
    private static void canonicalCharacters(){
        CharacterSheet k=CharacterSheet.kiaraCanonical();
        org.junit.jupiter.api.Assertions.assertTrue(k.totalAttributeLevel()==186,"Los atributos canónicos de Kiara suman 186.");
        org.junit.jupiter.api.Assertions.assertTrue(CharacterSheet.kenanCanonical().totalAttributeLevel()==9,"Kenan CHILD debe comenzar en nivel 9.");
    }
    private static void meleeProfiles(){
        profile(MeleeWeaponCatalog.cuchilloDeCarnicero().modes().getFirst().lethality(),0,65,15,"Carnicero");
        profile(MeleeWeaponCatalog.daga().modes().getFirst().lethality(),65,65,10,"Daga");
        profile(MeleeWeaponCatalog.cimitarra().modes().getFirst().lethality(),0,65,15,"Cimitarra");
    }
    private static void linearEvolutiveDamage(){
        org.junit.jupiter.api.Assertions.assertTrue(EvolutiveIntensityPolicy.intensity(75)==0,"75 no debe producir daño evolutivo.");
        org.junit.jupiter.api.Assertions.assertTrue(EvolutiveIntensityPolicy.intensity(76)==1,"76 debe producir 1 punto.");
        org.junit.jupiter.api.Assertions.assertTrue(EvolutiveIntensityPolicy.intensity(100)==25,"100 debe producir 25 puntos.");
        org.junit.jupiter.api.Assertions.assertTrue(EvolutiveIntensityPolicy.intensity(120)==45,"120 debe producir 45 puntos: tramo inclusivo 76-120.");
        org.junit.jupiter.api.Assertions.assertTrue(new ElectrogenesisPolicy().resolveUnarmedContact(CharacterSheet.of(120,1,1,1,1,1,1,1,1),true).electricityDamage()==45,"Electrogénesis debe usar escala lineal.");
    }
    private static void electricStunUsesNetDamage(){
        var naked=new NonConventionalDamageResolver().resolve(DamageType.ELECTRICITY,49,ArmorHitLocation.BODY,EquipmentState.empty(),0,false);
        close(naked.netDamage(),49,"Electricidad neta"); close(naked.stunSeconds(),0.49,"0,01 s por punto neto");
        var resisted=new NonConventionalDamageResolver().resolve(DamageType.ELECTRICITY,100,ArmorHitLocation.BODY,EquipmentState.empty(),50,false);
        close(resisted.netDamage(),50,"Resistencia eléctrica 50 %"); close(resisted.stunSeconds(),0.5,"El stun debe calcularse después de resistencia");
        var burn=new NonConventionalDamageResolver().resolve(DamageType.BURN,100,ArmorHitLocation.BODY,EquipmentState.empty(),0,false);
        close(burn.stunSeconds(),0,"Quemadura no debe heredar stun eléctrico");
    }
    private static void tribogenesisMaterials(){
        TribogenesisPolicy p=new TribogenesisPolicy();
        CharacterSheet a=CharacterSheet.of(1,1,120,1,1,1,1,1,1);
        org.junit.jupiter.api.Assertions.assertTrue(p.burnDamage(a,true,ArmorMaterial.BRONZE)==45,"Bronce activa Tribogénesis.");
        org.junit.jupiter.api.Assertions.assertTrue(p.burnDamage(a,true,ArmorMaterial.STEEL)==45,"Acero activa Tribogénesis.");
        org.junit.jupiter.api.Assertions.assertTrue(p.burnDamage(a,true,ArmorMaterial.ELECTROMECHANICAL_COMPOSITE)==45,"Compuesto activa Tribogénesis.");
        org.junit.jupiter.api.Assertions.assertTrue(p.burnDamage(a,true,ArmorMaterial.WOOD)==0,"Madera no activa Tribogénesis.");
        org.junit.jupiter.api.Assertions.assertTrue(p.burnDamage(a,true,ArmorMaterial.EBONY_WOOD)==0,"Ébano no activa Tribogénesis.");
    }
    private static void electrogenesisAndTribogenesisCanOverlap(){
        CharacterSheet both=CharacterSheet.of(120,1,120,1,1,1,1,1,1);
        int e=new ElectrogenesisPolicy().resolveUnarmedContact(both,true).electricityDamage();
        int b=new TribogenesisPolicy().burnDamage(both,true,ArmorMaterial.STEEL);
        org.junit.jupiter.api.Assertions.assertTrue(e==45&&b==45,"Ambas maestrías deben coexistir en el mismo contacto.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.STEEL.incomingDamageMultiplier(DamageType.ELECTRICITY)==2.0,"Acero debe duplicar electricidad.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.BRONZE.incomingDamageMultiplier(DamageType.ELECTRICITY)==2.0,"Bronce debe duplicar electricidad.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE.incomingDamageMultiplier(DamageType.ELECTRICITY)==1.0,"El compuesto no duplica electricidad.");
    }
    private static void profile(LethalityProfile p,double x,double c,double b,String n){close(p.piercing(),x,n+" P");close(p.slashing(),c,n+" C");close(p.blunt(),b,n+" B");}
    private static void close(double a,double b,String m){if(Math.abs(a-b)>EPS)throw new AssertionError(m+": "+a+" != "+b);}
    
}

package qa.domain;

import domain.ability.*;
import domain.ability.progression.MasteryProgressState;
import domain.ability.progression.MasteryProgressionPolicy;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.*;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.economy.*;
import domain.social.RelationshipType;
import presentation.menu.CharismaNarrative;

public final class IncitementAndCharismaVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        provoke(); warCry(); commerce(); caps(); progression(); narratives(); frost(); catalog();
    }
    private static void provoke(){
        var p=new ProvokeEncounterPolicy();
        var normal=p.resolve(Gender.HOMBRE,40,Gender.HOMBRE,30,true,true,false);
        org.junit.jupiter.api.Assertions.assertTrue(normal.applied()&&!normal.targetLockAllowed()&&close(normal.regenDelaySeconds(),1.2)&&close(normal.fullRecoverySeconds(),5),"Provocar base");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(Gender.HOMBRE,40,Gender.HOMBRE,30,true,true,true).fullRecoverySeconds(),3),"Provocar liberación");
        org.junit.jupiter.api.Assertions.assertTrue(!p.resolve(Gender.HOMBRE,30,Gender.HOMBRE,30,true,true,false).applied(),"FUERZA estrictamente superior");
        org.junit.jupiter.api.Assertions.assertTrue(!p.resolve(Gender.HOMBRE,40,Gender.MUJER,20,true,true,false).applied(),"No contra mujer");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(Gender.HOMBRE,40,Gender.HOMBRE,20,true,false,false).applied(),"Provocar no exige que ya esté atacando melee");
    }
    private static void warCry(){
        var p=new WarCryStaminaPolicy(); org.junit.jupiter.api.Assertions.assertTrue(!p.prepare(Gender.HOMBRE,99,100).accepted(),"PA incompletos"); org.junit.jupiter.api.Assertions.assertTrue(p.prepare(Gender.HOMBRE,100,100).accepted(),"PA completos");
        var man=p.resolveNextConnectedMelee(Gender.HOMBRE,20,true); org.junit.jupiter.api.Assertions.assertTrue(man.opportunityConsumed()&&man.freeHit()&&close(man.staminaCost(),0),"Golpe hombre gratuito");
        var woman=p.resolveNextConnectedMelee(Gender.MUJER,20,true); org.junit.jupiter.api.Assertions.assertTrue(woman.opportunityConsumed()&&!woman.freeHit()&&close(woman.staminaCost(),20),"Mujer consume oportunidad sin gratuidad");
    }
    private static void commerce(){
        org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.MUJER,Gender.HOMBRE,21,20,EconomicGoodType.FIRST_NECESSITY),1),"Capitalizar +1 necesidad");
        org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.MUJER,Gender.HOMBRE,30,20,EconomicGoodType.FIRST_NECESSITY),4),"Capitalizar +10 necesidad");
        org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.MUJER,Gender.HOMBRE,30,20,EconomicGoodType.SOCIAL_INTEREST),10),"Capitalizar social");
        org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.MUJER,Gender.HOMBRE,30,20,EconomicGoodType.PRIVATE_USE),30),"Capitalizar privativo");
        org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.HOMBRE,Gender.HOMBRE,100,1,EconomicGoodType.PRIVATE_USE),0),"Capitalizar sólo mujer-hombre");
        double[][] e={{100,66.2,100},{100,100,49.9},{8.2,52.4,.4},{.2,12.3,0},{0,1.3,0}};
        RelationshipType[] r={RelationshipType.FRIENDLY,RelationshipType.RELIABLE,RelationshipType.INDIFFERENT,RelationshipType.DISTRUSTFUL,RelationshipType.ANTIPATHETIC};
        EconomicGoodType[] t=EconomicGoodType.values(); for(int i=0;i<r.length;i++)for(int j=0;j<t.length;j++)org.junit.jupiter.api.Assertions.assertTrue(close(IncitementCommercePolicy.profitableSaleBonusPercent(Gender.MUJER,Gender.HOMBRE,r[i],t[j]),e[i][j]),"Matriz Rentabilizar");
    }
    private static void caps(){
        var soft=GenderSoftcapProfile.canonical(); org.junit.jupiter.api.Assertions.assertTrue(soft.softcaps(Gender.HOMBRE,Attribute.CARISMA).equals(java.util.List.of(25,50)),"Caps hombre"); org.junit.jupiter.api.Assertions.assertTrue(soft.softcaps(Gender.MUJER,Attribute.CARISMA).equals(java.util.List.of(18,21,40)),"Caps mujer");
        var policy=new AttributeCapPolicy(soft,CharacterClassDefinition.canonicalDefinitions());
        CharacterSheet base=CharacterSheet.of(20,20,20,20,20,20,20,40,20);
        org.junit.jupiter.api.Assertions.assertTrue(policy.maximumFor(Gender.HOMBRE,CharacterClass.INTELECTUAL,base,Attribute.CARISMA)==50,"Hardcap hombre 50");
        org.junit.jupiter.api.Assertions.assertTrue(policy.maximumFor(Gender.MUJER,CharacterClass.ESPECIALISTA,base,Attribute.CARISMA)==40,"Mujer no afín 40");
        org.junit.jupiter.api.Assertions.assertTrue(policy.maximumFor(Gender.MUJER,CharacterClass.HERALDO,base,Attribute.CARISMA)==75,"HERALDO afín 75");
    }
    private static void progression(){
        var c=CharacterMasteryCollection.forClass(CharacterClass.HERALDO); var p=new MasteryProgressionPolicy();
        p.evaluate(c,CharacterSheet.of(1,1,1,1,1,1,1,18,1),new MasteryProgressState(),Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(c.isStageUnlocked(MasteryId.INCITAR,"CAPITALIZAR"),"Capitalizar 18");
        p.evaluate(c,CharacterSheet.of(1,1,1,1,1,1,1,21,1),new MasteryProgressState(),Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(c.isStageUnlocked(MasteryId.INCITAR,"RENTABILIZAR"),"Rentabilizar 21");
    }
    private static void narratives(){
        org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(18,Gender.MUJER).toLowerCase().contains("capital erótico"),"Narrativa capital erótico");
        String n=CharismaNarrative.descriptionFor(21,Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(n.toLowerCase().contains("capital erótico")&&n.toLowerCase().contains("ahorro social"),"Narrativa acumulativa");
        org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(50,Gender.HOMBRE).contains("Papuchón consumado"),"Papuchón");
    }
    private static void frost(){
        var p=new domain.combat.stamina.StaminaLoadRecoveryPolicy(); org.junit.jupiter.api.Assertions.assertTrue(close(p.resolveFrost(100,0,60,false).fullRecoverySeconds(),5),"Frío 5"); org.junit.jupiter.api.Assertions.assertTrue(close(p.resolveFrost(100,0,60,true).fullRecoverySeconds(),3),"Frío 3"); org.junit.jupiter.api.Assertions.assertTrue(!p.resolveFrost(100,0,60,false).immobilized(),"Frío no inmoviliza");
    }
    private static void catalog(){
        var m=(StructuredMastery)MasteryCatalog.require(MasteryId.INCITAR); org.junit.jupiter.api.Assertions.assertTrue(m.narrativeDescription().contains("capital erótico")&&m.narrativeDescription().contains("ahorro social"),"Narrativa general renovada"); org.junit.jupiter.api.Assertions.assertTrue(m.stages().stream().map(MasteryStage::name).toList().equals(java.util.List.of("PROVOCAR","GRITO DE GUERRA","CAPITALIZAR","RENTABILIZAR")),"Nombres canónicos");
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;} 
}

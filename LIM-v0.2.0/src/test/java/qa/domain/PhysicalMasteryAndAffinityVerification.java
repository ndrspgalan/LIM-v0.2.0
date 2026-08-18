package qa.domain;

import domain.ability.*;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.StaggerPolicy;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.combat.CombatStaminaCostPolicy;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.social.RelationshipType;

import java.util.List;

public final class PhysicalMasteryAndAffinityVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        recoil(); feintAndPulsion(); attacksAndFlow(); massCosts(); refinement(); faithAndAffinity(); restorationAndCustody(); nullification(); provoke();
    }
    private static void recoil(){
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.knockbackDistanceMeters(1),.5),"FUERZA 1 -> 0,5 m");
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.knockbackDistanceMeters(50),2),"FUERZA 50 -> 2 m");
        org.junit.jupiter.api.Assertions.assertTrue(close(StaggerPolicy.knockbackDistanceMeters(100),2),"retroceso cap 50");
    }
    private static void feintAndPulsion(){
        var p=new PulsionCombatPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.feintStaminaCost(35,true),5)&&close(p.feintStaminaCost(50,true),3.5),"Reciclaje finta 5->3,5");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.jumpHeightMultiplier(35,true),1)&&close(p.jumpHeightMultiplier(50,true),1.5),"Reciclaje salto x1->x1,5");
        org.junit.jupiter.api.Assertions.assertTrue(new CombatTechniqueUnlockPolicy().canFeint(35)&&!new CombatTechniqueUnlockPolicy().canFeint(34),"Fintar DEX35");
    }
    private static void attacksAndFlow(){
        org.junit.jupiter.api.Assertions.assertTrue(close(AttackKind.LIGHT.staminaMultiplier(),1)&&close(AttackKind.HEAVY.staminaMultiplier(),1.2)&&close(AttackKind.CHARGED.staminaMultiplier(),1.3)&&close(AttackKind.JUMP.staminaMultiplier(),1.3),"PA melee");
        org.junit.jupiter.api.Assertions.assertTrue(close(AttackKind.CHARGED.bluntMultiplier(),1.3),"cargado B x1,3");
        var flow=new ConvergentTrajectoryPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(flow.onLightAttack(true,3,3,false,true),1.4)&&flow.unarmedChainOpen(),"remate abre Flow x1,4");
        org.junit.jupiter.api.Assertions.assertTrue(close(flow.onLightAttack(true,1,3,false,true),1.4),"Flow fijo x1,4");
        org.junit.jupiter.api.Assertions.assertTrue(close(flow.onLightAttack(true,3,3,false,true),1.4)&&!flow.unarmedChainOpen(),"otro combo cierra Flow sin escalar");
    }
    private static void massCosts(){
        var p=new CombatStaminaCostPolicy();
        var knife=ThrowingWeaponCatalog.throwingKnifeV881();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.remoteUseCost(knife),.100),"arrojadiza usa masa de una unidad, no del stack");
        var sling=RangedWeaponCatalog.sling(); org.junit.jupiter.api.Assertions.assertTrue(close(p.remoteUseCost(sling),sling.weightKg()),"arma a distancia usa peso real");
    }
    private static void refinement(){
        PairMastery pair=(PairMastery)MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA);
        org.junit.jupiter.api.Assertions.assertTrue(pair.name().equals("REFINAMIENTO DE ENERGÍA MALDITA"),"nombre par");
        org.junit.jupiter.api.Assertions.assertTrue(pair.original().name().equals("EXPLOSIÓN CINÉTICA")&&pair.original().type()==MasteryType.SUSTAINED,"explosión sostenida");
        org.junit.jupiter.api.Assertions.assertTrue(pair.refined().name().equals("ENDURECIMIENTO POTENCIAL")&&pair.refined().type()==MasteryType.SUSTAINED,"endurecimiento sostenido");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryGenderUnlockPolicy.kineticExplosionEndurance(Gender.HOMBRE)==20&&MasteryGenderUnlockPolicy.kineticExplosionEndurance(Gender.MUJER)==15,"umbrales explosión");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryGenderUnlockPolicy.potentialHardeningEndurance(Gender.HOMBRE)==40&&MasteryGenderUnlockPolicy.potentialHardeningEndurance(Gender.MUJER)==30,"umbrales endurecimiento");
        var trigger=new MalignantEnergyRefinementPolicy(); org.junit.jupiter.api.Assertions.assertTrue(trigger.canTrigger(0)&&!trigger.canTrigger(.01),"trigger PA=0");
        org.junit.jupiter.api.Assertions.assertTrue(trigger.mutuallyExclusive(MalignantEnergyRefinementPolicy.Mode.KINETIC_EXPLOSION,MalignantEnergyRefinementPolicy.Mode.POTENTIAL_HARDENING),"exclusión mutua");
    }
    private static void faithAndAffinity(){
        var s=GenderSoftcapProfile.canonical(); org.junit.jupiter.api.Assertions.assertTrue(s.softcaps(Gender.HOMBRE,Attribute.FE).equals(List.of(3,13,32,40,60))&&s.softcaps(Gender.MUJER,Attribute.FE).equals(List.of(3,13,32,40,60)),"softcaps FE");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryResonancePolicy.resonates(MasteryId.INCITAR,CharacterClass.LUCHADOR,Gender.HOMBRE),"Incitar hombre Luchador");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryResonancePolicy.resonates(MasteryId.INCITAR,CharacterClass.HERALDO,Gender.MUJER),"Incitar mujer Heraldo");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.ESPIRITU_INFATIGABLE).resonanceClass()==CharacterClass.APODERADO,"Espíritu -> Apoderado");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.REGENERACION_THETA).resonanceClass()==CharacterClass.APODERADO,"Theta -> Apoderado");
        StructuredMastery sanar=(StructuredMastery)MasteryCatalog.require(MasteryId.SANAR);
        org.junit.jupiter.api.Assertions.assertTrue(sanar.stages().stream().anyMatch(x->x.name().equals("RESTAURAR")&&x.threshold()==40),"Restaurar FE40");
        org.junit.jupiter.api.Assertions.assertTrue(sanar.stages().stream().anyMatch(x->x.name().equals("CUSTODIA")&&x.natures().contains(MasteryType.PASSIVE)&&x.threshold()==60),"Custodia FE60 pasiva");
    }
    private static void restorationAndCustody(){
        var r=new RestorePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!r.resolve(0,200,30,4,1).applied(),"0 PV está muerto: RESTAURAR prohibido");
        var heal=r.resolve(20,200,30,4,1); org.junit.jupiter.api.Assertions.assertTrue(heal.applied()&&close(heal.healthAfter(),24)&&close(heal.ceilingHealth(),30),"techo absoluto 30 y ritmo receptor");
        org.junit.jupiter.api.Assertions.assertTrue(!r.resolve(30,200,30,4,1).applied(),"no supera PV=PA máximos restaurador");
        var c=new CustodyRegenerationPolicy().resolve(5,List.of(
                new CustodyRegenerationPolicy.Member("a",RelationshipType.FRIENDLY,2,2),
                new CustodyRegenerationPolicy.Member("b",RelationshipType.RELIABLE,4,3),
                new CustodyRegenerationPolicy.Member("c",RelationshipType.HOSTILE,1,99),
                new CustodyRegenerationPolicy.Member("d",RelationshipType.ROMANTIC,6,10)));
        org.junit.jupiter.api.Assertions.assertTrue(close(c.sharedHealthRegen(),5)&&c.inhibitionImmune()&&c.beneficiaries().equals(List.of("a","b")),"Custodia filtra relación/radio, suma e inmuniza; sin repulsión");
    }
    private static void nullification(){
        org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.eligible(RelationshipType.HOSTILE,10,9)&&!NullificationPolicy.eligible(RelationshipType.FRIENDLY,10,9)&&!NullificationPolicy.eligible(RelationshipType.HOSTILE,9,10),"Anulación hostil + AGUANTE superior");
        var n=NullificationPolicy.apply(RelationshipType.HOSTILE,10,9,true,"Farolillo Lunar",false);
        org.junit.jupiter.api.Assertions.assertTrue(n.suppressed()&&!NullificationPolicy.accessoryPropertyUsable(n,"Farolillo Lunar")&&NullificationPolicy.accessoryPropertyUsable(n,"Otro abalorio"),"sólo abalorio capturado");
        org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.masteryUsable(n)&&NullificationPolicy.runicMarkUsable(n),"no inhibe maestrías ni marcas");
        for(var d: NullificationDeliveryPolicy.Delivery.values())org.junit.jupiter.api.Assertions.assertTrue(NullificationDeliveryPolicy.incidentalCanApply(d),"vías incidental");
    }
    private static void provoke(){
        var p=new ProvokeEncounterPolicy(); var r=p.resolve(Gender.HOMBRE,30,Gender.HOMBRE,20,true,false,false);
        org.junit.jupiter.api.Assertions.assertTrue(r.applied()&&close(r.regenDelaySeconds(),1.2)&&close(r.fullRecoverySeconds(),5),"Provocar no exige que ya ataque al usuario");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(Gender.HOMBRE,30,Gender.HOMBRE,20,true,false,true).fullRecoverySeconds(),3),"Liberación helicoidal 3 s");
    }
    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;} 
}

package qa.domain;

import domain.ability.*;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.StaminaRecovery;
import domain.combat.*;
import domain.combat.stamina.*;
import domain.environment.*;
import domain.inventory.equipment.EquipmentState;

public final class StaminaEnvironmentAndTransmutationVerification {
    private static final CharacterSheet SHEET=CharacterSheet.of(30,40,30,20,20,30,21,20,30);
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        staminaDelay(); loadRecovery(); spiritAndOptimization(); environment(); transmutation();
    }
    static void staminaDelay(){
        StaminaRegenerationDelayPolicy p=new StaminaRegenerationDelayPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.naturalDelaySeconds(SHEET),0.80),"AGUANTE 40 debe reducir 1,20 s a 0,80 s.");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.delaySeconds(SHEET, EquipmentState.empty(), NullificationPolicy.SuppressionState.none(),true),1.20),"Frío debe forzar 1,20 s.");
    }
    static void loadRecovery(){
        StaminaLoadRecoveryPolicy p=new StaminaLoadRecoveryPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,10,60,false).fullRecoverySeconds(),1),"<=1/3 normal 1 s");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,30,60,false).fullRecoverySeconds(),1.5),"1/3-2/3 normal 1,5 s");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,50,60,false).fullRecoverySeconds(),3),">2/3 normal 3 s");
        StaminaRecovery over=p.resolve(100,60,60,false); org.junit.jupiter.api.Assertions.assertTrue(over.immobilized()&&close(over.fullRecoverySeconds(),5)&&over.pointsPerSecond()>0,">=100% inmoviliza pero regenera en 5 s");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,10,60,true).fullRecoverySeconds(),.5),"helicoidal <=1/3 0,5 s");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,30,60,true).fullRecoverySeconds(),1),"helicoidal medio 1 s");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.resolve(100,50,60,true).fullRecoverySeconds(),1.5),"helicoidal alto 1,5 s");
        StaminaRecovery h=p.resolve(100,60,60,true); org.junit.jupiter.api.Assertions.assertTrue(h.immobilized()&&close(h.fullRecoverySeconds(),3)&&h.pointsPerSecond()>0,"helicoidal >=100% 3 s e inmoviliza");
    }
    static void spiritAndOptimization(){
        org.junit.jupiter.api.Assertions.assertTrue(SpiritInfatigablePolicy.globalStaminaCost(25,true,false)==0,"Espíritu debe anular cualquier PA fuera de combate.");
        org.junit.jupiter.api.Assertions.assertTrue(SpiritInfatigablePolicy.globalStaminaCost(25,true,true)==25,"Espíritu no actúa en combate.");
        StaminaRegenerationInhibitionPolicy p=new StaminaRegenerationInhibitionPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(p.canRegenerate(true,true)&&!p.canRegenerate(true,false),"Optimización debe inmunizar inhibición PA REGEN.");
    }
    static void environment(){
        StaminaRecovery r=new StaminaLoadRecoveryPolicy().resolve(40,0,40,false);
        EnvironmentalExposure tox=new EnvironmentalExposure();
        org.junit.jupiter.api.Assertions.assertTrue(close(tox.enter(EnvironmentalAdversity.VIRULENT_TOXICITY,3,SHEET,EquipmentState.empty(),r).requiredExposureSeconds(),3),"Toxicidad build-up 0,1 s por ADAPTABILIDAD.");
        org.junit.jupiter.api.Assertions.assertTrue(close(tox.enter(EnvironmentalAdversity.VIRULENT_TOXICITY,1,SHEET,EquipmentState.empty(),r).rawHealthDamage(),1),"Toxicidad activa 1 PV/s.");
        EnvironmentalExposure burn=new EnvironmentalExposure(); burn.enter(EnvironmentalAdversity.SUFFOCATING_HEAT,3,SHEET,EquipmentState.empty(),r);
        double burnDamage=burn.enter(EnvironmentalAdversity.SUFFOCATING_HEAT,1,SHEET,EquipmentState.empty(),r).rawHealthDamage();
        org.junit.jupiter.api.Assertions.assertTrue(close(burnDamage,1),"Quemadura 1 PV/s.");
        HostileEncounterState encounter=new HostileEncounterState(); encounter.begin(); ElementalHealthRegenerationPolicy hp=new ElementalHealthRegenerationPolicy();
        hp.registerEnvironmentalDamage(EnvironmentalAdversity.SUFFOCATING_HEAT,burnDamage,encounter); org.junit.jupiter.api.Assertions.assertTrue(!hp.healthRegenerationAllowed(encounter,false),"Quemadura Asfixiante activa debe inhibir PV REGEN.");
        ElementalHealthRegenerationPolicy toxHp=new ElementalHealthRegenerationPolicy(); toxHp.registerEnvironmentalDamage(EnvironmentalAdversity.VIRULENT_TOXICITY,1,encounter); org.junit.jupiter.api.Assertions.assertTrue(toxHp.healthRegenerationAllowed(encounter,false),"Toxicidad Virulenta no debe inhibir PV REGEN por sí sola.");
        EnvironmentalExposure frost=new EnvironmentalExposure(); var fr=frost.enter(EnvironmentalAdversity.BITING_FROST,30,SHEET,EquipmentState.empty(),r); org.junit.jupiter.api.Assertions.assertTrue(fr.active()&&fr.rawHealthDamage()==0,"Frío build-up 1 s/punto y no drena PV.");
        var fx=new EnvironmentalFunctionalEffectsPolicy().resolveBitingFrost(100,0,60,false); org.junit.jupiter.api.Assertions.assertTrue(!fx.targetLockAllowed()&&close(fx.staminaRegenDelaySeconds(),1.2)&&close(fx.staminaRecovery().fullRecoverySeconds(),5.0)&&fx.tremor().active(),"Frío: tres tercios, delay, sin lock, tembleque.");
    }
    static void transmutation(){
        TransmutationMastery t=(TransmutationMastery)MasteryCatalog.require(MasteryId.TRANSMUTACION);
        var n=t.orderedNodes();
        org.junit.jupiter.api.Assertions.assertTrue(n.get(0).id()==TransmutationNodeId.OVERCLOCK&&n.get(0).requirementMinimum()==11&&n.get(0).type()==MasteryType.SUSTAINED,"Overclock CL11 sostenida.");
        org.junit.jupiter.api.Assertions.assertTrue(n.get(1).id()==TransmutationNodeId.OVERDRIVE&&n.get(1).requirementMinimum()==22,"Overdrive CL22.");
        org.junit.jupiter.api.Assertions.assertTrue(n.get(2).id()==TransmutationNodeId.METAMORPHOSIS&&n.get(2).requirementMinimum()==33&&n.get(2).type()==MasteryType.SUSTAINED,"Metamorphosis CL33 sostenida.");
        org.junit.jupiter.api.Assertions.assertTrue(n.get(4).id()==TransmutationNodeId.MIRRORS_EDGE&&n.get(4).type()==MasteryType.PASSIVE&&n.get(4).requirementMinimum()==75,"Mirror's Edge CL75 pasiva.");
        MetamorphosisDamagePolicy m=new MetamorphosisDamagePolicy(); org.junit.jupiter.api.Assertions.assertTrue(m.transform(DamageType.CURSE,true)==DamageType.POISON&&m.transform(DamageType.POISON,true)==DamageType.CURSE,"Metamorphosis debe intercambiar Maldición/Veneno.");
        var rec=new IntersticeReciprocityPolicy().resolve(true); org.junit.jupiter.api.Assertions.assertTrue(rec.actorCanDamageIntersticeNormally()&&rec.intersticeCanDamageActorNormally(),"Mirror's Edge debe ser recíproca.");
        org.junit.jupiter.api.Assertions.assertTrue(t.node(TransmutationNodeId.OVERCLOCK).mechanicalDescription().contains("x4")&&t.node(TransmutationNodeId.OVERCLOCK).mechanicalDescription().contains("x2"),"Overclock x4 PV, x2 hambre/sed.");
        org.junit.jupiter.api.Assertions.assertTrue(t.node(TransmutationNodeId.OVERCLOCK).narrativeDescription().contains("segunda intención")&&t.node(TransmutationNodeId.MIRAGE).narrativeDescription().contains("percepción"),"Narrativas deben explicar mecanismo, no repetir efecto.");
    }
    static boolean close(double a,double b){return Math.abs(a-b)<1e-9;} 
}

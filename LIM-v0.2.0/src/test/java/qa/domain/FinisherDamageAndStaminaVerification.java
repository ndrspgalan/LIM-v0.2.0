package qa.domain;

import domain.ability.AttackKind;
import domain.ability.ConvergentTrajectoryPolicy;
import domain.combat.CombatStaminaCostPolicy;
import domain.combat.HeavyAttackImpactPolicy;
import domain.combat.LightComboFinisherPolicy;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

/**  — daño y PA de HEAVY, finishers, Trayectoria Convergente, Flow y H1→L4 de la Maza. */
public final class FinisherDamageAndStaminaVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyCanonicalAttackMultipliers();
        verifyHeavyDamage();
        verifyFinisherProgression();
        verifyFinisherStamina();
        verifyMaceHeavyContexts();
    }

    private static void verifyCanonicalAttackMultipliers() {
        close(AttackKind.LIGHT.staminaMultiplier(),1.0,"LIGHT PA");
        close(AttackKind.HEAVY.staminaMultiplier(),1.20,"HEAVY PA");
        close(AttackKind.CHARGED.staminaMultiplier(),1.30,"CHARGED PA");
        close(AttackKind.JUMP.staminaMultiplier(),1.30,"JUMP PA");
        close(AttackKind.DESTABILIZE.staminaMultiplier(),1.30,"DESTABILIZE PA");
        close(AttackKind.HEAVY.bluntMultiplier(),1.20,"HEAVY contundente ordinario");
    }

    private static void verifyHeavyDamage() {
        var helical=MeleeWeaponCatalog.espadaHelicoidal();
        var heavy=new HeavyAttackImpactPolicy();
        double base=helical.modes().getFirst().lethality().blunt();
        close(heavy.resolve(helical,helical.modes().getFirst()).blunt(),base*1.20,
                "HEAVY ordinario debe aplicar x1,20 contundente");
    }

    private static void verifyFinisherProgression() {
        close(LightComboFinisherPolicy.offensiveMultiplier(false),1.11,"finisher ordinario");
        close(LightComboFinisherPolicy.offensiveMultiplier(true),1.40,"finisher convergente");
        close(LightComboFinisherPolicy.staminaMultiplier(),1.11,"PA finisher fijo");

        var p=new ConvergentTrajectoryPolicy();
        close(p.onLightAttack(false,3,3,false,false),1.11,"finisher sin Trayectoria");
        org.junit.jupiter.api.Assertions.assertTrue(!p.unarmedChainOpen(),"Sin Trayectoria no debe abrir Flow.");
        close(p.onLightAttack(true,3,3,false,true),1.40,"finisher DESARMADO con Trayectoria");
        org.junit.jupiter.api.Assertions.assertTrue(p.unarmedChainOpen(),"Trayectoria debe abrir Flow desarmado.");
        close(p.onLightAttack(true,1,3,false,true),1.40,"Flow mantiene x1,40 ofensivo");
    }

    private static void verifyFinisherStamina() {
        var stamina=new CombatStaminaCostPolicy();
        var axe=MeleeWeaponCatalog.hachaDeLenador();
        close(stamina.cost(axe,WeaponCombatAction.LIGHT_ATTACK,false),axe.weightKg(),"LIGHT ordinario PA x1");
        close(stamina.cost(axe,WeaponCombatAction.LIGHT_ATTACK,true),axe.weightKg()*1.11,"finisher LIGHT PA x1,11");
        close(stamina.cost(axe,WeaponCombatAction.HEAVY_ATTACK,false),axe.weightKg()*1.20,"HEAVY ordinario PA x1,20");
        close(stamina.cost(axe,WeaponCombatAction.JUMP_ATTACK,false),axe.weightKg()*1.30,"JUMP PA x1,30");
        close(stamina.cost(axe,WeaponCombatAction.DESTABILIZE,false),axe.weightKg()*1.30,"DESTABILIZE PA x1,30");
    }

    private static void verifyMaceHeavyContexts() {
        var mace=MeleeWeaponCatalog.mazaElectroMecanicaV881();
        var heavy=new HeavyAttackImpactPolicy();
        var stamina=new CombatStaminaCostPolicy();
        double base=80.0;
        close(heavy.resolve(mace,mace.modes().getFirst(),false,false).blunt(),base*1.11,"Maza H1 normal daño x1,11");
        close(stamina.cost(mace,WeaponCombatAction.HEAVY_ATTACK,false),mace.weightKg()*1.11,"Maza H1 normal PA x1,11");
        close(heavy.resolve(mace,mace.modes().getFirst(),true,false).blunt(),base*1.11,"Maza H1→L4 finisher normal x1,11");
        close(heavy.resolve(mace,mace.modes().getFirst(),true,true).blunt(),base*1.40,"Maza H1→L4 convergente x1,40");
        close(stamina.cost(mace,WeaponCombatAction.HEAVY_ATTACK,true),mace.weightKg()*1.11,"Maza H1→L4 PA sigue x1,11");
    }

    private static void close(double actual,double expected,String message){
        if(Math.abs(actual-expected)>1e-9) throw new AssertionError(message+": "+actual+" != "+expected);
    }
    
}

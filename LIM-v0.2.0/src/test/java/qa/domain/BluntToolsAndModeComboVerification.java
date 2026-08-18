package qa.domain;

import domain.combat.HeavyAttackImpactPolicy;
import domain.combat.moveset.TransitionContinuity;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.meleeWeapons.special.ElectroMechanicalMacePolicy;
import domain.inventory.item.meleeWeapons.special.ElectroMechanicalMaceState;

/**  — Hacha/Martillo/Maza y continuidad ordinal PRIMARY↔ALTERNATIVE. */
public final class BluntToolsAndModeComboVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyMovesets();
        verifyMaceHeavy();
        verifyModeSwitchComboContinuity();
        verifyRestartTransfersFinisher();
    }

    private static void verifyMovesets(){
        var axe=MeleeWeaponCatalog.hachaDeLenador();
        var am=axe.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(am.lightAttackCount()==4,"Hacha debe poseer cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(am.motion("L1").orElseThrow().contactSurface().equals("Filo"),"Hacha L1 usa el único filo.");
        org.junit.jupiter.api.Assertions.assertTrue(am.motion("L2").orElseThrow().contactSurface().equals("Mismo filo"),"Hacha L2 debe recuperar el mismo filo.");
        org.junit.jupiter.api.Assertions.assertTrue(am.transition("L1","L2").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,
                "Hacha debe convertir recuperación del único filo en L2.");

        var hammer=MeleeWeaponCatalog.martilloDeBola();
        var hm=hammer.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(hm.lightAttackCount()==4,"Martillo de bola debe poseer cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(hm.motion("L1").orElseThrow().contactSurface().equals("Cara plana"),"Martillo L1 usa cara plana.");
        org.junit.jupiter.api.Assertions.assertTrue(hm.motion("L2").orElseThrow().contactSurface().equals("Bola"),"Martillo L2 usa bola.");

        var mace=MeleeWeaponCatalog.mazaElectroMecanicaV881();
        var mm=mace.offensiveMovesetFor(WeaponActionMode.PRIMARY).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(mm.lightAttackCount()==4,"Maza debe poseer cuatro LIGHT.");
        org.junit.jupiter.api.Assertions.assertTrue(mm.motion("H1").orElseThrow().action()==WeaponCombatAction.HEAVY_ATTACK,"Maza debe declarar H1.");
        org.junit.jupiter.api.Assertions.assertTrue(mm.motion("L4").orElseThrow().trajectory().contains("Bofetada de revés"),"L4 debe ser bofetada de revés.");
        org.junit.jupiter.api.Assertions.assertTrue(mm.motion("H1").orElseThrow().trajectory().contains("misma bofetada de revés"),"H1 comparte trayectoria física con L4.");
        org.junit.jupiter.api.Assertions.assertTrue(mm.transition("L3","H1").orElseThrow().continuity()==TransitionContinuity.EXCELLENT,
                "L3→H1 debe ser sustitución cinéticamente excelente.");
    }

    private static void verifyMaceHeavy(){
        var mace=MeleeWeaponCatalog.mazaElectroMecanicaV881();
        var heavy=new HeavyAttackImpactPolicy();
        close(heavy.resolve(mace,mace.modes().getFirst()).blunt(),88.8,"HEAVY de Maza debe ser Ct80 x1,11.");
        org.junit.jupiter.api.Assertions.assertTrue(heavy.canSubstituteLightComboFinisher(mace),"HEAVY de Maza debe poder sustituir legítimamente el L4 finisher.");
        close(heavy.resolve(mace,mace.modes().getFirst(),true,false).blunt(),88.8,"H1→L4 sin Trayectoria usa finisher x1,11.");
        close(heavy.resolve(mace,mace.modes().getFirst(),true,true).blunt(),112.0,"H1→L4 con Trayectoria usa finisher x1,40.");

        var state=new ElectroMechanicalMaceState(0.0);
        var policy=new ElectroMechanicalMacePolicy();
        var uncharged=policy.resolveHeavyImpact(mace,state,true);
        org.junit.jupiter.api.Assertions.assertTrue(uncharged.electricityDamage()==0,"Sin carga, H1 conserva su golpe físico pero no añade electricidad.");
        policy.advanceRealTime(mace,state,ElectroMechanicalMacePolicy.RECHARGE_SECONDS);
        var charged=policy.resolveHeavyImpact(mace,state,true);
        org.junit.jupiter.api.Assertions.assertTrue(charged.electricityDamage()==ElectroMechanicalMacePolicy.ELECTRICITY_DAMAGE,
                "Con carga, el mismo H1 añade Electricidad 33.");
    }

    private static void verifyModeSwitchComboContinuity(){
        var dagger=MeleeWeaponCatalog.daga();
        var resolver=new WeaponInputResolutionPolicy();
        var state=new DualWieldComboState();
        org.junit.jupiter.api.Assertions.assertTrue(light(resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state))==1,"Daga PRIMARY abre L1.");
        org.junit.jupiter.api.Assertions.assertTrue(light(resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state))==2,"Daga PRIMARY continúa L2.");
        dagger.selectActionMode(WeaponActionMode.ALTERNATIVE); // equivalente de dominio al MOUSE WHEEL contextual
        var l3=resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state);
        org.junit.jupiter.api.Assertions.assertTrue(light(l3)==3,"Tras PRIMARY L2, ALTERNATIVE debe continuar inmediatamente en L3.");
        org.junit.jupiter.api.Assertions.assertTrue(!l3.lightComboFinisherBonusApplies(),"ALT L3 todavía no es finisher de su cadena de cuatro.");
        var l4=resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state);
        org.junit.jupiter.api.Assertions.assertTrue(light(l4)==4 && l4.lightComboFinisherBonusApplies(),"ALT L4 debe resolver su finisher ordinario.");
    }

    private static void verifyRestartTransfersFinisher(){
        var dagger=MeleeWeaponCatalog.daga();
        var resolver=new WeaponInputResolutionPolicy();
        var state=new DualWieldComboState();
        for(int i=1;i<=5;i++){
            var r=resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state);
            org.junit.jupiter.api.Assertions.assertTrue(light(r)==i,"PRIMARY debe alcanzar L"+i+" antes del reinicio.");
        }
        dagger.selectActionMode(WeaponActionMode.ALTERNATIVE);
        var restart=resolver.resolve(WeaponInput.RIGHT_PRESS,dagger,null,false,false,state);
        org.junit.jupiter.api.Assertions.assertTrue(light(restart)==1,"ALTERNATIVE sin L6 debe reiniciar en L1.");
        org.junit.jupiter.api.Assertions.assertTrue(restart.lightComboFinisherBonusApplies(),"El L1 inmediatamente posterior al reinicio debe conservar el bonus de final de combo.");
    }

    private static int light(WeaponInputResolution r){
        org.junit.jupiter.api.Assertions.assertTrue(r.allowed() && r.action().orElseThrow()==WeaponCombatAction.LIGHT_ATTACK,"Se esperaba LIGHT permitido.");
        return r.lightAttackOrdinal().orElseThrow();
    }
    private static void close(double actual,double expected,String message){if(Math.abs(actual-expected)>1e-9)throw new AssertionError(message+": "+actual+" != "+expected);}
    
}

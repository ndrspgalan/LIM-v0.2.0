package qa.domain;

import domain.combat.*;
import domain.combat.moveset.*;
import domain.inventory.item.*;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.misc.*;
import domain.inventory.item.throwingWeapons.*;

/**  — Bō multimodo, intercepciones Helicoidales y tiempos CHARGED no-placeholder. */
public final class BoHelicalAndChargedTimingVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyBo(); verifyChargedTimes(); verifyHelicalThrown(); verifyNarratives();
    }

    private static void verifyBo() {
        var bo=MeleeWeaponCatalog.bo();
        org.junit.jupiter.api.Assertions.assertTrue(bo.availableConfigurations().contains(new WeaponConfiguration(GripMode.TWO_HANDED,WeaponActionMode.PRIMARY)),"Bō 2H PRIMARY");
        org.junit.jupiter.api.Assertions.assertTrue(bo.availableConfigurations().contains(new WeaponConfiguration(GripMode.ONE_HANDED,WeaponActionMode.ALTERNATIVE)),"Bō 1H ALTERNATIVE");
        org.junit.jupiter.api.Assertions.assertTrue(bo.lightAttackComboFor(WeaponActionMode.PRIMARY).attackCount()==1 && bo.lightAttackComboFor(WeaponActionMode.ALTERNATIVE).attackCount()==1,"Bō sin finisher LIGHT");
        org.junit.jupiter.api.Assertions.assertTrue(!bo.allowsCombatAction(WeaponCombatAction.DESTABILIZE) && bo.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),"Bō sustituye DESTABILIZE por CHARGED");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.strengthRequirement(1.0,GripMode.TWO_HANDED,bo.traits())==8,"Bō FUERZA 2H=8");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.strengthRequirement(1.0,GripMode.ONE_HANDED,bo.traits())==8,"Bō 1H no incrementa FUERZA");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponRequirementPolicy.dexterityRequirementForGrip(1.8,GripMode.ONE_HANDED,bo.traits(),null,false)==18,"Bō 1H no incrementa DESTREZA");
        var input=new WeaponInputResolutionPolicy();
        var d=input.resolve(WeaponInput.DESTABILIZE_PRESS,bo,null,false,false);
        org.junit.jupiter.api.Assertions.assertTrue(d.allowed()&&d.action().orElseThrow()==WeaponCombatAction.CHARGED_ATTACK,"DESTABILIZE del Bō redirige a CHARGED");
        org.junit.jupiter.api.Assertions.assertTrue(!input.resolve(WeaponInput.CHARGED_HOLD,bo,null,false,false).allowed(),"Bō no usa tecla CHARGED convencional");
        var tr=bo.crossModeTransitionProfile().transition(new ModeAttackRef(WeaponActionMode.PRIMARY,"2J"),new ModeAttackRef(WeaponActionMode.ALTERNATIVE,"1L")).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(tr.continuity()==TransitionContinuity.EXCELLENT,"Ruta MOUSE WHEEL 2J→1L del Bō excelente");
    }

    private static void verifyChargedTimes() {
        var scythe=MeleeWeaponCatalog.guadana();
        close(ChargedAttackTimingPolicy.preparationSeconds(scythe),1.20,"Guadaña CHARGED 1,20 s");
        var rotor=MeleeWeaponCatalog.espadonDeRotor();
        close(ChargedAttackTimingPolicy.preparationSeconds(rotor),0.95,"Rotor 2H CHARGED 0,95 s");
        rotor.selectActionMode(WeaponActionMode.ALTERNATIVE);
        close(ChargedAttackTimingPolicy.preparationSeconds(rotor),1.25,"Rotor 1H CHARGED 1,25 s");
        var bo=MeleeWeaponCatalog.bo(); close(ChargedAttackTimingPolicy.preparationSeconds(bo),0.70,"Bō 2H CHARGED 0,70 s");
        bo.selectActionMode(WeaponActionMode.ALTERNATIVE); close(ChargedAttackTimingPolicy.preparationSeconds(bo),0.80,"Bō 1H CHARGED 0,80 s");
        org.junit.jupiter.api.Assertions.assertTrue(Double.isInfinite(ChargedAttackTimingPolicy.preparationSeconds(MeleeWeaponCatalog.espadaHelicoidal())),"Helicoidal release-driven sin umbral");
        org.junit.jupiter.api.Assertions.assertTrue(Double.isInfinite(ChargedAttackTimingPolicy.preparationSeconds(MeleeWeaponCatalog.katanaTermoMecanicaV881())),"Katana release-driven sin umbral");
    }

    private static void verifyHelicalThrown() {
        var h=MeleeWeaponCatalog.espadaHelicoidal(); var p=new HelicalThrownInterceptionPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.LIGHT_ATTACK,AmmunitionCatalog.pebble(),true)==HelicalThrownInterceptionResult.DEFLECTED,"Helicoidal desvía guijarro");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.HEAVY_ATTACK,AmmunitionCatalog.piercingArrow(),true)==HelicalThrownInterceptionResult.DEFLECTED,"Helicoidal desvía flecha");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.JUMP_ATTACK,new CurrencyStack(CurrencyType.VALERITA,1),true)==HelicalThrownInterceptionResult.DEFLECTED,"Helicoidal desvía moneda");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.CHARGED_ATTACK,ThrowingWeaponCatalog.throwingKnifeV881(),true)==HelicalThrownInterceptionResult.DEFLECTED,"Helicoidal desvía cuchillo arrojadizo");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.LIGHT_ATTACK,ThrowingWeaponCatalog.ammoniaGasCapsuleV881(),true)==HelicalThrownInterceptionResult.DETONATES_ON_BLADE,"Amonio detona al interceptarse");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.LIGHT_ATTACK,ThrowingWeaponCatalog.incendiaryTerracottaGrenadeV881(),true)==HelicalThrownInterceptionResult.DETONATES_ON_BLADE,"Terracota incendiaria detona al interceptarse");
        org.junit.jupiter.api.Assertions.assertTrue(p.resolve(h,WeaponCombatAction.LIGHT_ATTACK,ThrowingWeaponCatalog.phosphorusSulfurEggGrenadeV881(),true)==HelicalThrownInterceptionResult.DETONATES_ON_BLADE,"Huevo de azufre detona al interceptarse");
    }

    private static void verifyNarratives(){
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadaHelicoidal().narrativeDescription().contains("guijarros"),"Narrativa Helicoidal explicita proyectiles ligeros");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().narrativeDescription().contains("semipiruetas"),"Narrativa Rotor explicita amplitud ofensiva real");
    }
     private static void close(double a,double b,String m){if(Math.abs(a-b)>1e-9)throw new AssertionError(m+": "+a);}
}

package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.character.sheet.Attribute;
import domain.combat.DestabilizingStrikePolicy;
import domain.combat.ParryResolution;
import domain.combat.WeaponDurabilityResolver;
import domain.inventory.item.*;
import domain.inventory.item.firearms.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.meleeWeapons.special.*;
import domain.inventory.item.misc.ResinJarItem;
import java.util.List;

public final class ThermoElectroAndBayonetVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyRebalancedMeleeCatalog();
        verifyWearPolicy();
        verifyParryDuration();
        verifyKatana();
        verifyMace();
        verifyRotorCopilot();
        verifyBayonet();
    }

    private static void verifyRebalancedMeleeCatalog() {
        checkProfile(MeleeWeaponCatalog.pico(), 90,65,65);
        checkProfile(MeleeWeaponCatalog.zapapico(),95,65,60);
        checkProfile(MeleeWeaponCatalog.piqueta(),0,65,30);
        checkProfile(MeleeWeaponCatalog.cuchilloDeCarnicero(),0,65,15);
        checkProfile(MeleeWeaponCatalog.daga(),65,65,10);
        checkProfile(MeleeWeaponCatalog.hachaDeLenador(),0,65,15);
        checkProfile(MeleeWeaponCatalog.cimitarra(),0,65,15);
        checkProfile(MeleeWeaponCatalog.espadaHelicoidal(),65,65,20);
        checkProfile(MeleeWeaponCatalog.espadonDeRotor(),65,65,100);
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().modes().size()==1,"El Rotor debe tener un único perfil.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.allCanonical().size()==18,"El catálogo debe contener 18 armas canónicas.");
    }

    private static void verifyWearPolicy() {
        WeaponItem mace=MeleeWeaponCatalog.mazaElectroMecanicaV881();
        WeaponMode mm=mace.modes().getFirst();
        int before=mace.currentBluntLethality(mm);
        var wear=mace.applyHeavyArmorWear(mm,new domain.inventory.item.armor.ArmorProtectionProfile(75,75,75));
        org.junit.jupiter.api.Assertions.assertTrue(wear.blunt()>0,"La maza debe desgastarse al colisionar con HEAVY aunque CORTANTE sea 0.");
        org.junit.jupiter.api.Assertions.assertTrue(mace.currentBluntLethality(mm)==before-1,"La maza pierde B1 contra HEAVY.");
        WeaponItem axe=MeleeWeaponCatalog.hachaDeLenador();
        WeaponMode am=axe.modes().getFirst();
        org.junit.jupiter.api.Assertions.assertTrue(axe.applyHeavyArmorWear(am,new domain.inventory.item.armor.ArmorProtectionProfile(75,75,75)).any(),"El hacha también se desgasta contra HEAVY.");
    }

    private static void verifyParryDuration(){
        close(ParryResolution.success().stunDurationSeconds(),2.0,"PARRY y Mirror Parry duran 2 s");
    }

    private static void verifyKatana() {
        WeaponItem katana=MeleeWeaponCatalog.katanaTermoMecanicaV881();
        close(katana.weightKg(),1.25,"Peso katana"); close(katana.reachMeters(),1.0,"Longitud katana");
        org.junit.jupiter.api.Assertions.assertTrue(katana.footprint().equals(new domain.inventory.InventoryFootprint(1,10)),"Katana 10 x 1.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(katana,Attribute.FUERZA)==10 && requirement(katana,Attribute.DESTREZA)==10,"Requisitos katana bimanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!katana.supportsOneHandedUse()&&katana.supportsTwoHandedUse()&&katana.isExclusivelyTwoHanded(),"Katana debe ser exclusivamente bimanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!katana.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE),"Katana no debe conservar ergonomía de compatibilidad monomanual.");
        checkProfile(katana,65,65,20);
        for(ItemPropertyId id:List.of(ItemPropertyId.COPILOT,ItemPropertyId.EQUESTRIAN,ItemPropertyId.BICYCLAR,ItemPropertyId.MOTORCYCLAR))
            org.junit.jupiter.api.Assertions.assertTrue(katana.properties().stream().anyMatch(p->p.id()==id),"Katana debe declarar "+id);
        ThermoMechanicalKatanaState state=new ThermoMechanicalKatanaState();
        ThermoMechanicalKatanaPolicy policy=new ThermoMechanicalKatanaPolicy();
        var amadou=MiscellaneousItemCatalog.amadou(); var resin=new ResinJarItem(3);
        org.junit.jupiter.api.Assertions.assertTrue(policy.prepare(katana,state,amadou,resin),"Amadou + resina deben preparar la katana.");
        close(state.remainingSeconds(),300,"Carga térmica completa");
        policy.draw(katana,state); org.junit.jupiter.api.Assertions.assertTrue(state.burning()&&policy.additionalBurnDamage(katana,state)==67,"Desenfundar debe prender con Quemadura 67.");
        policy.advanceRealTime(katana,state,120); close(state.remainingSeconds(),180,"Consumo térmico");
        policy.sheath(katana,state); policy.advanceRealTime(katana,state,60); close(state.remainingSeconds(),180,"Envainada no consume carga.");
        policy.draw(katana,state); policy.advanceRealTime(katana,state,180); org.junit.jupiter.api.Assertions.assertTrue(!state.burning()&&state.remainingSeconds()==0,"La carga debe agotarse a los 300 s acumulados.");
    }

    private static void verifyMace() {
        WeaponItem mace=MeleeWeaponCatalog.mazaElectroMecanicaV881();
        close(mace.weightKg(),1.0,"Peso maza"); close(mace.reachMeters(),.5,"Longitud maza");
        org.junit.jupiter.api.Assertions.assertTrue(mace.footprint().equals(new domain.inventory.InventoryFootprint(4,10)),"Maza 5 x 2.");
        org.junit.jupiter.api.Assertions.assertTrue(mace.supportsOneHandedUse()&&!mace.supportsTwoHandedUse(),"Maza exclusivamente monomanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!mace.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE),"Maza sin ERGONOMÍA SUFICIENTE.");
        checkProfile(mace,0,0,80);
        WeaponInputResolution heavy=new WeaponInputResolutionPolicy().resolve(WeaponInput.HEAVY_PRESS,mace,null,false,false);
        org.junit.jupiter.api.Assertions.assertTrue(heavy.allowed()&&heavy.action().orElseThrow()==WeaponCombatAction.HEAVY_ATTACK,"La maza excepciona ataque fuerte monomanual.");
        org.junit.jupiter.api.Assertions.assertTrue(!new WeaponInputResolutionPolicy().resolve(WeaponInput.CHARGED_HOLD,mace,null,false,false).allowed(),"La maza no tiene ataque cargado.");
        ElectroMechanicalMaceState state=new ElectroMechanicalMaceState(); ElectroMechanicalMacePolicy policy=new ElectroMechanicalMacePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.sparksVisible(mace,state),"Carga lista = chispas.");
        var miss=policy.resolveHeavyImpact(mace,state,false); org.junit.jupiter.api.Assertions.assertTrue(!miss.chargeConsumed()&&state.charged(),"Fallar no consume carga.");
        var hit=policy.resolveHeavyImpact(mace,state,true); org.junit.jupiter.api.Assertions.assertTrue(hit.electricityDamage()==33&&hit.chargeConsumed()&&!state.charged(),"Impacto cargado = Electricidad 33.");
        var physical=policy.resolveHeavyImpact(mace,state,true); org.junit.jupiter.api.Assertions.assertTrue(physical.electricityDamage()==0,"Sin carga, ataque fuerte físico normal.");
        policy.advanceRealTime(mace,state,11.99); org.junit.jupiter.api.Assertions.assertTrue(!state.charged(),"Antes de 12 s no hay carga.");
        policy.advanceRealTime(mace,state,.01); org.junit.jupiter.api.Assertions.assertTrue(state.charged()&&policy.sparksVisible(mace,state),"A 12 s vuelve la carga y las chispas.");
        for(ItemPropertyId id:List.of(ItemPropertyId.COPILOT,ItemPropertyId.EQUESTRIAN,ItemPropertyId.BICYCLAR,ItemPropertyId.MOTORCYCLAR))
            org.junit.jupiter.api.Assertions.assertTrue(mace.properties().stream().anyMatch(p->p.id()==id),"Maza debe declarar "+id);
    }

    private static void verifyRotorCopilot(){
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.espadonDeRotor().properties().stream().anyMatch(p->p.id()==ItemPropertyId.COPILOT),"Rotor debe admitir COPILOTO.");
    }

    private static void verifyBayonet(){
        RepeatingRifleFirearmItem rifle=FirearmCatalog.repeatingRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.destabilizingTechniqueDescription().toLowerCase().contains("bayonet"),"El desestabilizador del fusil debe ser bayonetazo.");
        check(rifle.destabilizingBayonetProfile(30),0,0,30,"Bayonetazo desestabilizador");
        check(rifle.bayonetChargeProfile(30),65,65,34.05,"Carga con bayoneta");
        var input=new FirearmInputResolutionPolicy().resolve(FirearmInput.CHARGED_HOLD,rifle);
        org.junit.jupiter.api.Assertions.assertTrue(input.allowed()&&input.action()==FirearmAction.BAYONET_CHARGE,"HOLD ataque cargado debe iniciar carga con bayoneta.");
        BayonetChargeState state=new BayonetChargeState(); BayonetChargePolicy policy=new BayonetChargePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(policy.begin(state,100).active(),"Debe iniciar con PA.");
        var tick=policy.advance(state,2,100,3); close(tick.staminaSpent(),6,"Debe consumir la misma tasa recibida de correr.");
        var impact=policy.impact(state,30,rifle.weightKg()); org.junit.jupiter.api.Assertions.assertTrue(impact.impact()&&!impact.active(),"Impactar termina la carga.");
        check(impact.impactProfile(),65,65,34.05,"Perfil de impacto de carga");
        check(new DestabilizingStrikePolicy().profile(30),0,0,30,"Política global de golpe desestabilizador");
    }

    private static int requirement(WeaponItem item, Attribute a){return item.requirements().stream().filter(r->r.attribute()==a).findFirst().orElseThrow().minimumValue();}
    private static void checkProfile(WeaponItem w,double p,double s,double b){check(w.modes().getFirst().lethality(),p,s,b,w.name());}
    private static void check(LethalityProfile l,double p,double s,double b,String m){close(l.piercing(),p,m+" P");close(l.slashing(),s,m+" C");close(l.blunt(),b,m+" Ct");}
    private static void close(double a,double b,String m){if(Math.abs(a-b)>1e-9)throw new AssertionError(m+": "+a+" != "+b);}
    
}
